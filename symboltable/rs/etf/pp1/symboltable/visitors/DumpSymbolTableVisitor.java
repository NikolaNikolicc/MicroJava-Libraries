package rs.etf.pp1.symboltable.visitors;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Module;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.ArrayList;
import java.util.List;

/**
 * Nadograđena verzija DumpSymbolTableVisitor-a koja:
 *  - ispisuje members za Obj kind==Type (class/interface),
 *  - ispisuje locals za Obj kind==Meth i Obj kind==Prog,
 *  - ako struct.getMembers() prazno, proverava obj.getLocalSymbols().
 */
public class DumpSymbolTableVisitor extends SymbolTableVisitor {

    private StringBuilder output = new StringBuilder("=== SYMBOL TABLE DUMP ===\n");

    // ASCII stil tabele
	private static final String TABLE_BORDER = "+-------------------------------------------------------------------------------------------------------------------+\n";
    private static final String HEADER_FORMAT = "| %-8s | %-20s | %-5s | %-5s | %-5s | %-20s | %-32s |\n";
    private static final String ROW_FORMAT    = "| %-8s | %-20s | %-5d | %-5d | %-5d | %-20s | %-32s |\n";
	private static final String SECTION_HEADER_FORMAT = "| %-113s |\n";

    private Obj currentObj = null;

	protected final String indent = "\t";
	protected StringBuilder currentIndent = new StringBuilder();
	
	protected void nextIndentationLevel() {
		currentIndent.append(indent);
	}
	
	protected void previousIndentationLevel() {
		if (currentIndent.length() > 0)
			currentIndent.setLength(currentIndent.length()-indent.length());
	}

    @Override
    public void visitObjNode(Obj objToVisit) {
    	// Zaglavlje tabele samo jednom, na početku dump-a
        if (output.toString().endsWith("=== SYMBOL TABLE DUMP ===\n")) {
            output.append(TABLE_BORDER);
            output.append(String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE"));
            output.append(TABLE_BORDER);
        }

		currentObj = objToVisit;
        String kindStr = getKindString(objToVisit);
        String typeStr = getTypeString(objToVisit.getType());
		currentObj = null;

        output.append(currentIndent.toString() + 
					String.format(ROW_FORMAT,
					kindStr,
					objToVisit.getName(),
					objToVisit.getAdr(),
					objToVisit.getLevel(),
					objToVisit.getFpPos(),
					typeStr,
					objToVisit.getModule().getName()
        ));

        // If Obj is a Type of kind Class or Interface, print its members
        if (objToVisit.getKind() == Obj.Type &&
                objToVisit.getType() != null &&
                (objToVisit.getType().getKind() == Struct.Class || objToVisit.getType().getKind() == Struct.Interface)) {
            if (!objToVisit.getType().getMembers().isEmpty()) {
				output.append(currentIndent.toString() + TABLE_BORDER);
				nextIndentationLevel();
				output.append(currentIndent.toString() + TABLE_BORDER);
                output.append(currentIndent.toString() + String.format(SECTION_HEADER_FORMAT, "  Members of " + objToVisit.getName()));
                output.append(currentIndent.toString() + TABLE_BORDER);
                output.append(currentIndent.toString() + String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE", "MODULE"));
                output.append(currentIndent.toString() + TABLE_BORDER);
                for (Obj member : objToVisit.getType().getMembers()) {
					output.append(currentIndent.toString() + TABLE_BORDER);
                    member.accept(this);
                }
				previousIndentationLevel();
            } else {
				output.append(currentIndent.toString() + TABLE_BORDER);
				nextIndentationLevel();
				output.append(currentIndent.toString() + TABLE_BORDER);
				String sectionTitle = "  Members of " + objToVisit.getName() + ": <none>";
				output.append(currentIndent.toString() + String.format(SECTION_HEADER_FORMAT,  sectionTitle));
				output.append(currentIndent.toString() + TABLE_BORDER);
				previousIndentationLevel();
			}
        }

        // If Obj is a method or program, print its local symbols
        if (objToVisit.getKind() == Obj.Meth || objToVisit.getKind() == Obj.Prog) {
            if (!objToVisit.getLocalSymbols().isEmpty()) {
				output.append(currentIndent.toString() + TABLE_BORDER);
				nextIndentationLevel();
                output.append(currentIndent.toString() + TABLE_BORDER);
				String sectionTitle = (objToVisit.getKind() == Obj.Meth) ? "  Locals of method " : "  Locals of program ";
				sectionTitle += objToVisit.getName();
				output.append(currentIndent.toString() + String.format(SECTION_HEADER_FORMAT,  sectionTitle));
                output.append(currentIndent.toString() + TABLE_BORDER);
                output.append(currentIndent.toString() + String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE", "MODULE"));
                output.append(currentIndent.toString() + TABLE_BORDER);
                for (Obj local : objToVisit.getLocalSymbols()) {
                    output.append(currentIndent.toString() + TABLE_BORDER);
					local.accept(this);
                }
                if (objToVisit.getKind() == Obj.Meth) output.append(currentIndent.toString() + TABLE_BORDER);
				previousIndentationLevel();
            } else {
				output.append(currentIndent.toString() + TABLE_BORDER);
				nextIndentationLevel();
				output.append(currentIndent.toString() + TABLE_BORDER);
				String sectionTitle = (objToVisit.getKind() == Obj.Meth) ? "  Locals of method " : "  Locals of program ";
				sectionTitle += objToVisit.getName() + ": <none>";
				output.append(currentIndent.toString() + String.format(SECTION_HEADER_FORMAT,  sectionTitle));
				output.append(currentIndent.toString() + TABLE_BORDER);
				previousIndentationLevel();
			}
        }
    }

    @Override
    public void visitScopeNode(Scope scope) {
        output.append("\n=== SCOPE CONTENT ===\n");
        output.append(TABLE_BORDER);
        output.append(String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE", "MODULE"));
        output.append(TABLE_BORDER);
        for (Obj o : scope.values()) {
			output.append(currentIndent.toString() + TABLE_BORDER);
            o.accept(this);
        }
        output.append(TABLE_BORDER);
    }

    @Override
    public void visitModuleNode(Module moduleToVisit) {
        output.append("\n=== MODULE: ").append(moduleToVisit.getName()).append(" ===\n");

        // Imports
        output.append("Imports:\n");
        if (moduleToVisit.getImportedModules().isEmpty()) {
            output.append("  <none>\n");
        } else {
            for (Module imported : moduleToVisit.getImportedModules()) {
                output.append("  ").append(imported.getName()).append("\n");
            }
        }

        // Aliases
        output.append("Aliases:\n");
        if (moduleToVisit.getImportedAliases().isEmpty()) {
            output.append("  <none>\n");
        } else {
            for (Module.ListNode alias : moduleToVisit.getImportedAliases()) {
                output.append("  alias -> ");
                alias.obj.accept(this);
            }
        }

        // Locals
        output.append("Local symbols:\n");
        if (moduleToVisit.getLocals().isEmpty()) {
            output.append("  <none>\n");
        } else {
            for (Obj local : moduleToVisit.getLocals()) {
                local.accept(this);
            }
        }
    }

    @Override
    public void visitStructNode(Struct structToVisit) {
		output.append(getTypeString(structToVisit));
    }

    // --- Pomoćne metode ---

	private static final List<Obj> classes = new ArrayList<>();
	private static final List<Obj> interfaces = new ArrayList<>();

    private String getKindString(Obj obj) {
        switch (obj.getKind()) {
            case Obj.Con:  return "Con";
            case Obj.Var:  return "Var";
            case Obj.Type: 
				if (obj.getType().getKind() == Struct.Class) {
					classes.add(obj);
				} else if (obj.getType().getKind() == Struct.Interface) {
					interfaces.add(obj);
				}
				return "Type";
            case Obj.Meth: return "Meth";
            case Obj.Fld:  return "Fld";
            case Obj.Prog: return "Prog";
            default: return "unknown";
        }
    }

    private String getTypeString(Struct type) {
        Obj obj = currentObj;

        // Specijalno: Con koji pokazuje na Class tretiramo kao null pokazivac
        if (obj != null && obj.getKind() == Obj.Con && type.getKind() == Struct.Class) {
            return "null";
        }

        switch (type.getKind()) {
			case Struct.None: return "void";
            case Struct.Int:  return "int";
            case Struct.Char: return "char";
            case Struct.Bool: return "bool";
            case Struct.Array: return "array";
            case Struct.Enum: return "set of int";
            case Struct.Class:
                if (obj == null ||obj.getKind() == Obj.Type) {
                    return "class";
                } else {
					// kind == Var, Fld, Meth, Con
                    String className = "unknown";
					for (Obj obj2 : classes) {
						if (obj2.getType() == type) {
							className = obj2.getName();
							return className;
						}
					}
					return className;
                }
            case Struct.Interface:
                if (obj == null || obj.getKind() == Obj.Type) {
                    return "interface";
                } else {
					// kind == Var, Fld, Meth, Con
					String interfaceName = "unknown";
					for (Obj obj2 : interfaces) {
						if (obj2.getType() == type) {
							interfaceName = obj2.getName();
							return interfaceName;
						}
					}
					return interfaceName;
                }
            default: return "unknown";
        }
    }

    public String getOutput() {
        return output.toString();
    }
}

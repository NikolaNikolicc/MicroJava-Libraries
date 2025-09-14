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

    /**
     * Appends the given string to the output, prepending the current indentation.
     * @param str the string to append
     */
    private void appendIndented(String str) {
        output.append(currentIndent.toString()).append(str);
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

        appendIndented(String.format(ROW_FORMAT,
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
                appendIndented(TABLE_BORDER);
                nextIndentationLevel();
                appendIndented(TABLE_BORDER);
                appendIndented(String.format(SECTION_HEADER_FORMAT, "  Members of " + objToVisit.getName()));
                appendIndented(TABLE_BORDER);
                appendIndented(String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE", "MODULE"));
                appendIndented(TABLE_BORDER);
                for (Obj member : objToVisit.getType().getMembers()) {
                    appendIndented(TABLE_BORDER);
                    member.accept(this);
                }
				previousIndentationLevel();
            } else {
                appendIndented(TABLE_BORDER);
                nextIndentationLevel();
                appendIndented(TABLE_BORDER);
                String sectionTitle = "  Members of " + objToVisit.getName() + ": <none>";
                appendIndented(String.format(SECTION_HEADER_FORMAT, sectionTitle));
                appendIndented(TABLE_BORDER);
                previousIndentationLevel();
            }
        }

        // If Obj is a method or program, print its local symbols
        if (objToVisit.getKind() == Obj.Meth || objToVisit.getKind() == Obj.Prog) {
            if (!objToVisit.getLocalSymbols().isEmpty()) {
                appendIndented(TABLE_BORDER);
                nextIndentationLevel();
                appendIndented(TABLE_BORDER);
                String sectionTitle = (objToVisit.getKind() == Obj.Meth) ? "  Locals of method " : "  Locals of program ";
                sectionTitle += objToVisit.getName();
                appendIndented(String.format(SECTION_HEADER_FORMAT, sectionTitle));
                appendIndented(TABLE_BORDER);
                appendIndented(String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE", "MODULE"));
                appendIndented(TABLE_BORDER);
                for (Obj local : objToVisit.getLocalSymbols()) {
                    appendIndented(TABLE_BORDER);
                    local.accept(this);
                }
                if (objToVisit.getKind() == Obj.Meth) appendIndented(TABLE_BORDER);
                previousIndentationLevel();
            } else {
                appendIndented(TABLE_BORDER);
                nextIndentationLevel();
                appendIndented(TABLE_BORDER);
                String sectionTitle = (objToVisit.getKind() == Obj.Meth) ? "  Locals of method " : "  Locals of program ";
                sectionTitle += objToVisit.getName() + ": <none>";
                appendIndented(String.format(SECTION_HEADER_FORMAT, sectionTitle));
                appendIndented(TABLE_BORDER);
                previousIndentationLevel();
            }
        }
    }

    @Override
    public void visitScopeNode(Scope scope) {
        appendIndented("\n=== SCOPE CONTENT ===\n");
        appendIndented(TABLE_BORDER);
        appendIndented(String.format(HEADER_FORMAT, "KIND", "NAME", "ADR", "LEVEL", "FPPOS", "TYPE", "MODULE"));
        appendIndented(TABLE_BORDER);
        for (Obj o : scope.values()) {
            appendIndented(TABLE_BORDER);
            o.accept(this);
        }
        appendIndented(TABLE_BORDER);
    }

    @Override
    public void visitModuleNode(Module moduleToVisit) {
        appendIndented("\n=== MODULE: " + moduleToVisit.getName() + " ===\n");

        // Imports
        appendIndented(TABLE_BORDER);
        appendIndented(String.format(SECTION_HEADER_FORMAT, "  Imports:"));
        appendIndented(TABLE_BORDER);
        if (moduleToVisit.getImportedModules().isEmpty()) {
            appendIndented(String.format(SECTION_HEADER_FORMAT, "  <none>"));
        } else {
            for (Module imported : moduleToVisit.getImportedModules()) {
                appendIndented(String.format(SECTION_HEADER_FORMAT, imported.getName()));
            }
        }
        appendIndented(TABLE_BORDER);

        // Imported names
        appendIndented(TABLE_BORDER);
        appendIndented(String.format(SECTION_HEADER_FORMAT, "  Imported names:"));
        appendIndented(TABLE_BORDER);
        if (moduleToVisit.getImportedNames().isEmpty()) {
            appendIndented(TABLE_BORDER);
            appendIndented(String.format(SECTION_HEADER_FORMAT, "  <none>"));
        } else {
            for (Obj name : moduleToVisit.getImportedNames()) {
                appendIndented(TABLE_BORDER);
                name.accept(this);
            }
        }
        appendIndented(TABLE_BORDER);

        // Locals
        appendIndented(TABLE_BORDER);
        appendIndented(String.format(SECTION_HEADER_FORMAT, "  Local symbols:"));
        appendIndented(TABLE_BORDER);
        if (moduleToVisit.getLocals().isEmpty()) {
            appendIndented(TABLE_BORDER);
            appendIndented(String.format(SECTION_HEADER_FORMAT, "  <none>"));
        } else {
            for (Obj local : moduleToVisit.getLocals()) {
                output.append(currentIndent.toString() + TABLE_BORDER);
                local.accept(this);
            }
        }
        appendIndented(TABLE_BORDER);
    }

    @Override
    public void visitStructNode(Struct structToVisit) {
		output.append(getTypeString(structToVisit));
    }

    // helper methods

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

        // Special case: if the object is a constant of class type, return "null"
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

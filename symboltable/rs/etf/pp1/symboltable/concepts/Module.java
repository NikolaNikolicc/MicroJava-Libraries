package rs.etf.pp1.symboltable.concepts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rs.etf.pp1.symboltable.factory.SymbolTableFactory;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class Module {

    private String name;

    // -----------------------------SEMANTIC ANALYSIS-------------------------------------
    // list of imported modules
    private List<Module> importedModules = new ArrayList<>();
    // list of single names that import this module, we are imitating Scope locals behavior because we must initialize our specific list
    private SymbolDataStructure importedNames;
    // list of local symbols (Obj) declared in this module (including formal parameters and local variables), we are imitating Obj locals behavior because we don't need to initialize our list, reference will do the job
    private SymbolDataStructure locals;

    // ----------------------------------CODE GEN----------------------------------------
    private byte code[];
    private int data[];
    private int mainPC;
    private int dataSize;

    // constructors
    public Module(String name) {
        this.name = name;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public List<Module> getImportedModules() {
        return importedModules;
    }

    public SymbolDataStructure getImportedNames() {
        return importedNames;
    }

    public Collection<Obj> getLocals() {
        return (locals != null) ? locals.symbols() : Collections.emptyList();
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public int getMainPC() {
        return mainPC;
    }

    public void setMainPC(int mainPC) {
        this.mainPC = mainPC;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    // print method
    public void accept(SymbolTableVisitor stv) {
        stv.visitModuleNode(this);
    }

    /**
     * Imports the given module into this module's list of imports.
     * Prevents duplicate imports and self-imports.
     * @param module the module to import
     * @return true if the module was successfully imported, false otherwise
     */
    public boolean importModule(Module module) {
        if (module == null || importedModules.contains(module) || module == this) {
            return false; // already imported or self-import
        }
        importedModules.add(module);
        return true;
    }

    /**
     * Imports the given name (Obj) into this module's list of imported names.
     * Prevents duplicate imports.
     * @param nameObj the Obj representing the name to import
     * @return true if the name was successfully imported, false otherwise
     */
    public boolean addToImportedNames(Obj nameObj) {
        if (importedNames == null) {
            importedNames = SymbolTableFactory.instance().createSymbolTableDataStructure();
        }
        return importedNames.insertKey(nameObj);
    }

    /**
     * Finds a symbol with the given name in the local symbols of this module. We must to return null because searchKey method returns null if the key is not found so basically we are covering both cases (locals is null or key is not found) with check if returned value is null.
     * @param name
     * @return Obj local if found, Tab.noObj otherwise
     */
    public Obj findNameInLocals(String name) {
        return (locals != null) ? locals.searchKey(name) : null;
    }

    /**
     * Sets the local symbols of this module to the given SymbolDataStructure.
     * @param locals the SymbolDataStructure containing local symbols
     */
    public void setLocals(SymbolDataStructure locals) {
        this.locals = locals;
    }
}

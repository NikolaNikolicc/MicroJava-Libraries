package rs.etf.pp1.symboltable.concepts;

import java.util.ArrayList;
import java.util.List;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class Module {

    private String name;

    // -----------------------------SEMANTIC ANALYSIS-------------------------------------
    // list of imported modules
    private List<Module> importedModules = new ArrayList<>();
    // list of single names that import this module
    private List<Obj> importedNames = new ArrayList<>();
    // list of local symbols (Obj) declared in this module (including formal parameters and local variables)
    private List<Obj> locals = new ArrayList<>();

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

    public List<Obj> getImportedNames() {
        return importedNames;
    }

    public List<Obj> getLocals() {
        return locals;
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
    public boolean importName(Obj nameObj) {
        if (nameObj == null || importedNames.contains(nameObj)) {
            return false;
        }
        importedNames.add(nameObj);
        return true;
    }

    /**
     * Finds a symbol with the given name in the local symbols of this module.
     * @param name
     * @return Obj local if found, Tab.noObj otherwise
     */
    public Obj findNameInLocals(String name) {
        for (Obj local : locals) {
            if (local.getName().equals(name)) {
                // Found matching local symbol
                System.out.println("Found local symbol: " + local.getName());
                return local;
            }
        }
        return Tab.noObj;
    }

}

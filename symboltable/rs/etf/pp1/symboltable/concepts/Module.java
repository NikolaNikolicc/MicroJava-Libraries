package rs.etf.pp1.symboltable.concepts;

import java.util.ArrayList;
import java.util.List;

import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class Module {

    private String name;

    // -----------------------------SEMANTIC ANALYSIS-------------------------------------
    public class ListNode {
        public ListNode next;
        public Obj obj;
    }
    // list of imported modules
    private List<Module> importedModules = new ArrayList<>();
    // list of single names that import this module
    private List<ListNode> importedAliases = new ArrayList<>();
    // list of local symbols (Obj) declared in this module (including formal parameters and local variables)
    private List<Obj> locals = new ArrayList<>();
    // list of symbols (Obj) that this module exports
    private List<ListNode> exportedSymbols = new ArrayList<>();

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

    public List<ListNode> getImportedAliases() {
        return importedAliases;
    }

    public List<Obj> getLocals() {
        return locals;
    }

    public List<ListNode> getExportedSymbols() {
        return exportedSymbols;
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

}

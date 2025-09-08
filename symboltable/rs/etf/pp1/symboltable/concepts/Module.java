package rs.etf.pp1.symboltable.concepts;

import java.util.ArrayList;
import java.util.List;

public class Module {

    private String name;

    // -----------------------------SEMANTIC ANALYSIS-------------------------------------
    public class ListNode {
        private ListNode next;
        private Obj obj;
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
    private int code[];
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

}

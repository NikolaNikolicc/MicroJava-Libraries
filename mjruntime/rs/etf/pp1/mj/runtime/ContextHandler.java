package rs.etf.pp1.mj.runtime;

import rs.etf.pp1.mj.runtime.concepts.Context;
import rs.etf.pp1.mj.runtime.factory.ContextTableFactory;
import rs.etf.pp1.mj.runtime.structure.ContextDataStructure;

public class ContextHandler {

    private static ContextHandler instance;

    private ContextHandler() {
        
    }

    public static ContextHandler getInstance() {
        if (instance == null) {
            instance = new ContextHandler();
        }
        return instance;
    }

    ContextDataStructure contextMap;

    public void addEntry(Context context) {
        if (contextMap == null) {
            contextMap = ContextTableFactory.instance().createContextDataStructure();
        }
        contextMap.insertKey(context);
    }

    public boolean switchContext(String newContextName) {
        if (contextMap == null || contextMap.searchKey(newContextName) == null) return false;
        Run.currContext = contextMap.searchKey(newContextName);
        return true;
    }
}

package rs.etf.pp1.mj.runtime;

import rs.etf.pp1.mj.runtime.concepts.Context;
import rs.etf.pp1.mj.runtime.factory.RuntimeFactory;
import rs.etf.pp1.mj.runtime.structure.ContextDataStructure;
import rs.etf.pp1.mj.runtime.structure.EntryDataStructure;

public class ContextHandler {

    private static ContextHandler instance;
    private EntryDataStructure entryMap;
    private ContextDataStructure contextMap;

    private ContextHandler() {
        
    }

    public static ContextHandler getInstance() {
        if (instance == null) {
            instance = new ContextHandler();
        }
        return instance;
    }

    public void addEntryToContextMap(Context context) {
        if (contextMap == null) {
            contextMap = RuntimeFactory.instance().createContextDataStructure();
        }
        contextMap.insertKey(context);
    }

    public void setEntryMap(EntryDataStructure entryMap) {
        this.entryMap = entryMap;
    }

    public String resolveIndexToName(int index) {
        if (entryMap == null) return null;
        return entryMap.resolveContextIndex(index);
    }

    private boolean switchContext(String newContextName) {
        if (contextMap == null || contextMap.searchKey(newContextName) == null) return false;
        Run.currContext = contextMap.searchKey(newContextName);
        return true;
    }

    public boolean switchCurrentContext(int index) {
        String contextName = resolveIndexToName(index);
        if (contextName == null) return false;
        return switchContext(contextName);
    }
}

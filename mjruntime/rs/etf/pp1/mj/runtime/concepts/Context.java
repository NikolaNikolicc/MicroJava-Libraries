package rs.etf.pp1.mj.runtime.concepts;

import java.util.Collection;
import java.util.Collections;

import rs.etf.pp1.mj.runtime.factory.RuntimeFactory;
import rs.etf.pp1.mj.runtime.structure.EntryDataStructure;

public class Context {
    public byte[] code;
    public int[] data;

    private int dataSize;
    public int startPC;

    public int timestamp;
    public String moduleName;
    public int moduleIndex;
    private  EntryDataStructure entryMap;

    public String getModuleName() {
        return moduleName;
    }

    public void setDataSize(int ds) {
        this.dataSize = ds;
        data = new int[this.dataSize];
    }

    public void addEntry(int index, String name) {
        if (entryMap == null) {
            entryMap = RuntimeFactory.instance().createEntryDataStructure();
        }
        entryMap.addEntry(index, name);
    }

    public String resolveContextIndex(int index) {
        if (entryMap == null) return null;
        return entryMap.resolveContextIndex(index);
    }

    public Collection<String> getModuleNames() {
        return entryMap != null ? entryMap.getModuleNames() : Collections.emptyList();
    }

    public EntryDataStructure getEntryMap() {
        return entryMap;
    }
}

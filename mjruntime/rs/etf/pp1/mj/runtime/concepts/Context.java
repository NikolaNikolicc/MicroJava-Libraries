package rs.etf.pp1.mj.runtime.concepts;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Context {
    public byte[] code;
    public int[] data;

    private int dataSize;
    public int startPC;

    public int timestamp;
    public String moduleName;
    public int moduleIndex;
    public HashMap<Integer, String> entryMap;

    public String getModuleName() {
        return moduleName;
    }

    public void setDataSize(int ds) {
        this.dataSize = ds;
        data = new int[this.dataSize];
    }

    public void addEntry(int index, String name) {
        if (entryMap == null) {
            entryMap = new HashMap<>();
        }
        entryMap.put(index, name);
    }

    public String resolveContextIndex(int index) {
        if (entryMap == null) return null;
        return entryMap.get(index);
    }

    public Collection<String> getModuleNames() {
        return entryMap != null ? entryMap.values() : Collections.emptyList();
    }
}

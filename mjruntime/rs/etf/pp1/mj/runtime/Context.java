package rs.etf.pp1.mj.runtime;

import java.util.HashMap;

public class Context {
    byte[] code;
    int[] data;

    private int dataSize;
    int startPC;

    int timestamp;
    String moduleName;
    int moduleIndex;
    HashMap<Integer, String> entryMap;

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
}

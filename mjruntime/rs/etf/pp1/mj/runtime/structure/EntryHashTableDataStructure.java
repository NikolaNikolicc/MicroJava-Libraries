package rs.etf.pp1.mj.runtime.structure;

import java.util.Collection;
import java.util.HashMap;

public class EntryHashTableDataStructure extends EntryDataStructure{

    public HashMap<Integer, String> hashTable = new HashMap<>();

    @Override
    public boolean addEntry(int index, String name) {
        if (hashTable.containsKey(index)) return false;
        hashTable.put(index, name);
        return true;
    }

    @Override
    public String resolveContextIndex(int index) {
        return hashTable.get(index);
    }

    @Override
    public Collection<String> getModuleNames() {
        return hashTable.values();
    }

}

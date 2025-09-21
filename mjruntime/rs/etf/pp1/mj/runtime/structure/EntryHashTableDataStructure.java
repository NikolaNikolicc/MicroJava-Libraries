package rs.etf.pp1.mj.runtime.structure;

import java.util.Collection;
import java.util.HashMap;

public class EntryHashTableDataStructure extends EntryDataStructure{

    public HashMap<Integer, String> hashTable = new HashMap<>();

    @Override
    public void addEntry(int index, String name) {
        hashTable.put(index, name);
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

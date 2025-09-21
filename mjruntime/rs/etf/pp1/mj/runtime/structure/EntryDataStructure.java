package rs.etf.pp1.mj.runtime.structure;

import java.util.Collection;

public abstract class EntryDataStructure {

    public abstract void addEntry(int index, String name);

    public abstract String resolveContextIndex(int index);

    public abstract Collection<String> getModuleNames();
}

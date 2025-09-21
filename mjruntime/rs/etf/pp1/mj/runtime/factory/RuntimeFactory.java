package rs.etf.pp1.mj.runtime.factory;

import rs.etf.pp1.mj.runtime.structure.ContextDataStructure;
import rs.etf.pp1.mj.runtime.structure.ContextHashTableDataStructure;
import rs.etf.pp1.mj.runtime.structure.EntryDataStructure;
import rs.etf.pp1.mj.runtime.structure.EntryHashTableDataStructure;

public class RuntimeFactory {

    private static RuntimeFactory inst = new RuntimeFactory();

    public static RuntimeFactory instance() {
        return inst;
    }

	public ContextDataStructure createContextDataStructure() {
		return new ContextHashTableDataStructure();
	}

    public EntryDataStructure createEntryDataStructure() {
        return new EntryHashTableDataStructure();
    }
}

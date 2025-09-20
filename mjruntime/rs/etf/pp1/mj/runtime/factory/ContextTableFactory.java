package rs.etf.pp1.mj.runtime.factory;

import rs.etf.pp1.mj.runtime.structure.ContextDataStructure;
import rs.etf.pp1.mj.runtime.structure.ContextHashTableDataStructure;

public class ContextTableFactory {

    private static ContextTableFactory inst = new ContextTableFactory();

    public static ContextTableFactory instance() {
        return inst;
    }

	public ContextDataStructure createContextDataStructure() {
		return new ContextHashTableDataStructure();
	}
}

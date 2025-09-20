package rs.etf.pp1.mj.runtime.structure;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import rs.etf.pp1.mj.runtime.concepts.Context;

public class ContextHashTableDataStructure extends ContextDataStructure{

    protected Map<String, Context> hashTable = new LinkedHashMap<String, Context>();

    @Override
    public Context searchKey(String key) {
        return hashTable.get(key);
    }

    @Override
    public boolean deleteKey(String key) {
        Context o = null;
		if (hashTable.containsKey(key)) {
			o = hashTable.remove(key);
		}
		return !hashTable.containsKey(key) && (o != null);
    }

    @Override
    public boolean insertKey(Context node) {
 		if (hashTable.containsKey(node.getModuleName())) 
			return false;
		else{
			hashTable.put(node.getModuleName(), node);
			return true;
		}
    }

    @Override
    public Collection<Context> contexts() {
        return hashTable.values();
    }

    @Override
    public int numContexts() {
        return hashTable.size();
    }

}

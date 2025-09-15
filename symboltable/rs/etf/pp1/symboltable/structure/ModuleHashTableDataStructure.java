package rs.etf.pp1.symboltable.structure;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import rs.etf.pp1.symboltable.concepts.Module;

public class ModuleHashTableDataStructure extends ModuleDataStructure {

    protected Map<String, Module> hashTable = new LinkedHashMap<String, Module>();

    @Override
    public Module searchKey(String key) {
        return hashTable.get(key);
    }

    @Override
    public boolean deleteKey(String key) {
        Module o = null;
		if (hashTable.containsKey(key)) {
			o = hashTable.remove(key);
		}
		return !hashTable.containsKey(key) && (o != null);
    }

    @Override
    public boolean insertKey(Module node) {
 		if (hashTable.containsKey(node.getName())) 
			return false;
		else{
			hashTable.put(node.getName(), node);
			return true;
		}
    }

    @Override
    public Collection<Module> modules() {
        return hashTable.values();
    }

    @Override
    public int numModules() {
        return hashTable.size();
    }

}

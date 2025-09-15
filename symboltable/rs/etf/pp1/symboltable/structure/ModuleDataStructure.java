package rs.etf.pp1.symboltable.structure;

import java.util.Collection;

import rs.etf.pp1.symboltable.concepts.Module;

public abstract class ModuleDataStructure {
	/**
	 * Pretrazivanje hash-a na odredjenu vrednost
	 * kljuca.
	 * 
	 * @return HashNode koji sadrzi kljuc po kome
	 * se pretrazivalo ukoliko je kljuc nadjen, null
	 * u suprotnom
	 */
	public abstract Module searchKey(String key);
	
	
	/** 
	 * Brisanje elementa sa vrednoscu kljuca key.
	 * 
	 * @return Uspesnosti operacije brisanja. true je indikacija uspesnog
	 * brisanja kljuca iz hash tabele. false u suprotnom
	 */    
	public abstract boolean deleteKey(String key);
	
	
	/** 
	 * Umetanje novog elementa node u hes.
	 * Element ne sme imati kljuc koji vec postoji u hesu.
	 * 
	 * @return true ukoliko je umetanje uspesno, false u suprotnom
	 */
	public abstract boolean insertKey(Module node);

	public abstract Collection<Module> modules();

	public abstract int numModules();
}

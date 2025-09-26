/* MicroJava Instruction Decoder  (HM 99-05-07)
   =============================
*/
package rs.etf.pp1.mj.runtime;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import rs.etf.pp1.mj.runtime.factory.RuntimeFactory;
import rs.etf.pp1.mj.runtime.structure.EntryDataStructure;

public class disasm {

	private static byte[] code = new byte[8192+14];;		// code buffer
	private static int cur=0;			// address of next byte to decode
	private static int adr;			// address of currently decoded instruction
	private static int off;    // size of the header of mj obj file

	private static int get() {
		return ((int)code[cur++])<<24>>>24;
	}
	
	private static int get2() {
		return (get()*256 + get())<<16>>16;
	}
	
	private static int get4() {
		return (get2()<<16) + (get2()<<16>>>16);
	}
	
	private static String jumpDist() {
		int dist = get2();
		int abs = adr + dist;
		return String.valueOf(dist)+" (="+String.valueOf(abs)+")";
	}

	private static String switchModule() {
		return " ["+String.valueOf(get()) + "]";
	}
	
	private static void P(String s) {
		System.out.println(adr+": "+s);
		adr = cur-off;
	}

	public static void decode(byte[] c, int len) {
		//int op;
		code = c;
		cur = off;
		adr = cur-off;
		while (cur < len) {
			switch(get()) {
				case 1: {P("load "+get()); break;}
				case 2: {P("load_0"); break;}
				case 3: {P("load_1"); break;}
				case 4: {P("load_2"); break;}
				case 5: {P("load_3"); break;}
				case 6: {P("store "+get()); break;}
				case 7: {P("store_0"); break;}
				case 8: {P("store_1"); break;}
				case 9: {P("store_2"); break;}
				case 10: {P("store_3"); break;}
				case 11: {P("getstatic "+get2()+switchModule()); break;}
				case 12: {P("putstatic "+get2()+switchModule()); break;}
				case 13: {P("getfield "+get2()); break;}
				case 14: {P("putfield "+get2()); break;}
				case 15: {P("const_0"); break;}
				case 16: {P("const_1"); break;}
				case 17: {P("const_2"); break;}
				case 18: {P("const_3"); break;}
				case 19: {P("const_4"); break;}
				case 20: {P("const_5"); break;}
				case 21: {P("const_m1"); break;}
				case 22: {P("const "+get4()); break;}
				case 23: {P("add"); break;}
				case 24: {P("sub"); break;}
				case 25: {P("mul"); break;}
				case 26: {P("div"); break;}
				case 27: {P("rem"); break;}
				case 28: {P("neg"); break;}
				case 29: {P("shl"); break;}
				case 30: {P("shr"); break;}
				case 31: {P("inc "+get()+","+get()); break;}
				case 32: {P("new "+get2()); break;}
				case 33: {P("newarray "+get()); break;}
				case 34: {P("aload"); break;}
				case 35: {P("astore"); break;}
				case 36: {P("baload"); break;}
				case 37: {P("bastore"); break;}
				case 38: {P("arraylength"); break;}
				case 39: {P("pop"); break;}
				case 40: {P("dup"); break;}
				case 41: {P("dup2"); break;}
				case 42: {P("jmp "+jumpDist()); break;}
				case 43: {P("jeq "+jumpDist()); break;}
				case 44: {P("jne "+jumpDist()); break;}
				case 45: {P("jlt "+jumpDist()); break;}
				case 46: {P("jle "+jumpDist()); break;}
				case 47: {P("jgt "+jumpDist()); break;}
				case 48: {P("jge "+jumpDist()); break;}
				case 49: {P("call "+jumpDist()+switchModule()); break;}
				case 50: {P("return"); break;}
				case 51: {P("enter "+get()+" "+get()); break;}
				case 52: {P("exit"); break;}
				case 53: {P("read"); break;}
				case 54: {P("print"); break;}
				case 55: {P("bread"); break;}
				case 56: {P("bprint"); break;}
				case 57: {P("trap "+get()); break;}
				case 58: { 
							String name=new String(); 
				            int moduleIndex=get();
							int a=get4();
							while (a!=-1) { name+=(char)a; a=get4(); }
							P("invokevirtual "+name+" ["+moduleIndex+"]");
							break;
						 }
				case 59: {P("dup_x1"); break;}
				case 60: {P("dup_x2"); break;}
				case 61: {P("module_switch "+switchModule()); break;}						 
				default: {P("-- error--"); break;}
			}
		}
	}

	private static EntryDataStructure entryMap;

	private static void addEntryToEntryMap(int index, String name) {
		if (entryMap == null) {
			entryMap = RuntimeFactory.instance().createEntryDataStructure();
		}
		entryMap.addEntry(index, name);
	}

	private static void readModuleNameAndIndex() {
		StringBuilder moduleNameBuilder = new StringBuilder();
		int c = get();
		while ((char) c != Run.delimiter1) {
			moduleNameBuilder.append((char) c);
			c = get();
		}
		System.out.println("moduleName=" + moduleNameBuilder.toString() + " (moduleIndex=" + get4() + ")");
	}

	private static void readModuleMap() {
		int c = get();
		while (true) {
			StringBuilder entryNameBuilder = new StringBuilder();
			while ((char) c != Run.delimiter1 && (char) c != Run.delimiter2) {
				entryNameBuilder.append((char) c);
				c = get();
			}
			if ((char) c == Run.delimiter2) {
				break;
			}
			int index = get4();
			System.out.println("moduleMap entry: " + entryNameBuilder.toString() + " -> " + index);
			addEntryToEntryMap(index, entryNameBuilder.toString());
			c = get();
		}
	}

	public static void readFile(String fileName) {
		try {
			InputStream s = new FileInputStream(fileName); 
			System.out.println("\n--- disassembly of file " + fileName + " ---");
			int len = s.read(code);
			int first = get();
			int second = get();
			if (first!='M' || second!='J')
				System.out.println("-- invalid microjava object file");
			else {
				System.out.println("codeSize="+get4());
				System.out.println("dataSize="+get4());
				System.out.println("mainPC="+get4());
				System.out.println("timestamp="+get4());
				readModuleNameAndIndex();
				readModuleMap();
				off=cur;
				decode(code, len);
				s.close();
			}
		} catch (IOException e) {
			System.out.println("-- could not open file " + fileName);
		}
	}

	public static void readFileForAllImportedModules(Path parentPath) {
		if (entryMap == null) return;

		for (String moduleName : entryMap.getModuleNames()) {
			String parsedName = Run.filterModuleName(moduleName);
			String fileName = parentPath.resolve(parsedName + ".obj").toString();
			cur = 0;
			readFile(fileName);
		}
	}


	public static void main(String[] arg) {
		if (arg.length == 0)
			System.out.println("-- no main filename specified");
		else {
			readFile(arg[0]);
			readFileForAllImportedModules(Paths.get(arg[0]).getParent());
		}
	}

}
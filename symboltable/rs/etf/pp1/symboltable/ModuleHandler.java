package rs.etf.pp1.symboltable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rs.etf.pp1.symboltable.concepts.Module;

public class ModuleHandler {

    // Singleton pattern
    private static ModuleHandler instance;

    private ModuleHandler() {
    }

    public static ModuleHandler getInstance() {
        if (instance == null) {
            instance = new ModuleHandler();
            instance.currentModule = instance.noModule;
        }
        return instance;
    }
    // default module for symbols not belonging to any module
    public Module noModule = new Module("noModule");
    // current module context (this attribute can be set only in open and close module methods)
    private Module currentModule = null;
    // path where all modules are located
    public Path modulePath = Paths.get("");

    // map of module names to Module objects
    private final Map<String, Module> modules = new HashMap<>();
    // for detecting circular dependencies
    private final Set<String> loadedModules = new HashSet<>();

    private Module createModule(String name) {
        if (modules.containsKey(name)) {
            return modules.get(name);
        }
        Module newModule = new Module(name);
        modules.put(name, newModule);
        return newModule;
    }

    public void openModule(String name){
        currentModule = createModule(joinModulePath(name));
    }

    /**
     * Spaja modulePath i ime modula u jedan string koristeći resolve.
     */
    private String joinModulePath(String name) {
        return modulePath.resolve(name).toString();
    }

    public void closeModule(){
        currentModule = noModule;
    }

    public Module getCurrentModule() {
        return currentModule;
    }
}

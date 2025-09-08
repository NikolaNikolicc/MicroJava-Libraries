package rs.etf.pp1.symboltable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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
    private final Stack<Module> circularImportDetectionPath = new Stack<>();

    private Module createModule(String name) {
        if (modules.containsKey(name)) {
            return modules.get(name);
        }
        Module newModule = new Module(name);
        modules.put(name, newModule);
        return newModule;
    }

    /**
     * Spaja modulePath i ime modula u jedan string koristeći resolve.
     */
    public String joinModulePath(String name) {
        return modulePath.resolve(name).toString();
    }

    /**
     * Proverava da li modul sa zadatim imenom postoji na putanji modulePath.
     * @param name ime modula
     * @return true ako postoji, false ako ne postoji
     */
    public boolean existsModuleOnPath(String name) {
        String fullPath = joinModulePath(name);
        return java.nio.file.Files.exists(java.nio.file.Paths.get(fullPath));
    }

    /**
     * Ucitava modul sa zadatim imenom ako postoji u modules mapi, u suprotnom vraca noModule.
     * Pretpostavlja se da modul postoji na putanji (provereno ranije).
     */
    public Module getModule(String name) {
        String fullPath = joinModulePath(name);
        Module m = modules.get(fullPath);
        if (m != null) {
            return m;
        }
        return noModule;
    }

    public void openModule(String name){
        Module m = getModule(joinModulePath(name));
        circularImportDetectionPath.push(currentModule);
        if (circularImportDetectionPath.contains(m)) {
            // we have a circular import (already in the path)
            currentModule = noModule;
        } else {
            currentModule = createModule(joinModulePath(name));
        }
    }

    public void closeModule(){
        currentModule = circularImportDetectionPath.pop();
    }

    public Module getCurrentModule() {
        return currentModule;
    }
}

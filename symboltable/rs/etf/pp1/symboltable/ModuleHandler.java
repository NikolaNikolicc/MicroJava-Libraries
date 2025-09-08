package rs.etf.pp1.symboltable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rs.etf.pp1.symboltable.concepts.Module;

/**
 * Handler for working with modules and module contexts.
 * Enables opening, closing, and loading modules, as well as circular dependency detection.
 * Implemented as a singleton.
 */
public class ModuleHandler {

    /** Singleton instance of the handler. */
     private static ModuleHandler instance;

    /**
     * Private constructor for singleton pattern.
     * Adds noModule to the modules map.
     */
    private ModuleHandler() {
        modules.put(noModule.getName(), noModule);
    }

    /**
     * Returns the single instance of the handler (singleton pattern).
     * @return ModuleHandler instance
     */
    public static ModuleHandler getInstance() {
        if (instance == null) {
            instance = new ModuleHandler();
            instance.currentModule = instance.noModule;
        }
        return instance;
    }
    /**
     * Module for symbols that do not belong to any module (default context).
     */
    public Module noModule = new Module("noModule");

    /**
     * Current module context (changes only in open/close methods).
     */
    private Module currentModule = null;

    /**
     * Path where all modules are located.
     */
    public Path modulePath = Paths.get("");

    /**
     * Map of all modules (key: path, value: Module object).
     */
    private final Map<String, Module> modules = new HashMap<>();

    /**
     * Stack for detecting circular dependencies during module import.
     */
    private final Stack<Module> circularImportDetectionPath = new Stack<>();

    /**
     * Creates a new module or returns an existing one from the map.
     * @param name name or path of the module
     * @return Module object
     */
    private Module createModule(String name) {
        if (modules.containsKey(name)) {
            return modules.get(name);
        }
        Module newModule = new Module(name);
        modules.put(name, newModule);
        return newModule;
    }

    /**
     * Joins modulePath and the module name into a single string using resolve.
     * @param name module name
     * @return string path to the module
     */
    public String joinModulePath(String name) {
        return modulePath.resolve(name).toString();
    }

    /**
     * Checks if a module with the given name exists at the modulePath.
     * @param name module name
     * @return true if exists, false otherwise
     */
    public boolean existsModuleOnPath(String name) {
        String fullPath = joinModulePath(name);
        return java.nio.file.Files.exists(java.nio.file.Paths.get(fullPath));
    }

    /**
     * Loads a module with the given name if it exists in the modules map, otherwise returns noModule.
     * Assumes the module exists at the path (checked earlier).
     * @param name module name
     * @return Module object or noModule if not found
     */
    public Module getModule(String name) {
        String fullPath = joinModulePath(name);
        Module m = modules.get(fullPath);
        if (m != null) {
            return m;
        }
        return noModule;
    }

    /**
     * Opens a module with the given name and sets it as the current module.
     * Detects circular dependencies and returns noModule in that case.
     * @param name module name
     */
    public void openModule(String name){
        Module m = getModule(joinModulePath(name));
        circularImportDetectionPath.push(currentModule);
        // check for circular import
        if (circularImportDetectionPath.contains(m)) {
            // we have a circular import (already in the path)
            currentModule = noModule;
        } else {
            // create/load new module
            currentModule = createModule(joinModulePath(name));
        }
    }

    /**
     * Closes the current module and restores the previous context from the stack.
     */
    public void closeModule(){
        // restore previous current module from stack
        currentModule = circularImportDetectionPath.pop();
    }

    /**
     * Returns the currently active module (context).
     * @return current Module
     */
    public Module getCurrentModule() {
        return currentModule;
    }
}

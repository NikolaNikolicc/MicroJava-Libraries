package rs.etf.pp1.symboltable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rs.etf.pp1.symboltable.concepts.Module;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

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
    public Module noModule = new Module("noModule", -1);

    /**
     * Current module context (changes only in open/close methods).
     */
    private Module currentModule = noModule;

    /**
     * Map of all modules (key: path, value: Module object).
     */
    private final Map<String, Module> modules = new HashMap<>();

    /**
     * Stack for detecting circular dependencies during module import.
     */
    private final Stack<Module> circularImportDetectionPath = new Stack<>();


    int globalModuleIndex = 0;

    /**
     * Creates a new module or returns an existing one from the map.
     * @param name name or path of the module
     * @return Module object
     */
    private Module createModule(String name) {
        if (modules.containsKey(name)) {
            return modules.get(name);
        }
        Module newModule = new Module(name, globalModuleIndex++);
        modules.put(name, newModule);
        return newModule;
    }

    /**
     * Checks if a module with the given name exists in project path
     * @param name module name (with .mj extension)
     * @return true if exists, false otherwise
     */
    public boolean existsModuleOnPath(Path path) {
        return Files.exists(path);
    }

    /**
     * Loads a module with the given name if it exists in the modules map, otherwise returns noModule. 
     * Important: we are returning null if the module does not exist in the map because when we are not in any module currentModule is set to noModule so noModule is always on the circularImportDetectionPath, so if we are returning noModule here in case we don't find the module in the map we will always have a circular import detected which is not desired.
     * Assumes the module exists at the path (checked earlier).
     * @param name module name
     * @return Module object or null if not found
     */
    public Module getModule(String name) {
        return modules.get(name);
    }

    /**
     * Opens a module with the given name and sets it as the current module.
     * Detects circular dependencies and returns noModule in that case.
     * @param name module name
     */
    public void openModule(String name){
        Module m = getModule(name);
        circularImportDetectionPath.push(currentModule);
        // check for circular import
        if (m != null && circularImportDetectionPath.contains(m)) {
            // we have a circular import (already in the path)
            currentModule = noModule;
        } else {
            // create/load new module
            currentModule = createModule(name);
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

    /**
     * Converts a file path to a package name by replacing file separators with dots and removing the .mj extension if present.
     * @param fullPath the full path to the module file
     * @return the package name as a string
     */
    public String toPackageName(Path fullPath) {
        String pathStr = fullPath.toString();

        // Remove .mj extension if present
        if (pathStr.endsWith(".mj")) {
            pathStr = pathStr.substring(0, pathStr.length() - 3);
        }

        // Replace OS-specific separator with '.'
        return pathStr.replace(File.separatorChar, '.');
    }

    /**
     * Converts a package name to a file system path, replacing dots with file separators and adding the .mj extension if missing.
     * @param packageName the package name
     * @return the corresponding Path object
     */
    public Path fromPackageName(String packageName) {
        // Replace '.' with OS-specific separator
        String pathStr = packageName.replace('.', File.separatorChar);

        // Add .mj extension if missing
        if (!pathStr.endsWith(".mj")) {
            pathStr += ".mj";
        }

        return Paths.get(pathStr);
    }

    /**
     * Removes the file extension from the given path.
     * @param path the path from which to remove the extension
     * @return the path without its file extension
     */
    public Path removeExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);

        return path.getParent() != null
                ? path.getParent().resolve(baseName)
                : Paths.get(baseName);
    }

    /**
     * Applies the given SymbolTableVisitor to all modules managed by this handler.
     * This is typically used to traverse or print the contents of all modules.
     *
     * @param stv the SymbolTableVisitor to apply to each module
     */
    public void dumpModules(SymbolTableVisitor stv) {
        for (Module module : modules.values()) {
            module.accept(stv);
        }
    }
}

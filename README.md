# MicroJava-Libraries

This repository contains Java libraries for the MicroJava compiler project, including symbol table and runtime components. The libraries are organized to support modular compilation and reuse in MicroJava-related tools and compilers.

## Structure

```
rs/
  etf/
    pp1/
      mj/
        runtime/         # MicroJava runtime classes
      symboltable/       # Symbol table implementation and related utilities
        concepts/        # Core symbol table concepts (Obj, Scope, Struct, ...)
        factory/         # Symbol table factory classes
        structure/       # Data structures for symbol table
        visitors/        # Visitors for symbol table traversal and dumping
scripts/
  buildJars.ps1      # PowerShell script to build boh jars (symboltable and mj-runtime)
```

## Building the Libraries

Use the provided PowerShell scripts to compile and package the libraries:

- **Symbol Table:**
  ```powershell
  ./scripts/build-symboltable.ps1
  ```
  This will compile all Java files under `rs/etf/pp1/symboltable` and package them into `symboltable.jar`.

The resulting JAR files are placed in the `../MicroJava-Compiler/lib` directory (relative to the project root). Feel free to change destination directory to desired destination.

## Usage

Include the generated JAR files in your MicroJava compiler or related projects by adding them to your classpath.

## License

This project is provided for educational and research purposes. See the LICENSE file for details.

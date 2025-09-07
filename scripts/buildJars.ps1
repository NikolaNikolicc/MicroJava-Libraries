# build-symboltable.ps1
$ErrorActionPreference = "Stop"

# Move to project root
Set-Location -Path "$PSScriptRoot\.."

$symboltableJavaFiles = Get-ChildItem -Recurse -Path "symboltable" -Filter "*.java" | ForEach-Object { $_.FullName }

javac -d symboltable $symboltableJavaFiles
if ($LASTEXITCODE -ne 0) { Write-Host "Symboltable compilation failed!"; exit 1 }

$destPath = "..\MicroJava-Compiler\lib"
# Ensure destination folder exists
if (-not (Test-Path $destPath)) {
	New-Item -ItemType Directory -Path $destPath | Out-Null
}

# Package into new symboltable.jar in project root
jar cf (Join-Path $destPath "symboltable.jar") -C "symboltable" .

Write-Host "Build for symboltable completed successfully (symboltable.jar created)."

# Find all .java files under rs/etf/pp1
$runtimeJavaFiles = Get-ChildItem -Recurse -Path "mjruntime" -Filter "*.java" | ForEach-Object { $_.FullName }

# Compile all .java files, preserving package structure
javac -d mjruntime -classpath "symboltable" $runtimeJavaFiles
if ($LASTEXITCODE -ne 0) { Write-Host "Compilation failed!"; exit 1 }

# Package into new mj-runtime.jar in project root
jar cf (Join-Path $destPath "mj-runtime.jar") -C "mjruntime" .

Write-Host "Build for mj-runtime completed successfully (mj-runtime.jar created)."

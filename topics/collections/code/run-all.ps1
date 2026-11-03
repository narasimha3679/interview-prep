# Compiles and runs every collections demo, printing a header per class.
$ErrorActionPreference = "Stop"

$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-17.0.19.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Set-Location -Path $PSScriptRoot

Write-Host "Compiling..." -ForegroundColor Cyan
javac *.java

$demos = @(
    'CollectionHierarchy',
    'ListDemos',
    'SetDemos',
    'MapDemos',
    'EqualsHashCodeDemo',
    'ComparatorDemo',
    'ConcurrentModificationDemo',
    'GotchasDemo'
)

foreach ($demo in $demos) {
    Write-Host "`n===== $demo =====" -ForegroundColor Yellow
    java $demo
}

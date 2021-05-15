# Welcome to Hyperion

The special task processing tool

## Main goals

At lot of things are planned (and already started):

 - written in Java running on all platforms
 - document in YAML format
 - supporting tasks
    - Powershell (code as .ps1 or embedded, Windows)
    - Batch (code as .bat or .cmd or embedded, Windows)
    - JShell (code as .java or .jsh or embedded, all platforms)
    - Groovy (code as .groovy or embedded, all platforms)
    - Kotlin (code as .kt or embedded, all platforms)      
    - file system operation (copy, move, delete of files and folders)
    - storing results into a task variable (optional with regex)
    - Docker
 - supporting task groups running tasks
    - in order
    - or in parallel
 - supporting models (flexible structure under your control)
 - supporting matrix (running same task groups multiple times for different variables)
 - supporting templating (reference values from variables or the model)
 - tags on tasks and task groups to allow filtering
 - conditional task execution
 - remote execution (client/server mode)
 - reports

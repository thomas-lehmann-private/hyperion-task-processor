# Application

## Introduction

The hyperion tool allows processing a YAML document with tasks. There can be many tasks
like:

 - Powershell (functional on Windows only)
 - Batch (functional on Windows only)
 - Groovy (all platforms because: embedded)
 - JShell (all platforms because: embedded)

That's the current list and will be updated when new tasks will be added.

## Command line interface

The current command line options look like following:
```
java -jar hyperion-1.0.0-SNAPSHOT.jar [global options] [command [command options]]
    version: 1.0.0-SNAPSHOT, build timestamp: 2021-05-20 03:48
    author: Thomas Lehmann <thomas.lehmann.private@gmail.com>

Global options:
    -h,      --help        - displaying this help
             --third-party - displaying used 3rd party libraries
    -t<str>, --tag=<str>   - provide tag to filter tasks [repeatable]

List of available commands:
    run - Running one document with tasks to be processed

Options for command 'run':
    -f<path>, --file=<path> - Document with tasks to be processed [required]
```

Also those help should be good enough to help on usage here a few notes:

 - if you use --help you get the help. However, if you would also specify the command "run"
   you are forced to specify the path of the document too.
 - Global options have to be specified before a command otherwise options would be
   interpreted as option related to a command and --help would be unknown then.
 - You can specify one command only.
 - Specifying a tag all tasks will run that have that tag only. Tasks with other
   tags or even without tags will be ignored.

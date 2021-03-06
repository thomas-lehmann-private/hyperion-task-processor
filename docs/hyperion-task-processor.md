# Application

## Introduction

The hyperion task processor tool allows processing a YAML document with tasks. There can be many tasks
like:

 - Powershell (functional on Windows only, for the moment)
 - Batch (functional on Windows only)
 - Shell (functional on Unix only)
 - Groovy (all platforms because: embedded)
 - JShell (all platforms because: embedded)

That's the current list and will be updated when new tasks will be added.

## Command line interface

The current command line options look like following:
```
java -jar hyperion-task-processor-2.0.0-SNAPPSHOT.jar [global options] [command [command options]]
    version: 2.0.0-SNAPSHOT, build timestamp: 2021-08-06 03:38
    author: Thomas Lehmann <thomas.lehmann.private@gmail.com>

Global options:
    -h,      --help                    - displaying this help
             --timeout-taskgroup=<int> - timeout for each taskgroup (minutes)
    -t<str>, --tag=<str>               - provide tag to filter tasks [repeatable]

List of available commands:
    run          - Running one document with tasks to be processed
    thirdparty   - Reporting the used third-party libraries
    capabilities - Reporting capabilities of system where Hyperion should run
    serve        - Running Hyperion task processor in server mode

Options for command 'run':
    -f<path>, --file=<path>           - Document with tasks to be processed [required]
              --temporary-path=<path> - Defining temporary path

Options for command 'serve':
    -p<int>, --port=<int> - port to run server on
```

Also those help should be good enough to help on usage here a few notes:

 - if you use --help you get the help. However, if you would also specify the command "run"
   you are forced to specify the path of the document too.
 - Global options have to be specified before a command otherwise options would be
   interpreted as option related to a command and --help would be unknown then.
 - You can specify one command only.
 - Specifying a tag all tasks will run that have that tag only. Tasks with other
   tags or even without tags will be ignored.

## Run command

Introduced in version: 1.0.0

The **run** command allows to run one document with tasks.

 - the required **--file** option is the path and filename of the **YAML** document
   containing the tasks.
 - the optional **--temporary-path** option allows you to specify a different path
   for temporary files than specified by the system. The path must exist and
   is required to be a directory. You have to ensure that all necessary permissions
   are given (as they are usually given for the default temporary path).

## Thirdparty command

Introduced in version: 1.0.0

The **thirdparty** command prints all used third party components.
Please also have a look at [build](build.md); dependencies are listed there too
additionally with url and license.

## Capabilities command

Introduced in version: 1.0.0

The **capabilities** command prints all system capabilities relevant for the
Hyperion tool. An example output might look like following:

```
> java -jar hyperion-task-processor-1.0.0-SNAPSHOT-shaded.jar capabilities
Java:                AdoptOpenJDK-11.0.11+9 (class version=55.0)
Operating System:    Windows 10 (arch=amd64)
Groovy (embedded):   3.0.7
Docker:              19.03.8, build afacb8b
Powershell:          5.1.19041.1023
```

## Serve command

Introduced in version: 2.0.0

The **serve** command runs the Hyperion task processor as a REST server.
The default port is 8000 but can be changed by using the parameter --port.

See [REST api](rest-api.md) for documentation about available requests.

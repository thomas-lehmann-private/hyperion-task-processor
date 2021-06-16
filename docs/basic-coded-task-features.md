# Basic code tasks

Coded task are those where you can put code on a task. Currently supported
are:

 - [Grooovy](groovy.md)
 - [Powershell](powershell.md)
 - [Windows Batch](windows-batch.md)
 - [Unix](shell.md)
 - [Docker image](docker-image.md)
 - [Docker container](docker-container.md)

Following attributes are commonly available:

 - **title** - the title is optional and is used for logging to show which task is running
 - **code** - the code depends on implementation (see list above). Optional the code also
   might be a valid path and filename. The code (currently the embedded one only) also
   allows [templating](templating.md).
 - **tags** - an optional list with texts that can be used to filter tasks.
   See [hyperion](hyperion.md) for the command line options on how to use tags.
 - **variable** - an optional variable definition. If not provided the default
   name of the variable is "**default**" and the whole stdout of the task (process)
   is captured.
   
# Variable

A variable has following attributes:

 - **name** optional name of the variable. The default name is "**default**".
 - **regex** optional Java regex. The default is to capture the whole stdout of the task (process).
 - **group** optional regex group. The default regex group is 0.
 - **linebyline** optional boolean to apply regex per line instead on whole captured stdout.
   The default is false.

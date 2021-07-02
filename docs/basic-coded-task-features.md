# Basic code tasks

Coded task are those where you can put code on a task.
Currently supported are:

 - [Grooovy](groovy.md)
 - [Powershell](powershell.md)
 - [Windows Batch](windows-batch.md)
 - [Unix](shell.md)
 - [Docker image](docker-image.md)
 - [Docker container](docker-container.md)

All Hyperion tasks have [basic task features](basic-task-features.md).
Following attributes are additional available for coded tasks:

 - **code** - the code depends on implementation (see list above). Optional the code also
   might be a valid path and filename. The code also allows [templating](templating.md).
   You can write multiline code by using a **pipe** symbol in the first line:

```yaml
- type: groovy
  code: |
      println 'hello world 1'
      println 'hello world 2'
      println 'hello world 3'
```

# Provide coded tasks

### Document meta data
 - **Type**: Requirement
 - **Id**: 1
 - **Context**: task feature

### Description

As **devops engineer** you should be able to write coded tasks with following
standard parameters:

 - **title** - the title of the task being logged
 - **code** - the concrete code to be executed or the path or filename of the script
              (depends on implementation of concrete task)
 - **variable** - optional to define how the stdout is captured
              (default is to capture all)
 - **tags** - optional a list of strings to allow filtering for concrete tasks
       
#### Variable
 - the **name** attribute that can be addressed in other tasks using templating.
   (the default name is "**default**")
 - the **regex** attribute represent the Java regex to define what to captured from stdout.
   (the default is to capture all)
 - the **group** attribute is an integer to define which regex group to use.
   (the default regex group is 0)
 - the **linbebyline** attribute is a boolean. When true the regex is applied on each
   single line to allow filtering otherwise the regex is applied on the whole output
   (default is false)

### Contraints

No constraints.

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

# providing a copy file task

### Document meta data
 - **Type**: Requirement
 - **Id**: 6
 - **Context**: task feature

### Description

As a **devops engineer** you should be able to copy a file from source to
destination. The standard parameters are those of a basic task
with additional following parameters:

 - **source** - the source location of a file that must exist. The attribute is required.
                [Templating](templating.md) is supported on this attribute.
 - **destination** - the destination location of a file. The attribute is required.
                     [Templating](templating.md) is supported on this attribute.
 - **overwrite** - optional boolean attribute allowing you to overwrite the destination
                  file when the file exists. The default should be false;
                  when the destination path exists the task operation should fail.
- **ensure-path** - optional boolean attribute ensuring that the destination path
                  does exist. The default should be false; when the path doesn't
                  exist the task operation should fail.
  **destination-is-directory** - optional boolean attribute telling the task how
                  to interpret the value defined in the attribute "destination".
                  The default is set to true; the filename of the source is then
                  appended to the destination.

### Constraints

 - the file operation depends on the permissions given on the file system.

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

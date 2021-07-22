# Provide a download task

### Document meta data
 - **Type**: Requirement
 - **Id**: 14
 - **Context**: file task feature
 - **Milestone**: 2.0.0

### Description

As a **devops engineer** you should be able to write content to a file.
The standard parameters are those of a basic task with additional following parameters:

- **url** - required parameter with a valid url where to download the file from.
- **destination** - required parameter with path and filename where to write the content.
- **overwrite** - optional boolean attribute allowing you to overwrite the destination
  file when the file exists. The default should be false;
  then the task operation should fail when the destination path exists.
- **ensure-path** - optional boolean attribute ensuring that the destination path
  does exist. The default should be false; then the task operation should fail
  when the path doesn't exist .
   
### Constraints

 - url end point must be reachable and file to download must exist.
 - the file operation depends on the permissions given on the file system.

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

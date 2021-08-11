# Provide Hyperion task processor as a server

### Document meta data
 - **Type**: Requirement
 - **Id**: 16
 - **Context**: server feature
 - **Milestone**: 2.0.0

### Description

As a **devops engineer** you should be able to run the tool in server mode. As an essential
function it must be possible to send a document with tasks to this server, and it also
must be possible to ask for the success of that processing.

Following options should be passed for the document requests (same as for run command):

 * **tag** - optional and repeatable for filtering tasks by it. 
 * **timeout** - optional for limiting execution time of a task group.
   
### Constraints

 * The choosen port where to run the server must be free.

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

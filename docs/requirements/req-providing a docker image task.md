# Providing a Docker image task

### Document meta data
 - **Type**: Requirement
 - **Id**: 4
 - **Context**: task feature

### Description

As a **devops engineer** you should be able to create a docker image by a task.
The **Dockerfile** code can be embedded in the document or referencing
a valid path and filename. The standard parameters are those of a coded task
with additional following parameters:

 - **repository-tag** - for details please read the external documentation (see links). 
   
### Contraints

Docker is required to be installed, running and the command should be in the search path. 

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

### Dependencies

 - [Provide coded tasks](req-provide%20coded%20tasks.md)


### External Documentation

 - https://docs.docker.com/engine/reference/builder/
 - https://docs.docker.com/engine/reference/commandline/tag/

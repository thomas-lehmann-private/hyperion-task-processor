# Provide a Docker container task

### Document meta data
 - **Type**: Requirement
 - **Id**: 11
 - **Context**: task feature

### Description

As a **devops engineer** you should be able to create a docker container by a task.
The script code can be embedded in the document or referencing
a valid path and filename. The standard parameters are those of a coded task
with additional following parameters:

 - **image-name** - required parameter. It's the name of the Docker image.
 - **image-version** - optional parameter. It's the version of the Docker image.
   If not specified the default version is 'latest'.
 - **platform** - optional parameter. The default is 'unix'. On Windows you also
   can use Docker to run so called Windows container. For the moment you can use
   use the one or the other only by switching manually to the platform you prefer.
   There's an experimental option in Docker allowing you to specify the platform on command
   line to run both but - since it is experimental - it is not yet supported by
   the task.

The Docker container runs in foreground mode. When the process has finished the Docker
container is automatically removed. The current working path is mapped into the Docker container as /work.
   
### Constraints

 * Docker is required to be installed, running and the command should be in the search path.
 * You have to choose manually which platform to use: Unix or Windows container.
 * No ports are published to outside the moment.

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

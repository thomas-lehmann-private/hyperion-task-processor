# Run docker container in detached mode

### Document meta data
 - **Type**: Requirement
 - **Id**: 15
 - **Context**: docker task feature
 - **Milestone**: 2.0.0

### Description

As a **devops engineer** you should be able to run a docker container in detached mode.
There the optional boolean attribute **detached** should be available, defaulted to **false**.

   
### Constraints

See [Provde a docker container task](req-provide%20a%20docker%20container%20task.md).

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

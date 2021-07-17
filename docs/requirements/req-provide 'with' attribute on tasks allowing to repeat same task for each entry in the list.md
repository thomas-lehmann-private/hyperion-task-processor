# Provide 'with' attribute on tasks allowing to repeat same task for each entry in the list

### Document meta data
 - **Type**: Requirement
 - **Id**: 5
 - **Context**: task feature
 - **Milestone**: 1.0.0

### Description

As a **devops engineer** you want to be able to define a list of entries that any
task is repeated for each entry. You can use [templating](templating.md) to access
current list entry. Each list entry can be one of:
 - a simple value
 - a map of something

The map works as the model. A map can have key/value and the value might be:

 - a simple value
 - a map of something
 - a list of something

Each value will be rendered (see [templating](templating.md))

Each entry might be different in its structure (if you prefer).
The tool does not check (validate) for this so you
have take care in the templating logic then.

### Constraints

No constraints.

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

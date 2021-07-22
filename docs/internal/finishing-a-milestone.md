# Finishing a milestone

Finishing a milestone is done in following phases:

## Phase 1

 - **Review documentation**
    - it's the last chance to eventually add missing documentation.
    - also looking for missing requirements.
    - also looking for missing examples.
 - **Full local review on source code** - it's the last chance to eventually fix things or to do small refactorings.
 - **Review code coverage** - it's the last chance to eventually improve the code coverage.

## Phase 2

 - **Changing the version to release**
    - a pom.xml modification.
    - a modification in the workflow yaml for the release (hyperion-task-processor-release-action.yaml).  
    - it can be done without branch.
 - **Updating the github pages**
 - **Tagging the repository with release version** - it should trigger the related workflow that
   does also one build and finally publishing the artifacts on Github.

----
 **Finish**:
  - Announcing new version at [here](https://github.com/thomas-lehmann-private/hyperion/discussions/categories/announcements).
  - Also communicate that the next milestone is planned and already has stories.
  - Ask the community for interest in new features not yet in the milestone.
  - Closing the milestone. 
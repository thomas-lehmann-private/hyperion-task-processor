# Docker image task

This doesn't intend to explain Docker. Please refer to official Docker
documentation for details on it. It's about the Docker container task.
The [basic coded task features](basic-coded-task-features.md) are language
independent and explained in the link including:

- Having an optional **title** attribute
- Using the **code** in attribute with same name
- Having an optional **tag** attribute
- Having an optional **variable** attribute
- Having an optional **with** attribute

A few things to mention here about Docker:

- Docker is an external tool that must exist otherwise you cannot
  use the task; even more the Hyperion tool will throw an error
  when the Docker tool has not been found.
- The default is to run create Unix images. On Windows you are
  able to switch to Windows platform so you could run Windows batch code inside
  the Docker container that does prepare the image. Usually you can switch to either
  use the one platform or the other platform but not both at same time. From documentation, it seems an
  experimental feature that can be turned on allowing you to specify the
  parameter **--platform** with the values **windows** or **unix** so you could
  use both at same time. Since it is experimental and I cannot rely on yet that this
  is turned on by default I don't generate that parameter; means you cannot
  use both at same time for now.
  
## The basic coded task features

Following features are basic task features:

 - variable
 - templating (model, matrix variables, task group variables)
 - tagging

Those features are documented in [basic coded task features](basic-coded-task-features.md),


## The minimal example

It doesn't differ much from other task. Following details:

 - The attribute **repository-tag** defines the **repository** and **tag** of
   the image when the build of the image has been successful.
 - The following code also can be found in the **example** folder.
   The batch **hello-world-docker-image-for-unix.cmd** demonstrates what the
   Hyperion tool is basically doing.
 - The **code** also can reference a path and filename of an existing Dockerfile.

```yaml
---
taskgroups:
  - title: test
    parallel: false
    tasks:
      - type: docker-image
        repository-tag: hello-world:latest
        code: |
          FROM centos:latest
          COPY hello-world.sh .
      - type: docker-container
        title: a simple example
        code: /hello-world.sh
        image-name: hello-world
```

## External Documentation

 - https://docs.docker.com/engine/reference/builder/
 - https://docs.docker.com/engine/reference/commandline/tag/

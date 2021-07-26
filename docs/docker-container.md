# Docker container task

This doesn't intend to explain Docker. Please refer to official Docker
documentation for details on it. It's about the Docker container task.
The docker container does offer  [basic coded task features](basic-coded-task-features.md) 
. In addition following attributes are available:

 - **image-name** - the mandatory attribute specifies the name of the docker image to use
 - **image-version** - the optional string attribute specifies the version of the docker image to use.
   If not specified the default is **latest**.
 - **detached** - the optional boolean attribute specified whether to run the docker container
   in detached (background) mode. If not specified the default is **false**.
 - **platform** - the optional parameter for what kind of Docker container to run.
   If not specified the Default is **unix** (read a bit later about platform).
   It does have the effect that the generated temporary script - that will be executed
   by the Docker container - is either passed to **cmd /c** or **sh -c**.

A few things to mention here about Docker:

 - Docker is an external tool that must exist otherwise you cannot
   use the task; even more the Hyperion tool will throw an error
   when the Docker tool has not been found.
 - The default is to run Docker container with Unix images. On Windows you are
   able to switch to Windows container so you could run Windows batch code inside
   the Docker container. Usually you can switch to either use the one platform or
   the other platform but not both at same time. From documentation, it seems an
   experimental feature that can be turned on allowing you to specify the
   parameter **--platform** with the values **windows** or **unix** so you could
   use both at same time. Since it is experimental and I cannot rely on yet that this
   is turned on by default I don't generate that parameter; means you cannot
   use both at same time for now.
 - Also on Windows you have to be aware that sharing a drive is to be adjusted in
   the settings of Docker. Be aware that right now Hyperion differs two mounts which
   are currently done automatically:
    - the mount of the current working directory (as /work)
    - the mount of the path usually used for generating temporary files and folders
      by the standard Java API. At least it means **you might have to allow more than
      one Windows drive to be shared with the Docker container**. Since you probably
      want to run Hyperion processing automatically it is advisable to adjust this in
      advance otherwise the Docker process is trying to ask you via Dialog.

## The minimal example

It's really easy and doesn't differ much from other task. Following details:

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: docker-container
        code: echo "hello world!"
        image-name: debian
```

## Running detached

It's nothing you would do in a real environment but that simple example demonstrates
how it does work:

 - After running you can do a "docker ps -a" and you will see the container running.
 - After 30 seconds the container will automatically go away since --rm is also used.

You can use this for running a Database or an application server in it.
The variable (also not specified here) will contain the id of the container that can
be used to work on the container (example: using the id to stop the container).

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: docker-container
        code: sleep 30
        image-name: debian
        detached: true
```

## Minimal example with tags

The application can be called with the repeatable option --tag. Specifying the
those filter task will be executed only contain those tags. Task with other tags
or even without any tags will be ignored then.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: docker-container
        code: echo "hello world!"
        image-name: debian
        tags:
          - simple
          - example         
```

## Example with variable

The example with variable shows how to use the variable to extract information.
It's exactly the same way as it does work for other tasks.
You can specify the regex for filtering and - if required - the regex group;
the default group is 0.

```yaml
---
taskgroups:
  - title: test
    tasks:
        - type: docker-container
          title: a simple example
          code: echo "---> this is a demo <---"
          image-name: debian
          variable:
              name: test2
              regex: ">(.*)<"
              group: 1
```
## Example with templating

The task group does store a map of key/value where key is the name of the variable, and
the value is a variable. If you do not specify a name for a variable the name is
'**default**'. In the following example the first task does write 'hello world!' into
the variable, and the second task does evaluate the value from the first task.
Of course the second one write into the same variable its output since also there
no name has been specified.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: docker-container
        title: a simple example 1
        code: echo "hello world!"
        docker-image: debian
      - type: dockier-container
        title: a simple example 2
        code: echo "{{ variables.default.value }}"
        docker-image: debian
```

In addition, you can evaluate the model if you have one.

```yaml
---
model:
  description: some description

taskgroups:
  - title: test
    tasks:
      - type: docker-container
        title: a simple example 1
        code: echo "{{ model.attributest.description }}"
        docker-image: debian
```

The rules on how to access the individual elements of a model are
explained [here](templating.md).

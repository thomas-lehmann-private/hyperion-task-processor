# Docker container task

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

 - The parameter **image-name** is required and should be the name of the Docker image.
 - The parameter **imager-version** is optional and should be the version of the Docker image.
   If not specified the version is set to **latest**.
 - The parameter **platform** is optional and can be either **windows** or **unix**. 
   If not specified the platform is set to **unix**.
   It does have the effect that the generated temporary script - that will be executed
   by the Docker container - is either passed to **cmd /c** or **sh -c**.
   As already mentioned you cannot use both platforms in one document since **--platform**
   is not yet generated. So you have to decide which platform Docker should use by switching to
   it manually. Once the experimental status goes away that might change.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: docker-container
        code: echo "hello world!"
        image-name: debian
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

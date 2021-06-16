# Windows Batch task

This page doesn't intend to explain Windows batch. So please refer to official Windows batch
documentation for details on it. It's about the Windows batch task.

## Minimal example

The minimal example does not require a variable but the task
does have one (always) with the name '**default**'.
The default regex is the whole text, and the default group is 0.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: batch
        code: echo "hello world!"
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
      - type: batch
        code: echo "hello world!"
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
        - type: batch
          title: a simple example
          code: echo "---> this is a demo <---"
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
      - type: batch
        title: a simple example 1
        code: echo "hello world!"
      - type: batch
        title: a simple example 2
        code: echo "{{ variables.default.value }}"
```

In addition, you can evaluate the model if you have one.

```yaml
---
model:
  description: some description

taskgroups:
  - title: test
    tasks:
      - type: batch
        title: a simple example 1
        code: echo "{{ model.attributest.description }}"
```

The rules on how to access the individual elements of a model are
explained [here](templating.md).


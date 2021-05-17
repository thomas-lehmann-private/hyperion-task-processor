# Welcome to the Groovy task

## Minimal example

The minimal example does not require a variable but the task
does have one (always) with the name '**default**'.
The default regex is the whole text, and the default group is 0.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: groovy
        title: a simple example
        code: println 'hello world!'
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
        - type: groovy
          title: a simple example
          code: println '---> this is a demo <---'
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
      - type: groovy
        title: a simple example 1
        code: println 'hello world!'
      - type: groovy
        title: a simple example 2
        code: println '{{ variables.default.value }}'
```

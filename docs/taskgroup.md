# Taskgroup

A task group basically has three information:

  - a required title
  - an optional boolean flag indicating whether tasks should run in parallel or in order
  - a required list of tasks

## Minimal example

The three tasks in this example are **running one after the other
in the specified order**.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: groovy
        code: println 'hello world 1!'
      - type: groovy
        code: println 'hello world 2!'
      - type: groovy
        code: println 'hello world 3!'
```

**Please note**: Also no variables have been specified the variables are there.
The name of those variables is then "default". You are able to evaluate the value
of a previously set variable with that name using templating
with following expression: **{{ variables.default.value }}**.

**See also**: [Templating](templating.md)

## Running tasks in parallel

The three tasks in this example are running in parallel.
The output will be the inverse order of what has
been specified because of the sleep statements.

```yaml
---
taskgroups:
  - title: test
    parallel: true
    tasks:
      - type: groovy
        code: |
          sleep(2000)
          println 'hello world 1!'
      - type: groovy
        code: |
          sleep(1000)
          println 'hello world 2!'
      - type: groovy
        code: |
          println 'hello world 3!'
```

**Please note**: Also the variables are not specified here they are given. 
It means that the stdout of the tasks is always stored. If you don't specify 
the variable yourself then name of the task variable is always "default".
Running tasks in parallel the situation is unfavorable since you cannot always
rely on which value will be written into the variable except you control
the order of the execution like shown in the example. For that situation a warning
is logged by the tool to inform you that you have duplicate variable names.

Of course this also will happen if you specify the same name for variables yourself
multiple times.

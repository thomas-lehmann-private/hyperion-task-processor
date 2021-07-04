# Basic tasks features

Following attributes are commonly available:

 - **title** - the title is optional and is used for logging to show which task is running.
    For the title [templating](templating.md) is supported.
 - **tags** - an optional list with texts that can be used to filter tasks.
   See [hyperion](hyperion-task-processor.md) for the command line options on how to use tags.
 - **variable** - an optional variable definition. If not provided the default
   name of the variable is "**default**" and the whole stdout of the task (process)
   is captured.
 - **with** - an optional list with values. For each entry the task will be executed.
   Through [templating](templating.md) th current value of the entry and the zero
   based index of the entry will be available in the **code** (**{{ with.index }}** and **{{ with.value }}**).
   Finally the use of it does not differ from writing as many tasks as you have "with"
   entries one after the other; it's a short way to write it using the attribute. In a task group where
   **parallel** is set to **false**  the tasks are running in the order they appear
   otherwise they run in parallel (including the "with" tasks). The value of the "with" list is
   not checked to be equal in its structure; the user has to care for it when this is the case.
   On each with value (structure) [templating](templating.md) is supported.
   
# Variable

A variable has following attributes:

 - **name** optional name of the variable. The default name is "**default**".
 - **regex** optional Java regex. The default is to capture the whole stdout of the task (process).
 - **group** optional regex group. The default regex group is 0.
 - **linebyline** optional boolean to apply regex per line instead on whole captured stdout.
   The default is false.

# Using the "with" attribute

The following example demonstrates one way to handle the situation that two entries
are different in its structure. The used Template Engine (see [build](build.md)) allows
to use an **if/else**. This example is also used in the Unittests.

```yaml
- type: groovy
  title: a simple example
  code: |
    {% if with.index == 0 %}
    println '{{ with.value }}'
    {% else %}
    println '{{ with.value.attributes.test3.attributes.test4.value }}'
    {% endif %}
  with:
    - hello world 1!
    - test1:
        - hello world 2!
      test2: hello world 3!
      test3:
        test4: hello world 4!
```

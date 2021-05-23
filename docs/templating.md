# Templating

## Introduction

One of the most powerful feature of the Hyperion tool is the templating that
allows you to the dynamic replacement in the code of the part.

Hyperion offers currently two data sources:

 - the model
 - the task group variables

The **model** is a kind of freestyle hierarchical structure you can define to model
the required data when running your tasks. The task group variables are initially
empty and filled whenever a task has finished successfully.

## Accessing the variables

Assuming you missed defining a variable on a task the name of a variable is "default".
You then can access the variable in your task code with **{{ variables.default.value }}**.

Assuming you evaluate a special path in your task writing it to a named variable "path"
the possible evaluation in the next task would be **{{ variables.path.value }}**.

It depends on of course whether tasks are running ordered or in parallel whether one
task can read a variable of a previous task. Even more it's unpredictable for parallel
tasks with same names to know which value will be given when you access it.

## Accessing the model

The model - once understood - is a great way to modularize your task code also allowing
reuse. For the next examples a few explanations:

 - **attributes** - the term means that the object behind stores for a key a value;
   of course the can be multiple keys. The value can be a string, a number, a boolean,
   a list or - again - a key/value object.
 - **values** - the means a list. The value can be a string, a number, a boolean or
  a key/value object.

```yaml
---
model:
    description: this is a simple example
```

Trying to access this description you have to write **{{ model.attributes.description }}**.

```yaml
---
model:
    subModel:
        description: this is a simple example
```

Here the value you get with **{{ model.attributes.subModel.attributes.description }}**

```yaml
---
model:
    descriptions:
        - this is a simple example
```
Here the value you get with **{{ model.attributes.descriptions.values[0] }}**.

```yaml
model:
    actions:
        - name: action A
          command: println 'hello world 1 !'
```
Here the name and the command can be accessed this way:
 - **{{ model.attributes.actions.values[0].attributes.name }}**
 - **{{ model.attributes.actions.values[0].attributes.command }}**

If you want to know more about how this works then please also read here: <br/>
https://pebbletemplates.io/wiki/guide/basic-usage/ <br/>
(it shows internal usage as well as what you can do with it)

## (Near) Future enhancements

- Matrix variables (a matrix runs all task groups again for a different set of variables)
- Defining task group variables in the document

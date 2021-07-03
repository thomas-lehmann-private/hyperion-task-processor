# File copy task

In addition to the [basic task features](basic-task-features.md) the file
copy task has following attributes:

 - **source** - required attribute with the path and filename that must exist.
                The attribute does allow [templating](templating.md).
 - **destination** - required attribute with the path of filename or directory.
                The attribute does allow [templating](templating.md).
                The meaning of the value will be specified with the boolean attribute
                "destination-is-directory"; when it is set to true the filename of
                the source is appended to the path otherwise it is assumed that you
                have specified a path and filename.
 - **overwrite** - optional boolean attribute. When set to true (default is false)
                 the copy operation will overwrite the destination otherwise
                 the task might fail when the destination already exists.
 - **ensure-path** - optional boolean attribute. When set to true (default is false)
                 the path of the filename or directory will be created when missing
                 otherwise the task might fail when the path does not exist.
   **destination-is-directory**  optional boolean attribute. When set to true
                 (default is true) then the path defined in "destination"
                 will be interpreted as directory.

## Minimal examples

The following example does a **copy and rename** of a file at same path;
when the destination file exists the task operation does fail.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: copy-file
        source: c:\temp\test1.txt
        destination: c:\temp\test2.txt
```

The following example copies a file (name remains) to a destination
path; when the destination path doesn't exist it will be created.
The expected result is here: c:\temp\test\test1.txt. 
The attribute **destination-is-directory** is required here otherwise
the task doesn't know that the defined destination is a path.
When the destination file exists the task operation does fail.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: copy-file
        source: c:\temp\test1.txt
        destination: c:\temp\test
        destination-is-directory: true
        ensure-path: true
```

The following example does a **copy and rename** of a file to a different path;
when the destination path doesn't exist it will be created.
The expected result is here: c:\temp\test\test2.txt.
When the destination file exists the task operation does fail.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: copy-file
        source: c:\temp\test1.txt
        destination: c:\temp\test\test2.txt
        ensure-path: true
```

That's the same example as the first one with the additional attribute **overwrite**
set to true; when the destination exists it will be overwritten.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: copy-file
        source: c:\temp\test1.txt
        destination: c:\temp\test2.txt
        overwrite: true
```

## Variable

Usually a variable is used to capture the stdout; the default is then to capture all.
In this case there is no stdout output. For this task the variable will contain
the final path and filename when the copy operation has been successful.
All variable options as explained in
[basic task features](basic-task-features.md) are still valid.

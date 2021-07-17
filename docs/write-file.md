# Write File Task

In addition to the [basic task features](basic-task-features.md) the file
copy task has following attributes:

- **content** - required attribute with the content to write to file.
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

## Minimal examples

The following example does write some text to a file;
when the destination file exists the task operation does fail.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: write-file
        content: hello world 1!
        destination: c:\temp\test.txt
```

The following example allows overwriting an existing file and and missing folders
will be automatically created.

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: write-file
        content: hello world 1!
        destination: c:\temp\temp\temp\test.txt
        overwrite: true
        ensure-path: true
```

## Variable

Usually a variable is used to capture the stdout; the default is then to capture all.
In this case there is no stdout output. For this task the variable will contain
the final path and filename when the copy operation has been successful.
All variable options as explained in
[basic task features](basic-task-features.md) are still valid.

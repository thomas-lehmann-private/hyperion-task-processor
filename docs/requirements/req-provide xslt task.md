# Provide xslt task

### Document meta data
 - **Type**: Requirement
 - **Id**: 12
 - **Context**: task feature

### Description

As a **devops engineer** you should be able to write a xslt script as task.
It basically requires two attributes:

 - **xsl** - the transformation script (embedded or as path and filename).
             The attribute is required.
             [Templating](templating.md) is supported on this attribute.
 - **xml** - the XML content to transform (embedded or as path and filename).
             The attribute is required.
             [Templating](templating.md) is supported on this attribute.

The result of the transformation is written into the variable.
XSLT 3.0 should be supported.

### Constraints

 - XSLT 3.0 is supported

### Criticality and Risk

!!! warning
    The possibility of something going wrong, and the associated consequences of failure,
    are highly dependent on the user of the Hyperion tool, as using the tool requires that
    the results be verified in a test environment.

    There is a risk that the user will be blocked in his work if the tool does not work
    properly, but there is no guarantee due to the license. Nevertheless, the author is
    extremely interested in correcting all reported errors in the near future.

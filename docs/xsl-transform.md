# XSL Transform Task

In addition to the [basic task features](basic-task-features.md) the file
copy task has following attributes:

 - **xsl** - the required attribute represents an embedded XSL transformation document
             or a valid path and filename of such a document.
             The attribute does allow [templating](templating.md).
- **xsl** - the required attribute represents an embedded XML document
             or a valid path and filename of such a document.
             The attribute does allow [templating](templating.md).

## Minimal examples

The following example does a transformation storing the result into the variable (here the
variable name is 'default').

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: xsl-transform
        xsl: c:\temp\test1.xsl
        xml: c:\temp\test2.xml
```

The following with embedded:

```yaml
---
taskgroups:
  - title: test
    tasks:
      - type: xsl-transform
        xsl: |
          <?xml version="1.0" encoding="UTF-8"?>
          <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                          xmlns:xs="http://www.w3.org/2001/XMLSchema"
                          version="3.0">
              <xsl:output method="html" indent="yes" />
              <xsl:template match="/">
                  <html>
                      <body>
                          <table>
                              <tr>
                                  <th>title</th>
                                  <th>author</th>
                              </tr>
                          <xsl:apply-templates select=".//book"/>
                          </table>
                      </body>
                  </html>
              </xsl:template>
            
              <xsl:template match="book">
                  <tr>
                      <td><xsl:value-of select="title" /></td>
                      <td><xsl:value-of select="author" /></td>
                  </tr>
              </xsl:template>
          </xsl:stylesheet>
        xml: |
          <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
          <books>
              <book>
                  <title>Death on the Nile</title>
                  <author>Agatha Christie</author>
              </book>
            
              <book>
                  <title>Evil Under the Sun</title>
                  <author>Agatha Christie</author>
              </book>
            
              <book>
                  <title>Too Many Cooks</title>
                  <author>Rex Stout</author>
              </book>
            
              <book>
                  <title>The Big Sleep</title>
                  <author>Raymond Chandler</author>
              </book>
            
              <book>
                  <title>The Long Good-Bye</title>
                  <author>Raymond Chandler</author>
              </book>
          </books>
```



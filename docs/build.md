# Build

ä# Quick and easy
The build is organized with Maven. You don't have to install Maven because **Maven Wrapper**
(https://github.com/takari/maven-wrapper) is used. On any system following command is sufficient
as long as Java is given (**minimum Version**: 11):

```
./mvnw
```

On Unix based systems this is the exact filename of the script that is executed. On Windows it's the
**mvnw.cmd** script (anyway you do not have to specify the extension that's why it looks
and feel identical on all systems).

## Maven Profiles

There are two profiles; one for Windows and one for Unix based systems. The main reason for
two profiles is that the code coverage is different since not every test can be executed on
every system (example: The UnixShellTaskTest can run on Unix based systems only). The reason
to use a profile in general is to be able to define default goals, so you do not have
to specify them on command line each time (convenience reason).

## Default goals

The default goals are:

```
clean package javadoc:jar verify
```

 - **clean** - removes the target folder
 - **package** - includes all previous goals like resource filtering, compiling and running tests.
   The package is combining the concrete jar with all dependencies into one jar that allows to
   use it standalone.
 - **javadoc:jar** - generate the Javadoc HTML documentation and the javadoc jar.
 - **verify** - is running all static code analysis (including the analysis of the code coverage).

## Maven Dependencies

### Test dependencies

Those dependencies are available only while testing.

Dependency | Homepage | License | Usage
---------- | -------- | ------- | -----
org.junit.jupiter:junit-jupiter-api:5.5.2 | https://junit.org/junit5/ | [Eclipse Public License - v 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md) | Unittests
org.junit.jupiter:junit-jupiter-engine:5.5.2 | https://junit.org/junit5/ | [Eclipse Public License - v 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md) | Unittests
org.junit.jupiter:junit-jupiter-params:5.5.2 | https://junit.org/junit5/ | [Eclipse Public License - v 2.0](https://github.com/junit-team/junit5/blob/main/LICENSE.md) | Unittests

### Runtime dependencies

Those dependencies are required at runtime and taken by the Maven shade plugin when packaging.

Dependency | Homepage | License | Usage
---------- | -------- | ------- | -----
com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.1 | https://github.com/FasterXML/jackson | [Eclipse Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) | Reading YAML files
ch.qos.logback:logback-classic:1.2.3 | http://logback.qos.ch/ | [Logback License](http://logback.qos.ch/license.html) | Logging
org.apache.commons:commons-lang3:3.12:0 | https://commons.apache.org/proper/commons-lang/ | [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Tool classes
io.pebbletemplates:pebble:3.1.5 | https://pebbletemplates.io/ | [Pebble License](https://github.com/PebbleTemplates/pebble/blob/master/LICENSE) | Template Engine
org.codehaus.groovy:groovy:3.0.7 | http://www.groovy-lang.org/ | [Apache 2.0 License](https://github.com/apache/groovy/blob/master/LICENSE) | Embedded Language for GroovyTask

### Build dependencies

Those dependencies are required while building only.

Dependency | Homepage | License | Usage
---------- | -------- | ------- | -----
pl.project13.maven:git-commit-id-plugin:4.0.0 | https://github.com/git-commit-id/git-commit-id-maven-plugin | [LGPL-3.0 License](https://github.com/git-commit-id/git-commit-id-maven-plugin/blob/master/LICENSE) | Providing Git Information for resource filtering
org.apache.maven.plugins:maven-compiler-plugin:3.8.0 | https://maven.apache.org/plugins/maven-compiler-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Compiling Java code
org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5 | https://maven.apache.org/surefire/maven-surefire-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Running tests
org.jacoco:jacoco-maven-plugin:0.8.6 | https://www.jacoco.org/jacoco/trunk/doc/maven.html | [EPL 2.0](https://www.jacoco.org/jacoco/trunk/doc/license.html) | Code coverage analysis
org.apache.maven.plugins:maven-checkstyle-plugin:3.1.2 | https://maven.apache.org/plugins/maven-checkstyle-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Static coded analysis
com.github.spotbugs:spotbugs-maven-plugin:4.1.3 | https://spotbugs.github.io/spotbugs-maven-plugin/ | [Apache License, 2.0](https://spotbugs.github.io/spotbugs-maven-plugin/licenses.html) | Static coded analysis
org.apache.maven.plugins:maven-pmd-plugin:3.14.0 | https://maven.apache.org/plugins/maven-dependency-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Static code analysis
org.apache.maven.plugins:maven-shade-plugin:3.2.4 | https://maven.apache.org/plugins/maven-shade-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | All in one jar generator
org.apache.maven.plugins:maven-javadoc-plugin:3.2.0 | http://maven.apache.org/plugins/maven-javadoc-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | API Documentation tool
org.apache.maven.plugins:maven-dependency-plugin:3.1.2 | https://maven.apache.org/plugins/maven-dependency-plugin/ | [Apache License. 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Maven dependencies tool

## Github actions

It's surprisingly easy to organize a build for all three platforms and multiple Java versions
with Github actions. The build is written in YAML format and is called **workflow**. Workflows
have to be located at .github/workflows. The workflow for Hyperion is in the file
**.github/workflows/hyperion-build-actions.yml**. Each time you push your changes remote the
workflow automatically starts. The success is then visible by an success/failure icon on each
location where the commit is shown. The history for all actions (including the currently running
one) you can find here: https://github.com/thomas-lehmann-private/hyperion/actions.

The file is really simple and the Github actions in general have good documentation.
For the code coverage the usage is well documentated by the integration itself.
The whole build is organized via a matrix build. All combinations of the values of the
existing lists define one build job. When something goes wrong you can read the logs online
at the individual build.

**Please note**: The **--batch-mode** is really important to reduce the verbose output of the
download operation for each single artefact.

## Jenkins

You also can run your project locally via Jenkins. You simply create a pipelineline
specifying the file url to your local repository. You shoudl have some plugins installed
like **Jacoco**, **Javadoc**, **AnsiColor**, **Built Timeout**, **Green Balls** and
**Workspace Cleanup**. The Jenkins job presents you out-of-the-box trend graphs for
your tests, the code coverage and the static code analyses. Very awesome. The
**Jenkinsfile** (default when you create a pipeline job) is given in the repository.
I hope you agree: It's a small file.

## How to handle things

 - In general violations will break the build, and the common expectation is that issues are correctly resolved.
 - When a Jacoco error is raised it does mean that you have added coded without testing it.
   Decreasing the limit is not acceptable.
 - When your code coverage does get better please increase those limits.
 - When **Checkstyle**, **PMD** or **Spotbug** raise an error for src/main/java the expectation
   is to fix it without any suppression in 99% of all cases. For the 1% where you think you have to
   suppress it the reason has to be well documented.
 - For some sort of error like magic numbers or repeated string literals happening in Unittests it's
   mostly ok when you use suppression.
 - It should never happen that the code is overwhelmed with suppression.

## Links

 - https://docs.github.com/en/actions
 - https://www.jenkins.io/doc/book/pipeline/syntax/
 - https://github.com/takari/maven-wrapper
 
# CLI

In future the code might move to an own individual respository.
Nevertheless it should be documented how it works...

## Defining an option

The creation of an option is best explained by an example:

```java
final var option = CliOption.builder()
    .setLongName("file")
    .setShortName("f")
    .setDescription("Provide document to process")
    .setType(OptionType.STRING)
    .setRequired(true)
    .setRepeatable(false)
    .build();
```

The **build()** will throw an CliException when the validation of your definition has failed.

### Long names

 1. The field is required.
 1. A long name has to be lower case.
 1. You can combine as maximum three so called sub names **namea-nameb-namec** by dashes.
 1. Each subname
    - has to start with a letter; then also numbers may be used (no other characters).
    - it's not allowed to have less than 2 characters and no more than 15 characters.
    
You can use the long name in two forms:

 - --name=value
 - --name value
 
### Short names

 1. The field is option (null or empty counts as not set).
 2. A short name can be upper case or lower case.
 3. You are allowed to define one character only (length equal to 1).

You can use the short name in two forms:

 - -v1234
 - -v 1234

### Description

 1. The field is required
 1. It cannot be null and also not be empty
 1. The length may not exceed 40 characters.

Keep in mind that this description should help the user.

### Repeatable

 - The default is false (not repeatable).
 - A repeatable option means that under given name a list of values is stored.
 - The repeatable flag is shown in the help.

### Required

 - The default is false (not required).
 - A required field means that you have to specify the parameter.
 - The required flag is shown in the help.

**Special scenario**: consider a --help (which should be optional). If you (as an example)
specify the option --file as required the CliParser will throw a CliException if you just
use --help. You can define a command and with such an option (example: run). Only if you
use that command you also have to specify the required option. 

### Type

 - The types are boolean, string, integer, double and path (for now).
 - The default is string.
 - The boolean means that you don't expect a value.
 - The types are currently used for the help only to show what value is expected.

### Validation

The **build()** function checks the rules as described before
throwing a *CliException* when validation has failed.

## Defining a command

The creation of a command is working pretty the same way as for the options.
The definition of the command options is exactly the same thing.

```java
final var command = CliCommand.builder()
    .setName("run")
    .setDescription("processing a document with tasks")
    .addOption(fileOption)
    .build();
```

### Name

 - The name is required.
 - The name should have at least two characters, and the length must not exceed 10 characters.
 - The name has to be lower case.

### Description

 1. The field is required
 1. It cannot be null and also not be empty

### Option

 - you can call **addOption** (several times) to add one option.
 - you can call **addAllOption** (several times) to add a list of options.

### Validation

The **build()** function checks the rules as described below
  throwing a *CliException* when validation has failed.

  - checking for name and description
  - checking for options
    - a long name may exist once only
    - a short name may exist once only
    - a description may exist once only 
   
##  The option list

The option list (CliOptionList) is special class keeping a list of options working
with the builder options as already seen for the other classes.
The **build()** function finally does also the validation:

 - a long name may exist once only
 - a short name may exist once only
 - a description may exist once only 

These option list is required for the parser.

## Using the parser

The parser does parse the command line arguments.

```java
final var parser = CliParser.builder()
   .setGlobalOptions(globalOptions)
   .setCommands(commands)
   .build();

final var result = parser.parse(arguments);
```

### Global options

You set the list of global options (see option list - CliOptionList).

### Commands

You have two options:

 - using **setCommands** to set a list of commands (old commands are lost).
 - using **addCommand** (several times) to add one command.

### Validation when parsing

While parsing the command line arguments further validation is done:

 - required options that are missing
 - repeated options that are not defined repeatable
 - if you use more than once command  
 - unknown options
 - unknown commands

**Please note**:

 - You have to use global options before any command
 - If you want to use required options don't use them on global options otherwise you run
   into problems when trying to use --help (as an example). Simply define a command for it.

## Using the result

When the parsing is fine you get an instance of type CliResult and you
three information are available then:

 - with **getGlobalOptions()** you get a map where the key is the long name of the option
 - the value is a list; if the option is repeatable it contains more than one value.
 - same for **getCommandOptions()**.
 - with **getCommandName()** you get the command.

It's on you how you handle it.

## Help printer

The setup of the help printer works similar as the parser. In addition to the defined
options and commands you specify the execution, the product version, the build timestamp
and the author:

```java
private void printHelp() throws CliException {
    final var helpPrinter = CliHelpPrinter.builder()
            .setExecution("java -jar "
                    + this.properties.getProperty(PROPERTY_FINAL_NAME) + ".jar")
            .setProductVersion(this.properties.getProperty(PROPERTY_PRODUCT_VERSION))
            .setBuildTimestamp(this.properties.getProperty(PROPERTY_BUILD_TIMESTAMP))
            .setAuthor(this.properties.getProperty(PROPERTY_AUTHOR))
            .setGlobalOptions(this.globalOptions)
            .setCommands(this.commands)
            .build();
    helpPrinter.print(LoggerFactory.getLogger("HELP")::info);
}
```

The code is extracted from the Hyperion application as I'm using it iself. With Maven
and resource filtering I'm passing the information like the final name of the jar,
the product version, the build timestamp and the author.

For the logging I'm using a custom logger to avoid the usually timestamped output.
At the time when I'm writing this documentation the result did look like following:

```
java -jar hyperion-1.0.0-SNAPSHOT.jar [global options] [command [command options]]
    version: 1.0.0-SNAPSHOT, build timestamp: 2021-05-14 15:21
    author: Thomas Lehmann <thomas.lehmann.private@gmail.com>

Global options:
    -h, --help        - displaying this help
        --third-party - displaying used 3rd party libraries

List of available commands:
    run - Running one document with tasks to be processed

Options for command 'run':
    -f<path>, --file=<path> - Document with tasks to be processed [required]
```

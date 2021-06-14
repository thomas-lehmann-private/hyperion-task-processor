/*
 * The MIT License
 *
 * Copyright 2021 Thomas Lehmann.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package magic.system.hyperion;

import magic.system.hyperion.cli.CliCommand;
import magic.system.hyperion.cli.CliException;
import magic.system.hyperion.cli.CliOption;
import magic.system.hyperion.cli.CliOptionList;
import magic.system.hyperion.cli.OptionType;

import java.util.List;

/**
 * Application options functions.
 *
 * @author Thomas Lehmann
 */
public final class ApplicationOptionsFunctions {
    /**
     * Not intended for instantiation.
     */
    private ApplicationOptionsFunctions() {
        // Nothing to do.
    }

    /**
     * Define the global options.
     *
     * @return list of global options
     * @throws CliException when validation of the definition has failed.
     */
    static CliOptionList defineGlobalOptions() throws CliException {
        return CliOptionList.builder().add(
                CliOption.builder()
                        .setShortName(ApplicationOptions.HELP.getShortName())
                        .setLongName(ApplicationOptions.HELP.getLongName())
                        .setDescription(ApplicationOptions.HELP.getDescription())
                        .setType(OptionType.BOOLEAN)
                        .build()).add(
                CliOption.builder()
                        .setShortName(ApplicationOptions.TIMEOUT_TASKGROUP.getShortName())
                        .setLongName(ApplicationOptions.TIMEOUT_TASKGROUP.getLongName())
                        .setDescription(ApplicationOptions.TIMEOUT_TASKGROUP.getDescription())
                        .setType(OptionType.INTEGER)
                        .setDefault("60")
                        .build()).add(
                CliOption.builder()
                        .setShortName(ApplicationOptions.TAG.getShortName())
                        .setLongName(ApplicationOptions.TAG.getLongName())
                        .setDescription(ApplicationOptions.TAG.getDescription())
                        .setType(OptionType.STRING)
                        .setRepeatable(true)
                        .build()
        ).build();
    }

    /**
     * Define the list of commands.
     *
     * @return list of commands.
     * @throws CliException when validation of the definition has failed.
     */
    static List<CliCommand> defineCommands() throws CliException {
        return List.of(CliCommand.builder()
                        .setName(ApplicationCommands.RUN.getCommand())
                        .setDescription(ApplicationCommands.RUN.getDescription())
                        .addOption(CliOption.builder()
                                .setShortName(ApplicationOptions.RUN_FILE.getShortName())
                                .setLongName(ApplicationOptions.RUN_FILE.getLongName())
                                .setDescription(ApplicationOptions.RUN_FILE.getDescription())
                                .setRequired(true)
                                .setType(OptionType.PATH)
                                .build())
                        .build(),
                CliCommand.builder()
                        .setName(ApplicationCommands.THIRD_PARTY.getCommand())
                        .setDescription(ApplicationCommands.THIRD_PARTY.getDescription())
                        .build(),
                CliCommand.builder()
                        .setName(ApplicationCommands.CAPABILITIES.getCommand())
                        .setDescription(ApplicationCommands.CAPABILITIES.getDescription())
                        .build()
        );
    }
}

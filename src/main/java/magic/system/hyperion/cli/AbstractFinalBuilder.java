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
package magic.system.hyperion.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder global options and commands.
 * @param <E> concrete builder type.
 */
public abstract class AbstractFinalBuilder<E> {
    /**
     * List of global options.
     */
    protected CliOptionList globalOptions;

    /**
     * List of commands.
     */
    protected final List<CliCommand> commands;

    /**
     * Initialize builder.
     *
     * @since 1.0.0
     */
    public AbstractFinalBuilder() {
        this.globalOptions = null;
        this.commands = new ArrayList<>();
    }

    /**
     * Define all global options.
     *
     * @param initGlobalOptions set global options.
     * @return builder itself to allow chaining.
     * @since 1.0.0
     */
    public AbstractFinalBuilder<E> setGlobalOptions(final CliOptionList initGlobalOptions) {
        this.globalOptions = initGlobalOptions;
        return this;
    }

    /**
     * Adding a command.
     *
     * @param command new command to add.
     * @return builder itself to allow chaining.
     * @since 1.0.0
     */
    public AbstractFinalBuilder<E> addCommand(final CliCommand command) {
        this.commands.add(command);
        return this;
    }

    /**
     * Assign a list of commands.
     *
     * @param initCommands list of commands.
     * @return builder itself to allow chaining.
     * @since 1.0.0
     */
    public AbstractFinalBuilder<E> setCommands(final List<CliCommand> initCommands) {
        this.commands.clear();
        this.commands.addAll(initCommands);
        return this;
    }

    /**
     * Building concrete instance.
     *
     * @return instance of given type.
     * @throws CliException when validation has failed.
     * @since 1.0.0
     */
    public abstract E build() throws CliException;
}

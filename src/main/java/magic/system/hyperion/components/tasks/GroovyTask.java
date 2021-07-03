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
package magic.system.hyperion.components.tasks;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.TaskResult;
import magic.system.hyperion.tools.FileExtensions;
import magic.system.hyperion.tools.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Task for running a Groovy script.
 */
@SuppressWarnings("checkstyle:classdataabstractioncoupling")
public class GroovyTask extends AbstractCodableTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyTask.class);

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     * @since 1.0.0
     */
    public GroovyTask(final String strInitTitle, final String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    public boolean isRegularFile() {
        return super.isRegularFile() && getCode().endsWith(FileExtensions.GROOVY.getValue());
    }

    @Override
    public TaskResult run(final TaskParameters parameters) {
        TaskResult taskResult = null;

        logTitle(parameters);

        try {
            final var writer = new StringWriter();
            final var binding = new Binding(Map.of("out", new PrintWriter(writer)));
            final var shell = new GroovyShell(binding);
            final var engine = new TemplateEngine();

            var strContent = getCode();

            if (isRegularFile()) {
                LOGGER.info("Processing Groovy file {}", getCode());
                strContent = Files.readString(Paths.get(getCode()));
            }

            final var renderedText = engine.render(
                    strContent, parameters.getTemplatingContext());

            shell.evaluate(renderedText);
            getVariable().setValue(writer.toString());
            taskResult = new TaskResult(true, getVariable());
        } catch (GroovyRuntimeException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            taskResult = new TaskResult(false, getVariable());
        }

        return taskResult;
    }

    @Override
    public AbstractTask copy() {
        return new GroovyTask(this.getTitle(), this.getCode());
    }
}

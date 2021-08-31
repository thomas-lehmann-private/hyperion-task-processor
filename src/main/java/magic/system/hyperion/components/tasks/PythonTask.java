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

import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.tools.Capabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Task for running a Python script.
 *
 * @author Thomas Lehmann
 */
public class PythonTask extends AbstractShellTask {
    /**
     * Separator for options.
     */
    private static final String SEPARATOR = " ";

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonTask.class);

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode  - Path and name of file of script or inline script.
     * @since 1.0.0
     */
    public PythonTask(String strInitTitle, String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    protected String getTempFilePrefix() {
        return "hyperion-python-task-";
    }

    @Override
    protected List<String> getFileExtensions() {
        return List.of("py");
    }

    @Override
    protected boolean isTempFileRelativePath() {
        return false;
    }

    @Override
    protected Process runFile(final Path path) throws IOException, HyperionException {
        final var baseCommand = List.of("python", path.toString());
        final var strCommand = String.join(SEPARATOR, Stream.of(baseCommand)
                .flatMap(Collection::stream).collect(Collectors.toList()));
        final var finalCommand = Capabilities.createCommand(strCommand);
        LOGGER.info("Running command: {}", String.join(SEPARATOR, finalCommand));
        return new ProcessBuilder(finalCommand).start();
    }

    @Override
    public AbstractTask copy() {
        final var task = new PythonTask(this.getTitle(), this.getCode());
        return task;
    }
}

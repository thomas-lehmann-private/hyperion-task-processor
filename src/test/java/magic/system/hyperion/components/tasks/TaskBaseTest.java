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

import magic.system.hyperion.tools.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base class for task tests.
 *
 * @author Thomas Lehmann.
 */
public class TaskBaseTest {
    /**
     * Root path of resources.
     */
    private static final String RESOURCES_ROOT = "/";

    /**
     * Existing folder for storing test data/results related to tasks.
     */
    private static final String TASKS_FOLDER = "tasks";

    @BeforeEach
    public void setup() throws URISyntaxException, IOException {
        final URL baseUrl = getClass().getResource(RESOURCES_ROOT);
        final var path = Paths.get(Path.of(baseUrl.toURI()).toString(), TASKS_FOLDER);
        Files.createDirectories(path);
    }

    @AfterEach
    public void cleanUp() throws IOException, URISyntaxException {
        final URL baseUrl = getClass().getResource(RESOURCES_ROOT);
        final var path = Paths.get(Path.of(baseUrl.toURI()).toString(), TASKS_FOLDER);
        assertTrue(FileUtils.removeDirectoryRecursive(path));
    }
}

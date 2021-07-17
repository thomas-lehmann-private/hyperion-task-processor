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
import magic.system.hyperion.tools.TemplateEngine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link WriteFileTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of class WriteFileTask")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class WriteFileTaskTest {
    @BeforeEach
    public void setup() throws URISyntaxException, IOException {
        final URL baseUrl = getClass().getResource("/");
        final var path = Paths.get(Path.of(baseUrl.toURI()).toString(), "tasks");
        Files.createDirectories(path);
    }

    @AfterEach
    public void cleanUp() throws IOException, URISyntaxException {
        final URL baseUrl = getClass().getResource("/");
        final var path = Paths.get(Path.of(baseUrl.toURI()).toString(), "tasks");
        assertTrue(FileUtils.removeDirectoryRecursive(path));
    }

    /**
     * Testing file copy operation.
     *
     * @param bExpectedToSucceed when true then expected to fail.
     * @param task               concrete write file task.
     * @param strExpectedContent the expected content to be written to file.
     * @throws IOException when reading lines has failed (comparing source and destination)
     */
    @ParameterizedTest(name = "#{index}, success={0}, task={1}, expected content={2}")
    @MethodSource("copyTestData")
    public void testWrite(final boolean bExpectedToSucceed, final WriteFileTask task,
                          final String strExpectedContent)
            throws IOException {
        final var result = task.run(TaskTestsTools.getSimpleTaskParameters());
        if (bExpectedToSucceed) {
            // execution of task has been successful (as expected).
            assertTrue(result.isSuccess());

            final var engine = new TemplateEngine();
            final var renderedDestinationPath = Paths.get(engine.render(
                    task.getDestinationPath(),
                    TaskTestsTools.getSimpleTaskParameters().getTemplatingContext()));

            //comparing content of source and destination
            assertEquals(strExpectedContent, Files.readString(renderedDestinationPath));
            assertEquals(renderedDestinationPath.toString(), result.getVariable().getValue());
        } else {
            // execution of task has been failed (as expected).
            assertFalse(result.isSuccess());
        }
    }

    /**
     * Providing test data.
     *
     * @return test data.
     */
    private static Stream<Arguments> copyTestData() throws URISyntaxException {
        final URL baseUrl = WriteFileTask.class.getResource("/");
        final var strBaseUrl = new File(baseUrl.toURI()).getAbsolutePath();

        return Stream.of(
                // path exists, file does not exist, all should be fine
                Arguments.of(true, createTask(
                        "hello world 1!", strBaseUrl + "/tasks/test1.txt",
                        false, false), "hello world 1!"),
                // Path does not exist and is not enabled to be created
                Arguments.of(false, createTask(
                        "hello world 2!", strBaseUrl + "/tasks/a/test2.txt",
                        false, false), null),
                // Path does not exist and is enabled to be created, all should be fine
                Arguments.of(true, createTask(
                        "hello world 3!", strBaseUrl + "/tasks/a/test3.txt",
                        false, true), "hello world 3!"),
                // path exists, file does not exist, all should be fine
                Arguments.of(true, createTask(
                        "{{ model.attributes.test.value }}",
                        strBaseUrl + "/tasks/{{ model.attributes.file.value }}",
                        false, false), "hello world 1!")
        );
    }

    /**
     * Creating a test task.
     *
     * @param strContent some string content.
     * @param strDestinationPath destination path where to write the file to.
     * @param bOverwrite when true then allow overwrite.
     * @param bEnsurePath when true then ensure that path does exist.
     * @return created task.
     */
    private static WriteFileTask createTask(final String strContent,
                                            final String strDestinationPath,
                                            final boolean bOverwrite,
                                            final boolean bEnsurePath) {
        final var task = new WriteFileTask("test");
        task.setContent(strContent);
        task.setDestinationPath(strDestinationPath);
        task.setOverwrite(bOverwrite);
        task.setEnsurePath(bEnsurePath);
        return task;
    }
}

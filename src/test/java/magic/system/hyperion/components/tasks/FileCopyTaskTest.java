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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Testing of {@link FileCopyTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of FileCopyTask class")
@SuppressWarnings({"checkstyle:multiplestringliterals", "checkstyle:classfanoutcomplexity"})
public class FileCopyTaskTest extends TaskBaseTest {
    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCopyTaskTest.class);

    /**
     * Test file.
     */
    private static final String TEST_FILE = "/file-copy-test.txt";

    /**
     * Test file that does not exist.
     */
    private static final String TEST_FILE_MISSING = "/file-copy-missing.txt";

    /**
     * Testing file copy operation.
     *
     * @param bExpectedToSucceed when true then expected to fail.
     * @param task               concrete file copy task.
     * @throws IOException when reading lines has failed (comparing source and destination)
     */
    @ParameterizedTest(name = "#{index}, success={0}, task={1}")
    @MethodSource("copyTestData")
    public void testCopy(final boolean bExpectedToSucceed, final FileCopyTask task)
            throws IOException {

        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        if (bExpectedToSucceed) {
            // execution of task has been successful (as expected).
            assertTrue(result.isSuccess());

            final var destinationPath = task.isDestinationIsDirectory()
                    ? Paths.get(task.getDestinationPath(),
                    Paths.get(task.getSourcePath()).getFileName().toString())
                    : Paths.get(task.getDestinationPath());

            //comparing content of source and destination
            assertEquals(
                    Files.readAllLines(Paths.get(task.getSourcePath())),
                    Files.readAllLines(destinationPath));
        } else {
            // execution of task has been failed (as expected).
            assertFalse(result.isSuccess());
        }
    }

    /**
     * Testing overwrite mode.
     *
     * @throws IOException when reading text file has changed.
     */
    @Test
    public void testOverwrite() throws IOException, URISyntaxException {
        final URL baseUrl = FileCopyTaskTest.class.getResource("/");
        final var strBaseUrl = new File(baseUrl.toURI()).getAbsolutePath();

        final var task = createTask(
                strBaseUrl + TEST_FILE, strBaseUrl + "/tasks/test.txt",
                false, false, false);

        Files.write(Path.of(task.getDestinationPath()),
                Files.readAllLines(Paths.get(task.getSourcePath())));

        var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertFalse(result.isSuccess());

        task.setOverwrite(true);
        result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertTrue(result.isSuccess());
    }

    /**
     * Providing test data.
     *
     * @return test data.
     */
    private static Stream<Arguments> copyTestData() throws URISyntaxException {
        final URL baseUrl = FileCopyTaskTest.class.getResource("/");
        final var strBaseUrl = new File(baseUrl.toURI()).getAbsolutePath();

        return Stream.of(
                // copy existing source file to existing directory
                Arguments.of(true, createTask(
                        strBaseUrl + TEST_FILE, strBaseUrl + "/tasks",
                        false, false, true)),
                // copy missing source file to existing directory
                Arguments.of(false, createTask(
                        strBaseUrl + TEST_FILE_MISSING, strBaseUrl + "/tasks",
                        false, false, true)),
                // copy existing source file to missing directory without automatic path creation
                Arguments.of(false, createTask(
                        strBaseUrl + TEST_FILE, strBaseUrl + "/tasks/a",
                        false, false, true)),
                // copy existing source file to missing directory with automatic path creation
                Arguments.of(true, createTask(
                        strBaseUrl + TEST_FILE, strBaseUrl + "/tasks/a",
                        false, true, true)),
                // copy existing source file to missing directory with automatic path creation
                // and rename of file
                Arguments.of(true, createTask(
                        strBaseUrl + TEST_FILE, strBaseUrl + "/tasks/a/test.txt",
                        false, true, false))
        );
    }

    /**
     * Helper function to create copy file task.
     *
     * @param strSourcePath           source path of the file to copy.
     * @param strDestinationPath      destination path (file or directory) where to copy to.
     * @param bOverwrite              when true overwrite is allowed.
     * @param bEnsurePath             when true missing paths will be created.
     * @param bDestinationIsDirectory when true destination will be interpreted as directory.
     * @return created task.
     */
    private static FileCopyTask createTask(final String strSourcePath,
                                           final String strDestinationPath,
                                           final boolean bOverwrite,
                                           final boolean bEnsurePath,
                                           final boolean bDestinationIsDirectory) {
        final var task = new FileCopyTask("test");
        task.setSourcePath(strSourcePath);
        task.setDestinationPath(strDestinationPath);
        task.setOverwrite(bOverwrite);
        task.setEnsurePath(bEnsurePath);
        task.setDestinationIsDirectory(bDestinationIsDirectory);
        return task;
    }
}

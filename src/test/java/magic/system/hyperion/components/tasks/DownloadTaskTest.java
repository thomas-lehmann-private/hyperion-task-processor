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

import magic.system.hyperion.tools.TemplateEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing of class {@link DownloadTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of DownloadTask")
@SuppressWarnings({"checkstyle:multiplestringliterals", "checkstyle:classfanoutcomplexity"})
public class DownloadTaskTest extends TaskBaseTest {
    /**
     * Base URL to existing project (this one) on Github.
     */
    private static final String TEST_BASE_URL
            = "https://raw.githubusercontent.com/thomas-lehmann-private/hyperion-task-processor";
    /**
     * Test url for an existing file.
     */
    private static final String TEST_EXISTING_URL_STRING
            = TEST_BASE_URL + "/main/README.md";

    /**
     * Test url for a not existing file.
     */
    private static final String TEST_NOT_EXISTING_URL_STRING
            = TEST_BASE_URL + "/main/DOES-NOT-EXIST.md";

    /**
     * Testing the download.
     *
     * @param bExpectedToSucceed when true then expected to succeed.
     * @param task the concrete download task.
     * @throws IOException when reading lines has failed (comparing source and destination)
     */
    @ParameterizedTest(name = "#{index}, success={0}, task={1}")
    @MethodSource("downloadTestData")
    public void testDownload(final boolean bExpectedToSucceed, final DownloadTask task)
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
            assertEquals(readContent(task.getUrl()), Files.readString(renderedDestinationPath));
            assertEquals(renderedDestinationPath.toString(), result.getVariable().getValue());
        } else {
            // execution of task has been failed (as expected).
            assertFalse(result.isSuccess());
        }
    }

    /**
     * Testing overwrite not allowed.
     *
     * @throws MalformedURLException when URL syntax is wrong.
     * @throws URISyntaxException when URI syntax is wrong.
     */
    @Test
    public void testOverwriteNowAllowed() throws MalformedURLException, URISyntaxException {
        final URL baseUrl = WriteFileTask.class.getResource("/");
        final var strBaseUrl = new File(baseUrl.toURI()).getAbsolutePath();

        final var task = createTask(new URL(TEST_EXISTING_URL_STRING),
                strBaseUrl + "/tasks/test1.txt",
                false, false);

        var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertTrue(result.isSuccess());
        result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertFalse(result.isSuccess());
    }

    /**
     * Testing overwrite is allowed.
     *
     * @throws MalformedURLException when URL syntax is wrong.
     * @throws URISyntaxException when URI syntax is wrong.
     */
    @Test
    public void testOverwriteAllowed() throws MalformedURLException, URISyntaxException {
        final URL baseUrl = WriteFileTask.class.getResource("/");
        final var strBaseUrl = new File(baseUrl.toURI()).getAbsolutePath();

        final var task = createTask(new URL(TEST_EXISTING_URL_STRING),
                strBaseUrl + "/tasks/test1.txt",
                true, false);

        var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertTrue(result.isSuccess());
        result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertTrue(result.isSuccess());
    }

    /**
     * Get test data.
     *
     * @return test data.
     * @throws MalformedURLException when URL syntax is wrong.
     * @throws URISyntaxException when URI syntax is wrong.
     */
    private static Stream<Arguments> downloadTestData()
            throws MalformedURLException, URISyntaxException {
        final URL baseUrl = WriteFileTask.class.getResource("/");
        final var strBaseUrl = new File(baseUrl.toURI()).getAbsolutePath();

        return Stream.of(
                // All good
                Arguments.of(true, createTask(new URL(TEST_EXISTING_URL_STRING),
                        strBaseUrl + "/tasks/test1.txt",
                        false, false)),
                // URL for a not existing file
                Arguments.of(false, createTask(new URL(TEST_NOT_EXISTING_URL_STRING),
                        strBaseUrl + "/tasks/test2.txt",
                        false, false)),
                // Destination path does not exist
                Arguments.of(false, createTask(new URL(TEST_EXISTING_URL_STRING),
                        strBaseUrl + "/tasks/a/test1.txt",
                        false, false)),
                // Destination path does not exist but will be created
                Arguments.of(true, createTask(new URL(TEST_EXISTING_URL_STRING),
                        strBaseUrl + "/tasks/a/test1.txt",
                        false, true))
        );
    }

    /**
     * Creating a test task.
     *
     * @param url where to download file from.
     * @param strDestinationPath destination path where to write the file to.
     * @param bOverwrite when true then allow overwrite.
     * @param bEnsurePath when true then ensure that path does exist.
     * @return created task.
     */
    private static DownloadTask createTask(final URL url,
                                            final String strDestinationPath,
                                            final boolean bOverwrite,
                                            final boolean bEnsurePath) {
        final var task = new DownloadTask("test");
        task.setUrl(url);
        task.setDestinationPath(strDestinationPath);
        task.setOverwrite(bOverwrite);
        task.setEnsurePath(bEnsurePath);
        return task;
    }

    /**
     * Read string content from URL.
     *
     * @param url where to read content from.
     * @return string content when found otherwise null.
     */
    private static String readContent(final URL url) {
        String strContent;
        try(var stream = url.openStream()) {
            strContent = new String(stream.readAllBytes());
        } catch (IOException e) {
            strContent = null;
        }
        return strContent;
    }
}

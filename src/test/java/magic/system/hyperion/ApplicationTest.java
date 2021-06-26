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

import magic.system.hyperion.tools.MessagesCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing class {@link Application}.
 */
@DisplayName("Testing class Application")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class ApplicationTest {
    /**
     * Testing the help feature.
     */
    @Test
    public void testHelp() {
        MessagesCollector.clear();
        Application.main(List.of("--help").toArray(String[]::new));
        // probe testing (we do not construct the help again here).
        assertTrue(MessagesCollector.getMessages().contains("Global options:"));
        assertTrue(MessagesCollector.getMessages().contains("List of available commands:"));
        assertTrue(MessagesCollector.getMessages().contains("Options for command 'run':"));
    }

    /**
     * Testing 3rd party option.
     * Please note that for this test the Maven compile goal has to run.
     */
    @Test
    public void test3rdParty() {
        MessagesCollector.clear();
        Application.main(List.of(
                ApplicationCommands.THIRD_PARTY.getCommand()).toArray(String[]::new));
        final var lines = MessagesCollector.getMessages();
        var count = 0;

        // probe testing (we do not construct the 3rd party again here).
        for (final var line : lines) {
            assertTrue(Pattern.matches("group id: .*, artifact id: .*, version: .*", line));
            count += 1;
        }

        assertTrue(count > 0, "there should be at least one dependency");
    }

    /**
     * Checking for capabilities.
     */
    @Test
    public void testCapabilities() {
        MessagesCollector.clear();
        Application.main(List.of(
                ApplicationCommands.CAPABILITIES.getCommand()).toArray(String[]::new));
        final var lines = MessagesCollector.getMessages();
        assertTrue(lines.stream().anyMatch(line -> line.contains("Docker:")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("Powershell:")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("Groovy (embedded):")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("Java:")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("Operating System:")));
    }

    /**
     * Testing to process a document.
     *
     * @throws URISyntaxException when URL for document has wrong syntax.
     */
    @Test
    public void testSimpleDocument() throws URISyntaxException {
        final var url = getClass().getResource("/documents/document-for-application-test.yml");
        final var file = new File(url.toURI());

        MessagesCollector.clear();
        Application.main(List.of("run", "--file", file.getAbsolutePath()).toArray(String[]::new));

        assertTrue(MessagesCollector.getMessages()
                .stream().anyMatch(line -> line.contains("hello world 1!")));
        assertTrue(MessagesCollector.getMessages()
                .stream().anyMatch(line -> line.contains("hello world 2!")));
        assertTrue(MessagesCollector.getMessages()
                .stream().anyMatch(line -> line.contains("hello world 3!")));
    }

    /**
     * Testing process a document with a temporary path.
     *
     * @throws URISyntaxException when URL for document has wrong syntax.
     */
    @Test
    public void testTemporaryFileOption() throws URISyntaxException {
        final var temporaryPathUrl = getClass().getResource("/");
        final var url = getClass().getResource("/documents/document-for-application-test.yml");
        final var temporaryPath = new File(temporaryPathUrl.toURI()).getAbsolutePath();

        MessagesCollector.clear();
        Application.main(List.of("run", "--file", new File(url.toURI()).getAbsolutePath(),
                "--temporary-path", temporaryPath).toArray(String[]::new));

        final var lines = MessagesCollector.getMessages().stream()
                .filter(line -> line.contains("Running script")).collect(Collectors.toList());

        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains(temporaryPath));
    }

    /**
     * Testing to process a document.
     *
     * @throws URISyntaxException when URL for document has wrong syntax.
     */
    @Test
    public void testSimpleDocumentWithFilter() throws URISyntaxException {
        final var url = getClass().getResource("/documents/document-for-application-test.yml");
        final var file = new File(url.toURI());

        MessagesCollector.clear();
        Application.main(List.of("--tag", "test1", "run",
                "--file", file.getAbsolutePath()).toArray(String[]::new));

        assertTrue(MessagesCollector.getMessages()
                .stream().anyMatch(line -> line.contains("hello world 1!")));
        assertTrue(MessagesCollector.getMessages()
                .stream().noneMatch(line -> line.contains("hello world 2!")));
    }
}

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

import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing class {@link Application}.
 */
@DisplayName("Testing class Application")
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

    @Test
    public void test3rdParty() {
        MessagesCollector.clear();
        Application.main(List.of("--third-party").toArray(String[]::new));
        // probe testing (we do not construct the 3rd party again here).
        for (final var line: MessagesCollector.getMessages()) {
            assertTrue(Pattern.matches("group id: .*, artifact id: .*, version: .*", line));
        }
    }
}

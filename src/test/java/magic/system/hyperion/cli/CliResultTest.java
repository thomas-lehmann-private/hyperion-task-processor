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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing of class {@link CliResult}.
 */
@DisplayName("Test CliResult class")
public class CliResultTest {
    /**
     * Testing default usage.
     */
    @Test
    public void testDefault() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals: ok here.
        final var result = CliResult.builder()
                .addGlobalOption("help", "true")
                .setCommandName("run")
                .addCommandOption("file", "c:\\temp\\test1.yml")
                .addCommandOption("file", "c:\\temp\\test2.yml")
                .build();

        assertEquals(1, result.getGlobalOptions().size());
        assertEquals(1, result.getCommandOptions().size());
        assertEquals(List.of("c:\\temp\\test1.yml", "c:\\temp\\test2.yml"),
                result.getCommandOptions().get("file"));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}

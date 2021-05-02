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
package magic.system.hyperion.components;

import magic.system.hyperion.generics.GenericReader;
import magic.system.hyperion.generics.GenericWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testing of class {@link Document}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing Document class")
public class DocumentTest {

    /**
     * Test write and read a document using YAML.
     * Finally also equals is used to compare the whole document.
     */
    @Test
    public void testWriteAndReadDocument() {
        final var document1 = createTestDocument();
        final String strContent = new GenericWriter().toYAML(document1);
        
        final var document2 = new GenericReader<>(Document.class).fromYAML(strContent);
        assertEquals(document1, document2);
    }

    /**
     * Creating a test document.
     *
     * @return test document.
     */
    private Document createTestDocument() {
        final var document = new Document();
        document.getModel().getData().set("mode", "test");
        final var taskGroup = new TaskGroup("running one after the other", false);
        taskGroup.add(new PowershellTask("say hello world 1",
                "Write-Host \"hello world 1!\""));
        taskGroup.add(new PowershellTask("say hello world 2",
                "Write-Host \"hello world 2!\""));
        document.add(taskGroup);
        return document;
    }
}

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing of class {@link XslTransformTask}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of class XslTransformTest")
public class XslTransformTaskTest {
    /**
     * Testing transformation.
     *
     * @param strXsl XSL string or path and filename.
     * @param strXml XML string or path and filename.
     * @param strExpected expected transformation result.
     */
    @ParameterizedTest(name = "#{index} - xsl={0}, xml={1}, expected={3}")
    @MethodSource("transformationTestData")
    public void testTransformation(final String strXsl, final String strXml,
                                   final String strExpected) {
        final var task = new XslTransformTask("test");
        task.setXsl(strXsl);
        task.setXml(strXml);
        final var result = task.run(TaskTestsTools.getDefaultTaskParameters());
        assertEquals(strExpected, result.getVariable().getValue());
    }

    /**
     * Provide test data for transformation tests.
     *
     * @return test data.
     */
    private static Stream<Arguments> transformationTestData()
            throws URISyntaxException, IOException {
        final var xmlUrl = XslTransformTaskTest.class.getResource("/xmlxsl/test1.xml");
        final var xmlFile = new File(xmlUrl.toURI());
        final var xmlContent = Files.readString(Path.of(xmlUrl.toURI()));

        final var xslUrl = XslTransformTaskTest.class.getResource("/xmlxsl/test1.xsl");
        final var xslFile = new File(xslUrl.toURI());
        final var xslContent = Files.readString(Path.of(xslUrl.toURI()));

        final var htmlUrl = XslTransformTaskTest.class.getResource("/xmlxsl/test1.html");
        final var htmlFile = new File(htmlUrl.toURI());
        final var htmlContent = Files.readString(Path.of(htmlUrl.toURI()));

        return Stream.of(
                Arguments.of(xslContent, xmlContent, htmlContent),
                Arguments.of(xslFile.getAbsolutePath(), xmlContent, htmlContent),
                Arguments.of(xslContent, xmlFile.getAbsolutePath(), htmlContent),
                Arguments.of(xslFile.getAbsolutePath(), xmlFile.getAbsolutePath(), htmlContent)
        );
    }
}

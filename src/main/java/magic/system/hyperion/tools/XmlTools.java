package magic.system.hyperion.tools;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XML tools.
 *
 * @author Thomas Lehmann
 */
public final class XmlTools {
    /**
     * Instantiation not allowed.
     */
    private XmlTools() {
        // Nothing to do.
    }

    /**
     * Transforming XML via XSL.
     *
     * @param strXslContent XSL transformation document.
     * @param strXmlContent XML document.
     * @return transformation result.
     * @throws SaxonApiException when compilation of XSL has failed.
     */
    public static String transform(final String strXslContent, final String strXmlContent)
            throws SaxonApiException {
        // prepare stylesheet (compile)
        final var processor = new Processor(false);
        final var compiler = processor.newXsltCompiler();
        final var stylesheet = compiler.compile(
                new StreamSource(new StringReader(strXslContent)));

        // prepare target where to write to (string).
        final var writer = new StringWriter();
        final var out = processor.newSerializer(writer);

        // transform XML via XSL
        final var transformer = stylesheet.load30();
        transformer.transform(new StreamSource(new StringReader(strXmlContent)), out);

        return writer.toString();
    }
}

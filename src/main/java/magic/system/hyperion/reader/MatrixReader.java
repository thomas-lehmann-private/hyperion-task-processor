package magic.system.hyperion.reader;

import com.fasterxml.jackson.databind.JsonNode;
import magic.system.hyperion.components.Document;
import magic.system.hyperion.components.MatrixParameters;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.matcher.Matcher;

import java.util.TreeMap;

/**
 * Reading the matrix at a node.
 */
public class MatrixReader implements INodeReader {
    /**
     * Document to initialize with the matrix configuration.
     */
    private final Document document;

    /**
     * Initialize reader with document.
     *
     * @param initDocument document to initialize with matrix.
     */
    public MatrixReader(final Document initDocument) {
        this.document = initDocument;
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var iter = node.elements();
        while (iter.hasNext()) {
            // there should be nothing else but individual matrix item.
            readMatrixParameters(iter.next());
        }
    }

    /**
     * Reading parameters of one matrix item.
     *
     * @param node the node to evaluate.
     * @throws HyperionException when matrix parameters are not setup correctly.
     */
    private void readMatrixParameters(final JsonNode node) throws HyperionException {
        final var names = Converters.convertToSortedList(node.fieldNames());
        final var matcher = Matcher.of(names);

        matcher.requireExactlyOnce(DocumentReaderFields.TITLE.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.PARAMETERS.getFieldName());

        if (matcher.matches(names)) {
            final var matrixParameters = new MatrixParameters(
                    node.get(DocumentReaderFields.TITLE.getFieldName()).asText());
            final var parameters = new TreeMap<String, String>();

            final var childNodes = node.get(DocumentReaderFields.PARAMETERS.getFieldName());
            final var iter = childNodes.fields();
            while (iter.hasNext()) {
                final var strKey = iter.next().getKey();
                parameters.put(strKey, childNodes.get(strKey).asText());
            }

            matrixParameters.setParameters(parameters);
            this.document.add(matrixParameters);
        } else {
            throw new HyperionException("Matrix parameters are not setup correctly!");
        }
    }
}

package magic.system.hyperion.reader;

import com.fasterxml.jackson.databind.JsonNode;
import magic.system.hyperion.components.TaskGroup;
import magic.system.hyperion.components.tasks.XslTransformTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.generics.Converters;
import magic.system.hyperion.interfaces.ITaskCreator;

/**
 * Reader for a {@link XslTransformTask}.
 *
 * @author Thomas Lehmann
 */
public class XslTransformTaskReader extends AbstractBasicTaskReader {
    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public XslTransformTaskReader(TaskGroup initTaskGroup, ITaskCreator initTaskCreator) {
        super(initTaskGroup, initTaskCreator);
    }

    @Override
    public void read(JsonNode node) throws HyperionException {
        final var matcher = getMatcher(node);

        matcher.requireExactlyOnce(DocumentReaderFields.XSL.getFieldName());
        matcher.requireExactlyOnce(DocumentReaderFields.XML.getFieldName());

        final var names = Converters.convertToSortedList(node.fieldNames());
        if (!matcher.matches(names)) {
            throw new HyperionException("The xsl transform task fields are not correct!");
        }

        final var task = (XslTransformTask) taskCreator.createTask();
        readBasic(task, node);

        task.setXsl(node.get(
                DocumentReaderFields.XSL.getFieldName()).asText());
        task.setXml(node.get(
                DocumentReaderFields.XML.getFieldName()).asText());

        this.taskGroup.add(task);
    }
}

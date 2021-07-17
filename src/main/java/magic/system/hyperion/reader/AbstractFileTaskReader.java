package magic.system.hyperion.reader;

import com.fasterxml.jackson.databind.JsonNode;
import magic.system.hyperion.components.TaskGroup;
import magic.system.hyperion.components.tasks.AbstractFileTask;
import magic.system.hyperion.components.tasks.AbstractTask;
import magic.system.hyperion.exceptions.HyperionException;
import magic.system.hyperion.interfaces.ITaskCreator;

public abstract class AbstractFileTaskReader extends AbstractBasicTaskReader {
    /**
     * Initialize with task group where to add the coded task.
     *
     * @param initTaskGroup   keeper of the list of tasks.
     * @param initTaskCreator the function that provides the creator for a task.
     * @since 1.0.0
     */
    public AbstractFileTaskReader(TaskGroup initTaskGroup, ITaskCreator initTaskCreator) {
        super(initTaskGroup, initTaskCreator);
    }

    /**
     * Reading options required by more than one file task.
     *
     * @param task file based task (copy, move or write).
     * @param node the node of the task.
     * @throws HyperionException when something went wrong.
     */
    protected void readFile(final AbstractTask task, final JsonNode node)
            throws HyperionException {
        final var fileTask = (AbstractFileTask)task;

        if (node.has(DocumentReaderFields.ENSURE_PATH.getFieldName())) {
            fileTask.setEnsurePath(node.get(
                    DocumentReaderFields.ENSURE_PATH.getFieldName()).asBoolean());
        }

        if (node.has(DocumentReaderFields.OVERWRITE.getFieldName())) {
            fileTask.setOverwrite(node.get(
                    DocumentReaderFields.OVERWRITE.getFieldName()).asBoolean());
        }
    }
}
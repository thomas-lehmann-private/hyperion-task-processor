package magic.system.hyperion.reader;

/**
 * Document reader messages.
 *
 * @author Thomas Lehmann
 */
public enum DocumentReaderMessage {
    /**
     * For logging when a node is not supported.
     */
    NODE_TYPE_NOT_SUPPORTED("Node type {} not supported!"),

    /**
     * Validation of the task group fields has failed.
     */
    MISSING_OR_UNKNOWN_TASK_GROUP_FIELDS("Task group fields are missing or unknown!");

    /**
     * Concrete message.
     */
    private final String strMessage;

    /**
     * Initialize enum with concrete message.
     *
     * @param strValue the message.
     * @version 1.0.0
     */
    DocumentReaderMessage(final String strValue) {
        this.strMessage = strValue;
    }

    /**
     * Get concrete message.
     *
     * @return concrete message.
     * @version 1.0.0
     */
    public String getMessage() {
        return this.strMessage;
    }

}

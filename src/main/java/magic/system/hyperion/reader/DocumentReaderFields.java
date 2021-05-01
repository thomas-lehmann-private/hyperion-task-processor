/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magic.system.hyperion.reader;

/**
 * Field names.
 *
 * @author Thomas Lehmann
 */
public enum DocumentReaderFields {
    /**
     * Title for many components.
     */
    TITLE("title"),
    /**
     * Tasks for task groups.
     */
    TASKS("tasks"),
    /**
     * In a task group a field to adjust whether task should run in parallel.
     */
    PARALLEL("parallel"),
    /**
     * Mainly for tasks to indicate which one is meant.
     */
    TYPE("type");

    /**
     * Name of the field.
     */
    private final String strFieldName;

    /**
     * Initialize constant with real field name.
     *
     * @param strInitFieldName real field name of field.
     */
    DocumentReaderFields(final String strInitFieldName) {
        this.strFieldName = strInitFieldName;
    }

    /**
     * Get real field name.
     *
     * @return read field name.
     */
    public String getFieldName() {
        return this.strFieldName;
    }
}

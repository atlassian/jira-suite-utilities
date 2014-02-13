package com.googlecode.jsu.workflow;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.Field;

/**
 * Simple wrapper around {@link Field} class which provides a type property
 * and replaces the original name to "name + ( TYPE )".
 */
public class FieldContainer implements Field {

    private final String name;
    private final String type;
    protected final Field field;

    public FieldContainer(final Field field) {
        this.field = field;
        if(field instanceof CustomField) {
            CustomField cf = (CustomField)field;
            this.type = cf.getCustomFieldType().getName();
            this.name = field.getName() + " (" + this.type + ")";
        } else {
            this.type = field.getId();
            this.name = field.getName();
        }
    }

    public String getId() {
        return field.getId();
    }

    public String getName() {
        return name;
    }

    public String getNameKey() {
        return field.getNameKey();
    }

    public String toString() {
        return field.toString();
    }

    public int hashCode() {
        return field.hashCode();
    }

    public boolean equals(Object o) {
        return field.equals(o);
    }

    public int compareTo(Object o) {
        return field.compareTo(o);
    }

    public String getType() {
        return type;
    }
}

package com.googlecode.jsu.workflow;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;

/**
 * @author Alexey Abashev
 */
public class WorkflowClearFieldValueFunctionPluginFactory
        extends AbstractWorkflowPluginFactory
        implements WorkflowPluginFunctionFactory {

    public static final String FIELD = "field";
    public static final String SELECTED_FIELD = "selectedField";
    public static final String FIELD_LIST = "fieldList";

    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final WorkflowUtils workflowUtils;

    /**
     * @param fieldCollectionsUtils
     * @param workflowUtils
     */
    public WorkflowClearFieldValueFunctionPluginFactory(FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils) {
        this.fieldCollectionsUtils = fieldCollectionsUtils;
        this.workflowUtils = workflowUtils;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    @SuppressWarnings("unchecked")
    protected void getVelocityParamsForEdit(Map velocityParams, AbstractDescriptor descriptor) {
        this.getVelocityParamsForInput(velocityParams);

        velocityParams.put(SELECTED_FIELD, workflowUtils.getFieldFromDescriptor(descriptor, FIELD));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForInput(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    protected void getVelocityParamsForInput(Map velocityParams) {
        List<Field> fields = fieldCollectionsUtils.getAllClearableFields();

        velocityParams.put(FIELD_LIST, Collections.unmodifiableList(fields));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    @SuppressWarnings("unchecked")
    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor) {
        velocityParams.put(SELECTED_FIELD, workflowUtils.getFieldFromDescriptor(descriptor, FIELD));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getDescriptorParams(Map conditionParams) {
        Map<String, String> params = new HashMap<String, String>();

        try{
            String sourceField = extractSingleParam(conditionParams, FIELD);

            params.put(FIELD, sourceField);
        } catch(IllegalArgumentException e) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }
}

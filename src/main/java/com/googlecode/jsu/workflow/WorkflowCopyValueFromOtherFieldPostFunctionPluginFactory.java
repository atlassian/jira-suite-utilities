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
 * This class defines the parameters available for Copy Value From Other Field Post Function.
 *
 * @author Gustavo Martin.
 */
public class WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {
    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final WorkflowUtils workflowUtils;

    public WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory(
            FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils
    ) {
        this.fieldCollectionsUtils = fieldCollectionsUtils;
        this.workflowUtils = workflowUtils;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForInput(java.util.Map)
     */
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        List<Field> sourceFields = fieldCollectionsUtils.getCopyFromFields();
        List<Field> destinationFields = fieldCollectionsUtils.getCopyToFields();

        velocityParams.put("val-sourceFieldsList", Collections.unmodifiableList(sourceFields));
        velocityParams.put("val-destinationFieldsList", Collections.unmodifiableList(destinationFields));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);

        Field sourceFieldId = workflowUtils.getFieldFromDescriptor(descriptor, "sourceField");
        Field destinationField = workflowUtils.getFieldFromDescriptor(descriptor, "destinationField");

        velocityParams.put("val-sourceFieldSelected", sourceFieldId);
        velocityParams.put("val-destinationFieldSelected", destinationField);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        Field sourceFieldId = workflowUtils.getFieldFromDescriptor(descriptor, "sourceField");
        Field destinationField = workflowUtils.getFieldFromDescriptor(descriptor, "destinationField");

        velocityParams.put("val-sourceFieldSelected", sourceFieldId);
        velocityParams.put("val-destinationFieldSelected", destinationField);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> conditionParams) {
        Map<String, String> params = new HashMap<String, String>();

        try{
            String sourceField = extractSingleParam(conditionParams, "sourceFieldsList");
            String destinationField = extractSingleParam(conditionParams, "destinationFieldsList");

            params.put("sourceField", sourceField);
            params.put("destinationField", destinationField);
        } catch (IllegalArgumentException iae) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }
}

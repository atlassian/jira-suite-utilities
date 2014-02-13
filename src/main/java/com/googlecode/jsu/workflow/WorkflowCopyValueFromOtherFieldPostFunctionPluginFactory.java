package com.googlecode.jsu.workflow;

import java.util.*;

import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

/**
 * This class defines the parameters available for Copy Value From Other Field Post Function.
 *
 * @author Gustavo Martin.
 */
public class WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {

    public static final String PARAM_SOURCE_FIELD = "sourceField";
    public static final String PARAM_DEST_FIELD = "destinationField";
    public static final String PARAM_COPY_TYPE = "copyType";
    private static final String VALUE_SOURCE_LIST = "val-sourceFieldsList";
    private static final String VALUE_DEST_LIST = "val-destinationFieldsList";
    private static final String VALUE_SOURCE_SELECTED = "val-sourceFieldSelected";
    private static final String VALUE_DEST_SELECTED = "val-destinationFieldSelected";
    private static final String VALUE_COPY_TYPE = "val-copyType";


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
        List<FieldContainer> sourceFields = fieldCollectionsUtils.getFieldContainers(fieldCollectionsUtils.getCopyFromFields());
        List<FieldContainer> destinationFields = fieldCollectionsUtils.getFieldContainers(fieldCollectionsUtils.getCopyToFields());

        velocityParams.put(VALUE_SOURCE_LIST, Collections.unmodifiableList(sourceFields));
        velocityParams.put(VALUE_DEST_LIST, Collections.unmodifiableList(destinationFields));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);

        Field sourceFieldId = workflowUtils.getFieldFromDescriptor(descriptor, PARAM_SOURCE_FIELD);
        Field destinationField = workflowUtils.getFieldFromDescriptor(descriptor, PARAM_DEST_FIELD);

        velocityParams.put(VALUE_SOURCE_SELECTED, sourceFieldId);
        velocityParams.put(VALUE_DEST_SELECTED, destinationField);

        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;
        velocityParams.put(VALUE_COPY_TYPE,getCopyType(functionDescriptor));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        Field sourceFieldId = workflowUtils.getFieldFromDescriptor(descriptor, PARAM_SOURCE_FIELD);
        Field destinationField = workflowUtils.getFieldFromDescriptor(descriptor, PARAM_DEST_FIELD);

        velocityParams.put(VALUE_SOURCE_SELECTED, sourceFieldId);
        velocityParams.put(VALUE_DEST_SELECTED, destinationField);

        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;
        velocityParams.put(VALUE_COPY_TYPE,getCopyType(functionDescriptor));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> conditionParams) {
        Map<String, String> params = new HashMap<String, String>();

        try{
            String sourceField = extractSingleParam(conditionParams, "sourceFieldsList");
            String destinationField = extractSingleParam(conditionParams, "destinationFieldsList");
            String copyType = extractSingleParam(conditionParams, PARAM_COPY_TYPE);

            params.put(PARAM_SOURCE_FIELD, sourceField);
            params.put(PARAM_DEST_FIELD, destinationField);
            params.put(PARAM_COPY_TYPE, copyType);
        } catch (IllegalArgumentException iae) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }

    private String getCopyType(FunctionDescriptor functionDescriptor) {
        String value = (String) functionDescriptor.getArgs().get(PARAM_COPY_TYPE);

        if (value == null || value.equals("null")) {
            return "same";
        } else {
            return value;
        }
    }
}

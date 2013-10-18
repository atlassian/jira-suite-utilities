package com.googlecode.jsu.workflow;

import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines the parameters available for Regexp Field Validator.
 */
public class WorkflowRegexpFieldValidatorPluginFactory
        extends AbstractWorkflowPluginFactory
        implements WorkflowPluginValidatorFactory {

    public static final String PARAM_VALIDATE_FIELD = "fieldSelected";
    public static final String PARAM_EXPRESSION = "expressionSelected";

    private static final String VAL_VALIDATE_FIELD = "val-" + PARAM_VALIDATE_FIELD;
    private static final String VAL_EXPRESSION = "val-" + PARAM_EXPRESSION;

    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final WorkflowUtils workflowUtils;

    public WorkflowRegexpFieldValidatorPluginFactory(FieldCollectionsUtils fieldCollectionsUtils,WorkflowUtils workflowUtils) {
        this.fieldCollectionsUtils = fieldCollectionsUtils;
        this.workflowUtils = workflowUtils;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForInput(java.util.Map)
     */
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        List<Field> allTextFields = fieldCollectionsUtils.getAllTextFields();

        velocityParams.put("val-fieldList", allTextFields);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams,descriptor);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        @SuppressWarnings("unchecked")
        Map<String, Object> args = validatorDescriptor.getArgs();

        String validateField = (String) args.get(PARAM_VALIDATE_FIELD);
        String expression = (String) args.get(PARAM_EXPRESSION);

        velocityParams.put(VAL_VALIDATE_FIELD, workflowUtils.getFieldFromKey(validateField));
        velocityParams.put(VAL_EXPRESSION, expression);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> validatorParams) {
        Map<String, Object> params = new HashMap<String, Object>();

        try{
            String validateField = extractSingleParam(validatorParams, "fieldList");
            String expression = extractSingleParam(validatorParams, "expression");

            params.put(PARAM_VALIDATE_FIELD, validateField);
            params.put(PARAM_EXPRESSION, expression);

        } catch(IllegalArgumentException iae) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }
}

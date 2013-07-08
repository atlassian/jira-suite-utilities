package com.googlecode.jsu.workflow;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;

/**
 * This class defines the parameters available for Fields Required Validator.
 *
 * @author Gustavo Martin.
 */
public class WorkflowFieldsRequiredValidatorPluginFactory
        extends AbstractWorkflowPluginFactory
        implements WorkflowPluginValidatorFactory {

    public static final String SELECTED_FIELDS = "hidFieldsList";

    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final WorkflowUtils workflowUtils;

    public WorkflowFieldsRequiredValidatorPluginFactory(
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
        List<Field> allFields = fieldCollectionsUtils.getRequirableFields();

        velocityParams.put("val-fieldsList", allFields);
        velocityParams.put("val-splitter", WorkflowUtils.SPLITTER);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForEdit(
            Map<String, Object> velocityParams, AbstractDescriptor descriptor
    ) {
        getVelocityParamsForInput(velocityParams);

        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        Map<String, Object> args = validatorDescriptor.getArgs();

        velocityParams.remove("val-fieldsList");

        Collection<Field> fieldsSelected = getSelectedFields(args);
        List<Field> allFields = fieldCollectionsUtils.getRequirableFields();

        allFields.removeAll(fieldsSelected);

        velocityParams.put("val-fieldsListSelected", fieldsSelected);
        velocityParams.put("val-hidFieldsList", workflowUtils.getStringField(fieldsSelected, WorkflowUtils.SPLITTER));
        velocityParams.put("val-fieldsList", allFields);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForView(
            Map<String, Object> velocityParams, AbstractDescriptor descriptor
    ) {
        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        Map<String, Object> args = validatorDescriptor.getArgs();

        velocityParams.put("val-fieldsListSelected", getSelectedFields(args));
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> validatorParams) {
        Map<String, String> params = new HashMap<String, String>();
        String strFieldsSelected = extractSingleParam(validatorParams, SELECTED_FIELDS);

        if ("".equals(strFieldsSelected)) {
            throw new IllegalArgumentException("At least one field must be selected");
        }

        params.put(SELECTED_FIELDS, strFieldsSelected);

        return params;
    }

    /**
     * Get fields were selected in UI.
     */
    public Collection<Field> getSelectedFields(Map<String, Object> args) {
        String strFieldsSelected = (String) args.get(SELECTED_FIELDS);

        return workflowUtils.getFields(strFieldsSelected, WorkflowUtils.SPLITTER);
    }
}

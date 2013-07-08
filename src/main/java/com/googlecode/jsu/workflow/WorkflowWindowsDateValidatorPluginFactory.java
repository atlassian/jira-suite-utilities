package com.googlecode.jsu.workflow;

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
import org.apache.commons.lang.StringUtils;

/**
 * @author Gustavo Martin.
 *
 * This class defines the parameters available for Windows Date Validator.
 *
 */
public class WorkflowWindowsDateValidatorPluginFactory extends
        AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {

    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final WorkflowUtils workflowUtils;

    public WorkflowWindowsDateValidatorPluginFactory(
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
        List<Field> allDateFields = fieldCollectionsUtils.getAllDateFields();

        velocityParams.put("val-date1FieldsList", allDateFields);
        velocityParams.put("val-date2FieldsList", allDateFields);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForEdit(
            Map<String, Object> velocityParams,
            AbstractDescriptor descriptor
    ) {
        getVelocityParamsForInput(velocityParams);

        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        Map args = validatorDescriptor.getArgs();

        String date1 = (String) args.get("date1Selected");
        String date2 = (String) args.get("date2Selected");
        String windowsDays = (String) args.get("windowsDays");

        velocityParams.put("val-date1Selected", workflowUtils.getFieldFromKey(date1));
        velocityParams.put("val-date2Selected", workflowUtils.getFieldFromKey(date2));
        velocityParams.put("val-windowsDays", windowsDays);

    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForView(
            Map<String, Object> velocityParams,
            AbstractDescriptor descriptor
    ) {
        ValidatorDescriptor validatorDescriptor = (ValidatorDescriptor) descriptor;
        Map args = validatorDescriptor.getArgs();

        String date1 = (String) args.get("date1Selected");
        String date2 = (String) args.get("date2Selected");
        String windowsDays = (String) args.get("windowsDays");

        velocityParams.put("val-date1Selected", workflowUtils.getFieldFromKey(date1));
        velocityParams.put("val-date2Selected", workflowUtils.getFieldFromKey(date2));
        velocityParams.put("val-windowsDays", windowsDays);

    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> validatorParams) {
        Map<String, Object> params = new HashMap<String, Object>();

        try{
            String date1 = extractSingleParam(validatorParams, "date1FieldsList");
            String date2 = extractSingleParam(validatorParams, "date2FieldsList");
            String windowsDays = extractSingleParam(validatorParams, "windowsDays");
            if (StringUtils.isEmpty(windowsDays)) {
                windowsDays = "0";
            }

            params.put("date1Selected", date1);
            params.put("date2Selected", date2);
            params.put("windowsDays", windowsDays);

        }catch(IllegalArgumentException iae){
            // Aggregate so that Transitions can be added.
        }

        return params;
    }

}

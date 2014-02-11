package com.googlecode.jsu.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import org.apache.commons.lang3.StringUtils;

/**
 * Class responsible for setting up all that is necessary for the execution of
 * the plugin.
 *
 * @author Cristiane Fontana
 * @version 1.0 Plugin creation.
 */
public class WorkflowUpdateIssueCustomFieldFunctionPluginFactory extends
        AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {

    public static final String PARAM_FIELD_ID = "fieldId";
    public static final String PARAM_FIELD_VALUE = "fieldValue";
    public static final String TARGET_FIELD_NAME = "field.name";
    public static final String TARGET_FIELD_VALUE = "field.value";
    public static final String PARAM_APPEND_VALUE = "appendValue";
    public static final String TARGET_APPEND_VALUE = "append.value";

    private final CustomFieldManager customFieldManager;

    public WorkflowUpdateIssueCustomFieldFunctionPluginFactory(CustomFieldManager customFieldManager) {
        this.customFieldManager = customFieldManager;
    }

    public Map getDescriptorParams(Map conditionParams) {
        Map<String, String> params = new HashMap<String, String>();

        String fieldId = extractSingleParam(conditionParams, PARAM_FIELD_ID);
        params.put(TARGET_FIELD_NAME, fieldId);

        String fieldValue = extractSingleParam(conditionParams, PARAM_FIELD_VALUE);
        params.put(TARGET_FIELD_VALUE, fieldValue.trim());

        if(conditionParams.containsKey(PARAM_APPEND_VALUE)) {
            String appendValue = extractSingleParam(conditionParams, PARAM_APPEND_VALUE);
            if(!StringUtils.isBlank(appendValue)) {
                params.put(TARGET_APPEND_VALUE, appendValue.trim());
            }
        }

        return params;
    }

    protected void getVelocityParamsForEdit(Map velocityParams,	AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);

        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a FunctionDescriptor.");
        }

        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;

        velocityParams.put(PARAM_FIELD_ID, functionDescriptor.getArgs().get(TARGET_FIELD_NAME));

        String value = (String) functionDescriptor.getArgs().get(TARGET_FIELD_VALUE);

        if (value == null || value.equals("null")) {
            velocityParams.put(PARAM_FIELD_VALUE, null);
        } else {
            velocityParams.put(PARAM_FIELD_VALUE, value.trim());
        }

        String appendValue = (String) functionDescriptor.getArgs().get(TARGET_APPEND_VALUE);

        if (appendValue == null) {
            velocityParams.put(PARAM_APPEND_VALUE, null);
        } else {
            velocityParams.put(PARAM_APPEND_VALUE, appendValue.trim());
        }
    }

    protected void getVelocityParamsForInput(Map velocityParams) {
        List<CustomField> customFields = customFieldManager.getCustomFieldObjects();

        velocityParams.put("fields", customFields);
    }

    protected void getVelocityParamsForView(Map velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a FunctionDescriptor.");
        } else {
            FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;

            String fieldName = (String) functionDescriptor.getArgs().get(TARGET_FIELD_NAME);

            velocityParams.put(
                    PARAM_FIELD_ID,
                    customFieldManager.getCustomFieldObject(fieldName).getNameKey()
            );
            velocityParams.put(
                    PARAM_FIELD_VALUE,
                    functionDescriptor.getArgs().get(TARGET_FIELD_VALUE)
            );

            String appendValue = (String) functionDescriptor.getArgs().get(TARGET_APPEND_VALUE);
            if(!StringUtils.isBlank(appendValue)) {
                velocityParams.put(PARAM_APPEND_VALUE, appendValue.trim());
            }

        }
    }
}

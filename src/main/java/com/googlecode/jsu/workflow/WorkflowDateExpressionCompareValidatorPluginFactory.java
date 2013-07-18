package com.googlecode.jsu.workflow;

import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.googlecode.jsu.helpers.ConditionCheckerFactory;
import com.googlecode.jsu.helpers.ConditionType;
import com.googlecode.jsu.helpers.YesNoType;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.jsu.helpers.YesNoType.NO;
import static com.googlecode.jsu.helpers.YesNoType.YES;
import static java.util.Arrays.asList;

/**
 * This class defines the parameters available for Date Expression Compare Validator.
 *
 */
public class WorkflowDateExpressionCompareValidatorPluginFactory extends
        AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory {

    public static final String PARAM_DATE_FIELD = "dateFieldSelected";
    public static final String PARAM_EXPRESSION = "expressionSelected";
    public static final String PARAM_CONDITION = "conditionSelected";
    public static final String PARAM_INCLUDE_TIME = "includeTimeSelected";

    private static final String VAL_DATE_FIELD = "val-" + PARAM_DATE_FIELD;
    private static final String VAL_EXPRESSION = "val-" + PARAM_EXPRESSION;
    private static final String VAL_CONDITION = "val-" + PARAM_CONDITION;
    private static final String VAL_INCLUDE_TIME = "val-" + PARAM_INCLUDE_TIME;


    private final ConditionCheckerFactory conditionCheckerFactory;
    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final WorkflowUtils workflowUtils;

    public WorkflowDateExpressionCompareValidatorPluginFactory(ConditionCheckerFactory conditionCheckerFactory,
                                                               FieldCollectionsUtils fieldCollectionsUtils, WorkflowUtils workflowUtils) {
        this.conditionCheckerFactory = conditionCheckerFactory;
        this.fieldCollectionsUtils = fieldCollectionsUtils;
        this.workflowUtils = workflowUtils;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForInput(java.util.Map)
     */
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        List<Field> allDateFields = fieldCollectionsUtils.getAllDateFields();
        List<ConditionType> conditionList = conditionCheckerFactory.getConditionTypes();

        velocityParams.put("val-dateFieldsList", allDateFields);
        velocityParams.put("val-conditionList", conditionList);
        velocityParams.put("val-includeTime", asList(YES, NO));
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

        String date = (String) args.get(PARAM_DATE_FIELD);
        String expression = (String) args.get(PARAM_EXPRESSION);
        String conditionId = (String) args.get(PARAM_CONDITION);
        String includeTime = (String) args.get(PARAM_INCLUDE_TIME);

        ConditionType condition = conditionCheckerFactory.findConditionById(conditionId);
        YesNoType ynTime = (Integer.parseInt(includeTime) == 1) ? YES : NO;

        velocityParams.put(VAL_DATE_FIELD, workflowUtils.getFieldFromKey(date));
        velocityParams.put(VAL_EXPRESSION, expression);
        velocityParams.put(VAL_CONDITION, condition);
        velocityParams.put(VAL_INCLUDE_TIME, ynTime);
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

        String date = (String) args.get(PARAM_DATE_FIELD);
        String expression = (String) args.get(PARAM_EXPRESSION);
        String conditionId = (String) args.get(PARAM_CONDITION);
        String includeTime = (String) args.get(PARAM_INCLUDE_TIME);

        ConditionType condition = conditionCheckerFactory.findConditionById(conditionId);
        YesNoType ynTime = (Integer.parseInt(includeTime) == 1) ? YES : NO;

        velocityParams.put(VAL_DATE_FIELD, workflowUtils.getFieldFromKey(date));
        velocityParams.put(VAL_EXPRESSION, expression);
        velocityParams.put(VAL_CONDITION, condition);
        velocityParams.put(VAL_INCLUDE_TIME, ynTime);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> validatorParams) {
        Map<String, Object> params = new HashMap<String, Object>();

        try{
            String date = extractSingleParam(validatorParams, "dateFieldsList");
            String expression = extractSingleParam(validatorParams, "expression");
            String condition = extractSingleParam(validatorParams, "conditionList");
            String includeTime = extractSingleParam(validatorParams, "includeTimeList");

            params.put(PARAM_DATE_FIELD, date);
            params.put(PARAM_EXPRESSION, expression);
            params.put(PARAM_CONDITION, condition);
            params.put(PARAM_INCLUDE_TIME, includeTime);

        } catch(IllegalArgumentException iae) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }
}

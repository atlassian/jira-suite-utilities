package com.googlecode.jsu.workflow.validator;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.helpers.ConditionType;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;

import java.text.SimpleDateFormat;

/**
 * Abstract base class for date validators.
 */
public abstract class AbstractDateCompareValidator extends GenericValidator {

    private final ApplicationProperties applicationProperties;
    private final I18nHelper.BeanFactory beanFactory;

    public AbstractDateCompareValidator (
            ApplicationProperties applicationProperties,
            FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils,
            I18nHelper.BeanFactory beanFactory
    ) {
        super(fieldCollectionsUtils, workflowUtils);

        this.applicationProperties = applicationProperties;
        this.beanFactory = beanFactory;
    }

    protected abstract void validate() throws InvalidInputException, WorkflowException;

    /**
     * @param fldDate
     *
     * Throws an Exception if the field is null, but it is required.
     */
    protected void validateRequired(Field fldDate) {
        final Issue issue = getIssue();

        if (fieldCollectionsUtils.isFieldRequired(issue, fldDate)) {
            String msg = this.beanFactory.getInstance(
                    ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser())
                    .getText("datecompare-validator-view.is_required", fldDate.getName());
            this.setExceptionMessage(
                    fldDate,
                    msg,
                    msg
            );
        }
    }

    /**
     * @param dateField The date field to which the expression belongs to.
     * @param expression The expression value.
     *
     * Throws an Exception, because given expression is invalid (null or syntax)
     */
    protected void invalidExpression(Field dateField, String expression) {
        String msg = this.beanFactory.getInstance(
                ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser())
                .getText("dateexpressioncompare-validator-view.is_invalid", expression);
        this.setExceptionMessage(
                dateField,
                msg,
                msg
        );
    }

    protected void wrongDataErrorMessage(
            Field field, Object fieldValue
    ) {
        String msg = this.beanFactory.getInstance(
                ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser())
                .getText("datecompare-validator-view.not_a_date",field.getName(),fieldValue.toString());
        this.setExceptionMessage(
                field,
                msg,
                msg
        );
    }

    protected void generateErrorMessage(
            Field field1, Object fieldValue1,
            String nameOrValue, Object fieldValue2,
            ConditionType condition, boolean includeTime
    ) {
        // Formats date to current locale to display the Exception.
        SimpleDateFormat formatter;
        SimpleDateFormat defaultFormatter;

        if (includeTime) {
            defaultFormatter = new SimpleDateFormat(
                    applicationProperties.getDefaultString(APKeys.JIRA_DATE_PICKER_JAVA_FORMAT)
            );
            formatter = new SimpleDateFormat(
                    applicationProperties.getDefaultString(APKeys.JIRA_DATE_PICKER_JAVA_FORMAT),
                    applicationProperties.getDefaultLocale()
            );
        }else{
            defaultFormatter = new SimpleDateFormat(
                    applicationProperties.getDefaultString(APKeys.JIRA_DATE_TIME_PICKER_JAVA_FORMAT)
            );
            formatter = new SimpleDateFormat(
                    applicationProperties.getDefaultString(APKeys.JIRA_DATE_TIME_PICKER_JAVA_FORMAT),
                    applicationProperties.getDefaultLocale()
            );
        }

        String errorMsg;

        try{
            errorMsg = " ( " + formatter.format(fieldValue2) + " )";
        } catch (IllegalArgumentException e) {
            try {
                errorMsg = " ( " + defaultFormatter.format(fieldValue2) + " )";
            } catch(Exception e1) {
                errorMsg = " ( " + fieldValue2 + " )";
            }
        }

        I18nHelper i18nh = this.beanFactory.getInstance(
                ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser());
        String msg = i18nh.getText("datecompare-validator-view.is_not",
                field1.getName(),i18nh.getText(condition.getDisplayTextKey()),nameOrValue==null?"":nameOrValue,errorMsg);

        this.setExceptionMessage(
                field1,
                msg,
                msg
        );
    }
}

package com.googlecode.jsu.workflow.validator;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.annotation.Argument;
import com.googlecode.jsu.helpers.ComparisonType;
import com.googlecode.jsu.helpers.ConditionChecker;
import com.googlecode.jsu.helpers.ConditionCheckerFactory;
import com.googlecode.jsu.helpers.ConditionType;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

import static com.googlecode.jsu.helpers.ConditionCheckerFactory.DATE;
import static com.googlecode.jsu.helpers.ConditionCheckerFactory.DATE_WITHOUT_TIME;

/**
 * This validator compare two datetime fields, using the given comparison type.
 * And returning an exception if it doesn't fulfill the condition.
 */
public class DateCompareValidator extends AbstractDateCompareValidator {
    @Argument("date1Selected")
    private String date1;

    @Argument("date2Selected")
    private String date2;

    @Argument("conditionSelected")
    private String conditionId;

    @Argument("includeTimeSelected")
    private String includeTimeValue;

    private final Logger log = LoggerFactory.getLogger(DateCompareValidator.class);

    private final ApplicationProperties applicationProperties;
    private final ConditionCheckerFactory conditionCheckerFactory;
    private final I18nHelper.BeanFactory beanFactory;

    public DateCompareValidator(
            ApplicationProperties applicationProperties,
            ConditionCheckerFactory conditionCheckerFactory,
            FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils,
            I18nHelper.BeanFactory beanFactory
    ) {
        super(applicationProperties,fieldCollectionsUtils, workflowUtils,beanFactory);

        this.applicationProperties = applicationProperties;
        this.conditionCheckerFactory = conditionCheckerFactory;
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.validator.GenericValidator#validate()
     */
    protected void validate() throws InvalidInputException, WorkflowException {
        Field field1 = workflowUtils.getFieldFromKey(date1);
        Field field2 = workflowUtils.getFieldFromKey(date2);

        ConditionType condition = conditionCheckerFactory.findConditionById(conditionId);
        boolean includeTime = Integer.parseInt(includeTimeValue) == 1;

        // Compare Dates.
        if ((field1 != null) && (field2 != null)) {
            Object objValue1 = workflowUtils.getFieldValueFromIssue(getIssue(), field1);
            Object objValue2 = workflowUtils.getFieldValueFromIssue(getIssue(), field2);
            Date objDate1, objDate2;

            try {
                objDate1 = (Date) objValue1;
            } catch (ClassCastException e) {
                wrongDataErrorMessage(field1, objValue1);

                return;
            }

            try {
                objDate2 = (Date) objValue2;
            } catch (ClassCastException e) {
                wrongDataErrorMessage(field2, objValue2);

                return;
            }

            if ((objDate1 != null) && (objDate2 != null)) {
                ComparisonType comparison = (includeTime) ? DATE : DATE_WITHOUT_TIME;
                ConditionChecker checker = conditionCheckerFactory.getChecker(comparison, condition);

                Calendar calDate1 = Calendar.getInstance(applicationProperties.getDefaultLocale());
                Calendar calDate2 = Calendar.getInstance(applicationProperties.getDefaultLocale());

                calDate1.setTime( objDate1);
                calDate2.setTime( objDate2);

                boolean result = checker.checkValues(calDate1, calDate2);

                if (log.isDebugEnabled()) {
                    log.debug(
                            "Compare field \"" + field1.getName() +
                            "\" and field \"" + field2.getName() +
                            "\" with values [" + calDate1 +
                            "] and [" + calDate2 +
                            "] with result " + result
                    );
                }

                if (!result) {
                    generateErrorMessage(field1, objDate1, field2.getName(), objDate2, condition, includeTime);
                }
            } else {
                // If any of fields are null, validates if the field is required. Otherwise, doesn't throws an Exception.
                if (objDate1 == null) {
                    validateRequired(field1);
                }

                if (objDate2 == null) {
                    validateRequired(field2);
                }
            }
        } else {
            log.error("Unable to find field with ids [" + date1 + "] and [" + date2 + "]");
        }
    }
}

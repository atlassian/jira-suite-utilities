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
 * This validator compares a datetime field to a datetime expression, like for
 * example now, or 2d, for plus 2 days, or -1m for one month ago, using the given
 * comparison type. And returning an exception if it doesn't fulfill the condition.
 */
public class DateExpressionCompareValidator extends AbstractDateCompareValidator {
    @Argument("dateFieldSelected")
    private String dateField;

    @Argument("expressionSelected")
    private String expression;

    @Argument("conditionSelected")
    private String conditionId;

    @Argument("includeTimeSelected")
    private String includeTimeValue;

    private final Logger log = LoggerFactory.getLogger(DateExpressionCompareValidator.class);

    private final ApplicationProperties applicationProperties;
    private final ConditionCheckerFactory conditionCheckerFactory;

    public DateExpressionCompareValidator(
            ApplicationProperties applicationProperties,
            ConditionCheckerFactory conditionCheckerFactory,
            FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils,
            I18nHelper.BeanFactory beanFactory
    ) {
        super(applicationProperties,fieldCollectionsUtils, workflowUtils,beanFactory);

        this.applicationProperties = applicationProperties;
        this.conditionCheckerFactory = conditionCheckerFactory;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.validator.GenericValidator#validate()
     */
    protected void validate() throws InvalidInputException, WorkflowException {
        Field field = workflowUtils.getFieldFromKey(dateField);

        ConditionType condition = conditionCheckerFactory.findConditionById(conditionId);
        boolean includeTime = Integer.parseInt(includeTimeValue) == 1;

        // Compare Dates.
        if ((field != null) && (expression != null)) {
            Object objValue1 = workflowUtils.getFieldValueFromIssue(getIssue(), field);

            Date objDate1, objDate2;

            try {
                objDate1 = (Date) objValue1;
            } catch (ClassCastException e) {
                wrongDataErrorMessage(field, objValue1);

                return;
            }

            objDate2 = getSecondDate();
            if(objDate2==null) {
                invalidExpression(field,expression);

                return;
            }

            if ((objDate1 != null)) {
                ComparisonType comparison = (includeTime) ? DATE : DATE_WITHOUT_TIME;
                ConditionChecker checker = conditionCheckerFactory.getChecker(comparison, condition);

                Calendar calDate1 = Calendar.getInstance(applicationProperties.getDefaultLocale());
                Calendar calDate2 = Calendar.getInstance(applicationProperties.getDefaultLocale());

                calDate1.setTime( objDate1);
                calDate2.setTime( objDate2);

                boolean result = checker.checkValues(calDate1, calDate2);

                if (log.isDebugEnabled()) {
                    log.debug(
                            "Compare field \"" + field.getName() +
                            "\" and expression \"" + expression +
                            "\" with values [" + calDate1 +
                            "] and [" + calDate2 +
                            "] with result " + result
                    );
                }

                if (!result) {
                    generateErrorMessage(field, objDate1, null, objDate2, condition, includeTime);
                }
            } else {
                // If date field is null, validate if the field is required. Otherwise, doesn't throws an Exception.
                validateRequired(field);
            }
        } else {
            log.error("Unable to find field with id [" + dateField + "]");
        }
    }

    private Date getSecondDate() {
        if(expression!=null) {
            if("now".equals(expression)) {
                return new Date();
            } else {
                Integer offset;
                String unit = expression.substring(expression.length()-1);
                try {
                    offset = Integer.parseInt(expression.substring(0,expression.length()-1));
                } catch(Exception e) {
                    return null;
                }

                Calendar cal = Calendar.getInstance();
                int calField;
                if("d".equals(unit)) {
                    calField = Calendar.DAY_OF_YEAR;
                } else if("w".equals(unit)) {
                    calField = Calendar.WEEK_OF_YEAR;
                } else if("m".equals(unit)) {
                    calField = Calendar.MONTH;
                } else if("y".equals(unit)) {
                    calField = Calendar.YEAR;
                } else {
                    return null;
                }
                cal.add(calField, offset);
                return cal.getTime();
            }
        }

        return null;
    }
}
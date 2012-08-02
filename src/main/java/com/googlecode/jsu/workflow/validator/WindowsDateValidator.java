package com.googlecode.jsu.workflow.validator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.annotation.Argument;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;

/**
 * @author Gustavo Martin
 *
 * This validator compare two datetime fields, and verifies if the first of them,
 * is less than the second plus a number of days.
 * And returning an exception if it doesn't fulfill the condition.
 *
 */
public class WindowsDateValidator extends GenericValidator {
    @Argument("date1Selected")
    private String date1;

    @Argument("date2Selected")
    private String date2;

    @Argument
    private String windowsDays;

    private final ApplicationProperties applicationProperties;
    private final I18nHelper.BeanFactory beanFactory;

    public WindowsDateValidator(
            FieldCollectionsUtils fieldCollectionsUtils,
            ApplicationProperties applicationProperties,
            WorkflowUtils workflowUtils,
            I18nHelper.BeanFactory beanFactory
    ) {
        super(fieldCollectionsUtils, workflowUtils);

        this.applicationProperties = applicationProperties;
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.validator.GenericValidator#validate()
     */
    protected void validate() throws InvalidInputException, WorkflowException {
        Field fldDate1 = workflowUtils.getFieldFromKey(date1);
        Field fldDate2 = workflowUtils.getFieldFromKey(date2);

        // Compare Dates.
        if ((fldDate1 != null) && (fldDate2 != null)) {
            checkDatesCondition(fldDate1, fldDate2, windowsDays);
        }
    }

    /**
     * @param fldDate1
     * @param fldDate2
     * @param window
     *
     * It makes the comparison properly this.
     */
    private void checkDatesCondition(Field fldDate1, Field fldDate2, String window) {
        boolean condOK = false;

        Object objDate1 = workflowUtils.getFieldValueFromIssue(getIssue(), fldDate1);
        Object objDate2 = workflowUtils.getFieldValueFromIssue(getIssue(), fldDate2);

        I18nHelper i18nh = this.beanFactory.getInstance(
            ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser());

        if ((objDate1 != null) && (objDate2 != null)) {
            // It Takes the Locale for inicialize dates.
            Locale locale = applicationProperties.getDefaultLocale();

            Calendar calDate1 = Calendar.getInstance(locale);
            Calendar calDate2 = Calendar.getInstance(locale);
            Calendar calWindowsDate = Calendar.getInstance(locale);

            calDate1.setTime((Date) objDate1);
            calDate2.setTime((Date) objDate2);
            calWindowsDate.setTime((Date) objDate2);
            calWindowsDate.add(Calendar.DATE, Integer.parseInt(window));

            fieldCollectionsUtils.clearCalendarTimePart(calDate1);
            fieldCollectionsUtils.clearCalendarTimePart(calDate2);
            setTimePartMidnight(calWindowsDate);

            Date date1 = calDate1.getTime();
            Date date2 = calDate2.getTime();
            Date windowsDate = calWindowsDate.getTime();

            int comparison = date1.compareTo(windowsDate);

            if(comparison < 0){
                comparison = date1.compareTo(date2);

                if(comparison>=0){
                    condOK = true;
                }
            }

            if (!condOK) {
                // Formats date to current locale, for display the Exception.
                SimpleDateFormat defaultFormatter = new SimpleDateFormat(
                        applicationProperties.getDefaultString(APKeys.JIRA_DATE_PICKER_JAVA_FORMAT)
                );
                SimpleDateFormat formatter = new SimpleDateFormat(
                        applicationProperties.getDefaultString(APKeys.JIRA_DATE_PICKER_JAVA_FORMAT), locale
                );

                String errorMsg;
                try {
                    errorMsg = i18nh.getText("windowsdate-validator-view.between_and",formatter.format(date2),formatter.format(windowsDate));
                } catch (IllegalArgumentException e) {
                    try {
                        errorMsg = i18nh.getText("windowsdate-validator-view.between_and",defaultFormatter.format(date2),defaultFormatter.format(windowsDate));
                    } catch (Exception e1) {
                        errorMsg = i18nh.getText("windowsdate-validator-view.between_and",date2.toString(),windowsDate.toString());
                    }
                }

                String msg = i18nh.getText("windowsdate-validator-view.not_within",
                    fldDate1.getName(),fldDate2.getName(),window,errorMsg);

                this.setExceptionMessage(
                        fldDate1,
                        msg,
                        msg
                );
            }
        } else {
            // If any of fields are null, validates if the field is required. Otherwise, doesn't throws an Exception.
            if (objDate1 == null) {
                validateRequired(fldDate1,i18nh);
            }

            if (objDate2 == null) {
                validateRequired(fldDate2,i18nh);
            }
        }
    }

    private void setTimePartMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    /**
     * @param fldDate
     *
     * Throws an Exception if the field is null, but it is required.
     */
    private void validateRequired(Field fldDate, I18nHelper i18nHelper){
        if (fieldCollectionsUtils.isFieldRequired(getIssue(), fldDate)) {
            String msg = i18nHelper.getText("windowsdate-validator-view.is_required",fldDate.getName());
            this.setExceptionMessage(
                    fldDate,
                    msg,
                    msg
            );
        }
    }
}

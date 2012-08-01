package com.googlecode.jsu.workflow.validator;

import static com.googlecode.jsu.helpers.ConditionCheckerFactory.EQUAL;
import static com.googlecode.jsu.helpers.ConditionCheckerFactory.STRING;
import static com.googlecode.jsu.workflow.WorkflowFieldsRequiredValidatorPluginFactory.SELECTED_FIELDS;

import java.util.Collection;
import java.util.List;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.util.I18nHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.Field;
import com.googlecode.jsu.annotation.Argument;
import com.googlecode.jsu.helpers.ConditionChecker;
import com.googlecode.jsu.helpers.ConditionCheckerFactory;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;

/**
 * This validator verifies that certain fields must be required at execution of a transition.
 *
 * @author Gustavo Martin
 */
public class FieldsRequiredValidator extends GenericValidator {
    private static final Logger log = LoggerFactory.getLogger(FieldsRequiredValidator.class);

    @Argument(SELECTED_FIELDS)
    private String fieldList;

    private final ConditionCheckerFactory conditionCheckerFactory;
    private final I18nHelper.BeanFactory beanFactory;

    /**
     * @param conditionCheckerFactory
     * @param fieldCollectionsUtils
     */
    public FieldsRequiredValidator(
            ConditionCheckerFactory conditionCheckerFactory,
            FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils,
            I18nHelper.BeanFactory beanFactory
    ) {
        super(fieldCollectionsUtils, workflowUtils);

        this.conditionCheckerFactory = conditionCheckerFactory;
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.workflow.Validator#validate(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    protected void validate() throws InvalidInputException, WorkflowException {
        final ConditionChecker checker = conditionCheckerFactory.getChecker(STRING, EQUAL);

        // It obtains the fields that are required for the transition.
        Collection<Field> fieldsSelected = workflowUtils.getFields(fieldList, WorkflowUtils.SPLITTER);
        final Issue issue = getIssue();
        String issueKey = issue.getKey();

        if (issueKey == null) {
            issueKey = "'New issue'";
        }

        if (log.isDebugEnabled()) {
            log.debug(issueKey + ": Found " + fieldsSelected.size() + " fields for validation");
        }

        for (Field field : fieldsSelected) {
            if (fieldCollectionsUtils.isIssueHasField(issue, field)) {
                Object fieldValue;
                if (IssueFieldConstants.COMMENT.equals(field.getId())) {
                    fieldValue = getTransitionComment();
                } else if (IssueFieldConstants.ATTACHMENT.equals(field.getId())) {
                    try {
                        //Actually what we get here are is just the id of the first attahcment. - If there is an attachment at all.
                        fieldValue = ((List<Long>) ((MutableIssue) issue).getModifiedFields().get(IssueFieldConstants.ATTACHMENT).getNewValue()).get(0);
                    } catch (Exception e) {
                        fieldValue = null; //No attachments.
                    }
                } else if (IssueFieldConstants.TIME_SPENT.equals(field.getId())) {
                    try {
                        fieldValue = ((MutableIssue) issue).getModifiedFields().get(IssueFieldConstants.WORKLOG).getNewValue();
                    } catch (Exception e) {
                        fieldValue = null; //No time spent.
                    }
                } else {
                    fieldValue = workflowUtils.getFieldValueFromIssue(issue, field);
                }

                if (log.isDebugEnabled()) {
                    log.debug(
                            issueKey + ": Field '" + field.getName() +
                            " - " +	field.getId() +
                            "' has value [" + fieldValue + "]"
                    );
                }

                if (checker.checkValues(fieldValue, null)) {
                    I18nHelper i18nh = this.beanFactory.getInstance(
                        ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser());
                    String msg1 = i18nh.getText("fieldsrequired-validator-view.is_required",field.getName());
                    String msg2 = i18nh.getText("fieldsrequired-validator-view.is_required_not_present",field.getName());
                    // Sets Exception message.
                    this.setExceptionMessage(
                            field,
                            msg1,
                            msg2
                    );
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(
                            issueKey + ": Field '" + field.getName() +
                            " - " +	field.getId() +
                            "' is not assigned for the issue"
                    );
                }
            }
        }
    }
}

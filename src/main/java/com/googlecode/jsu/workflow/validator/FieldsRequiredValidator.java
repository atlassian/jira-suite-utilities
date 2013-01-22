package com.googlecode.jsu.workflow.validator;

import static com.googlecode.jsu.helpers.ConditionCheckerFactory.EQUAL;
import static com.googlecode.jsu.helpers.ConditionCheckerFactory.STRING;
import static com.googlecode.jsu.workflow.WorkflowFieldsRequiredValidatorPluginFactory.SELECTED_FIELDS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.IssueFieldConstants;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.AffectedVersionsSystemField;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
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
    private final VersionManager versionManager;

    public FieldsRequiredValidator(
            ConditionCheckerFactory conditionCheckerFactory,
            FieldCollectionsUtils fieldCollectionsUtils,
            WorkflowUtils workflowUtils,
            I18nHelper.BeanFactory beanFactory,
            VersionManager versionManager
    ) {
        super(fieldCollectionsUtils, workflowUtils);

        this.conditionCheckerFactory = conditionCheckerFactory;
        this.beanFactory = beanFactory;
        this.versionManager = versionManager;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.workflow.Validator#validate(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    protected void validate() throws InvalidInputException, WorkflowException {
        final ConditionChecker checker = conditionCheckerFactory.getChecker(STRING, EQUAL);

        // It obtains the fields that are required for the transition.
        Collection<Field> fieldsSelected = workflowUtils.getFields(fieldList, WorkflowUtils.SPLITTER);
        final Issue issue = getIssue();
        final Issue originalissue = getOriginalIssue();
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
                } else if (IssueFieldConstants.AFFECTED_VERSIONS.equals(field.getId())) {
                    //special case, archived versions, they are no longer selectable, nor removable, thus if the
                    //original issue has an archived version in affected field, use that as value too
                    Collection originalAffectedVersions = originalissue!=null?originalissue.getAffectedVersions():new ArrayList<Version>();
                    fieldValue = getVersionsWithArchived(issue,field,originalAffectedVersions);
                } else if (IssueFieldConstants.FIX_FOR_VERSIONS.equals(field.getId())) {
                    Collection originalFixVersions = originalissue!=null?originalissue.getFixVersions():new ArrayList<Version>();
                    fieldValue = getVersionsWithArchived(issue,field,originalFixVersions);
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

    private Collection getVersionsWithArchived(Issue issue, Field field, Collection alreadySelectedVersions) {
        Collection archived = versionManager.getVersionsArchived(issue.getProjectObject());
        archived.retainAll(alreadySelectedVersions);
        Object selected = workflowUtils.getFieldValueFromIssue(issue,field);
        if(selected!=null) {
            archived.addAll((Collection)selected);
        }
        return archived.isEmpty()?null:archived;
    }
}

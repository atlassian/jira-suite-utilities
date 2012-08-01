package com.googlecode.jsu.workflow.function;

import java.util.Map;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

/**
 * @author Gustavo Martin
 *
 * This function copies the value from a field to another one.
 */
public class CopyValueFromOtherFieldPostFunction extends AbstractPreserveChangesPostFunction {
    private final WorkflowUtils workflowUtils;
    private final I18nHelper.BeanFactory beanFactory;

    public CopyValueFromOtherFieldPostFunction(WorkflowUtils workflowUtils, I18nHelper.BeanFactory beanFactory) {
        this.workflowUtils = workflowUtils;
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.function.AbstractPreserveChangesPostFunction#executeFunction(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet, com.atlassian.jira.issue.util.IssueChangeHolder)
     */
    @Override
    protected void executeFunction(
            Map<String, Object> transientVars, Map<String, String> args,
            PropertySet ps, IssueChangeHolder holder
    ) throws WorkflowException {
        String fieldFromKey = args.get("sourceField");
        String fieldToKey = args.get("destinationField");

        Field fieldFrom = workflowUtils.getFieldFromKey(fieldFromKey);
        Field fieldTo = workflowUtils.getFieldFromKey(fieldToKey);

        String fieldFromName = (fieldFrom != null) ? fieldFrom.getName() : fieldFromKey;
        String fieldToName = (fieldTo != null) ? fieldTo.getName() : fieldToKey;

        try {
            MutableIssue issue = getIssue(transientVars);

            // It gives the value from the source field.
            Object sourceValue = workflowUtils.getFieldValueFromIssue(issue, fieldFrom);

            if (log.isDebugEnabled()) {
                log.debug(
                        String.format(
                                "Copying value [%s] from issue %s field '%s' to '%s'",
                                sourceValue, issue.getKey(),
                                fieldFromName,
                                fieldToName
                        )
                );
            }

            // It set the value to field.
            workflowUtils.setFieldValue(issue, fieldToKey, sourceValue, holder);

            if (log.isDebugEnabled()) {
                log.debug("Value was successfully copied");
            }
        } catch (Exception e) {
            I18nHelper i18nh = this.beanFactory.getInstance(
                ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser());
            String message = i18nh.getText("copyvaluefromfield-function-view.unable_to_copy",fieldFromName,fieldToName);

            log.error(message, e);

            throw new WorkflowException(message);
        }
    }
}

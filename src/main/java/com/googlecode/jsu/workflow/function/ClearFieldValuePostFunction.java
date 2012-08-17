package com.googlecode.jsu.workflow.function;

import java.util.Map;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.util.WorkflowUtils;
import com.googlecode.jsu.workflow.WorkflowClearFieldValueFunctionPluginFactory;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

/**
 * This function clears field value.
 *
 * @author Alexey Abashev
 */
public class ClearFieldValuePostFunction extends AbstractPreserveChangesPostFunction {
    private final WorkflowUtils workflowUtils;
    private final I18nHelper.BeanFactory beanFactory;

    /**
     * @param workflowUtils
     */
    public ClearFieldValuePostFunction(WorkflowUtils workflowUtils, I18nHelper.BeanFactory beanFactory) {
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
        String fieldKey = args.get(WorkflowClearFieldValueFunctionPluginFactory.FIELD);
        Field field = workflowUtils.getFieldFromKey(fieldKey);

        final String fieldName = (field != null) ? field.getName() : "null";

        // It set the value to field.
        try {
            User currentUser = getCaller(transientVars, args);
            MutableIssue issue = getIssue(transientVars);

            if (log.isDebugEnabled()) {
                log.debug(String.format(
                        "Clean field '%s - %s' in the issue [%s]",
                        fieldKey, fieldName, issue.getKey()
                ));
            }

            workflowUtils.setFieldValue(currentUser, issue, fieldKey, null, holder);
        } catch (Exception e) {
            I18nHelper i18nh = this.beanFactory.getInstance(
                ComponentManager.getInstance().getJiraAuthenticationContext().getLoggedInUser());
            String message = i18nh.getText("clearfieldvalue-function-view.unable_to_purge",fieldKey,fieldName);

            log.error(message, e);

            throw new WorkflowException(message);
        }
    }
}

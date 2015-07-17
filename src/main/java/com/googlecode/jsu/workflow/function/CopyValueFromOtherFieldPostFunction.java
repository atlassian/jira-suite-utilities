package com.googlecode.jsu.workflow.function;


import static com.googlecode.jsu.workflow.WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory.PARAM_COPY_TYPE;
import static com.googlecode.jsu.workflow.WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory.PARAM_DEST_FIELD;
import static com.googlecode.jsu.workflow.WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory.PARAM_SOURCE_FIELD;
import static com.googlecode.jsu.workflow.WorkflowCopyValueFromOtherFieldPostFunctionPluginFactory.PARAM_APPEND_VALUE;

import java.util.Map;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.user.ApplicationUser;
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
        String fieldFromKey = args.get(PARAM_SOURCE_FIELD);
        String fieldToKey = args.get(PARAM_DEST_FIELD);
        String copyType = args.get(PARAM_COPY_TYPE);
        String appendValue = args.get(PARAM_APPEND_VALUE);
        boolean append = "true".equals(appendValue);

        Field fieldFrom = workflowUtils.getFieldFromKey(fieldFromKey);
        Field fieldTo = workflowUtils.getFieldFromKey(fieldToKey);

        String fieldFromName = (fieldFrom != null) ? fieldFrom.getName() : fieldFromKey;
        String fieldToName = (fieldTo != null) ? fieldTo.getName() : fieldToKey;

        try {
            ApplicationUser currentUser = getCallerUser(transientVars, args);
            MutableIssue issue = getIssue(transientVars);
            MutableIssue sourceIssue;
            MutableIssue destIssue;
            if("parent".equals(copyType)) {
                sourceIssue = (MutableIssue) issue.getParentObject();
                destIssue = issue;
                if(sourceIssue==null) {
                    log.debug("Issue (" + destIssue.getKey() + ") has no parent, wont do anything.");
                    return;
                }
            } else {
                //either same, not set, past versions of this did not have this feature, they will be unset
                sourceIssue = issue;
                destIssue = issue;
            }

            // It gives the value from the source field.
            Object sourceValue = workflowUtils.getFieldValueFromIssue(sourceIssue, fieldFrom, true);

            if (log.isDebugEnabled()) {
                log.debug(
                        String.format(
                                "Copying value [%s] from issue %s field '%s' to issue %s field '%s'",
                                sourceValue,
                                sourceIssue.getKey(),
                                fieldFromName,
                                destIssue.getKey(),
                                fieldToName
                        )
                );
            }

            // It set the value to field.
            workflowUtils.setFieldValue(currentUser, destIssue, fieldTo, sourceValue, append, holder);

            if (log.isDebugEnabled()) {
                log.debug("Value was successfully copied");
            }
        } catch (Exception e) {
            I18nHelper i18nh = this.beanFactory.getInstance(
                    ComponentAccessor.getJiraAuthenticationContext().getUser().getDirectoryUser());
            String message = i18nh.getText("copyvaluefromfield-function-view.unable_to_copy",fieldFromName,fieldToName);
            log.error(message, e);
            throw new WorkflowException(message);
        }
    }
}

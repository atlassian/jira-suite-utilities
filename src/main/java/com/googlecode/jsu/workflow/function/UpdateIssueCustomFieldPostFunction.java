package com.googlecode.jsu.workflow.function;

import static com.googlecode.jsu.workflow.WorkflowUpdateIssueCustomFieldFunctionPluginFactory.TARGET_APPEND_VALUE;
import static com.googlecode.jsu.workflow.WorkflowUpdateIssueCustomFieldFunctionPluginFactory.TARGET_FIELD_NAME;
import static com.googlecode.jsu.workflow.WorkflowUpdateIssueCustomFieldFunctionPluginFactory.TARGET_FIELD_VALUE;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.customfields.impl.VersionCFType;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.apache.commons.lang3.StringUtils;

/**
 * Class related to the execution of the plugin.
 *
 * @author Cristiane Fontana
 * @version 1.0
 *
 */
public class UpdateIssueCustomFieldPostFunction extends AbstractPreserveChangesPostFunction {
    private final WorkflowUtils workflowUtils;
    private final I18nHelper.BeanFactory beanFactory;

    public UpdateIssueCustomFieldPostFunction(WorkflowUtils workflowUtils, I18nHelper.BeanFactory beanFactory) {
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
        String fieldKey = args.get(TARGET_FIELD_NAME);
        String appendValue = args.get(TARGET_APPEND_VALUE);
        boolean append = "true".equals(appendValue);

        final Field field = workflowUtils.getFieldFromKey(fieldKey);
        final String fieldName = (field != null) ? field.getName() : "null";

        String configuredValue = args.get(TARGET_FIELD_VALUE);
        Object newValue;

        ApplicationUser currentUser;
        try {
            currentUser = getCallerUser(transientVars, args);
        } catch (Exception e) {
            log.error("Unable to find caller for function", e);
            throw(new WorkflowException(e));
        }

        MutableIssue issue = null;

        try {
            issue = getIssue(transientVars);

            if ("null".equals(configuredValue)) {
                newValue = null;
            } else if ("%%CURRENT_USER%%".equals(configuredValue)) {
                if(append) {
                    String s = workflowUtils.getFieldStringValue(issue,fieldKey);
                    if(s!=null && s.length()>0) {
                        newValue = s + ", " + currentUser.getUsername();
                    } else {
                        newValue = currentUser.getUsername();
                    }
                } else {
                    newValue = currentUser.getUsername();
                }
            //TODO obsolete since JSUTIL-202, but need to stay for backwards compatibility for a while, after that could adapt the texts in updateissuefield-function-view.vm, to overwrite and append accordingly
            } else if ("%%ADD_CURRENT_USER%%".equals(configuredValue)) {
                String s = workflowUtils.getFieldStringValue(issue,fieldKey);
                if(s!=null && s.length()>0) {
                    newValue = s + ", " + currentUser.getUsername();
                } else {
                    newValue = currentUser.getUsername();
                }
            } else if ("%%CURRENT_DATETIME%%".equals(configuredValue)) {
                newValue = new Timestamp(System.currentTimeMillis());
            } else if (append) {
                Object o = workflowUtils.getFieldValueFromIssue(issue,field);
                if(o instanceof List) {
                    CustomField cf = (CustomField)field;
                    List l = (List)o;
                    if(cf.getCustomFieldType() instanceof VersionCFType) {
                        l.addAll(workflowUtils.convertValueToVersions(issue, configuredValue));
                    } else {
                        l.add(workflowUtils.convertStringToOption(issue,cf,configuredValue));
                    }
                    newValue = o;
                } else if (o instanceof Set) {
                    Set newSet  = new HashSet((Set)o);
                    newSet.add(configuredValue);
                    newValue = newSet;
                } else {
                    newValue = configuredValue;
                }
            } else {
                newValue = configuredValue;
            }

            if (log.isDebugEnabled()) {
                log.debug(String.format(
                        "Updating custom field '%s - %s' in issue [%s] with value [%s] (append is %b)",
                        fieldKey, fieldName,
                        issueToString(issue),
                        newValue,
                        append
                ));
            }

            workflowUtils.setFieldValue(currentUser, issue, fieldKey, newValue, holder);
        } catch (Exception e) {
            I18nHelper i18nh = this.beanFactory.getInstance(
                    ComponentAccessor.getJiraAuthenticationContext().getUser().getDirectoryUser());
            String message = i18nh.getText("updateissuefield-function-view.unable_to_update",fieldKey,fieldName,issueToString(issue));

            log.error(message, e);

            throw new WorkflowException(message);
        }
    }

    private String issueToString(MutableIssue issue) {
        return ((issue != null) ? ((issue.getKey() == null) ? "new issue" : issue.getKey()) : "null");
    }
}

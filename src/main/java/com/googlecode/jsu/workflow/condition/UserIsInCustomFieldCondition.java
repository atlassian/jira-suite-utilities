package com.googlecode.jsu.workflow.condition;

import static com.googlecode.jsu.workflow.WorkflowUserIsInCustomFieldConditionPluginFactory.getAllowUserInField;

import java.util.Collection;
import java.util.Map;

import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowContext;

/**
 * This Condition validates if the current user is in any of the selected groups.
 * @author Anton Afanassiev
 */
public class UserIsInCustomFieldCondition extends AbstractJiraCondition {
    private final Logger log = LoggerFactory.getLogger(UserIsInCustomFieldCondition.class);

    private final UserManager userManager;
    private final WorkflowUtils workflowUtils;

    public UserIsInCustomFieldCondition(UserManager userManager, WorkflowUtils workflowUtils) {
        this.userManager = userManager;
        this.workflowUtils = workflowUtils;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.workflow.Condition#passesCondition(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        boolean allowUser = false;

        // Obtains the current user.
        WorkflowContext context = (WorkflowContext) transientVars.get("context");
        ApplicationUser userLogged = userManager.getUserByKey(context.getCaller());

        if (userLogged == null) {
            log.warn("Unable to check condition");

            return false;
        }

        // If there aren't groups selected, hidGroupsList is equal to "".
        // And groupsSelected will be an empty collection.
        String fieldKey = (String) args.get("fieldsList");

        boolean allowUserInField = getAllowUserInField(args);

        Field field = workflowUtils.getFieldFromKey(fieldKey);
        Issue issue = getIssue(transientVars);

        Object fieldValue = workflowUtils.getFieldValueFromIssue(issue, field);

        if (fieldValue != null) {
            if (fieldValue instanceof Collection) {
                // support for MultiUser lists. user must be member of that list to pass condition
                for (Object value : (Collection) fieldValue) {
                    allowUser = compareValues(value, userLogged, allowUserInField);

                    if (allowUser == allowUserInField) {
                        break;
                    }
                }
            } else {
                allowUser = compareValues(fieldValue, userLogged, allowUserInField);
            }
        } else {
            allowUser = !allowUserInField;
        }

        return allowUser;
    }

    private boolean compareValues(Object fieldValue, ApplicationUser user, boolean allowUserInField) {
        boolean result = !allowUserInField;

        if (fieldValue instanceof String) {
            if (fieldValue.equals(user.getUsername())) {
                result = allowUserInField;
            }
        } else {
            if (fieldValue.equals(user)) {
                result = allowUserInField;
            }
        }

        return result;
    }
}

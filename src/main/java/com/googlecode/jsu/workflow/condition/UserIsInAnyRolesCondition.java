package com.googlecode.jsu.workflow.condition;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.module.propertyset.PropertySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * This Condition validates if the current user is in any of the selected roles.
 */
public class UserIsInAnyRolesCondition extends AbstractJiraCondition {
    private static final Logger LOG = LoggerFactory.getLogger(UserIsInAnyRolesCondition.class);

    private final WorkflowUtils workflowUtils;
    private final UserManager userManager;
    private final ProjectRoleManager projectRoleManager;

    public UserIsInAnyRolesCondition(WorkflowUtils workflowUtils, UserManager userManager, ProjectRoleManager projectRoleManager) {
        this.workflowUtils = workflowUtils;
        this.userManager = userManager;
        this.projectRoleManager = projectRoleManager;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.workflow.Condition#passesCondition(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    public boolean passesCondition(Map transientVars, Map args, PropertySet ps) {
        // Obtains the current user.
        Issue issue=getIssue(transientVars);
        String caller = getCallerKey(transientVars,args);

        if (caller != null) { // null -> User not logged in
            ApplicationUser userLogged = workflowUtils.getApplicationUser(caller);

            // If there aren't roles selected, hidRolesList is equal to "".
            // And rolesSelected will be an empty collection.
            String strRolesSelected = (String) args.get("hidRolesList");
            Collection<ProjectRole> rolesSelected = workflowUtils.getRoles(strRolesSelected, WorkflowUtils.SPLITTER);

            for (ProjectRole role : rolesSelected) {
                try {
                    if(projectRoleManager.isUserInProjectRole(userLogged, role, issue.getProjectObject())) {
                        return true;
                    }
                } catch (Exception e) {
                    //see JSUTIL-68
                }
            }
        }

        return false;
    }
    /**
     * This ist deprecated because Atlassian API is not working with ApplicationUser
     * As soon as this is working this method can be deleted
     */
    @Deprecated
    private User convertApplicationUserToCrowdEmbeddedUser(ApplicationUser applicationUser){
        return userManager.getUserObject(applicationUser.getUsername());
    }


}

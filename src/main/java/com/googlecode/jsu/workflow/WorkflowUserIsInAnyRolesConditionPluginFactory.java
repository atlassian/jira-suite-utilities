package com.googlecode.jsu.workflow;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the parameters available for User Is In Any Roles Condition.
 */
public class WorkflowUserIsInAnyRolesConditionPluginFactory extends
        AbstractWorkflowPluginFactory implements WorkflowPluginConditionFactory {

    private final WorkflowUtils workflowUtils;
    private final ProjectRoleManager projectRoleManager;

    public WorkflowUserIsInAnyRolesConditionPluginFactory(WorkflowUtils workflowUtils, ProjectRoleManager projectRoleManager) {
        this.workflowUtils = workflowUtils;
        this.projectRoleManager = projectRoleManager;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForInput(java.util.Map)
     */
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put("val-rolesList", projectRoleManager.getProjectRoles());
        velocityParams.put("val-splitter", WorkflowUtils.SPLITTER);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForEdit(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForEdit(
            Map<String, Object> velocityParams,
            AbstractDescriptor descriptor
    ) {

        getVelocityParamsForInput(velocityParams);

        ConditionDescriptor conditionDescriptor = (ConditionDescriptor) descriptor;
        Map args = conditionDescriptor.getArgs();

        velocityParams.remove("val-rolesList");

        String strRolesSelected = (String)args.get("hidRolesList");
        Collection<ProjectRole> rolesSelected = workflowUtils.getRoles(strRolesSelected, WorkflowUtils.SPLITTER);

        Collection<ProjectRole> roles = projectRoleManager.getProjectRoles();

        Collection<ProjectRole> availableRoles = new ArrayList<ProjectRole>();
        //do not use remove or removeAll, does not work in OD for whatever reason
        for(ProjectRole r:roles) {
            if(!rolesSelected.contains(r)) {
                availableRoles.add(r);
            }
        }
        velocityParams.put("val-rolesListSelected", rolesSelected);
        velocityParams.put("val-hidRolesList", workflowUtils.getStringRole(rolesSelected, WorkflowUtils.SPLITTER));
        velocityParams.put("val-rolesList", availableRoles);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForView(java.util.Map, com.opensymphony.workflow.loader.AbstractDescriptor)
     */
    protected void getVelocityParamsForView(
            Map<String, Object> velocityParams,
            AbstractDescriptor descriptor
    ) {
        ConditionDescriptor conditionDescriptor = (ConditionDescriptor) descriptor;
        Map args = conditionDescriptor.getArgs();

        String strRolesSelected = (String)args.get("hidRolesList");
        Collection rolesSelected = workflowUtils.getRoles(strRolesSelected, WorkflowUtils.SPLITTER);

        velocityParams.put("val-rolesListSelected", rolesSelected);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> conditionParams) {
        Map<String, Object> params = new HashMap<String, Object>();

        try {
            String strRolesSelected = extractSingleParam(conditionParams, "hidRolesList");

            params.put("hidRolesList", strRolesSelected);
        } catch(IllegalArgumentException iae) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }

}

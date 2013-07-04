package com.googlecode.jsu.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.atlassian.jira.security.groups.GroupManager;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;

/**
 * @author Gustavo Martin.
 *
 * This class defines the parameters available for User Is In Any Group Condition.
 *
 */
public class WorkflowUserIsInAnyGroupsConditionPluginFactory extends
        AbstractWorkflowPluginFactory implements WorkflowPluginConditionFactory {

    private final WorkflowUtils workflowUtils;
    private final GroupManager groupManager;

    public WorkflowUserIsInAnyGroupsConditionPluginFactory(WorkflowUtils workflowUtils, GroupManager groupManager) {
        this.workflowUtils = workflowUtils;
        this.groupManager = groupManager;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.AbstractWorkflowPluginFactory#getVelocityParamsForInput(java.util.Map)
     */
    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put("val-groupsList", groupManager.getAllGroups());
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

        velocityParams.remove("val-groupsList");

        String strGroupsSelected = (String)args.get("hidGroupsList");
        Collection<Group> groupsSelected = workflowUtils.getGroups(strGroupsSelected, WorkflowUtils.SPLITTER);

        Collection<Group> groups = groupManager.getAllGroups();

        Collection<Group> availableGroups = new ArrayList<Group>();
        //do not use remove or removeAll, does not work in OD for whatever reason
        for(Group g:groups) {
            if(!groupsSelected.contains(g)) {
                availableGroups.add(g);
            }
        }
        velocityParams.put("val-groupsListSelected", groupsSelected);
        velocityParams.put("val-hidGroupsList", workflowUtils.getStringGroup(groupsSelected, WorkflowUtils.SPLITTER));
        velocityParams.put("val-groupsList", availableGroups);
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

        String strGroupsSelected = (String)args.get("hidGroupsList");
        Collection groupsSelected = workflowUtils.getGroups(strGroupsSelected, WorkflowUtils.SPLITTER);

        velocityParams.put("val-groupsListSelected", groupsSelected);
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.workflow.WorkflowPluginFactory#getDescriptorParams(java.util.Map)
     */
    public Map<String, ?> getDescriptorParams(Map<String, Object> conditionParams) {
        Map<String, Object> params = new HashMap<String, Object>();

        try {
            String strGroupsSelected = extractSingleParam(conditionParams, "hidGroupsList");

            params.put("hidGroupsList", strGroupsSelected);
        } catch(IllegalArgumentException iae) {
            // Aggregate so that Transitions can be added.
        }

        return params;
    }

}

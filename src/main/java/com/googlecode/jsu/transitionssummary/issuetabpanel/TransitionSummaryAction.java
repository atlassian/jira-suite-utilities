package com.googlecode.jsu.transitionssummary.issuetabpanel;

import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueAction;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanelModuleDescriptor;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.googlecode.jsu.transitionssummary.TransitionSummary;
import org.ofbiz.core.util.UtilMisc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Gustavo Martin
 *
 * This is a valid Action, that it allows to visualize the Transition Summaries.
 */
public class TransitionSummaryAction extends AbstractIssueAction {
    protected final IssueTabPanelModuleDescriptor descriptor;
    protected List<TransitionSummary> tranSummaries;
    protected Timestamp timePerformed;
    protected final UserManager userManager;

    /**
     * @param tranSummaries List containing TransitionSummary objects.
     */
    public TransitionSummaryAction(List<TransitionSummary> tranSummaries, IssueTabPanelModuleDescriptor descriptor, UserManager userManager){
        super(descriptor);

        this.tranSummaries = tranSummaries;
        this.descriptor = descriptor;
        this.timePerformed = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.userManager = userManager;
    }

    /**
     * @return a List
     *
     * It allows Velocity to obtain the List of Transition Summaries.
     */
    public List<TransitionSummary> getTransitions() {
        return tranSummaries;
    }

    /* (non-Javadoc)
     * @see com.atlassian.jira.issue.action.IssueAction#getTimePerformed()
     */
    public Date getTimePerformed() {
        return this.timePerformed;
    }

    /* (non-Javadoc)
     * @see com.atlassian.jira.issue.action.IssueAction#getHtml(com.atlassian.jira.web.action.JiraWebActionSupport)
     */
    public String getHtml(JiraWebActionSupport webAction) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params = UtilMisc.toMap("webAction", webAction, "action", this);

        return descriptor.getHtml("view", params);
    }

    protected void populateVelocityParams(Map params) {
        params.put("action", this);
    }


    public boolean isUserExists(String username) {
        try {
            return userManager.getUserByName(username) != null;
        } catch (Throwable t) {
            return false;
        }
    }
}

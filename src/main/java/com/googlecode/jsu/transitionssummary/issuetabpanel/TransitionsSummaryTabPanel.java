package com.googlecode.jsu.transitionssummary.issuetabpanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.plugin.issuetabpanel.IssueAction;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanel;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanelModuleDescriptor;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.transitionssummary.TransitionSummary;
import com.googlecode.jsu.transitionssummary.TransitionsManager;

/**
 * @author Gustavo Martin
 *
 * It will be add a new Issue Tab Panel.
 *
 */
public class TransitionsSummaryTabPanel implements IssueTabPanel {

    protected IssueTabPanelModuleDescriptor descriptor;

    private final TransitionsManager transitionsManager;
    private final UserManager userManager;
    private final I18nHelper.BeanFactory beanFactory;

    public TransitionsSummaryTabPanel(TransitionsManager transitionsManager,
                                      UserManager userManager,
                                      I18nHelper.BeanFactory beanFactory) {
        this.transitionsManager = transitionsManager;
        this.userManager = userManager;
        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.issuetabpanel.IssueTabPanel#init(com.googlecode.jsu.issuetabpanel.IssueTabPanelModuleDescriptor)
     */
    public void init(IssueTabPanelModuleDescriptor descriptor) {
        this.descriptor = descriptor;
        
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.issuetabpanel.IssueTabPanel#getActions(org.ofbiz.core.entity.GenericValue, com.opensymphony.user.User)
     */
    public List<com.atlassian.jira.plugin.issuetabpanel.IssueAction> getActions(Issue issue, User remoteUser) {
        List<IssueAction> retList = new ArrayList<IssueAction>();
        List<TransitionSummary> transitions = transitionsManager.getTransitionSummary(issue);

        // Allway adds only one record to the tab. This is thus, because if there are transition sumeries,
        // it exposes the List with all Transition Summaries. Then, velocity will be in charge of render it properly.
        if (transitions == null || transitions.isEmpty()) {

            GenericMessageAction action = new GenericMessageAction(
                this.beanFactory.getInstance(remoteUser).getText("transitions-summary-view.not_yet_executed"));
            retList.add(action);
        } else {
            Collections.sort(transitions, new TransitionSummaryComparator());

            retList.add(new TransitionSummaryAction(transitions, descriptor, userManager));
        }

        return retList;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.issuetabpanel.IssueTabPanel#showPanel(org.ofbiz.core.entity.GenericValue)
     */
    public boolean showPanel(Issue issue, User remoteUser) {
        return true;
    }

}

package com.googlecode.jsu.workflow.function;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.history.ChangeItemBean;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

/**
 * Abstract post-function with transparent change tracking.
 *
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
abstract class AbstractPreserveChangesPostFunction extends AbstractJiraFunctionProvider {
    private static final String CHANGE_ITEMS = "changeItems";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Mirror for execute method but with holder for changes
     *
     * @throws WorkflowException
     */
    protected abstract void executeFunction(
            Map<String, Object> transientVars, Map<String, String> args,
            PropertySet ps, IssueChangeHolder holder
    ) throws WorkflowException;

    @SuppressWarnings("unchecked")
    public final void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        IssueChangeHolder holder = createChangeHolder(transientVars);

        if (log.isDebugEnabled()) {
            log.debug(
                    "Executing function with [transientVars=" +
                    transientVars +
                    ";args=" +
                    args +
                    ";ps=" +
                    ps +
                    "]"
            );
        }

        try {
            executeFunction(transientVars, args, ps, holder);
        } finally {
            releaseChangeHolder(holder, transientVars);
        }
    }

    /**
     * Create new holder with changes from transient vars
     */
    @SuppressWarnings("unchecked")
    private IssueChangeHolder createChangeHolder(Map<String, Object> transientVars) {
        List<ChangeItemBean> changeItems = (List<ChangeItemBean>) transientVars.get(CHANGE_ITEMS);

        if (changeItems == null) {
            changeItems = new LinkedList<ChangeItemBean>();
        }

        if (log.isDebugEnabled()) {
            log.debug("Create new holder with items - " + changeItems.toString());
        }

        IssueChangeHolder holder = new DefaultIssueChangeHolder();

        holder.setChangeItems(changeItems);

        return holder;
    }

    /**
     * Release holder for changes.
     */
    @SuppressWarnings("unchecked")
    private void releaseChangeHolder(IssueChangeHolder holder, Map<String, Object> transientVars) {
        List<ChangeItemBean> items = holder.getChangeItems();

        if (log.isDebugEnabled()) {
            log.debug("Release holder with items - " + items.toString());
        }

        transientVars.put(CHANGE_ITEMS, items);
    }
}

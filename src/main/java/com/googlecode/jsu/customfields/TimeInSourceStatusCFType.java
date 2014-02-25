package com.googlecode.jsu.customfields;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.googlecode.jsu.helpers.FormattableDuration;
import com.googlecode.jsu.transitionssummary.TransitionSummary;
import com.googlecode.jsu.transitionssummary.TransitionsManager;

import java.util.List;

/**
 * Time in source status custom field type, a calculated ready only field showing the amount
 * of time that has been spent in the current status of the issue until now. That amount is
 * the accumulated duration in milliseconds the issue is, or has been, even multiple times,
 * in that status.
 *
 * In the usual issue view, the time will be shown formatted in seconds, minutes and so on
 * depending its length in milliseconds, for issue navigator its list view or exports, milliseconds
 * will be taken instead, to be able to make computations with the field its value.
 *
 * NOTE: This does not properly work yet, this is why it is not enabled in atlassian-plugin.xml
 * The {@link com.googlecode.jsu.helpers.FormattableDuration}, used to be able to have either the
 * duration in millis and on the other hand properly formatted human readable, can not be handled
 * by the restore functionality of JIRA. This means backup/restore fails and integration tests too.
 *
 * NOTE 2: Not yet clear and to be tested, performance. Does this really work well in large
 * installations with workflows having lots of steps, and where transitions are used often?
 */
public class TimeInSourceStatusCFType extends CalculatedCFType<FormattableDuration,FormattableDuration> {

    protected final CustomFieldValuePersister customFieldValuePersister;
    protected final GenericConfigManager genericConfigManager;
    private final TransitionsManager transitionsManager;

    public TimeInSourceStatusCFType(CustomFieldValuePersister customFieldValuePersister, GenericConfigManager genericConfigManager, TransitionsManager transitionsManager) {
        this.customFieldValuePersister = customFieldValuePersister;
        this.genericConfigManager = genericConfigManager;
        this.transitionsManager = transitionsManager;
    }

    @Override
    public String getStringFromSingularObject(FormattableDuration value) {
        return value!=null?value.toString():"0";
    }

    @Override
    public FormattableDuration getSingularObjectFromString(String value) throws FieldValidationException {
        return value!=null?new FormattableDuration(value):new FormattableDuration(0);
    }

    @Override
    public FormattableDuration getValueFromIssue(CustomField customField, Issue issue) {
        //TODO field not yet sortable nor searchable ??
        List<TransitionSummary> summaries = transitionsManager.getTransitionSummary(issue);

        long duration = 0;
        TransitionSummary lastSummary = null;
        for(TransitionSummary summary:summaries) {
            if(summary.getFromStatus().getId().equals(issue.getStatusObject().getId())) {
                duration += summary.getDurationInMillis();
            }
            lastSummary = summary;
        }

        //add time since very last switch, which lead to the current status, or since created if no transition yet
        if(lastSummary!=null) {
            duration += System.currentTimeMillis() - lastSummary.getLastUpdate().getTime();
        } else {
            duration += System.currentTimeMillis() - issue.getCreated().getTime();
        }

        // duration is in millis, return as seconds
        return new FormattableDuration(duration);
    }

    public FormattableDuration getDefaultValue(FieldConfig fieldConfig) {
        return new FormattableDuration(0);
    }
}

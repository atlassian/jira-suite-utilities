package com.googlecode.jsu.transitionssummary;

import java.sql.Timestamp;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.util.I18nHelper;

/**
 * @author Gustavo Martin
 *
 * This class represent a Workflow Transition. And it will keep, temporarily, all its values.
 * It obtains the values, reading the Change History.
 *
 */
public class Transition {
    private final I18nHelper i18nHelper;

    private String changedBy;
    private Timestamp changedAt;
    private StatusDelegate fromStatus;
    private StatusDelegate toStatus;
    private Timestamp startAt;
    private Long duration;

    public Transition (final I18nHelper i18nHelper) {
        this.startAt = null;
        this.i18nHelper = i18nHelper;
    }

    public Long getDurationInMillis(){
        return this.duration;
    }

    private void setDuration(){
        Long retVal = new Long("-1");

        // It calculates the duration since the transition began until the next one is executed.
        if (this.startAt != null) {
            retVal = this.changedAt.getTime() - this.startAt.getTime();
        }

        this.duration = retVal;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }
    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }
    public String getChangedBy() {
        return changedBy;
    }
    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
    public StatusDelegate getFromStatus() {
        return fromStatus;
    }
    public void setFromStatus(Long fromStatus) {
        this.fromStatus = new StatusDelegate(ComponentAccessor.getConstantsManager().getStatusObject(String.valueOf(fromStatus)),i18nHelper);
    }
    public StatusDelegate getToStatus() {
        return toStatus;
    }
    public void setToStatus(Long toStatus) {
        this.toStatus = new StatusDelegate(ComponentAccessor.getConstantsManager().getStatusObject(String.valueOf(toStatus)),i18nHelper);
    }
    public Timestamp getStartAt() {
        return startAt;
    }
    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
        setDuration();
    }
}

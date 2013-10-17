package com.googlecode.jsu.transitionssummary;

import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.util.I18nHelper;

/**
 * Simple delegate to encapsulate the data we need from a status, to be
 * independent of API changes, mostly from JIRA 6.0 to 6.1
 *
 * @author Stephan Bielmann
 */
public class StatusDelegate {
    private final Status status;
    private final I18nHelper i18nHelper;

    public StatusDelegate(Status status, I18nHelper i18nHelper) {
        this.status = status;
        this.i18nHelper = i18nHelper;
    }

    public String getDescTranslation() {
        return status!=null? status.getDescription():i18nHelper.getText("transitions-summary-view.removed-status.description");
    }

    public String getNameTranslation() {
        return status!=null? status.getName():i18nHelper.getText("transitions-summary-view.removed-status.name");
    }

    public String getIconUrl() {
        return status!=null?status.getIconUrl():null;
    }

    public String getId() {
        return status!=null?status.getId():"-1";
    }
}

package com.googlecode.jsu.transitionssummary;

import java.util.Locale;

import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.util.I18nHelper;
import com.opensymphony.module.propertyset.PropertySet;

public class RemovedStatusImpl implements Status {
    public void deleteTranslation(String issueConstantPrefix, Locale locale) {
    }

    public String getDescription() {
      //TODO i18n
        return "Status representing an old Status removed from Database";
    }

    public String getDescTranslation() {
        return getDescription();
    }

    public String getDescTranslation(I18nHelper i18n) {
        return getDescription();
    }

    public String getDescTranslation(String locale) {
        return getDescription();
    }

    public GenericValue getGenericValue() {
        return null;
    }

    public String getIconUrl() {
        return null;
    }

    public String getId() {
        return "-1";
    }

    public String getName() {
      //TODO i18n
        return "Removed Status";
    }

    public String getNameTranslation() {
        return getName();
    }

    public String getNameTranslation(I18nHelper i18n) {
        return getName();
    }

    public String getNameTranslation(String locale) {
        return getName();
    }

    public PropertySet getPropertySet() {
        return null;
    }

    public Long getSequence() {
        return null;
    }

    public void setDescription(String description) {
    }

    public void setIconUrl(String iconURL) {
    }

    public void setName(String name) {
    }

    public void setSequence(Long sequence) {
    }

    @Override
    public String getCompleteIconUrl() {
        return null;
    }

    public void setTranslation(String translatedName, String translatedDesc, String issueConstantPrefix, Locale locale) {
    }

    public int compareTo(Object arg0) {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.atlassian.jira.issue.IssueConstant#getIconUrlHtml()
     */
    public String getIconUrlHtml() {
        return "";
    }
}

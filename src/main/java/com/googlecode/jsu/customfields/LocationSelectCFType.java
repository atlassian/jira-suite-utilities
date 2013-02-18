package com.googlecode.jsu.customfields;

import com.atlassian.jira.issue.customfields.impl.SelectCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.fields.rest.json.beans.JiraBaseUrls;

/**
 * Wrapper on Jira SelectCFType for using inside plugins v2.
 *
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
//TODO Can we remove dependencies to jira-core (instead only jira-api) by using interfaces (instead of classes) of referenced custom field types?
public class LocationSelectCFType extends SelectCFType {
    /**
     * @param customFieldValuePersister
     * @param optionsManager
     * @param genericConfigManager
     * @param jiraBaseUrls
     */
    public LocationSelectCFType(
            CustomFieldValuePersister customFieldValuePersister,
            OptionsManager optionsManager,
            GenericConfigManager genericConfigManager,
            JiraBaseUrls jiraBaseUrls
    ) {
        super(customFieldValuePersister, optionsManager, genericConfigManager, jiraBaseUrls);
    }
}

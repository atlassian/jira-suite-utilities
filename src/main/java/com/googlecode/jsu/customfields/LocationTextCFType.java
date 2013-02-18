package com.googlecode.jsu.customfields;

import com.atlassian.jira.issue.customfields.converters.StringConverter;
import com.atlassian.jira.issue.customfields.impl.RenderableTextCFType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;

/**
 * Wrapper on Jira RenderableTextCFType for using inside plugins v2.
 *
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id$
 */
//TODO Can we remove dependencies to jira-core (instead only jira-api) by using interfaces (instead of classes) of referenced custom field types?
public class LocationTextCFType extends RenderableTextCFType {
    /**
     * @param customFieldValuePersister
     * @param genericConfigManager
     */
    public LocationTextCFType(
            CustomFieldValuePersister customFieldValuePersister,
            GenericConfigManager genericConfigManager
    ) {
        super(customFieldValuePersister, genericConfigManager);
    }
}

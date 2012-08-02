package com.googlecode.jsu.customfields;

import com.atlassian.jira.issue.customfields.searchers.SelectSearcher;
import com.atlassian.jira.util.JiraComponentFactory;
import com.atlassian.jira.util.JiraComponentLocator;

/**
 * Simple wrapper around {@link SelectSearcher} with instances of component factory and locator
 * for its constructor.
 *
 * @author Stephan Bielmann
 */
public class LocationSelectSearcher extends SelectSearcher {
    /**
     * Simply invokes {@link SelectSearcher#SelectSearcher(com.atlassian.jira.util.ComponentFactory, com.atlassian.jira.util.ComponentLocator)}
     * with appropriate arguments.
     */
    public LocationSelectSearcher() {
        super(JiraComponentFactory.getInstance(), new JiraComponentLocator());
    }
}
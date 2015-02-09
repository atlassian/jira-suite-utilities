package com.googlecode.jsu.helpers.checkers;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.label.Label;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.issue.customfields.option.Option;
import org.apache.commons.lang.StringUtils;
import org.ofbiz.core.entity.GenericEntity;

import com.atlassian.jira.issue.IssueConstant;
import com.atlassian.jira.project.Project;


import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
public class ConverterString implements ValueConverter {
    /* (non-Javadoc)
     * @see com.googlecode.jsu.helpers.checkers.ValueConverter#getComparable(java.lang.Object)
     */
    public Comparable<?> getComparable(Object object) {
        if (object == null) {
            return null;
        }

        String result = convert(object);

        if (StringUtils.isBlank(result)) {
            return null;
        }

        return result;
    }


    public String convert(Object value) {
        if (value == null || value instanceof String) {
            return (String) value;
        } else if (value instanceof IssueConstant) {
            return ((IssueConstant) value).getName();
        } else if (value instanceof Project) {
            return ((Project)value).getKey();
        } else if (value instanceof Collection && ((Collection) value).size() == 1) {
            return convert(((Collection) value).iterator().next());
        } else if (value instanceof Option) {
            return ((Option) value).getValue();
        } else if (value instanceof com.atlassian.jira.issue.fields.option.Option) {
            return ((com.atlassian.jira.issue.fields.option.Option) value).getName();
        } else if (value instanceof ApplicationUser) {
            return ((ApplicationUser) value).getName();
            //https://developer.atlassian.com/display/JIRADEV/Renamable+Users+in+JIRA+6.0
        } else if (value instanceof User) {
            return ((User) value).getName();
        } else if (value instanceof Label) {
            return ((Label) value).getLabel();
        } else if (value instanceof GenericEntity) {
            String s = ((GenericEntity) value).getString("name");
            if (StringUtils.isEmpty(s)) {
                s = ((GenericEntity) value).getString("id");
                if (StringUtils.isEmpty(s)) {
                    s = value.toString();
                }
            }
            return s;
        } else {
            try {
                Method getName = value.getClass().getMethod("getName");
                return getName.invoke(value).toString();
            } catch (Exception e) { /* try getId() ... */ }
            try {
                Method getId = value.getClass().getMethod("getId");
                return getId.invoke(value).toString();
            } catch (Exception e) { /* use toString() ... */ }
            return value.toString();
        }
    }
}

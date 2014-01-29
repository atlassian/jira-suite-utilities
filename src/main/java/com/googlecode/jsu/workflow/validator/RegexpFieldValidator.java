package com.googlecode.jsu.workflow.validator;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.util.I18nHelper;
import com.googlecode.jsu.annotation.Argument;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This validator verifies that a given field its contents are not empty and match agains
 * a regular expression.
 */
public class RegexpFieldValidator extends GenericValidator {
    private static final Logger log = LoggerFactory.getLogger(RegexpFieldValidator.class);

    @Argument("fieldSelected")
    private String validateField;

    @Argument("expressionSelected")
    private String expression;

    private final I18nHelper.BeanFactory beanFactory;

    public RegexpFieldValidator(FieldCollectionsUtils fieldCollectionsUtils,
                                WorkflowUtils workflowUtils,
                                I18nHelper.BeanFactory beanFactory
    ) {
        super(fieldCollectionsUtils, workflowUtils);

        this.beanFactory = beanFactory;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.workflow.Validator#validate(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    protected void validate() throws InvalidInputException, WorkflowException {
        Field field = workflowUtils.getFieldFromKey(validateField);

        // check that field contents match against regular expression, split into single ones if list type
        if((field != null) && (expression != null)) {
            Object objValue = workflowUtils.getFieldValueFromIssue(getIssue(), field);

            String completeValue = "";
            ArrayList<String> list = new ArrayList<String>();
            if(objValue!=null) {
                completeValue = objValue.toString();
                if(objValue instanceof List) {
                    List l = (List)objValue;
                    for(Object o:l) {
                        list.add(o.toString());
                    }
                } else {
                    list.add(objValue.toString());
                }
            } else {
                list.add("");
            }

            String lastValue = "";
            boolean result = true;
            Iterator<String> it = list.iterator();
            while(result && it.hasNext()) {
                lastValue = it.next();
                result = lastValue.matches(expression);
            }

            if (log.isDebugEnabled()) {
                log.debug(
                        "Validate field \"" + field.getName() +
                                "\" and expression \"" + expression +
                                "\" with value [" + completeValue + "] with result " + result
                );
            }

            if(!result) {
                I18nHelper i18nh = this.beanFactory.getInstance(
                        ComponentAccessor.getJiraAuthenticationContext().getUser().getDirectoryUser());
                String msg = i18nh.getText("regexpfield-validator-view.not_matching",field.getName(),lastValue,expression);
                this.setExceptionMessage(
                        field,
                        msg,
                        msg
                );
            }
        } else {
            log.error("Unable to find field with id [" + validateField + "]");
        }
    }
}

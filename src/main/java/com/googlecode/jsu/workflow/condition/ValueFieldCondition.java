package com.googlecode.jsu.workflow.condition;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.googlecode.jsu.helpers.ComparisonType;
import com.googlecode.jsu.helpers.ConditionCheckerFactory;
import com.googlecode.jsu.helpers.ConditionType;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.module.propertyset.PropertySet;

/**
 * This condition evaluates if a given field fulfills the condition.
 *
 * @author Gustavo Martin
 */
public class ValueFieldCondition extends AbstractJiraCondition {
    private final Logger log = LoggerFactory.getLogger(ValueFieldCondition.class);

    private final ConditionCheckerFactory conditionCheckerFactory;
    private final WorkflowUtils workflowUtils;

    /**
     * @param conditionCheckerFactory
     * @param workflowUtils
     */
    public ValueFieldCondition(ConditionCheckerFactory conditionCheckerFactory, WorkflowUtils workflowUtils) {
        this.conditionCheckerFactory = conditionCheckerFactory;
        this.workflowUtils = workflowUtils;
    }

    /* (non-Javadoc)
     * @see com.opensymphony.workflow.Condition#passesCondition(java.util.Map, java.util.Map, com.opensymphony.module.propertyset.PropertySet)
     */
    public boolean passesCondition(
            @SuppressWarnings("rawtypes") Map transientVars,
            @SuppressWarnings("rawtypes") Map args,
            PropertySet ps
    ) {
        final Issue issue = getIssue(transientVars);

        String fieldId = (String) args.get("fieldsList");
        String valueForCompare = (String) args.get("fieldValue");

        ComparisonType comparison = conditionCheckerFactory.findComparisonById((String) args.get("comparisonType"));
        ConditionType condition = conditionCheckerFactory.findConditionById((String) args.get("conditionList"));

        boolean result = false;

        try {
            Field field = workflowUtils.getFieldFromKey(fieldId);
            Object fieldValue = workflowUtils.getFieldValueFromIssue(issue, field);

            //multiple values slightly different, equal means contains, not equal means does not contain
            if (fieldValue instanceof Collection) {
                if(condition.equals(ConditionCheckerFactory.NOT_EQUAL)) {
                    result = checkCollectionDoesNotContain(comparison,condition,(Collection)fieldValue,valueForCompare);
                } else {
                    result = checkCollection(comparison,condition,(Collection)fieldValue,valueForCompare);
                }
            } else {
                result = conditionCheckerFactory.
                    getChecker(comparison, condition).
                    checkValues(fieldValue, valueForCompare);
            }

            if (log.isDebugEnabled()) {
                log.debug(
                        "Comparing field '" + fieldId +
                        "': [" + fieldValue + "]" +
                        condition.getValue() +
                        "[" + valueForCompare + "] as " +
                        comparison.getValueKey() + " = " + result
                );
            }
        } catch (Exception e) {
            log.error("Unable to compare values for field '" + fieldId + "'", e);
        }

        return result;
    }

    //ensures that given value to compare is not within the collection items
    private boolean checkCollectionDoesNotContain(ComparisonType comparison,
                                                  ConditionType condition,
                                                  Collection values,
                                                  String valueToCompare) {
        return !checkCollection(comparison,ConditionCheckerFactory.EQUAL,values,valueToCompare);
    }

    //if only one single item fulfils the condition, will return
    private boolean checkCollection(ComparisonType comparison,
                                    ConditionType condition,
                                    Collection values,
                                    String valueToCompare) {
        boolean result = false;
        for (Object o : values) {
            result = conditionCheckerFactory.
                    getChecker(comparison, condition).
                    checkValues(o, valueToCompare);
            if (result) {
                break;
            }
        }
        return result;
    }
}

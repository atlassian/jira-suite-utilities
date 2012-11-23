package com.googlecode.jsu.workflow.validator;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.googlecode.jsu.annotation.AnnotationProcessor;
import com.googlecode.jsu.annotation.Argument;
import com.googlecode.jsu.annotation.MapFieldProcessor;
import com.googlecode.jsu.annotation.TransientVariable;
import com.googlecode.jsu.util.FieldCollectionsUtils;
import com.googlecode.jsu.util.ValidatorErrorsBuilder;
import com.googlecode.jsu.util.WorkflowUtils;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 * @version $Id: GenericValidator.java 173 2008-10-14 13:04:43Z abashev $
 */
public abstract class GenericValidator implements Validator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ValidatorErrorsBuilder errorBuilder;
    private FieldScreen fieldScreen = null;
    private Issue issue = null;
    private Issue originalIssue = null;
    private String transitionComment = null;

    protected final FieldCollectionsUtils fieldCollectionsUtils;
    protected final WorkflowUtils workflowUtils;

    /**
     * @param fieldCollectionsUtils
     * @param workflowUtils
     */
    public GenericValidator(FieldCollectionsUtils fieldCollectionsUtils, WorkflowUtils workflowUtils) {
        this.fieldCollectionsUtils = fieldCollectionsUtils;
        this.workflowUtils = workflowUtils;
    }

    protected abstract void validate() throws InvalidInputException, WorkflowException;

    @SuppressWarnings("unchecked")
    public final void validate(
            Map transientVars, Map args, PropertySet ps
    ) throws InvalidInputException, WorkflowException {
        if (log.isDebugEnabled()) {
            log.debug(
                    "Validation request: [transientVars=" +
                    transientVars +
                    ";args=" +
                    args +
                    ";property=" +
                    ps +
                    "]"
            );
        }

        initObject(transientVars, args);

        this.fieldScreen = initScreen(transientVars);
        this.errorBuilder = new ValidatorErrorsBuilder(hasViewScreen());
        this.issue = (Issue) transientVars.get("issue");
        this.originalIssue = (Issue) transientVars.get("originalissueobject");
        this.transitionComment = (String) transientVars.get("comment");

        this.validate();

        this.errorBuilder.process();
    }

    /**
     * Initialize object with maps of parameters.
     * @param vars
     * @param arguments
     */
    protected void initObject(Map<String, Object> vars, Map<String, Object> arguments) {
        final AnnotationProcessor processor = new AnnotationProcessor();

        processor.addVisitor(new MapFieldProcessor(Argument.class, arguments));
        processor.addVisitor(new MapFieldProcessor(TransientVariable.class, vars));

        processor.processAnnotations(this);
    }

    protected final Issue getIssue() {
        return this.issue;
    }

    protected final Issue getOriginalIssue() {
        return this.originalIssue;
    }

    protected final String getTransitionComment() {
        return this.transitionComment;
    }

    protected final boolean hasViewScreen() {
        return (fieldScreen != null);
    }

    protected final FieldScreen getFieldScreen() {
        return this.fieldScreen;
    }

    /**
     * Setting error message for validator.
     *
     * @param field
     * @param messageIfOnScreen
     * @param messageIfHidden
     */
    protected final void setExceptionMessage(
            Field field,
            String messageIfOnScreen, String messageIfHidden
    ) {
        if (hasViewScreen()) {
            if (fieldCollectionsUtils.isFieldOnScreen(this.issue, field, getFieldScreen())) {
                if (fieldCollectionsUtils.cannotSetValidationMessageToField(field)) {
                    this.errorBuilder.addError(messageIfOnScreen);
                } else {
                    this.errorBuilder.addError(field, messageIfOnScreen);
                }
            } else {
                this.errorBuilder.addError(messageIfHidden);
            }
        } else {
            this.errorBuilder.addError(messageIfOnScreen);
        }
    }

    private FieldScreen initScreen(Map<String, Object> vars) {
        if (vars.containsKey("descriptor") && vars.containsKey("actionId")) {
            WorkflowDescriptor workflowDescriptor = (WorkflowDescriptor) vars.get("descriptor");
            Integer actionId = (Integer) vars.get("actionId");
            ActionDescriptor actionDescriptor = workflowDescriptor.getAction(actionId.intValue());

            return workflowUtils.getFieldScreen(actionDescriptor);
        } else {
            return null;
        }
    }
}

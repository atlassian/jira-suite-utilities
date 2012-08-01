package com.googlecode.jsu.util;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.issue.fields.Field;
import com.opensymphony.workflow.InvalidInputException;

/**
 * Container for storing and processing validator errors.
 *
 * @author Alexey Abashev
 */
public class ValidatorErrorsBuilder {
    private List<Field> fields = new ArrayList<Field>();
    private List<String> messages = new ArrayList<String>();
    private boolean forScreen;

    /**
     * @param forScreen is we throwing exception for screen
     */
    public ValidatorErrorsBuilder(boolean forScreen) {
        this.forScreen = forScreen;
    }

    public void addError(Field field, String message) {
        this.fields.add(field);
        this.messages.add(message);
    }

    public void addError(String message) {
        this.addError(null, message);
    }

    public void process() throws InvalidInputException {
        if (this.fields.size() == 0) {
            return;
        }

        InvalidInputException e = new InvalidInputException();

        for (int i = 0; i < fields.size(); i++) {
            Field f = this.fields.get(i);
            String m = this.messages.get(i);

            if (f == null || ! forScreen) {
                e.addError(m);
            } else {
                e.addError(f.getId(), m);
            }
        }

        throw e;
    }
}

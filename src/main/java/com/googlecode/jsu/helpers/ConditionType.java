package com.googlecode.jsu.helpers;

/**
 * @author Gustavo Martin
 *
 * This class represents a Condition Type. This will be used in Workflow Condition, Validator, or Function.
 *
 */
public class ConditionType {
    private final int id;
    private final String shortText;
    private final String displayTextKey;
    private final String mnemonic;

    public ConditionType(
            int id, String shortText, String displayTextKey, String mnemonic
    ) {
        this.id = id;
        this.shortText = shortText;
        this.displayTextKey = displayTextKey;
        this.mnemonic = mnemonic;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return shortText;
    }

    /**
     * @return the mnemonic
     */
    public String getMnemonic() {
        return mnemonic;
    }

    /**
     * Get display text key for condition.
     */
    public String getDisplayTextKey() {
        return displayTextKey;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ConditionType))
            return false;
        ConditionType other = (ConditionType) obj;
        if (id != other.id)
            return false;
        return true;
    }
}

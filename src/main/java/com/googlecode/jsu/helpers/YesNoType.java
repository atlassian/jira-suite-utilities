package com.googlecode.jsu.helpers;

/**
 * @author Gustavo Martin
 *
 * This class represents a Yes/No Type. Its values could be YES or NO.
 * It will be used in Workflow Condition, Validator, or Function.
 *
 */
public class YesNoType {
    public static final YesNoType YES = new YesNoType(1, "yes");
    public static final YesNoType NO = new YesNoType(2, "no");

    private final int id;
    private final String value;

    private YesNoType(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
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
        if (!(obj instanceof YesNoType))
            return false;
        YesNoType other = (YesNoType) obj;
        return id == other.id;
    }
}

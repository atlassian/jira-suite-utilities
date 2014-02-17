package com.googlecode.jsu.helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jsu.helpers.checkers.CheckerCompositeFactory;

/**
 * Return object for checking conditions.
 *
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
public class ConditionCheckerFactory {
    public static final ConditionType GREATER = new ConditionType(1, ">", "conditiontype.greater_than", "G");
    public static final ConditionType GREATER_EQUAL = new ConditionType(2, ">=", "conditiontype.greater_than_or_equal_to", "GE");
    public static final ConditionType EQUAL = new ConditionType(3, "=", "conditiontype.equal_to", "E");
    public static final ConditionType LESS_EQUAL = new ConditionType(4, "<=", "conditiontype.less_than_or_equal_to", "LE");
    public static final ConditionType LESS = new ConditionType(5, "<", "contitiontype.less_than", "L");
    public static final ConditionType NOT_EQUAL = new ConditionType(6, "!=", "conditiontype.not_equal_to", "NE");

    public static final ComparisonType STRING = new ComparisonType(1, "comparisontype.string", "String");
    public static final ComparisonType NUMBER = new ComparisonType(2, "comparisontype.number", "Number");
    public static final ComparisonType DATE = new ComparisonType(3, "comparisontype.date_with_time", "Date");
    public static final ComparisonType DATE_WITHOUT_TIME = new ComparisonType(4, "comparisontype.date_without_time", "DateWithoutTime");
    public static final ComparisonType OPTIONID = new ComparisonType(5, "comparisontype.optionid", "OptionID");

    /** Template for checker class. */
    private static final String PACKAGE = ConditionCheckerFactory.class.getPackage().getName();
    private static final String CONDITION_CLASS_TEMPLATE = PACKAGE + ".checkers.Snipet";
    private static final String COMPARISON_CLASS_TEMPLATE = PACKAGE + ".checkers.Converter";

    /** Cache for searching through conditions */
    @SuppressWarnings("serial")
    private static final Map<Integer, ConditionType> CONDITIONS_CACHE =
        new LinkedHashMap<Integer, ConditionType>(6) {{
            put(GREATER.getId(), GREATER);
            put(GREATER_EQUAL.getId(), GREATER_EQUAL);
            put(EQUAL.getId(), EQUAL);
            put(LESS_EQUAL.getId(), LESS_EQUAL);
            put(LESS.getId(), LESS);
            put(NOT_EQUAL.getId(), NOT_EQUAL);
    }};

    /** Cache for searching through types */
    @SuppressWarnings("serial")
    private static final Map<Integer, ComparisonType> COMPARISONS_CACHE =
        new LinkedHashMap<Integer, ComparisonType>(4) {{
            put(STRING.getId(), STRING);
            put(NUMBER.getId(), NUMBER);
            put(DATE.getId(), DATE);
            put(DATE_WITHOUT_TIME.getId(), DATE_WITHOUT_TIME);
            put(OPTIONID.getId(),OPTIONID);
    }};

    private final Logger log = LoggerFactory.getLogger(ConditionCheckerFactory.class);
    private final CheckerCompositeFactory checkerCompositeFactory = new CheckerCompositeFactory();

    public ConditionChecker getChecker(ComparisonType type, ConditionType condition) {
        String conditionClassName = CONDITION_CLASS_TEMPLATE + condition.getMnemonic();
        String comparisonClassName = COMPARISON_CLASS_TEMPLATE + type.getMnemonic();

        if (log.isDebugEnabled()) {
            log.debug(
                    "Using class [" + conditionClassName +
                    "] for condition [" + condition.getValue() +
                    "]; class [" + comparisonClassName +
                    "] for type [" + type.getValueKey() +
                    "]"
            );
        }

        return checkerCompositeFactory.getComposite(comparisonClassName, conditionClassName);
    }

    /**
     * Get all possible condition types.
     */
    public List<ConditionType> getConditionTypes() {
        return new ArrayList<ConditionType>(CONDITIONS_CACHE.values());
    }

    /**
     * Get all possible comparison types.
     */
    public List<ComparisonType> getComparisonTypes() {

        List<ComparisonType> comparisonTypes = new ArrayList<ComparisonType>(COMPARISONS_CACHE.values());
        return comparisonTypes;
    }

    /**
     * Find condition by id.
     */
    public ConditionType findConditionById(String id) {
        return CONDITIONS_CACHE.get(Integer.valueOf(id));
    }

    /**
     * Find comparison by id.
     */
    public ComparisonType findComparisonById(String id) {
        return COMPARISONS_CACHE.get(Integer.valueOf(id));
    }
}

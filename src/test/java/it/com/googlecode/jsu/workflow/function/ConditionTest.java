package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;

@WebTest({ Category.FUNC_TEST, Category.REST })
public class ConditionTest extends AbstractTestBase {
    private static final String ISSUE_C9_PASS = "TJP-24";
    private static final String ISSUE_C9_FAIL = "TJP-25";
    private static final String ISSUE_C10_PASS = "TJP-26";
    private static final String ISSUE_C10_PASS_1 = "TJP-28";
    private static final String ISSUE_C10_FAIL = "TJP-27";
    private static final String ISSUE_C11_PASS = "TJP-4";
    private static final String ISSUE_C11_FAIL = "TJP-5";
    private static final String ISSUE_C12_PASS = "TJP-6";
    private static final String ISSUE_C12_FAIL = "TJP-7";
    private static final String ISSUE_C13_PASS = "TJP-8";
    private static final String ISSUE_C13_FAIL = "TJP-9";
    private static final String ISSUE_C14_PASS = "TJP-10";
    private static final String ISSUE_C14_FAIL = "TJP-11";
    private static final String ISSUE_C15_PASS = "TJP-30";
    private static final String ISSUE_C15_FAIL = "TJP-31";

    private static final String TRANSITION_C9 = "1181";
    private static final String TRANSITION_C10 = "1191";
    private static final String TRANSITION_C11 = "1211";
    private static final String TRANSITION_C12 = "1221";
    private static final String TRANSITION_C13 = "1231";
    private static final String TRANSITION_C14 = "1241";
    private static final String TRANSITION_C15 = "1321";

    public void testC9Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C9_PASS,TRANSITION_C9,"superuser"));
    }

    public void testC9Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C9_FAIL,TRANSITION_C9,"admin"));
    }

    public void testC10Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C10_PASS,TRANSITION_C10,"user"));
    }

    public void testC10Pass1() throws Exception {
        assertTrue(hasTransition(ISSUE_C10_PASS_1,TRANSITION_C10,"marketing"));
    }

    public void testC10Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C10_FAIL,TRANSITION_C10,"admin"));
    }

    public void testC11Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C11_PASS,TRANSITION_C11));
    }

    public void testC11Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C11_FAIL,TRANSITION_C11));
    }

    public void testC12Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C12_PASS,TRANSITION_C12));
    }

    public void testC12Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C12_FAIL,TRANSITION_C12));
    }

    public void testC13Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C13_PASS,TRANSITION_C13));
    }

    public void testC13Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C13_FAIL,TRANSITION_C13));
    }

    public void testC14Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C14_PASS,TRANSITION_C14));
    }

    public void testC14Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C14_FAIL,TRANSITION_C14));
    }

    public void testC15Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C15_PASS,TRANSITION_C15,"renamed.will"));
    }

    public void testC15Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C15_FAIL,TRANSITION_C15,"admin"));
    }
}

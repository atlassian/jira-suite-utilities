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
    private static final String ISSUE_C16_PASS = "TJP-63";
    private static final String ISSUE_C16_FAIL = "TJP-64";
    private static final String ISSUE_C17_PASS = "TJP-65";
    private static final String ISSUE_C17_FAIL = "TJP-66";
    private static final String ISSUE_C18_PASS = "TJP-67";
    private static final String ISSUE_C18_FAIL = "TJP-68";
    private static final String ISSUE_C18_PASS_2 = "TJP-69";
    private static final String ISSUE_C19_PASS = "TJP-70";
    private static final String ISSUE_C19_FAIL = "TJP-71";
    private static final String ISSUE_C20_PASS = "TJP-72";
    private static final String ISSUE_C20_FAIL = "TJP-73";
    private static final String ISSUE_C20_PASS_2 = "TJP-74";
    private static final String ISSUE_C21_PASS = "TJP-75";
    private static final String ISSUE_C21_FAIL = "TJP-76";
    private static final String ISSUE_C22_PASS = "TJP-77";
    private static final String ISSUE_C22_FAIL = "TJP-78";
    private static final String ISSUE_C22_PASS_2 = "TJP-79";
    private static final String ISSUE_C23_PASS = "TJP-80";
    private static final String ISSUE_C23_FAIL = "TJP-81";
    private static final String ISSUE_C24_PASS = "TJP-82";
    private static final String ISSUE_C24_FAIL = "TJP-83";
    private static final String ISSUE_C25_PASS = "TJP-84";
    private static final String ISSUE_C25_FAIL = "TJP-85";
    private static final String ISSUE_C25_PASS_2 = "TJP-86";
    private static final String ISSUE_C26_PASS = "TJP-87";
    private static final String ISSUE_C26_FAIL = "TJP-88";
    private static final String ISSUE_C27_PASS = "TJP-89";
    private static final String ISSUE_C27_FAIL = "TJP-90";
    private static final String ISSUE_C27_PASS_2 = "TJP-91";
    private static final String ISSUE_C28_PASS = "TJP-92";
    private static final String ISSUE_C28_FAIL = "TJP-93";
    private static final String ISSUE_C29_PASS = "TJP-94";
    private static final String ISSUE_C29_FAIL = "TJP-95";
    private static final String ISSUE_C29_PASS_2 = "TJP-96";

    private static final String TRANSITION_C9 = "1181";
    private static final String TRANSITION_C10 = "1191";
    private static final String TRANSITION_C11 = "1211";
    private static final String TRANSITION_C12 = "1221";
    private static final String TRANSITION_C13 = "1231";
    private static final String TRANSITION_C14 = "1241";
    private static final String TRANSITION_C15 = "1321";
    private static final String TRANSITION_C16 = "1561";
    private static final String TRANSITION_C17 = "1571";
    private static final String TRANSITION_C18 = "1581";
    private static final String TRANSITION_C19 = "1591";
    private static final String TRANSITION_C20 = "1601";
    private static final String TRANSITION_C21 = "1611";
    private static final String TRANSITION_C22 = "1621";
    private static final String TRANSITION_C23 = "1631";
    private static final String TRANSITION_C24 = "1641";
    private static final String TRANSITION_C25 = "1651";
    private static final String TRANSITION_C26 = "1661";
    private static final String TRANSITION_C27 = "1671";
    private static final String TRANSITION_C28 = "1681";
    private static final String TRANSITION_C29 = "1691";

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

    public void testC16Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C16_PASS,TRANSITION_C16));
    }

    public void testC16Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C16_FAIL,TRANSITION_C16));
    }

    public void testC17Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C17_PASS,TRANSITION_C17));
    }

    public void testC17Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C17_FAIL,TRANSITION_C17));
    }

    public void testC18Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C18_PASS,TRANSITION_C18));
    }

    public void testC18Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C18_FAIL,TRANSITION_C18));
    }

    public void testC18Pass_2() throws Exception {
        assertTrue(hasTransition(ISSUE_C18_PASS_2,TRANSITION_C18));
    }

    public void testC19Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C19_PASS,TRANSITION_C19));
    }

    public void testC19Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C19_FAIL,TRANSITION_C19));
    }

    public void testC20Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C20_PASS,TRANSITION_C20));
    }

    public void testC20Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C20_FAIL,TRANSITION_C20));
    }

    public void testC20Pass_2() throws Exception {
        assertTrue(hasTransition(ISSUE_C20_PASS_2,TRANSITION_C20));
    }

    public void testC21Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C21_PASS,TRANSITION_C21));
    }

    public void testC21Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C21_FAIL,TRANSITION_C21));
    }

    public void testC22Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C22_PASS,TRANSITION_C22));
    }

    public void testC22Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C22_FAIL,TRANSITION_C22));
    }

    public void testC22Pass_2() throws Exception {
        assertTrue(hasTransition(ISSUE_C22_PASS_2,TRANSITION_C22));
    }

    public void testC23Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C23_PASS,TRANSITION_C23));
    }

    public void testC23Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C23_FAIL,TRANSITION_C23));
    }

    public void testC24Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C24_PASS,TRANSITION_C24));
    }

    public void testC24Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C24_FAIL,TRANSITION_C24));
    }

    public void testC25Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C25_PASS,TRANSITION_C25));
    }

    public void testC25Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C25_FAIL,TRANSITION_C25));
    }

    public void testC25Pass_2() throws Exception {
        assertTrue(hasTransition(ISSUE_C25_PASS_2,TRANSITION_C25));
    }

    public void testC26Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C26_PASS,TRANSITION_C26));
    }

    public void testC26Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C26_FAIL,TRANSITION_C26));
    }

    public void testC27Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C27_PASS,TRANSITION_C27));
    }

    public void testC27Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C27_FAIL,TRANSITION_C27));
    }

    public void testC27Pass_2() throws Exception {
        assertTrue(hasTransition(ISSUE_C27_PASS_2,TRANSITION_C27));
    }

    public void testC28Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C28_PASS,TRANSITION_C28));
    }

    public void testC28Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C28_FAIL,TRANSITION_C28));
    }

    public void testC29Pass() throws Exception {
        assertTrue(hasTransition(ISSUE_C29_PASS,TRANSITION_C29));
    }

    public void testC29Fail() throws Exception {
        assertTrue(!hasTransition(ISSUE_C29_FAIL,TRANSITION_C29));
    }

    public void testC29Pass_2() throws Exception {
        assertTrue(hasTransition(ISSUE_C29_PASS_2,TRANSITION_C29));
    }
}

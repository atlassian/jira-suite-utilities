package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.testkit.client.restclient.Issue;

@WebTest({ Category.FUNC_TEST, Category.REST })
public class OtherTest extends AbstractTestBase {
    private static final String ISSUE_O1_PASS = "TJP-32";

    //Verifies that text "Removed Status" is on the "Transition Summary", for Status Test that has been removed
    public void testO1Pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_O1_PASS);
        assertNotNull(issue);

        navigation.gotoPage("/browse/TJP-32?page=com.googlecode.jira-suite-utilities:transitions-summary-tabpanel");
        text.assertTextPresent("Removed Status");
    }
}

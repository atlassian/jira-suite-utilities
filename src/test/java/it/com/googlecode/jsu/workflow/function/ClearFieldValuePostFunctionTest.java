package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.rest.api.issue.IssueFields;
import com.atlassian.jira.rest.api.issue.IssueUpdateRequest;
import com.atlassian.jira.rest.api.issue.ResourceRef;
import com.atlassian.jira.testkit.client.restclient.Issue;
import com.atlassian.jira.testkit.client.restclient.IssueClient;
import com.atlassian.jira.testkit.client.restclient.Response;
import com.atlassian.jira.testkit.client.restclient.TransitionsClient;
import com.atlassian.jira.testkit.client.util.TimeBombLicence;
import com.atlassian.jira.webtests.ztests.bundledplugins2.rest.RestFuncTest;


@WebTest({ Category.FUNC_TEST, Category.REST })
public class ClearFieldValuePostFunctionTest extends RestFuncTest {

    private IssueClient issueClient;
    private TransitionsClient transitionsClient;


    @Override
    protected void setUpTest() {
        super.setUpTest();
        administration.restoreDataWithLicense("test1.xml", TimeBombLicence.LICENCE_FOR_TESTING);
        issueClient = new IssueClient(getEnvironmentData());
        transitionsClient = new TransitionsClient(getEnvironmentData());
    }


    public void testClearDueDate() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1151")); // PF28      clear field value

        final Response response = transitionsClient.postResponse("TJP-1", issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get("TJP-1");
        assertNull(issue.fields.duedate);
    }
}

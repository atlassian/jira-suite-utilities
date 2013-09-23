package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.rest.api.issue.IssueFields;
import com.atlassian.jira.rest.api.issue.IssueUpdateRequest;
import com.atlassian.jira.rest.api.issue.ResourceRef;
import com.atlassian.jira.testkit.client.restclient.Issue;
import com.atlassian.jira.testkit.client.restclient.Response;

@WebTest({ Category.FUNC_TEST, Category.REST })
public class ConditionTest extends AbstractTestBase {
    private static final String ISSUE_C11_PASS = "TJP-4";
    private static final String ISSUE_C11_FAIL = "TJP-5";
    private static final String ISSUE_C12_PASS = "TJP-6";
    private static final String ISSUE_C12_FAIL = "TJP-7";
    private static final String ISSUE_C13_PASS = "TJP-8";
    private static final String ISSUE_C13_FAIL = "TJP-9";
    private static final String ISSUE_C14_PASS = "TJP-10";
    private static final String ISSUE_C14_FAIL = "TJP-11";

    private static final String TRANSITION_C11 = "1211";
    private static final String TRANSITION_C12 = "1221";
    private static final String TRANSITION_C13 = "1231";
    private static final String TRANSITION_C14 = "1241";

    public void testC11Pass() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C11));

        final Response response = transitionsClient.postResponse(ISSUE_C11_PASS, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C11_PASS);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    public void testC11Fail() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C11));

        final Response response = transitionsClient.postResponse(ISSUE_C11_FAIL, issueUpdateRequest);

        assertEquals(400, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C11_FAIL);
        assertEquals(STATUS_OPEN,issue.fields.status.name());
    }

    public void testC12Pass() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C12));

        final Response response = transitionsClient.postResponse(ISSUE_C12_PASS, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C12_PASS);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    public void testC12Fail() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C12));

        final Response response = transitionsClient.postResponse(ISSUE_C12_FAIL, issueUpdateRequest);

        assertEquals(400, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C12_FAIL);
        assertEquals(STATUS_OPEN,issue.fields.status.name());
    }

    public void testC13Pass() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C13));

        final Response response = transitionsClient.postResponse(ISSUE_C13_PASS, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C13_PASS);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    public void testC13Fail() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C13));

        final Response response = transitionsClient.postResponse(ISSUE_C13_FAIL, issueUpdateRequest);

        assertEquals(400, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C13_FAIL);
        assertEquals(STATUS_OPEN,issue.fields.status.name());
    }

    public void testC14Pass() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C14));

        final Response response = transitionsClient.postResponse(ISSUE_C14_PASS, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C14_PASS);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    public void testC14Fail() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_C14));

        final Response response = transitionsClient.postResponse(ISSUE_C14_FAIL, issueUpdateRequest);

        assertEquals(400, response.statusCode);

        Issue issue = issueClient.get(ISSUE_C14_FAIL);
        assertEquals(STATUS_OPEN,issue.fields.status.name());
    }
}

package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.rest.api.issue.IssueFields;
import com.atlassian.jira.rest.api.issue.IssueUpdateRequest;
import com.atlassian.jira.rest.api.issue.ResourceRef;
import com.atlassian.jira.testkit.client.restclient.*;
import com.atlassian.jira.testkit.client.util.TimeBombLicence;
import com.atlassian.jira.webtests.ztests.bundledplugins2.rest.RestFuncTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebTest({ Category.FUNC_TEST, Category.REST })
public class PostFunctionTest extends RestFuncTest {

    private IssueClient issueClient;
    private TransitionsClient transitionsClient;

    private static final String FIRST_ISSUE_KEY = "TJP-1"; // empty one, resolved
    private static final String SECOND_ISSUE_KEY = "TJP-2"; // with all field data set, resolved

    @Override
    protected void setUpTest() {
        super.setUpTest();
        administration.restoreDataWithLicense("test1.xml", TimeBombLicence.LICENCE_FOR_TESTING);
        issueClient = new IssueClient(getEnvironmentData());
        transitionsClient = new TransitionsClient(getEnvironmentData());
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF1</td></tr>
     * <th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <th style="text-align:left;">Transition (id)</th><td>copy value from other field (781)</td></tr>
     * <th style="text-align:left;">Description</th><td>The field FreeText (10101) will take the value from Assignee.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF1() throws Exception {
        ensureResolved(FIRST_ISSUE_KEY);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("781"));

        final Response response = transitionsClient.postResponse(FIRST_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(FIRST_ISSUE_KEY);
        assertEquals(issue.fields.get("customfield_10101"),issue.fields.assignee.name);
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF2</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 1 (791)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field LocationText (10001) will take the value from LocationSelect (10000)</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF2() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("791"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals(issue.fields.get("customfield_10001"),((HashMap)issue.fields.get("customfield_10000")).get("value").toString());
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF3</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 2 (931)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field FreeText (10101) will take the value from MultiUser (10113).</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF3() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("931"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        List<Map> multi = issue.fields.get("customfield_10113");
        StringBuilder sb = new StringBuilder();
        for(Map map:multi) {
            if(sb.length()>0) {
                sb.append(",");
            }
            sb.append(map.get("name"));
        }
        assertEquals(issue.fields.get("customfield_10101"),sb.toString());
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF3a</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 2a (1171)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field MultiUser1 (10200) will take the value from MultiUser (10113).</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF3a() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1171"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals(issue.fields.get("customfield_10200"),issue.fields.get("customfield_10113"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF4</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 3 (941)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field DateTime (10109) will take the value from Updated.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF4() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotSame(issue.fields.updated,issue.fields.get("customfield_10109"));
        String oldUpdated = issue.fields.updated;

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("941"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals(issue.fields.get("customfield_10109"),oldUpdated);
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF28</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>clear field value 5 (1151)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The contents of the field Due Date will be purged.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF28() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull(issue.fields.duedate);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1151"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNull(issue.fields.duedate);
    }

    // all PostFunction transitions made out of resolved state
    private void ensureResolved(String issueKey) throws Exception {
        Issue issue = issueClient.get(issueKey);
        if(!"Resolved".equals(issue.fields.status.name())) {
            IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
            issueUpdateRequest.fields(new IssueFields());
            issueUpdateRequest.transition(ResourceRef.withId("5"));

            transitionsClient.postResponse(issueKey, issueUpdateRequest);
        }
    }
}

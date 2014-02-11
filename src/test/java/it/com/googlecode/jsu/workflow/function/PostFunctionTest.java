package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.rest.api.issue.IssueFields;
import com.atlassian.jira.rest.api.issue.IssueUpdateRequest;
import com.atlassian.jira.rest.api.issue.ResourceRef;
import com.atlassian.jira.testkit.client.restclient.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@WebTest({ Category.FUNC_TEST, Category.REST })
public class PostFunctionTest extends AbstractTestBase {
    private static final String FIRST_ISSUE_KEY = "TJP-1"; // empty one, resolved
    private static final String SECOND_ISSUE_KEY = "TJP-2"; // with all field data set, resolved
    private static final String SUBTASK_ISSUE_KEY = "TJP-3"; // empty one, resolved
    private static final String ISSUE_PF31 = "TJP-29";
    private static final String ISSUE_PF32 = "TJP-39";
    private static final String ISSUE_PF33 = "TJP-40";
    private static final String ISSUE_PF34 = "TJP-53";
    private static final String ISSUE_PF35 = "TJP-54";
    private static final String ISSUE_PF36 = "TJP-55";
    private static final String ISSUE_PF37 = "TJP-56";
    private static final String ISSUE_PF38 = "TJP-57";
    private static final String ISSUE_PF39 = "TJP-58";
    private static final String ISSUE_PF40 = "TJP-59";
    private static final String ISSUE_PF41 = "TJP-60";
    private static final String ISSUE_PF42 = "TJP-61";
    private static final String ISSUE_PF43 = "TJP-62";

    private static final String TRANSITION_PF31 = "1311";
    private static final String TRANSITION_PF32 = "1371";
    private static final String TRANSITION_PF33 = "1381";
    private static final String TRANSITION_PF34 = "1461";
    private static final String TRANSITION_PF35 = "1471";
    private static final String TRANSITION_PF36 = "1481";
    private static final String TRANSITION_PF37 = "1491";
    private static final String TRANSITION_PF38 = "1501";
    private static final String TRANSITION_PF39 = "1511";
    private static final String TRANSITION_PF40 = "1521";
    private static final String TRANSITION_PF41 = "1531";
    private static final String TRANSITION_PF42 = "1541";
    private static final String TRANSITION_PF43 = "1551";

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF1</td></tr>
     * <th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <th style="text-align:left;">Transition (id)</th><td>copy value from other field (781)</td></tr>
     * <th style="text-align:left;">Description</th><td>The field FreeText will take the value from Assignee.</td></tr>
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
        assertEquals(issue.fields.get(FIELD_FREE_TEXT),issue.fields.assignee.name);
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF2</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 1 (791)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field LocationText (10001) will take the value from LocationSelect</td></tr>
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
        Map single = issue.fields.get(FIELD_LOCATION_SELECT);

        assertEquals(issue.fields.get(FIELD_LOCATION_TEXT),getSingleAsString(single,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF3</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 2 (931)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field FreeText will take the value from MultiUser.</td></tr>
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
        List<Map> multi = issue.fields.get(FIELD_MULTI_USER);
        assertEquals(issue.fields.get(FIELD_FREE_TEXT),getMultiAsString(multi,"name"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF3a</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 2a (1171)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field MultiUser1 will take the value from MultiUser.</td></tr>
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
        assertEquals(issue.fields.get(FIELD_MULTI_USER1),issue.fields.get(FIELD_MULTI_USER));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF4</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 3 (941)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field DateTime will take the value from Updated.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF4() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotSame(issue.fields.updated,issue.fields.get(FIELD_DATE_TIME));
        String oldUpdated = issue.fields.updated;

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("941"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals(issue.fields.get(FIELD_DATE_TIME),oldUpdated);
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF5</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 4 (951)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field Assignee will take the value from UserPicker.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF5() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map single = issue.fields.get(FIELD_USER_PICKER);
        assertNotSame(issue.fields.assignee.name,getSingleAsString(single,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("951"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        single = issue.fields.get(FIELD_USER_PICKER);
        assertEquals(issue.fields.assignee.name,getSingleAsString(single,"name"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF6</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 5 (961)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field Description will take the value from MultiSelect.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF6() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        List<Map> multi = issue.fields.get(FIELD_MULTI_SELECT);

        assertNotSame(issue.fields.description,getMultiAsString(multi,"value"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("961"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        multi = issue.fields.get(FIELD_MULTI_SELECT);

        assertEquals(issue.fields.description,getMultiAsString(multi,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF7</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 6 (971)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field MultiGroup will take the value from GroupPicker.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF7() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map group = issue.fields.get(FIELD_GROUP_PICKER);
        List<Map> multi = issue.fields.get(FIELD_MULTI_GROUP);
        assertNotSame(getSingleAsString(group,"name"),getMultiAsString(multi,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("971"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        group = issue.fields.get(FIELD_GROUP_PICKER);
        multi = issue.fields.get(FIELD_MULTI_GROUP);

        assertEquals(getSingleAsString(group,"name"),getMultiAsString(multi,"name"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF8</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 7 (981)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field ReadonlyText will take the value from Summary.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF8() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotSame(issue.fields.summary,issue.fields.get(FIELD_READONLY_TEXT));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("981"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);

        assertEquals(issue.fields.summary,issue.fields.get(FIELD_READONLY_TEXT));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF9</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 8 (991)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field FreeText will take the value from MultiSelect.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF9() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        List<Map> multi = issue.fields.get(FIELD_MULTI_SELECT);

        assertNotSame(issue.fields.get(FIELD_FREE_TEXT),getMultiAsString(multi,"value"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("991"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        multi = issue.fields.get(FIELD_MULTI_SELECT);

        assertEquals(issue.fields.get(FIELD_FREE_TEXT),getMultiAsString(multi,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF10</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 9 (1001)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field FreeText will take the value from CascadingSelect.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF10() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        //TODO could process cascading select automatically instead of using string constant
        assertNotSame(issue.fields.get(FIELD_FREE_TEXT),"Option BC");

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("991"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals(issue.fields.get(FIELD_FREE_TEXT),"Option A,Option C");
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF11</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>copy value from other field 10 (1011)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The field UserPicker will take the value from Reporter.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF11() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map single = issue.fields.get(FIELD_USER_PICKER);
        assertNotSame(issue.fields.reporter.name,getSingleAsString(single,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1011"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        single = issue.fields.get(FIELD_USER_PICKER);
        assertEquals(issue.fields.reporter.name,getSingleAsString(single,"name"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF12</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field (801)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The FreeText of the issue will be set to %%CURRENT_USER%%.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF12() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotSame("admin",issue.fields.get(FIELD_FREE_TEXT));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("801"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals("admin",issue.fields.get(FIELD_FREE_TEXT));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF13</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 1 (811)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The DateTime of the issue will be set to %%CURRENT_DATETIME%%.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF13() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        String oldDate = issue.fields.get(FIELD_DATE_TIME);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("811"));

        String newDate = DATE_FORMAT.format(new Date());

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotSame(issue.fields.get(FIELD_DATE_TIME),oldDate);
        assertEquals(newDate,issue.fields.get(FIELD_DATE_TIME).toString().substring(0,10));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF14</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 2 (1021)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The LocationText of the issue will be set to Zurich.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF14() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull("Zurich",issue.fields.get(FIELD_LOCATION_TEXT));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1021"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertEquals("Zurich",issue.fields.get(FIELD_LOCATION_TEXT));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF15</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 3 (1031)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The LocationSelect of the issue will be set to Berlin.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF15() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map single = issue.fields.get(FIELD_LOCATION_SELECT);
        assertNotSame("Berlin",getSingleAsString(single,"value"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1031"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        single = issue.fields.get(FIELD_LOCATION_SELECT);
        assertEquals("Berlin",getSingleAsString(single,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF16</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 4 (1041)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The CascadingSelect of the issue will be set to Option AB.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF16() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map cascadeContent = issue.fields.get(FIELD_CASCADING_SELECT);
        assertNotSame("Option AB",getCascadeChildValue(cascadeContent));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1041"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        cascadeContent = issue.fields.get(FIELD_CASCADING_SELECT);

        assertEquals("Option AB",getCascadeChildValue(cascadeContent));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF17</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 5 (1051)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The MultiSelect of the issue will be set to Option C.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF17() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        List<Map> multiContent = issue.fields.get(FIELD_MULTI_SELECT);
        assertNotSame("Option C",getMultiAsString(multiContent,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1051"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        multiContent = issue.fields.get(FIELD_MULTI_SELECT);

        assertEquals("Option C",getMultiAsString(multiContent,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF18</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 6 (1061)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The UserPicker of the issue will be set to user.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF18() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map single = issue.fields.get(FIELD_USER_PICKER);
        assertNotSame("user",getSingleAsString(single,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1061"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        single = issue.fields.get(FIELD_USER_PICKER);

        assertEquals("user",getSingleAsString(single,"name"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF19</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 7 (1071)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The RadioButtons of the issue will be set to Option A.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF19() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map radio = issue.fields.get(FIELD_RADIO_BUTTONS);
        assertNotSame("Option A",getSingleAsString(radio,"value"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1071"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        radio = issue.fields.get(FIELD_RADIO_BUTTONS);

        assertEquals("Option A",getSingleAsString(radio,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF20</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 8 (1081)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The GroupPicker of the issue will be set to jira-administrators.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF20() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map group = issue.fields.get(FIELD_GROUP_PICKER);
        assertNotSame("jira-administrators",getSingleAsString(group,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1081"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        group = issue.fields.get(FIELD_GROUP_PICKER);

        assertEquals("jira-administrators",getSingleAsString(group,"name"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF21</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 9 (1091)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The DatePicker of the issue will be set to %%CURRENT_DATETIME%%.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF21() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        String oldDate = issue.fields.get(FIELD_DATE_PICKER);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1091"));

        String newDate = DATE_FORMAT.format(new Date());

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotSame(issue.fields.get(FIELD_DATE_PICKER),oldDate);
        assertEquals(newDate,issue.fields.get(FIELD_DATE_PICKER));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF22</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>update issue custom field</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>update issue custom field 10 (1101)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The SelectList of the issue will be set to Option B.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF22() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        Map listContent = issue.fields.get(FIELD_SELECT_LIST);
        assertNotSame("Option B",getSingleAsString(listContent,"value)"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1101"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        listContent = issue.fields.get(FIELD_SELECT_LIST);
        assertEquals("Option B",getSingleAsString(listContent,"value"));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF23</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>clear field value</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>clear field value (821)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The contents of the field LocationText will be purged.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF23() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull(issue.fields.get(FIELD_LOCATION_TEXT));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("821"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNull(issue.fields.get(FIELD_LOCATION_TEXT));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF24</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>clear field value</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>clear field value 1 (1111)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The contents of the field GroupPicker will be purged.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF24() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull(issue.fields.get(FIELD_GROUP_PICKER));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1111"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNull(issue.fields.get(FIELD_GROUP_PICKER));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF25</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>clear field value</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>clear field value 2 (1121)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The contents of the field UserPicker will be purged.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF25() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull(issue.fields.get(FIELD_USER_PICKER));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1121"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNull(issue.fields.get(FIELD_USER_PICKER));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF26</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>clear field value</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>clear field value 3 (1131)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The contents of the field MultiUser will be purged.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF26() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull(issue.fields.get(FIELD_MULTI_USER));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1131"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNull(issue.fields.get(FIELD_MULTI_USER));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF27</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>clear field value</td></tr>
     * <tr><th style="text-align:left;">Transition (id)</th><td>clear field value 4 (1141)</td></tr>
     * <tr><th style="text-align:left;">Description</th><td>The contents of the field SelectList will be purged.</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF27() throws Exception {
        ensureResolved(SECOND_ISSUE_KEY);

        Issue issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNotNull(issue.fields.get(FIELD_SELECT_LIST));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1141"));

        final Response response = transitionsClient.postResponse(SECOND_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(SECOND_ISSUE_KEY);
        assertNull(issue.fields.get(FIELD_SELECT_LIST));
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF28</td></tr>
     * <tr><th style="text-align:left;">Post Function</th><td>clear field value</td></tr>
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

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF29</td></tr>
     * <th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <th style="text-align:left;">Transition (id)</th><td>copy value from other field 11 (1201)</td></tr>
     * <th style="text-align:left;">Description</th><td>The field TextField will take the value from Assignee. Source issue is the parent, destination the sub-task. (issue is sub-task)</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF29() throws Exception {
        ensureResolved(SUBTASK_ISSUE_KEY);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1201"));

        final Response response = transitionsClient.postResponse(SUBTASK_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue parent = issueClient.get(SECOND_ISSUE_KEY);
        Issue subtask = issueClient.get(SUBTASK_ISSUE_KEY);
        assertEquals(subtask.fields.get(FIELD_TEXT_FIELD),parent.fields.assignee.name);
    }

    /**
     * Single test using transition.
     * <table>
     * <tr><th style="text-align:left;">Test-Nr</th><td>PF30</td></tr>
     * <th style="text-align:left;">Post Function</th><td>copy value from other field</td></tr>
     * <th style="text-align:left;">Transition (id)</th><td>copy value from other field 11 (1201)</td></tr>
     * <th style="text-align:left;">Description</th><td>The field TextField will take the value from Assignee. Source issue is the parent, destination the sub-task. (issue is casual issue)</td></tr>
     * </table>
     * @throws Exception
     */
    public void testPF30() throws Exception {
        ensureResolved(FIRST_ISSUE_KEY);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId("1201"));

        final Response response = transitionsClient.postResponse(FIRST_ISSUE_KEY, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(FIRST_ISSUE_KEY);
        assertNotSame(issue.fields.get(FIELD_TEXT_FIELD),issue.fields.assignee.name);
    }

    //TODO obsolete since JSUTIL-202, but need to stay for backwards compatibility for a while
    //The MultiUser of the issue will be set to %%ADD_CURRENT_USER%%.
    public void testPF31() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF31));

        final Response response = transitionsClient.postResponse(ISSUE_PF31, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF31);
        List<Map> multi = issue.fields.get(FIELD_MULTI_USER);
        assertEquals("admin,user",getMultiAsString(multi,"name"));
    }

    //The contents of the field Watchers will be purged.
    public void testPF32() throws Exception {
        Issue issue = issueClient.get(ISSUE_PF32);
        assertTrue(issue.fields.watches.watchCount>0);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF32));

        final Response response = transitionsClient.postResponse(ISSUE_PF32, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_PF32);
        assertTrue(issue.fields.watches.watchCount==0);
    }

    //The field UserPicker will take the value from Assignee. Source and destination issue are the same.
    public void testPF33() throws Exception {
        Issue issue = issueClient.get(ISSUE_PF33);
        Map single = issue.fields.get(FIELD_USER_PICKER);
        assertNotSame(issue.fields.assignee.name,getSingleAsString(single,"name"));

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF33));

        final Response response = transitionsClient.postResponse(ISSUE_PF33, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_PF33);
        single = issue.fields.get(FIELD_USER_PICKER);
        assertEquals(issue.fields.assignee.name,getSingleAsString(single,"name"));
    }

    //The value %%CURRENT_USER%% will be appended, or set in multi fields, to field FreeText.
    public void testPF34() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF34));

        final Response response = transitionsClient.postResponse(ISSUE_PF34, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF34);
        assertEquals("Yesterday, admin",issue.fields.get(FIELD_FREE_TEXT));
    }

    //The value Option C will be appended, or set in multi fields, to field MultiSelect.
    public void testPF35() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF35));

        final Response response = transitionsClient.postResponse(ISSUE_PF35, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF35);
        List<Map> multi = issue.fields.get(FIELD_MULTI_SELECT);

        assertEquals("Option A,Option C",getMultiAsString(multi,"value"));
    }

    //The value Option A will be appended, or set in multi fields, to field RadioButtons.
    public void testPF36() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF36));

        final Response response = transitionsClient.postResponse(ISSUE_PF36, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF36);
        Map radio = issue.fields.get(FIELD_RADIO_BUTTONS);
        //RadioButtons is a single selection field, thus even though appending, only the new option must be present
        assertEquals("Option A", getSingleAsString(radio, "value"));
    }

    //The value %%CURRENT_USER%% will be appended, or set in multi fields, to field MultiUser.
    public void testPF37() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF37));

        final Response response = transitionsClient.postResponse(ISSUE_PF37, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF37);
        List<Map> multi = issue.fields.get(FIELD_MULTI_USER);
        assertEquals("admin,developer",getMultiAsString(multi, "name"));
    }

    //The value two will be appended, or set in multi fields, to field Labels.
    public void testPF38() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF38));

        final Response response = transitionsClient.postResponse(ISSUE_PF38, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF38);
        List<Map> multi = issue.fields.get(FIELD_LABELS);
        assertEquals("[one, two]",multi.toString());
    }

    //The value Option D will be appended, or set in multi fields, to field MultiCheckboxes.
    public void testPF39() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF39));

        final Response response = transitionsClient.postResponse(ISSUE_PF39, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF39);
        List<Map> multi = issue.fields.get(FIELD_MULTI_CHECKBOXES);
        assertEquals("Option B,Option D",getMultiAsString(multi,"value"));
    }

    //The value 3.0 will be appended, or set in multi fields, to field VersionPicker.
    public void testPF40() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF40));

        final Response response = transitionsClient.postResponse(ISSUE_PF40, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF40);
        List<Map> multi = issue.fields.get(FIELD_VERSION_PICKER);
        assertEquals("2.0,3.0",getMultiAsString(multi,"name"));
    }

    //The SelectList of the issue will be set to 10102.
    public void testPF41() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF41));

        final Response response = transitionsClient.postResponse(ISSUE_PF41, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF41);
        Map listContent = issue.fields.get(FIELD_SELECT_LIST);
        assertEquals("Option C",getSingleAsString(listContent,"value"));
    }

    //The MultiSelect of the issue will be set to 10023.
    public void testPF42() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF42));

        final Response response = transitionsClient.postResponse(ISSUE_PF42, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF42);
        List<Map> multi = issue.fields.get(FIELD_MULTI_SELECT);
        assertEquals("Option C",getMultiAsString(multi,"value"));
    }

    //The value 10023 will be appended to field MultiSelect, or set if it is a multi field.
    public void testPF43() throws Exception {
        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_PF43));

        final Response response = transitionsClient.postResponse(ISSUE_PF43, issueUpdateRequest);

        assertEquals(204, response.statusCode);

        Issue issue = issueClient.get(ISSUE_PF43);
        List<Map> multi = issue.fields.get(FIELD_MULTI_SELECT);
        assertEquals("Option A,Option C",getMultiAsString(multi,"value"));
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

    // get string out of multi field, stored as list of maps
    private String getMultiAsString(List<Map> multiContent, String key) {
        StringBuilder sb = new StringBuilder();
        for(Map map:multiContent) {
            if(sb.length()>0) {
                sb.append(",");
            }
            sb.append(map.get(key));
        }
        return sb.toString();
    }

    // get string out of field, stored as map
    private String getSingleAsString(Map singleContent, String key) {
        if(singleContent!=null) {
            Object value = singleContent.get(key);
            if(value!=null) {
                return value.toString();
            }
        }
        return null;
    }

    // get value of selected cascading select its child
    private String getCascadeChildValue(Map cascadeContent) {
        Map child = (Map)cascadeContent.get("child");
        return (String)child.get("value");
    }
}

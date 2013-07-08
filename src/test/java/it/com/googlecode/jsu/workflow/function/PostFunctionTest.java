package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.rest.api.issue.IssueFields;
import com.atlassian.jira.rest.api.issue.IssueUpdateRequest;
import com.atlassian.jira.rest.api.issue.ResourceRef;
import com.atlassian.jira.testkit.client.restclient.*;
import com.atlassian.jira.testkit.client.util.TimeBombLicence;
import com.atlassian.jira.webtests.ztests.bundledplugins2.rest.RestFuncTest;

import java.util.List;
import java.util.Map;


@WebTest({ Category.FUNC_TEST, Category.REST })
public class PostFunctionTest extends RestFuncTest {

    private IssueClient issueClient;
    private TransitionsClient transitionsClient;

    private static final String FIRST_ISSUE_KEY = "TJP-1"; // empty one, resolved
    private static final String SECOND_ISSUE_KEY = "TJP-2"; // with all field data set, resolved

    private static final String FIELD_LOCATION_TEXT = "customfield_10001";
    private static final String FIELD_GROUP_PICKER = "customfield_10110";
    private static final String FIELD_USER_PICKER = "customfield_10002";
    private static final String FIELD_MULTI_USER = "customfield_10113";
    private static final String FIELD_SELECT_LIST = "customfield_10106";
    private static final String FIELD_FREE_TEXT = "customfield_10101";
    private static final String FIELD_LOCATION_SELECT = "customfield_10000";
    private static final String FIELD_MULTI_USER1 = "customfield_10200";
    private static final String FIELD_DATE_TIME = "customfield_10109";
    private static final String FIELD_MULTI_SELECT = "customfield_10103";
    private static final String FIELD_MULTI_GROUP = "customfield_10112";
    private static final String FIELD_READONLY_TEXT = "customfield_10115";
    private static final String FIELD_CASCADING_SELECT = "customfield_10108";

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
        assertEquals(issue.fields.get("customfield_10101"),getMultiAsString(multi,"name"));
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
        return singleContent.get(key).toString();
    }
}

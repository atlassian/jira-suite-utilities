package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.functest.framework.util.date.DateUtil;
import com.atlassian.jira.rest.api.issue.IssueFields;
import com.atlassian.jira.rest.api.issue.IssueUpdateRequest;
import com.atlassian.jira.rest.api.issue.ResourceRef;
import com.atlassian.jira.testkit.client.restclient.Issue;
import com.atlassian.jira.testkit.client.restclient.Response;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;

@WebTest({ Category.FUNC_TEST, Category.REST })
public class ValidatorTest extends AbstractTestBase {
    private static final String ISSUE_V10_PASS = "TJP-12";
    private static final String ISSUE_V10_FAIL = "TJP-13";
    private static final String ISSUE_V11_PASS = "TJP-14";
    private static final String ISSUE_V11_FAIL = "TJP-15";
    private static final String ISSUE_V12_PASS = "TJP-16";
    private static final String ISSUE_V12_FAIL = "TJP-17";
    private static final String ISSUE_V13_PASS = "TJP-18";
    private static final String ISSUE_V13_FAIL = "TJP-19";
    private static final String ISSUE_V14_PASS = "TJP-20";
    private static final String ISSUE_V14_FAIL = "TJP-22";
    private static final String ISSUE_V15_PASS = "TJP-23";
    private static final String ISSUE_V15_FAIL = "TJP-21";

    private static final String TRANSITION_V10 = "1251";
    private static final String TRANSITION_V11 = "1261";
    private static final String TRANSITION_V12 = "1271";
    private static final String TRANSITION_V13 = "1281";
    private static final String TRANSITION_V14 = "1291";
    private static final String TRANSITION_V15 = "1301";

    //Verifies that field DatePicker is less than value of now. Only date part.
    public void testV10Pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V10_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(), -2)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V10_PASS, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V10));
        final Response response = transitionsClient.postResponse(ISSUE_V10_PASS, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V10_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Verifies that field DatePicker is less than value of now. Only date part.
    public void testV10Fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V10_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),+2)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V10_FAIL, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V10));
        final Response response = transitionsClient.postResponse(ISSUE_V10_FAIL, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V10_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Verifies that field DateTime is greater than value of 5d. Including time part.
    public void testV11Pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V11_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_TIME_ID,DATE_TIME_FORMAT.format(DateUtils.addDays(new Date(),+6)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V11_PASS, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V11));
        final Response response = transitionsClient.postResponse(ISSUE_V11_PASS, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V11_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Verifies that field DateTime is greater than value of 5d. Including time part.
    public void testV11Fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V11_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_TIME_ID,DATE_TIME_FORMAT.format(DateUtils.addDays(new Date(),+4)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V11_FAIL, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V11));
        final Response response = transitionsClient.postResponse(ISSUE_V11_FAIL, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V11_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Verifies that field DatePicker is less than value of -1m. Only date part.
    public void testV12Pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V12_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),-40)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V12_PASS, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V12));
        final Response response = transitionsClient.postResponse(ISSUE_V12_PASS, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V12_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Verifies that field DatePicker is less than value of -1m. Only date part.
    public void testV12Fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V12_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),-25)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V12_FAIL, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V12));
        final Response response = transitionsClient.postResponse(ISSUE_V12_FAIL, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V12_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Verifies that field DatePicker is greater than or equal to value of 1w. Only date part.
    public void testV13Pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V13_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),7)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V13_PASS, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V13));
        final Response response = transitionsClient.postResponse(ISSUE_V13_PASS, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V13_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Verifies that field DatePicker is greater than or equal to value of 1w. Only date part.
    public void testV13Fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V13_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),6)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V13_FAIL, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V13));
        final Response response = transitionsClient.postResponse(ISSUE_V13_FAIL, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V13_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Verifies that field DatePicker is not equal to value of now. Only date part.
    public void testV14Pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V14_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),1)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V14_PASS, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V14));
        final Response response = transitionsClient.postResponse(ISSUE_V14_PASS, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V14_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Verifies that field DatePicker is not equal to value of now. Only date part.
    public void testV14Fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V14_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(new Date()));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V14_FAIL, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V14));
        final Response response = transitionsClient.postResponse(ISSUE_V14_FAIL, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V14_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Verifies that field DatePicker is greater than value of 1y. Only date part.
    public void testV15ass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V15_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),370)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V15_PASS, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V15));
        final Response response = transitionsClient.postResponse(ISSUE_V15_PASS, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V15_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Verifies that field DatePicker is greater than value of 1y. Only date part.
    public void testV15Fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V15_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        IssueFields issueFields = new IssueFields();
        issueFields.customField(FIELD_DATE_PICKER_ID,DATE_FORMAT.format(DateUtils.addDays(new Date(),350)));
        issueUpdateRequest.fields(issueFields);
        final Response updateReponse = issueClient.updateResponse(ISSUE_V15_FAIL, issueUpdateRequest);
        assertEquals(204, updateReponse.statusCode);

        issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V15));
        final Response response = transitionsClient.postResponse(ISSUE_V15_FAIL, issueUpdateRequest);
        assertEquals(204, response.statusCode);

        issue = issueClient.get(ISSUE_V15_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }
}

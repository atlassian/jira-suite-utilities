package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
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
    private static final String ISSUE_V16_PASS = "TJP-33";
    private static final String ISSUE_V16_FAIL = "TJP-34";
    private static final String ISSUE_V17_PASS = "TJP-35";
    private static final String ISSUE_V17_FAIL = "TJP-36";
    private static final String ISSUE_V18_PASS = "TJP-37";
    private static final String ISSUE_V18_FAIL = "TJP-38";
    private static final String ISSUE_V19_PASS = "TJP-41";
    private static final String ISSUE_V19_FAIL = "TJP-42";
    private static final String ISSUE_V20_PASS = "TJP-43";
    private static final String ISSUE_V21_PASS = "STJP-1";
    private static final String ISSUE_V21_FAIL = "STJP-2";
    private static final String ISSUE_V22_FAIL = "TJP-44";
    private static final String ISSUE_V23_PASS = "STJP-3";
    private static final String ISSUE_V23_FAIL = "STJP-4";

    private static final String TRANSITION_V10 = "1251";
    private static final String TRANSITION_V11 = "1261";
    private static final String TRANSITION_V12 = "1271";
    private static final String TRANSITION_V13 = "1281";
    private static final String TRANSITION_V14 = "1291";
    private static final String TRANSITION_V15 = "1301";
    private static final String TRANSITION_V16 = "1341";
    private static final String TRANSITION_V17 = "1351";
    private static final String TRANSITION_V18 = "1361";
    private static final String TRANSITION_V19 = "1391";
    private static final String TRANSITION_V20 = "1401";
    private static final String TRANSITION_V21 = "1401";
    private static final String TRANSITION_V22 = "1411";
    private static final String TRANSITION_V23 = "1411";

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
    public void testV15pass() throws Exception {
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

    //The field TextField its contents must match against the regular expression ^[ABC][qwer]+[0-9]{3}
    public void testV16pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V16_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V16));

        final Response reponse = transitionsClient.postResponse(ISSUE_V16_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V16_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //The field TextField its contents must match against the regular expression ^[ABC][qwer]+[0-9]{3}
    public void testV16fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V16_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V16));

        final Response reponse = transitionsClient.postResponse(ISSUE_V16_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V16_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //The field NumberField its contents must match against the regular expression [0-4]{5}\.0
    public void testV17pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V17_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V17));

        final Response reponse = transitionsClient.postResponse(ISSUE_V17_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V17_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //The field NumberField its contents must match against the regular expression [0-4]{5}\.0
    public void testV17fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V17_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V17));

        final Response reponse = transitionsClient.postResponse(ISSUE_V17_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V17_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //The field URLField its contents must match against the regular expression http://[w]{3}\.[a-z]*\.ch
    public void testV18pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V18_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V18));

        final Response reponse = transitionsClient.postResponse(ISSUE_V18_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V18_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //The field URLField its contents must match against the regular expression http://[w]{3}\.[a-z]*\.ch
    public void testV18fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V18_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V18));

        final Response reponse = transitionsClient.postResponse(ISSUE_V18_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V18_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Required fields: Σ Time Spent
    public void testV19pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V19_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V19));

        final Response reponse = transitionsClient.postResponse(ISSUE_V19_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V19_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Required fields: Σ Time Spent
    public void testV19fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V19_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V19));

        final Response reponse = transitionsClient.postResponse(ISSUE_V19_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V19_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Required fields: Textfield2
    public void testV20pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V20_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V20));

        final Response reponse = transitionsClient.postResponse(ISSUE_V20_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V20_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Required fields: Textfield2
    public void testV21pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V21_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V21));

        final Response reponse = transitionsClient.postResponse(ISSUE_V21_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V21_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Required fields: Textfield2
    public void testV21fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V21_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V21));

        final Response reponse = transitionsClient.postResponse(ISSUE_V21_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V21_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Required fields (ignoring context): Textfield2
    public void testV22fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V22_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V22));

        final Response reponse = transitionsClient.postResponse(ISSUE_V22_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V22_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }

    //Required fields (ignoring context): Textfield2
    public void testV23pass() throws Exception {
        Issue issue = issueClient.get(ISSUE_V23_PASS);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V23));

        final Response reponse = transitionsClient.postResponse(ISSUE_V23_PASS, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V23_PASS);
        assertEquals(STATUS_RESOLVED,issue.fields.status.name());
    }

    //Required fields (ignoring context): Textfield2
    public void testV23fail() throws Exception {
        Issue issue = issueClient.get(ISSUE_V23_FAIL);
        assertNotNull(issue);

        IssueUpdateRequest issueUpdateRequest = new IssueUpdateRequest();
        issueUpdateRequest.fields(new IssueFields());
        issueUpdateRequest.transition(ResourceRef.withId(TRANSITION_V23));

        final Response reponse = transitionsClient.postResponse(ISSUE_V23_FAIL, issueUpdateRequest);
        assertEquals(204, reponse.statusCode);

        issue = issueClient.get(ISSUE_V23_FAIL);
        assertEquals(STATUS_IN_PROGRESS,issue.fields.status.name());
    }
}

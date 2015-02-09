package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.FuncTestCase;
import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.testkit.client.restclient.IssueClient;
import com.atlassian.jira.testkit.client.restclient.IssueTransitionsMeta;
import com.atlassian.jira.testkit.client.restclient.TransitionsClient;
import com.atlassian.jira.testkit.client.restclient.WatchersClient;
import com.atlassian.jira.testkit.client.util.TimeBombLicence;

import java.text.SimpleDateFormat;

@WebTest({ Category.FUNC_TEST, Category.REST })
public abstract class AbstractTestBase extends FuncTestCase {
    protected IssueClient issueClient;
    protected TransitionsClient transitionsClient;
    protected WatchersClient watchersClient;

    protected static final String STATUS_IN_PROGRESS = "In Progress";
    protected static final String STATUS_RESOLVED = "Resolved";

    protected static final long FIELD_DATE_TIME_ID = 10109L;
    protected static final long FIELD_DATE_PICKER_ID = 10100L;

    protected static final String FIELD_LOCATION_TEXT = "customfield_10001";
    protected static final String FIELD_GROUP_PICKER = "customfield_10110";
    protected static final String FIELD_USER_PICKER = "customfield_10002";
    protected static final String FIELD_MULTI_USER = "customfield_10113";
    protected static final String FIELD_SELECT_LIST = "customfield_10106";
    protected static final String FIELD_FREE_TEXT = "customfield_10101";
    protected static final String FIELD_LOCATION_SELECT = "customfield_10000";
    protected static final String FIELD_MULTI_USER1 = "customfield_10200";
    protected static final String FIELD_DATE_TIME = "customfield_" + FIELD_DATE_TIME_ID;
    protected static final String FIELD_MULTI_SELECT = "customfield_10103";
    protected static final String FIELD_MULTI_GROUP = "customfield_10112";
    protected static final String FIELD_READONLY_TEXT = "customfield_10115";
    protected static final String FIELD_CASCADING_SELECT = "customfield_10108";
    protected static final String FIELD_RADIO_BUTTONS = "customfield_10105";
    protected static final String FIELD_DATE_PICKER = "customfield_" + FIELD_DATE_PICKER_ID;
    protected static final String FIELD_TEXT_FIELD = "customfield_10107";
    protected static final String FIELD_LABELS = "customfield_10111";
    protected static final String FIELD_MULTI_CHECKBOXES = "customfield_10102";
    protected static final String FIELD_VERSION_PICKER = "customfield_10118";

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    protected static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    protected void setUpTest() {
        super.setUpTest();
        administration.restoreDataWithLicense("test1.zip", TimeBombLicence.LICENCE_FOR_TESTING);
        issueClient = new IssueClient(getEnvironmentData());
        transitionsClient = new TransitionsClient(getEnvironmentData());
        watchersClient = new WatchersClient(getEnvironmentData());
    }

    protected boolean hasTransition(String issueKey, String transitionId, String user) {
        transitionsClient.loginAs(user);
        return hasTransition(issueKey, transitionId);
    }

    protected boolean hasTransition(String issueKey, String transitionId) {
        IssueTransitionsMeta meta = transitionsClient.get(issueKey);
        for(IssueTransitionsMeta.Transition transition:meta.transitions) {
            if(transition.id==Integer.parseInt(transitionId)) {
                return true;
            }
        }
        return false;
    }
}

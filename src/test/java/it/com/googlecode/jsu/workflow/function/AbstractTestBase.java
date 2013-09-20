package it.com.googlecode.jsu.workflow.function;

import com.atlassian.jira.functest.framework.suite.Category;
import com.atlassian.jira.functest.framework.suite.WebTest;
import com.atlassian.jira.testkit.client.restclient.IssueClient;
import com.atlassian.jira.testkit.client.restclient.TransitionsClient;
import com.atlassian.jira.testkit.client.util.TimeBombLicence;
import com.atlassian.jira.webtests.ztests.bundledplugins2.rest.RestFuncTest;

@WebTest({ Category.FUNC_TEST, Category.REST })
public abstract class AbstractTestBase extends RestFuncTest {
    protected IssueClient issueClient;
    protected TransitionsClient transitionsClient;

    @Override
    protected void setUpTest() {
        super.setUpTest();
        administration.restoreDataWithLicense("test1.xml", TimeBombLicence.LICENCE_FOR_TESTING);
        issueClient = new IssueClient(getEnvironmentData());
        transitionsClient = new TransitionsClient(getEnvironmentData());
    }

}

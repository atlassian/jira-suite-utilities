package com.googlecode.jsu.util;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.bc.project.component.ProjectComponentManager;
import com.atlassian.jira.config.PriorityManager;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.*;
import com.atlassian.jira.issue.customfields.CustomFieldType;
import com.atlassian.jira.issue.customfields.MultipleCustomFieldType;
import com.atlassian.jira.issue.customfields.MultipleSettableCustomFieldType;
import com.atlassian.jira.issue.customfields.impl.*;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.issue.customfields.option.Options;
import com.atlassian.jira.issue.customfields.view.CustomFieldParams;
import com.atlassian.jira.issue.customfields.view.CustomFieldParamsImpl;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.atlassian.jira.issue.label.LabelManager;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.priority.Priority;
import com.atlassian.jira.issue.resolution.Resolution;
import com.atlassian.jira.issue.security.IssueSecurityLevel;
import com.atlassian.jira.issue.security.IssueSecurityLevelManager;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.issue.worklog.WorkRatio;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.project.version.Version;
import com.atlassian.jira.project.version.VersionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.util.ObjectUtils;
import com.atlassian.jira.workflow.WorkflowActionsBean;
import com.googlecode.jsu.helpers.checkers.ConverterString;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import org.apache.commons.lang.StringUtils;
import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.GenericValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.opensymphony.user.Entity;

/**
 * @author Gustavo Martin.
 *
 * This utils class exposes common methods to custom workflow objects.
 *
 */

//TODO Can we remove dependencies to jira-core (instead only jira-api) by using interfaces (instead of classes) of referenced custom field types?
public class WorkflowUtils {
    public static final String SPLITTER = "@@";

    private final WorkflowActionsBean workflowActionsBean = new WorkflowActionsBean();
    private final Logger log = LoggerFactory.getLogger(WorkflowUtils.class);

    private final FieldManager fieldManager;
    private final IssueManager issueManager;
    private final ProjectComponentManager projectComponentManager;
    private final VersionManager versionManager;
    private final IssueSecurityLevelManager issueSecurityLevelManager;
    private final ApplicationProperties applicationProperties;
    private final FieldCollectionsUtils fieldCollectionsUtils;
    private final IssueLinkManager issueLinkManager;
    private final UserManager userManager;
    private final CrowdService crowdService;
    private final OptionsManager optionsManager;
    private final ProjectManager projectManager;
    private final PriorityManager priorityManager;
    private final LabelManager labelManager;

    /**
     * @param fieldManager
     * @param issueManager
     * @param projectComponentManager
     * @param versionManager
     * @param issueSecurityLevelManager
     * @param applicationProperties
     * @param fieldCollectionsUtils
     * @param issueLinkManager
     * @param labelManager;
     */
    public WorkflowUtils(
            FieldManager fieldManager, IssueManager issueManager,
            ProjectComponentManager projectComponentManager, VersionManager versionManager,
            IssueSecurityLevelManager issueSecurityLevelManager, ApplicationProperties applicationProperties,
            FieldCollectionsUtils fieldCollectionsUtils, IssueLinkManager issueLinkManager,
            UserManager userManager, CrowdService crowdService, OptionsManager optionsManager,
            ProjectManager projectManager, PriorityManager priorityManager, LabelManager labelManager) {
        this.fieldManager = fieldManager;
        this.issueManager = issueManager;
        this.projectComponentManager = projectComponentManager;
        this.versionManager = versionManager;
        this.issueSecurityLevelManager = issueSecurityLevelManager;
        this.applicationProperties = applicationProperties;
        this.fieldCollectionsUtils = fieldCollectionsUtils;
        this.issueLinkManager = issueLinkManager;
        this.userManager = userManager;
        this.crowdService = crowdService;
        this.optionsManager = optionsManager;
        this.projectManager = projectManager;
        this.priorityManager = priorityManager;
        this.labelManager = labelManager;
    }

    /**
     * @param key
     * @return a String with the field name from given key.
     */
    public String getFieldNameFromKey(String key) {
        return getFieldFromKey(key).getName();
    }

    /**
     * @param key
     * @return a Field object from given key. (Field or Custom Field).
     */
    public Field getFieldFromKey(String key) {
        Field field;

        if (fieldManager.isCustomField(key)) {
            field = fieldManager.getCustomField(key);
        } else {
            field = fieldManager.getField(key);
        }

        if (field == null) {
            throw new IllegalArgumentException("Unable to find field '" + key + "'");
        }

        return field;
    }

    public Field getFieldFromDescriptor(AbstractDescriptor descriptor, String name) {
        FunctionDescriptor functionDescriptor = (FunctionDescriptor) descriptor;
        Map args = functionDescriptor.getArgs();
        String fieldKey = (String) args.get(name);

        return getFieldFromKey(fieldKey);
    }

    /**
     * @param issue The issue to work with.
     * @param fieldKey Key of the field for which to retrieve the current value.
     * @return The value of the field on given issue, comma separated if field contains multiple values, null if none.
     */
    public String getFieldStringValue(Issue issue, String fieldKey) {
        Object value = getFieldValueFromIssue(issue,getFieldFromKey(fieldKey));

        if(value instanceof String) {
            return (String)value;
        } else if(value instanceof List) {
            StringBuilder sb = new StringBuilder();
            for(Object o:((List)value)) {
                if(sb.length()>0) {
                    sb.append(", ");
                }
                if(o instanceof User) {
                    sb.append(((User)o).getName());
                } else {
                    sb.append(o.toString());
                }
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * @param issue
     *            an issue object.
     * @param field
     *            a field object. (May be a Custom Field)
     * @return an Object
     *
     * It returns the value of a field within issue object. May be a Collection,
     * a List, a Strong, or any FildType within JIRA.
     *
     */
    public Object getFieldValueFromIssue(Issue issue, Field field) {
        Object retVal = null;

        try {
            if (fieldManager.isCustomField(field)) {
                // Return the CustomField value. It could be any object.
                CustomField customField = (CustomField) field;
                Object value = issue.getCustomFieldValue(customField);

                if (customField.getCustomFieldType() instanceof CascadingSelectCFType) {
                    HashMap<String, Option> hashMapEntries = (HashMap<String, Option>) value;

                    if (hashMapEntries != null) {
                        Option parent =  hashMapEntries.get(CascadingSelectCFType.PARENT_KEY);
                        Option child =  hashMapEntries.get(CascadingSelectCFType.CHILD_KEY);

                        if (parent != null) {
                            if (ObjectUtils.isValueSelected(child)) {
                                retVal = child.toString();
                            } else {
                                final List<Option> childOptions = parent.getChildOptions();

                                if ((childOptions == null) || (childOptions.isEmpty())) {
                                    retVal = parent.toString();
                                }
                            }
                        }
                    }
                } else {
                    retVal = value; 
                }

                if (log.isDebugEnabled()) {
                    log.debug(
                            String.format(
                                    "Got field value [object=%s;class=%s]",
                                    retVal, ((retVal != null) ? retVal.getClass() : "")
                            )
                    );
                }
            } else {
                String fieldId = field.getId();
                Collection<?> retCollection = null;

                // Special treatment of fields.
                if (fieldId.equals(IssueFieldConstants.ATTACHMENT)) {
                    // return a collection with the attachments associated to given issue.
                    retCollection = issue.getAttachments();

                    if (retCollection != null && !retCollection.isEmpty()) {
                        retVal = retCollection;
                    }
                } else if (fieldId.equals(IssueFieldConstants.AFFECTED_VERSIONS)) {
                    retCollection = issue.getAffectedVersions();

                    if (retCollection != null && !retCollection.isEmpty()) {
                        retVal = retCollection;
                    }
                } else if (fieldId.equals(IssueFieldConstants.COMMENT)) {
                    // return a list with the comments of a given issue.
                    try {
                        retCollection = issueManager.getEntitiesByIssueObject(
                                IssueRelationConstants.COMMENTS, issue
                        );

                        if (retCollection != null && !retCollection.isEmpty()) {
                            retVal = retCollection;
                        }
                    } catch (GenericEntityException e) {
                        retVal = null;
                    }
                } else if (fieldId.equals(IssueFieldConstants.COMPONENTS)) {
                    retCollection = issue.getComponents();

                    if (retCollection != null && !retCollection.isEmpty()) {
                        retVal = retCollection;
                    }
                } else if (fieldId.equals(IssueFieldConstants.FIX_FOR_VERSIONS)) {
                    retCollection = issue.getFixVersions();

                    if (retCollection != null && !retCollection.isEmpty()) {
                        retVal = retCollection;
                    }
                } else if (fieldId.equals(IssueFieldConstants.THUMBNAIL)) {
                    // Not implemented, yet.
                } else if (fieldId.equals(IssueFieldConstants.ISSUE_TYPE)) {
                    retVal = issue.getIssueTypeObject();
                } else if (fieldId.equals(IssueFieldConstants.TIMETRACKING)) {
                    // Not implemented, yet.
                } else if (fieldId.equals(IssueFieldConstants.ISSUE_LINKS)) {
                    retVal = issueLinkManager.getIssueLinks(issue.getId());
                } else if (fieldId.equals(IssueFieldConstants.WORKRATIO)) {
                    retVal = String.valueOf(WorkRatio.getWorkRatio(issue));
                } else if (fieldId.equals(IssueFieldConstants.ISSUE_KEY)) {
                    retVal = issue.getKey();
                } else if (fieldId.equals(IssueFieldConstants.SUBTASKS)) {
                    retCollection = issue.getSubTaskObjects();

                    if (retCollection != null && !retCollection.isEmpty()) {
                        retVal = retCollection;
                    }
                } else if (fieldId.equals(IssueFieldConstants.PRIORITY)) {
                    retVal = issue.getPriorityObject();
                } else if (fieldId.equals(IssueFieldConstants.RESOLUTION)) {
                    retVal = issue.getResolutionObject();
                } else if (fieldId.equals(IssueFieldConstants.STATUS)) {
                    retVal = issue.getStatusObject();
                } else if (fieldId.equals(IssueFieldConstants.PROJECT)) {
                    retVal = issue.getProjectObject();
                } else if (fieldId.equals(IssueFieldConstants.SECURITY)) {
                    retVal = issue.getSecurityLevel();
                } else if (fieldId.equals(IssueFieldConstants.TIME_ESTIMATE)) {
                    retVal = issue.getEstimate();
                } else if (fieldId.equals(IssueFieldConstants.TIME_SPENT)) {
                    retVal = issue.getTimeSpent();
                } else if (fieldId.equals(IssueFieldConstants.ASSIGNEE)) {
                    retVal = issue.getAssigneeUser();
                } else if (fieldId.equals(IssueFieldConstants.REPORTER)) {
                    retVal = issue.getReporterUser();
                } else if (fieldId.equals(IssueFieldConstants.DESCRIPTION)) {
                    retVal = issue.getDescription();
                } else if (fieldId.equals(IssueFieldConstants.ENVIRONMENT)) {
                    retVal = issue.getEnvironment();
                } else if (fieldId.equals(IssueFieldConstants.SUMMARY)) {
                    retVal = issue.getSummary();
                } else if (fieldId.equals(IssueFieldConstants.DUE_DATE)) {
                    retVal = issue.getDueDate();
                } else if (fieldId.equals(IssueFieldConstants.UPDATED)) {
                    retVal = issue.getUpdated();
                } else if (fieldId.equals(IssueFieldConstants.CREATED)) {
                    retVal = issue.getCreated();
                } else if (fieldId.equals(IssueFieldConstants.RESOLUTION_DATE)) {
                    retVal = issue.getResolutionDate();
                } else if (fieldId.equals(IssueFieldConstants.LABELS)) {
                    retVal = issue.getLabels();
                } else {
                    log.warn("Issue field \"" + fieldId + "\" is not supported.");

                    GenericValue gvIssue = issue.getGenericValue();

                    if (gvIssue != null) {
                        retVal = gvIssue.get(fieldId);
                    }
                }
            }
        } catch (NullPointerException e) {
            retVal = null;

            log.error("Unable to get field \"" + field.getId() + "\" value", e);
        }

        return retVal;
    }

    /**
     * Sets specified value to the field for the issue.
     *
     * @param currentUser
     * @param issue
     * @param field
     * @param value
     */
    public void setFieldValue(ApplicationUser currentUser, MutableIssue issue, Field field, Object value, IssueChangeHolder changeHolder) {
        if (fieldManager.isCustomField(field)) {
            CustomField customField = (CustomField) field;
            Object oldValue = issue.getCustomFieldValue(customField);
            FieldLayoutItem fieldLayoutItem;
            CustomFieldType cfType = customField.getCustomFieldType();

            if (log.isDebugEnabled()) {
                log.debug(
                        String.format(
                                "Set custom field value " +
                                "[field=%s,type=%s,oldValue=%s,newValueClass=%s,newValue=%s]",
                                customField,
                                cfType,
                                oldValue,
                                (value != null) ? value.getClass().getName() : "null",
                                value
                        )
                );
            }

            fieldLayoutItem = fieldCollectionsUtils.getFieldLayoutItem(issue, field);
            Object newValue = value;

            if (value instanceof IssueConstant) {
                newValue = ((IssueConstant) value).getName();
            } else if (value instanceof GenericValue) {
                final GenericValue gv = (GenericValue) value;

                if ("SchemeIssueSecurityLevels".equals(gv.getEntityName())) { // We got security level
                    newValue = gv.getString("name");
                }
            } else if (value instanceof Option && ! (cfType instanceof MultipleSettableCustomFieldType)) {
                newValue = ((Option) newValue).getValue();
            } else  if (value instanceof Timestamp && ! fieldCollectionsUtils.getAllDateFields().contains(field)) {
                String format = applicationProperties.getDefaultBackedString(APKeys.JIRA_DATE_TIME_PICKER_JAVA_FORMAT);
                DateFormat dateFormat = new SimpleDateFormat(format);
                newValue = dateFormat.format(value);
            }

            if (cfType instanceof VersionCFType) {
                newValue = convertValueToVersions(issue, newValue);
            } else if (cfType instanceof ProjectCFType) {
                Project p = convertValueToProject(newValue);
                if(p!=null) {
                    //JIRA 5.1 and 5.2, custom fields still expect GenericValue
                    newValue = p.getGenericValue();
                } else {
                    newValue = null;
                }
            } else if (cfType instanceof SelectCFType) {
                if(newValue!=null) {
                    SelectCFType selectCFType = (SelectCFType)cfType;
                    Options options = selectCFType.getOptions(customField.getRelevantConfig(issue),null);
                    newValue = options.getOptionForValue(newValue.toString(),null);
                }
            } else if (newValue instanceof String) {
                if (cfType instanceof MultipleSettableCustomFieldType) {
                    Option option = convertStringToOption(issue, customField, (String) newValue);
                    if (cfType instanceof MultiSelectCFType) {
                        newValue = asArrayList(option);
                    } else if (cfType instanceof CascadingSelectCFType) {
                        newValue = convertOptionToCustomFieldParamsImpl(customField, option);
                    } else {
                        newValue = option;
                    }
                } else if (cfType instanceof LabelsCFType) {
                    Set<String> set = convertToSetForLabels((String) newValue);
                    this.labelManager.setLabels(convertApplicationUserToCrowdEmbeddedUser(currentUser),issue.getId(),customField.getIdAsLong(),set,false,true);
                } else {
                    //convert from string to Object
                    CustomFieldParams fieldParams = new CustomFieldParamsImpl(customField, newValue);
                    newValue = cfType.getValueFromCustomFieldParams(fieldParams);
                }
            } else if (newValue instanceof Collection<?>) {
                if ((cfType instanceof AbstractMultiCFType) ||
                        (cfType instanceof MultipleCustomFieldType)) {
                    // format already correct
                } else if (cfType instanceof  LabelsCFType) {
                    Set<String> set = new HashSet<String>();
                    for(Object o:(Collection)newValue) {
                        set.add(o.toString());
                    }
                    this.labelManager.setLabels(convertApplicationUserToCrowdEmbeddedUser(currentUser),issue.getId(),customField.getIdAsLong(),set,false,true);
                } else {
                    //convert from string to Object
                    CustomFieldParams fieldParams = new CustomFieldParamsImpl(customField,convertToString(newValue));

                    newValue = cfType.getValueFromCustomFieldParams(fieldParams);
                }
            } else if (cfType instanceof UserCFType) {
                newValue = convertValueToUser(newValue);
            } else if (cfType instanceof LabelsCFType) {
                if (newValue == null) {
                    this.labelManager.setLabels(convertApplicationUserToCrowdEmbeddedUser(currentUser),issue.getId(),customField.getIdAsLong(),new HashSet<String>(),false,true);
              }else{
                    String stringValue = convertToString(newValue);
                    stringValue = stringValue.replaceAll(",", " ");
                    Set<String> set = convertToSetForLabels(stringValue);
                    this.labelManager.setLabels(convertApplicationUserToCrowdEmbeddedUser(currentUser),issue.getId(),customField.getIdAsLong(),set,false,true);
                }

            } else if (cfType instanceof AbstractMultiCFType) {
                if (cfType instanceof MultiUserCFType) {
                    newValue = convertValueToUser(newValue);
                }
                if (newValue != null) {
                    newValue = asArrayList(newValue);
                }
            } else if (newValue instanceof User && !(cfType instanceof UserCFType)) {
                newValue = ((User)newValue).getName();
            } else if (cfType instanceof GenericTextCFType) {
                if (newValue instanceof Project) {
                    newValue = ((Project)newValue).getKey();
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Got new value [class=" +
                        ((newValue != null) ? newValue.getClass().getName() : "null") +
                        ",value=" +
                        newValue +
                        "]"
                );
            }

            // Updating internal custom field value if it is not a label, which got handled before by the label manager
            if(!(cfType instanceof LabelsCFType)) {
                issue.setCustomFieldValue(customField, newValue);

                customField.updateValue(
                        fieldLayoutItem, issue,
                        new ModifiedValue(oldValue, newValue),	changeHolder
                );
            }

            if (log.isDebugEnabled()) {
                log.debug(
                        "Issue [" +
                        issue +
                        "] got modfied fields - [" +
                        issue.getModifiedFields() +
                        "]"
                );
            }

            // Not new
            if (issue.getKey() != null) {
                // Remove duplicated issue update
                if (issue.getModifiedFields().containsKey(field.getId())) {
                    issue.getModifiedFields().remove(field.getId());
                }
            }
        } else { //----- System Fields -----
            final String fieldId = field.getId();

            // Special treatment of fields.
            if (fieldId.equals(IssueFieldConstants.ATTACHMENT)) {
                throw new UnsupportedOperationException("Not implemented");
                //				// return a collection with the attachments associated to given issue.
                //				retCollection = (Collection)issue.getExternalFieldValue(fieldId);
                //				if(retCollection==null || retCollection.isEmpty()){
                //					isEmpty = true;
                //				}else{
                //					retVal = retCollection;
                //				}
            } else if (fieldId.equals(IssueFieldConstants.AFFECTED_VERSIONS)) {
                Collection<Version> versions = convertValueToVersions(issue, value);
                issue.setAffectedVersions(versions);
            } else if (fieldId.equals(IssueFieldConstants.COMMENT)) {
                throw new UnsupportedOperationException("Not implemented");

                //				// return a list with the comments of a given issue.
                //				try {
                //					retCollection = ManagerFactory.getIssueManager().getEntitiesByIssue(IssueRelationConstants.COMMENTS, issue.getGenericValue());
                //					if(retCollection==null || retCollection.isEmpty()){
                //						isEmpty = true;
                //					}else{
                //						retVal = retCollection;
                //					}
                //				} catch (GenericEntityException e) {
                //					retVal = null;
                //				}
            } else if (fieldId.equals(IssueFieldConstants.COMPONENTS)) {
                Collection<ProjectComponent> components = convertValueToComponents(issue, value);
                issue.setComponentObjects(components);
            } else if (fieldId.equals(IssueFieldConstants.FIX_FOR_VERSIONS)) {
                Collection<Version> versions = convertValueToVersions(issue, value);
                issue.setFixVersions(versions);
            } else if (fieldId.equals(IssueFieldConstants.THUMBNAIL)) {
                throw new UnsupportedOperationException("Not implemented");

                //				// Not implemented, yet.
                //				isEmpty = true;
            } else if (fieldId.equals(IssueFieldConstants.ISSUE_TYPE)) {
                throw new UnsupportedOperationException("Not implemented");
                //
                //				retVal = issue.getIssueTypeObject();
            } else if (fieldId.equals(IssueFieldConstants.TIMETRACKING)) {
                throw new UnsupportedOperationException("Not implemented");
                //
                //				// Not implemented, yet.
                //				isEmpty = true;
            } else if (fieldId.equals(IssueFieldConstants.ISSUE_LINKS)) {
                throw new UnsupportedOperationException("Not implemented");
                //
                //				retVal = ComponentManager.getInstance().getIssueLinkManager().getIssueLinks(issue.getId());
            } else if (fieldId.equals(IssueFieldConstants.WORKRATIO)) {
                throw new UnsupportedOperationException("Not implemented");
                //
                //				retVal = String.valueOf(WorkRatio.getWorkRatio(issue));
            } else if (fieldId.equals(IssueFieldConstants.ISSUE_KEY)) {
                throw new UnsupportedOperationException("Not implemented");
                //
                //				retVal = issue.getKey();
            } else if (fieldId.equals(IssueFieldConstants.SUBTASKS)) {
                throw new UnsupportedOperationException("Not implemented");
                //
                //				retCollection = issue.getSubTasks();
                //				if(retCollection==null || retCollection.isEmpty()){
                //					isEmpty = true;
                //				}else{
                //					retVal = retCollection;
                //				}
            } else if (fieldId.equals(IssueFieldConstants.PRIORITY)) {
                if (value == null) {
                    issue.setPriorityObject(null);
                } else {
                    Priority priority = convertValueToPriority(value);
                    if (priority != null) {
                        issue.setPriorityObject(priority);
                    } //else leave it untouched.
                }
            } else if (fieldId.equals(IssueFieldConstants.RESOLUTION)) {
                if (value == null) {
                    issue.setResolutionObject(null);
                } else if (value instanceof Resolution) {
                    issue.setResolutionId(((Resolution) value).getId());
                } else {
                    Collection<Resolution> resolutions = ComponentManager.getInstance().getConstantsManager().getResolutionObjects();
                    Resolution resolution = null;
                    String s = value.toString().trim();

                    for (Resolution r : resolutions) {
                        if (r.getName().equalsIgnoreCase(s)) {
                            resolution = r;

                            break;
                        }
                    }

                    if (resolution != null) {
                        issue.setResolutionId(resolution.getId());
                    } else {
                        throw new IllegalArgumentException("Unable to find resolution with name \"" + value + "\"");
                    }
                }
            } else if (fieldId.equals(IssueFieldConstants.STATUS)) {
                if (value == null) {
                    issue.setStatusObject(null);
                } else if (value instanceof Status) {
                    issue.setStatusId(((Status) value).getId());
                } else {
                    Status status = ComponentManager.getInstance().getConstantsManager().getStatusByName(value.toString());

                    if (status != null) {
                        issue.setStatusObject(status);
                    } else {
                        throw new IllegalArgumentException("Unable to find status with name \"" + value + "\"");
                    }
                }
            } else if (fieldId.equals(IssueFieldConstants.SECURITY)) {
                if (value == null) {
                    issue.setSecurityLevelId(null);
                } else if (value instanceof Long) {
                    issue.setSecurityLevelId((Long) value);
                } else {
                    Collection<IssueSecurityLevel> levels;
                    levels = issueSecurityLevelManager.getIssueSecurityLevelsByName(value.toString());

                    if (levels == null) {
                        throw new IllegalArgumentException("Unable to find security level \"" + value + "\"");
                    }

                    if (levels.size() > 1) {
                        throw new IllegalArgumentException("More that one security level with name \"" + value + "\"");
                    }

                    issue.setSecurityLevelId(levels.iterator().next().getId());
                }
            } else if (fieldId.equals(IssueFieldConstants.ASSIGNEE)) {
                User user = convertApplicationUserToCrowdEmbeddedUser(convertValueToUser(value));
                issue.setAssignee(user);
            } else if (fieldId.equals(IssueFieldConstants.DUE_DATE)) {
                if (value == null) {
                    issue.setDueDate(null);
                }

                if (value instanceof Timestamp) {
                    issue.setDueDate((Timestamp) value);
                } else if (value instanceof String) {
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            applicationProperties.getDefaultString(APKeys.JIRA_DATE_TIME_PICKER_JAVA_FORMAT)
                    );

                    try {
                        Date date = formatter.parse((String) value);

                        if (date != null) {
                            issue.setDueDate(new Timestamp(date.getTime()));
                        } else {
                            issue.setDueDate(null);
                        }
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Wrong date format exception for \"" + value + "\"");
                    }
                }
            } else if (fieldId.equals(IssueFieldConstants.REPORTER)) {
                User user =  convertApplicationUserToCrowdEmbeddedUser(convertValueToUser(value));
                issue.setReporter(user);
            } else if (fieldId.equals(IssueFieldConstants.SUMMARY)) {
                if ((value == null) || (value instanceof String)) {
                    issue.setSummary((String) value);
                } else {
                    issue.setSummary(value.toString());
                }
            } else if (fieldId.equals(IssueFieldConstants.DESCRIPTION)) {
                if ((value == null) || (value instanceof String)) {
                    issue.setDescription((String) value);
                } else {
                    issue.setDescription(convertToString(value));
//                    issue.setDescription(value.toString());
                }
            } else if (fieldId.equals(IssueFieldConstants.ENVIRONMENT)) {
                if ((value == null) || (value instanceof String)) {
                    issue.setEnvironment((String) value);
                } else {
                    issue.setEnvironment(value.toString());
                }
            } else {
                log.error("Issue field \"" + fieldId + "\" is not supported for setting.");
            }
        }
    }

    private Set<String> convertToSetForLabels(String newValue) {
        Set<String> set = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(newValue," ");
        while(st.hasMoreTokens()) {
            set.add(st.nextToken());
        }
        return set;
    }

    private static final ConverterString CONVERTER_STRING = new ConverterString();
    public String convertToString(Object value) {
        if (value instanceof Collection) {
            List list = (List) value;
            List<Object> resultList = new ArrayList<Object>();
            for (Object object : list) {
                object = convertToString(object);
                resultList.add(object);
            }
            return StringUtils.join((Collection<?>) resultList, ",");
        } else {
            return CONVERTER_STRING.convert(value);
        }
    }

    private Option convertStringToOption(Issue issue, CustomField customField, String value) {
        FieldConfig relevantConfig = customField.getRelevantConfig(issue);
        List<Option> options = optionsManager.findByOptionValue(value);
        if (options.size() == 0) {
            try {
                Long optionId = Long.parseLong(value);
                Option option = optionsManager.findByOptionId(optionId);
                options = Collections.singletonList(option);
            } catch (NumberFormatException e) { /* IllegalArgumentException will be thrown at end of this method. */ }
        }
        for (Option option : options) {
            FieldConfig fieldConfig = option.getRelatedCustomField();
            if (relevantConfig != null && relevantConfig.equals(fieldConfig)) {
                return option;
            }
        }
        throw new IllegalArgumentException("No option found with value '" + value + "' for custom field " + customField.getName() + " on issue " + issue.getKey() + ".");
    }

    private Collection<ProjectComponent> convertValueToComponents(Issue issue, Object value) {
        if (value == null) {
            return Collections.<ProjectComponent>emptySet();
        } else if (value instanceof ProjectComponent) {
            return Arrays.asList((ProjectComponent) value);
        } else if (value instanceof Collection) {
            return (Collection<ProjectComponent>) value;
        } else {
            ProjectComponent v = projectComponentManager.findByComponentName(
                    issue.getProjectObject().getId(), convertToString(value)
            );

            if (v != null) {
                return Arrays.asList(v);
            }
            throw new IllegalArgumentException("Wrong component value '" + value + "'.");
        }
    }

    private Collection<Version> convertValueToVersions(Issue issue, Object value) {
        if (value == null) {
            return Collections.emptySet();
        } else if (value instanceof Version) {
            return (Arrays.asList((Version) value));
        } else if (value instanceof Collection) {
            return (Collection<Version>) value;
        } else {
            Version v = versionManager.getVersion(issue.getProjectObject().getId(), convertToString(value));
            if (v != null) {
                return Arrays.asList(v);
            }
            throw new IllegalArgumentException("Wrong version value '" + value + "'.");
        }
    }

    private ApplicationUser convertValueToUser(Object value) {
        if (value instanceof Collection<?>) {
            value = firstValue((Collection) value);
        }
        if (value == null || value instanceof ApplicationUser) {
            return (ApplicationUser)value;
        } else {
            ApplicationUser user = userManager.getUserByName(convertToString(value));
            if (user != null) {
                return user;
            }
            throw new IllegalArgumentException("User '" + value + "' not found.");
        }
    }

    private Project convertValueToProject(Object value) {
        Project project;
        if (value == null || value instanceof Project) {
            return (Project) value;
        } else if (value instanceof GenericValue) {
            value = ((GenericValue) value).get("id");
        }

        if (value instanceof Long) {
            project = projectManager.getProjectObj((Long) value);
            if (project != null) return project;
        } else {
            String s = convertToString(value);
            try {
                Long id = Long.parseLong(s);
                project = projectManager.getProjectObj(id);
                if (project != null) return project;
            } catch (NumberFormatException e) {
                project = projectManager.getProjectObjByKey(s);
                if (project == null) {
                    project = projectManager.getProjectObjByName(s);
                }
                if (project != null) return project;
            }
        }
        throw new IllegalArgumentException("Wrong project value '" + value + "'.");
    }

    private Priority convertValueToPriority(Object value) {
        if (value == null) {
            return null;
        }

        Priority priority;
        if (value instanceof Priority) {
            return (Priority) value;
        } else {
            String priorityAsString = value.toString();
            priority = priorityManager.getPriority(priorityAsString);
            if (priority == null) {
                for (Priority searchPriority : priorityManager.getPriorities()) {
                    if (priorityAsString.equals(searchPriority.getName())) {
                        priority = searchPriority;
                        break;
                    }
                }
            }
            if (priority == null) {
                String defaultLocale = applicationProperties.getDefaultLocale().toString();
                for (Priority searchPriority : priorityManager.getPriorities()) {
                    if (priorityAsString.equals(searchPriority.getNameTranslation(defaultLocale))) {
                        priority = searchPriority;
                        break;
                    }
                }
            }
        }
        return priority;
    }

    private CustomFieldParamsImpl convertOptionToCustomFieldParamsImpl(CustomField customField, Option option) {
        CustomFieldParamsImpl params = new CustomFieldParamsImpl(customField);
        Option upperOption = option.getParentOption();
        Collection<String> val;
        if (upperOption != null) {
            val = asArrayList(upperOption.getOptionId().toString());
            params.put(CascadingSelectCFType.PARENT_KEY, val);
            val = asArrayList(option.getOptionId().toString());
            params.put(CascadingSelectCFType.CHILD_KEY, val);
        } else {
            val = asArrayList(option.getOptionId().toString());
            params.put(CascadingSelectCFType.PARENT_KEY, val);
        }
        params.transformStringsToObjects();
        return params;
    }


    /**
     *This ist deprecated because Atlassian API is not working with ApplicationUser
     * As soon as this is working this method can be deleted
     * @param applicationUser
     * @return
     */
    @Deprecated
    private User convertApplicationUserToCrowdEmbeddedUser(ApplicationUser applicationUser){
        return applicationUser == null ? null : applicationUser.getDirectoryUser();
    }

    private <T> ArrayList<T> asArrayList(T value) {
        ArrayList<T> list = new ArrayList<T>(1);
        list.add(value);
        return list;
    }

    private Object firstValue(Collection col) {
        int s = col.size();
        if (s == 0) {
            return null;
        } else {
            if (s > 1) {
                log.debug("Got multiple values: " + col.toString() + ". Using only one of them.");
            }
            return col.iterator().next();
        }
    }

    /**
     * Method sets value for issue field. Field was defined as string
     *
     * @param currentUser
     *            Current user.
     * @param issue
     *            Muttable issue for changing
     * @param fieldKey
     *            Field name
     * @param value
     *            Value for setting
     */
    public void setFieldValue(
            ApplicationUser currentUser, MutableIssue issue, String fieldKey, Object value,
            IssueChangeHolder changeHolder
    ) {
        final Field field = getFieldFromKey(fieldKey);

        setFieldValue(currentUser, issue, field, value, changeHolder);
    }

    /**
     * @param strGroups
     * @param splitter
     * @return a List of Group
     *
     * Get Groups from a string.
     *
     */
    public List<Group> getGroups(String strGroups, String splitter) {
        String[] groups = strGroups.split("\\Q" + splitter + "\\E");
        List<Group> groupList = new ArrayList<Group>(groups.length);

        for (String s : groups) {
            Group group = crowdService.getGroup(s);
            groupList.add(group);
        }

        return groupList;
    }

    /**
     * @param groups
     * @param splitter
     * @return a String with the groups selected.
     *
     * Get Groups as String.
     *
     */
    public String getStringGroup(Collection<Group> groups, String splitter) {
        StringBuilder sb = new StringBuilder();

        for (Group g : groups) {
            sb.append(g.getName()).append(splitter);
        }

        return sb.toString();
    }

    /**
     * @param strFields
     * @param splitter
     * @return a List of Field
     *
     * Get Fields from a string.
     *
     */
    public List<Field> getFields(String strFields, String splitter) {
        String[] fields = strFields.split("\\Q" + splitter + "\\E");
        List<Field> fieldList = new ArrayList<Field>(fields.length);

        for (String s : fields) {
            final Field field = fieldManager.getField(s);

            if (field != null) {
                fieldList.add(field);
            }
        }

        return fieldCollectionsUtils.sortFields(fieldList);
    }

    /**
     * @param fields
     * @param splitter
     * @return a String with the fields selected.
     *
     * Get Fields as String.
     *
     */
    public String getStringField(Collection<Field> fields, String splitter) {
        StringBuilder sb = new StringBuilder();

        for (Field f : fields) {
            sb.append(f.getId()).append(splitter);
        }

        return sb.toString();
    }

    /**
     * @param actionDescriptor
     * @return the FieldScreen of the transition. Or null, if the transition
     *         hasn't a screen asociated.
     *
     * It obtains the fieldscreen for a transition, if it have one.
     *
     */
    public FieldScreen getFieldScreen(ActionDescriptor actionDescriptor) {
        return workflowActionsBean.getFieldScreenForView(actionDescriptor);
    }
}

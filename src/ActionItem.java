package com.smartbear.collaborator.qa.entities;

import com.smartbear.collaborator.qa.entities.user.StandardUserRole;
import com.smartbear.collaborator.qa.json.api.commands.user.data.ActionItemData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionItem {
    private static final String REVIEW_NAME_CAPTURING = "Review #[0-9]+: \"(.*?)\"$";
    private ActionItemData actionItemData;

    public ActionItem() {
        this.actionItemData = new ActionItemData();
    }

    public ActionItem(ActionItemData actionItemData) {
        this.actionItemData = actionItemData;
    }

    public String getReviewText() {
        return this.actionItemData.getReviewText();
    }

    public String getReviewTitle() throws Exception {
        Pattern pattern = Pattern.compile(REVIEW_NAME_CAPTURING);
        Matcher matcher = pattern.matcher(this.getReviewText());
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new Exception("can't parse review name from action item text");
        }
    }

    public ActionItemProgressValues getProgress() {
        String progress = this.actionItemData.getNextActionText();
        return ActionItemProgressValues.fromValue(progress);
    }

    public StandardUserRole getRole() throws Exception {
        String role = this.actionItemData.getRoleText();
        return StandardUserRole.fromValue(role);
    }

    public String getRelativeUrl() {
        return this.actionItemData.getRelativeUrl();
    }

    public int getReviewId() {
        return this.actionItemData.getReviewId();
    }
     /*"text" : "Finish creating: ReviewSummaryData #1: \"Untitled ReviewSummaryData\"",
    "nextActionText" : "Finish creating",
    "relativeUrl" : "go?page=ReviewDisplay&reviewid=1",
    "reviewText" : "ReviewSummaryData #1: \"Untitled ReviewSummaryData\"",
    "roleText" : "",
    "reviewId" : 1,
    "reviewNeedsCommit" : false,
    "requiresUserAction" : true*/
}

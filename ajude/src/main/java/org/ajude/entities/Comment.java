package org.ajude.entities;

public class Comment {

    private String comment;
    private String ownerEmail;
    private Comment answer;
    private Long campaignID;

    public Comment() {
    }

    public Comment(String comment, String ownerEmail, Comment answer, Long campaignID) {
        this.comment = comment;
        this.ownerEmail = ownerEmail;
        this.answer = answer;
        this.campaignID = campaignID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Comment getAnswer() {
        return answer;
    }

    public void setAnswer(Comment answer) {
        this.answer = answer;
    }

    public Long getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(Long campaignID) {
        this.campaignID = campaignID;
    }
}

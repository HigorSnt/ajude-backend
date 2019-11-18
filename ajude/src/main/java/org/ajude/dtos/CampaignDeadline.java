package org.ajude.dtos;

import java.util.Date;

public class CampaignDeadline {

    private Date deadline;

    public CampaignDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public CampaignDeadline() {
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}

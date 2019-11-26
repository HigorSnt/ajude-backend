package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.ajude.dtos.CampaignDTO;
import org.ajude.dtos.UserNameEmail;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class Donation {

    @Id
    @GeneratedValue
    private Long id;
    private Double value;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idCampaign")
    @JsonIgnore
    private Campaign campaign;

    @JsonIgnore
    private ZonedDateTime date;


    public Donation(Double value, Campaign campaign, ZonedDateTime date) {
        this.value = value;
        this.campaign = campaign;
        this.date = date;
    }

    public Donation(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public CampaignDTO getCampaignTarget(){
        return new CampaignDTO(this.campaign.getShortName(), this.campaign.getUrlIdentifier(),
                campaign.getDescription(), this.campaign.getDeadline(),
                campaign.getRegisterDateTime(),campaign.getStatus(), campaign.getGoal(),
                campaign.getReceived(), campaign.getNumLikes(), campaign.getNumDislikes());
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getDonationDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.date.minusHours(3).format(formatter);
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}

package org.ajude.dtos;

import java.util.List;

public class UserProfile {

    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private List<CampaignDTO> campaignList;

    public UserProfile(String email, String firstName, String lastName, String username, List<CampaignDTO> campaignList) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.campaignList = campaignList;
    }

    public List<CampaignDTO> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<CampaignDTO> campaignList) {
        this.campaignList = campaignList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

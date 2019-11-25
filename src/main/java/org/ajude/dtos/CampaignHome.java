package org.ajude.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.ajude.utils.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
public class CampaignHome {

    private String shortName;
    private String urlIdentifier;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    private Status status;
    private double goal;
    private double remaining;
    private int likes;
    private int deslikes;

    public CampaignHome(String shortName, String urlIdentifier, String description, Date deadline,
                        Status status, double goal, double remaining, int likes, int deslikes) {
        this.shortName = shortName;
        this.urlIdentifier = urlIdentifier;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.goal = goal;
        this.remaining = remaining;
        this.likes = likes;
        this.deslikes = deslikes;
    }
}

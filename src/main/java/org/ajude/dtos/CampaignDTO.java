package org.ajude.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.ajude.utils.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
public class CampaignDTO {

    private String shortName;
    private String urlIdentifier;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @Getter(AccessLevel.NONE)
    private ZonedDateTime registerDateTime;
    private Status status;
    private double goal;
    private double remaining;
    private int likes;
    private int dislikes;

    public CampaignDTO(String shortName, String urlIdentifier, String description, Date deadline, ZonedDateTime registerDateTime,
                       Status status, double goal, double remaining, int likes, int dislikes) {
        this.shortName = shortName;
        this.urlIdentifier = urlIdentifier;
        this.description = description;
        this.deadline = deadline;
        this.registerDateTime = registerDateTime;
        this.status = status;
        this.goal = goal;
        this.remaining = remaining;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public String getRegisterDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.registerDateTime.minusHours(3).format(formatter);
    }
}

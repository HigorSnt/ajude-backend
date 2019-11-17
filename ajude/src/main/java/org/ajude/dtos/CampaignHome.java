package org.ajude.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ajude.utils.Status;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class CampaignHome {

    private String shortName;
    private String urlIdentifier;
    private String description;
    private Date deadline;
    private Status status;
    private double goal;
    private double remaining;
}

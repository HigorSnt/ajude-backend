package org.ajude.services;

import org.ajude.entities.campaigns.Campaign;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.repositories.CampaignRepository;
import org.ajude.utils.Status;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class CampaignService {
    private CampaignRepository<Campaign, Long> campaignRepository;

    public CampaignService(CampaignRepository<Campaign, Long> campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign register(Campaign campaign) throws InvalidDateException {

        if (campaign.getDeadline().before(Date.from(Instant.now()))){
            throw new InvalidDateException();
        }

        campaign.setStatus(Status.A);
        this.campaignRepository.save(campaign);

        return campaign;
    }

    public Campaign getCampaign(String urlIdentifier) {
        Campaign campaign = this.campaignRepository.findByUrlIdentifier(urlIdentifier);

        if (campaign != null) {
            return campaign;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
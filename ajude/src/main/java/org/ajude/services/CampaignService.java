package org.ajude.services;

import org.ajude.entities.campaigns.Campaign;
import org.ajude.repositories.CampaignRepository;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {
    private CampaignRepository<Campaign, Long> campaignRepository;

    public CampaignService(CampaignRepository<Campaign, Long> campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign register(Campaign campaign) {
        this.campaignRepository.save(campaign);
        return campaign;
    }
}

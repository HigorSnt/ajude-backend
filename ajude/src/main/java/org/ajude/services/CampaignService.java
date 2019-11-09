package org.ajude.services;

import org.ajude.entities.Campaign;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.repositories.CampaignRepository;
import org.ajude.utils.Status;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CampaignService {
    private CampaignRepository<Campaign, Long> campaignRepository;

    public CampaignService(CampaignRepository<Campaign, Long> campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign register(Campaign campaign) throws InvalidDateException {

        if (campaign.getDeadline().before(Date.from(Instant.now()))) {
            throw new InvalidDateException();
        }

        campaign.setStatus(Status.A);
        this.campaignRepository.save(campaign);

        return campaign;
    }

    public Campaign getCampaign(String urlIdentifier) throws NotFoundException {
        Campaign campaign = this.campaignRepository.findByUrlIdentifier(urlIdentifier);

        if (campaign != null) {
            campaign.verifyDeadline();
            this.campaignRepository.save(campaign);

            return campaign;
        } else {
            throw new NotFoundException();
        }
    }

    public List<Campaign> searchCampaigns(String substring, String status) {
        List<Campaign> result = new ArrayList<>();
        //List<Campaign> campaigns = this.campaignRepository.findByShortNameContainingIgnoreCase(substring, status);

        /*for (Campaign c : campaigns) {
            c.verifyDeadline();
            this.campaignRepository.save(c);

            if (!c.getStatus().equals(Status.valueOf(status))) {
                campaigns.remove(c);
            }
        }
    */
        return result;
    }

    public Campaign closeCampaign(String campaignUrl, String userEmail)
            throws UnauthorizedException, NotFoundException {

        Campaign campaign = this.getCampaign(campaignUrl);

        if (!campaign.getOwnerEmail().equals(userEmail)) {
            throw new UnauthorizedException();
        }

        if (campaign.getStatus().equals(Status.A)) {
            campaign.setStatus(Status.C);
            campaignRepository.saveAndFlush(campaign);
        }

        return campaign;
    }
}
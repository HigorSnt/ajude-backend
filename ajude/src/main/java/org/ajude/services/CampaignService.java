package org.ajude.services;

import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.entities.Campaign;
import org.ajude.entities.Comment;
import org.ajude.exceptions.CommentNotFoundException;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.InvalidGoalException;
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

    public Campaign register(Campaign campaign) throws InvalidDateException, InvalidGoalException {

        verifyDate(campaign.getDeadline());
        verifyGoal(campaign.getGoal());
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

        verifyIfIsOwner(userEmail, campaign);

        if (campaign.getStatus().equals(Status.A)) {
            campaign.setStatus(Status.C);
            campaignRepository.saveAndFlush(campaign);
        }

        return campaign;
    }

    public Comment addCampaignComment(String campaignUrl, Comment comment) {
        Campaign campaign = this.campaignRepository.findByUrlIdentifier(campaignUrl);

        Comment c = campaign.addComment(comment);
        this.campaignRepository.save(campaign);

        return c;
    }

    public Comment addCommentResponse(String campaignUrl, Long commentId, Comment reply) throws CommentNotFoundException {
        Campaign campaign = this.campaignRepository.findByUrlIdentifier(campaignUrl);

        Comment c = campaign.addCommentResponse(commentId, reply);

        this.campaignRepository.save(campaign);

        return c;
    }

    public Campaign setDeadline(String campaignUrl, CampaignDeadline newDeadline, String userEmail)
            throws NotFoundException, UnauthorizedException, InvalidDateException {

        Campaign campaign = this.getCampaign(campaignUrl);
        verifyIfIsOwner(userEmail, campaign);

        verifyDate(newDeadline.getDeadline());

        campaign.setDeadline(newDeadline.getDeadline());
        this.campaignRepository.saveAndFlush(campaign);
        return campaign;
    }

    public Campaign setGoal(String campaignUrl, CampaignGoal newGoal, String userEmail)
            throws NotFoundException, UnauthorizedException, InvalidDateException, InvalidGoalException {

        Campaign campaign = this.getCampaign(campaignUrl);
        verifyIfIsOwner(userEmail, campaign);
        verifyDate(campaign.getDeadline());
        verifyGoal(newGoal.getGoal());

        campaign.setGoal(newGoal.getGoal());
        this.campaignRepository.saveAndFlush(campaign);
        return campaign;
    }

    private void verifyGoal(double goal) throws InvalidGoalException {
        if (goal <= 0){
            throw new InvalidGoalException();
        }
    }

    private void verifyDate(Date deadline) throws InvalidDateException {
        if (deadline.before(Date.from(Instant.now()))) {
            throw new InvalidDateException();
        }
    }

    private void verifyIfIsOwner(String userEmail, Campaign campaign) throws UnauthorizedException {
        if (!campaign.getOwner().getEmail().equals(userEmail)) {
            throw new UnauthorizedException();
        }
    }
}
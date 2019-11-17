package org.ajude.services;

import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.dtos.CampaignHome;
import org.ajude.dtos.DonationDateValue;
import org.ajude.entities.*;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.InvalidGoalException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.repositories.CampaignRepository;
import org.ajude.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {
    
    private CampaignRepository<Campaign, Long> campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository<Campaign, Long> campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign register(Campaign campaign) throws InvalidDateException, InvalidGoalException {

        verifyDate(campaign.getDeadline());
        verifyGoal(campaign.getGoal());

        campaign.setRemaining(campaign.getGoal());
        campaign.setStatus(Status.A);
        this.campaignRepository.save(campaign);

        return campaign;
    }

    public Campaign getCampaign(String urlIdentifier) throws NotFoundException {

        Optional<Campaign> campaign = this.campaignRepository.findByUrlIdentifier(urlIdentifier);

        if (!campaign.isEmpty()) {
            return campaign.get();
        } else {
            throw new NotFoundException("The Campaign " + urlIdentifier + " was not found");
        }
    }

    public List<Campaign> searchCampaigns(String substring, Status status) {
        List<Campaign> campaigns = this.campaignRepository.findByShortNameContainingIgnoreCaseAndStatus(substring,
                status);

        return campaigns;
    }

    public Campaign closeCampaign(String campaignUrl, String userEmail)
            throws UnauthorizedException, NotFoundException {

        Campaign campaign = this.getCampaign(campaignUrl);

        verifyIfIsOwner(userEmail, campaign);

        if (campaign.getStatus().equals(Status.A)) {
            campaign.setStatus(Status.C);
            this.campaignRepository.saveAndFlush(campaign);
        }

        return campaign;
    }

    public Comment addCampaignComment(String campaignUrl, Comment comment) throws NotFoundException {
        Optional<Campaign> campaignOptional = this.campaignRepository.findByUrlIdentifier(campaignUrl);

        if (!campaignOptional.isEmpty()) {
            Campaign campaign = campaignOptional.get();
            campaign.addComment(comment);
            campaign = this.campaignRepository.save(campaign);

            return campaign.lastCommentAdded();

        } else {
            throw new NotFoundException("The Campaign " + campaignUrl + " was not found");
        }
    }

    public Comment addCommentResponse(String campaignUrl, Long commentId, Comment reply) throws NotFoundException {
        Optional<Campaign> campaignOptional = this.campaignRepository.findByUrlIdentifier(campaignUrl);
        if (!campaignOptional.isEmpty()) {
            Campaign campaign = campaignOptional.get();
            Comment comment = campaign.addCommentResponse(commentId, reply);

            this.campaignRepository.save(campaign);

            return comment;
        } else {
            throw new NotFoundException("The Campaign " + campaignUrl + " was not found");
        }
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
        if (goal <= 0) {
            throw new InvalidGoalException("Goal cannot be zero or less");
        }
    }

    private void verifyDate(Date deadline) throws InvalidDateException {
        if (deadline.before(Date.from(Instant.now()))) {
            throw new InvalidDateException("Date needs to be in the future");
        }
    }

    private void verifyIfIsOwner(String userEmail, Campaign campaign)
            throws UnauthorizedException {

        if (!campaign.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("User is not the owner of this campaign");
        }
    }

    public Campaign deleteComment(String campaignUrl, User owner, Long commentId)
            throws UnauthorizedException, NotFoundException {

        Campaign campaign = this.getCampaign(campaignUrl);

        campaign.deleteComment(owner, commentId);
        this.campaignRepository.save(campaign);

        return campaign;
    }

    public Campaign donate(String campaignURL, User user,
                           DonationDateValue donationDTO) throws NotFoundException {

        Donation donation = new Donation(donationDTO.getValue(), user, donationDTO.getDate());
        Campaign campaign = this.getCampaign(campaignURL);

        campaign.addDonation(donation);
        this.campaignRepository.save(campaign);
        return campaign;
    }

    public List<CampaignHome> getCampaignHomeByGoal() {
        List<Campaign> campaigns = this.campaignRepository.findTop5ByStatusOrderByRemainingDesc(Status.A);

        return transformCampaignsToCampaignsHome(campaigns);
    }

    public List<CampaignHome> getCampaignHomeByDate() {
        return new ArrayList();
    }

    public List<CampaignHome> getCampaignHomeByLike() {
        return new ArrayList();
    }

    private List<CampaignHome> transformCampaignsToCampaignsHome(List<Campaign> campaigns) {
        List<CampaignHome> homeList = new ArrayList<>();

        campaigns.forEach(campaign -> {
            homeList.add(new CampaignHome(campaign.getShortName(),
                    campaign.getUrlIdentifier(), campaign.getDescription(),
                    campaign.getDeadline(), campaign.getStatus(),
                    campaign.getGoal(), campaign.getRemaining()));
        });

        return homeList;
    }
    public Like addLike(String campaignUrl, Like like) throws NotFoundException {
        Campaign campaign = this.getCampaign(campaignUrl);
        Like result = campaign.addLike(like);
        campaignRepository.save(campaign);
        return result;
    }

    public Dislike addDislike(String campaignUrl, Dislike dislike) throws NotFoundException {
        Campaign campaign = this.getCampaign(campaignUrl);
        Dislike result = campaign.addDislike(dislike);
        campaignRepository.save(campaign);
        return result;
    }
}
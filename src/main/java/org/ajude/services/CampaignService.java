package org.ajude.services;

import org.ajude.dtos.CampaignRegister;
import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.dtos.CampaignDTO;
import org.ajude.entities.*;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.InvalidValueException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.repositories.CampaignRepository;
import org.ajude.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public CampaignRegister register(CampaignRegister campaignRegister) throws InvalidDateException, InvalidValueException {

        verifyDate(campaignRegister.getDeadline());
        verifyValue(campaignRegister.getGoal());

        Campaign campaign = new Campaign(campaignRegister.getShortName(),
                campaignRegister.getDescription(), campaignRegister.getUrlIdentifier(),
                campaignRegister.getDeadline(), campaignRegister.getGoal(), campaignRegister.getOwner());

        campaign.setReceived(0.0);
        campaign.setStatus(Status.A);
        campaign.setRegisterDateTime(ZonedDateTime.now(ZoneId.of("UTC")));
        this.campaignRepository.save(campaign);

        return campaignRegister;
    }

    public Campaign getCampaign(String urlIdentifier) throws NotFoundException {

        Optional<Campaign> campaign = this.campaignRepository.findByUrlIdentifier(urlIdentifier);

        if (!campaign.isEmpty()) {
            Campaign c = campaign.get();
            c.getComments();
            c.getLikeList();

            return c;
        } else {
            throw new NotFoundException("The Campaign " + urlIdentifier + " was not found");
        }
    }

    public List<Campaign> searchCampaigns(String substring) {
        List<Campaign> campaigns = this.campaignRepository.findByShortNameContainingIgnoreCase(substring);

        for (Campaign c : campaigns) {
            c.getComments();
            c.getLikeList();
        }

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
        Campaign campaign = this.getCampaign(campaignUrl);
        comment.setPostedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        System.out.println(comment.getPostedAt());
        campaign.addComment(comment);
        campaign.getComments();
        campaign.getLikeList();

        campaign = this.campaignRepository.save(campaign);
        return campaign.lastCommentAdded();
    }

    public Comment addCommentResponse(String campaignUrl, Long commentId, Comment reply) throws NotFoundException {
        Campaign campaign = this.getCampaign(campaignUrl);
        reply.setPostedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        System.out.println(reply.getPostedAt());
        Comment comment = campaign.addCommentResponse(commentId, reply);

        this.campaignRepository.save(campaign);
        return comment;
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
            throws NotFoundException, UnauthorizedException, InvalidDateException, InvalidValueException {

        Campaign campaign = this.getCampaign(campaignUrl);
        verifyIfIsOwner(userEmail, campaign);
        verifyDate(campaign.getDeadline());
        verifyValue(newGoal.getGoal());

        campaign.setGoal(newGoal.getGoal());
        this.campaignRepository.saveAndFlush(campaign);

        campaign.getLikeList();
        campaign.getComments();

        return campaign;
    }

    private void verifyValue(double v) throws InvalidValueException {
        if (v <= 0) {
            throw new InvalidValueException("Value cannot be zero or less");
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

    public Donation donate(String campaignURL, User user,
                           Double value) throws NotFoundException, InvalidValueException {

        verifyValue(value);
        Campaign campaign = this.getCampaign(campaignURL);
        Donation donation = new Donation(value, campaign, ZonedDateTime.now(ZoneId.of("UTC")));
        campaign.addDonatio(value);
        user.addDonation(donation);
        this.campaignRepository.save(campaign);
        return donation;
    }

    public List<CampaignDTO> getCampaignHomeByReceived() {
        List<Campaign> campaigns = this.campaignRepository.findTop5ByStatusOrderByReceivedDesc(Status.A);

        return transformCampaignsToCampaignsHome(campaigns);
    }

    public List<CampaignDTO> getCampaignHomeByDate() {
        List<Campaign> campaigns = this.campaignRepository.findTop5ByStatusOrderByDeadlineAsc(Status.A);

        return transformCampaignsToCampaignsHome(campaigns);
    }

    public List<CampaignDTO> getCampaignHomeByLike() {
        List<Campaign> campaigns = this.campaignRepository.findTop5ByStatusOrderByLikes();

        return transformCampaignsToCampaignsHome(campaigns);
    }

    private List<CampaignDTO> transformCampaignsToCampaignsHome(List<Campaign> campaigns) {
        List<CampaignDTO> homeList = new ArrayList<>();

        campaigns.forEach(campaign -> {
            homeList.add(new CampaignDTO(
                    campaign.getShortName(), campaign.getUrlIdentifier(),
                    campaign.getDescription(), campaign.getDeadline(), campaign.getRegisterDateTime(),
                    campaign.getStatus(), campaign.getGoal(), campaign.getReceived(),
                    campaign.getNumLikes(), campaign.getNumDislikes()));
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
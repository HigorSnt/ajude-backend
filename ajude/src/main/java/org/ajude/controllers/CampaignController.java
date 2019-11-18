package org.ajude.controllers;

import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.dtos.CampaignHome;
import org.ajude.dtos.DonationDateValue;
import org.ajude.entities.Campaign;
import org.ajude.entities.Dislike;
import org.ajude.entities.Like;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.InvalidGoalException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.ajude.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CampaignController {

    private CampaignService campaignService;
    private UserService userService;
    private JwtService jwtService;

    @Autowired
    public CampaignController(CampaignService campaignService, UserService userService, JwtService jwtService) {
        this.campaignService = campaignService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/home/remaining")
    public ResponseEntity<List<CampaignHome>> getCampaignHomeByRemaining() {
        return new ResponseEntity(this.campaignService.getCampaignHomeByRemaining(), HttpStatus.OK);
    }

    @GetMapping("/home/date")
    public ResponseEntity<List<CampaignHome>> getCampaignHomeByDate() {
        return new ResponseEntity(this.campaignService.getCampaignHomeByDate(), HttpStatus.OK);
    }

    @GetMapping("/home/like")
    public ResponseEntity<List<CampaignHome>> getCampaignHomeByLike() {
        return new ResponseEntity(this.campaignService.getCampaignHomeByLike(), HttpStatus.OK);
    }

    @PostMapping("/campaign/register")
    public ResponseEntity registerCampaign(@RequestHeader("Authorization") String header,
                                           @Valid @RequestBody Campaign campaign)
            throws ServletException, InvalidGoalException, InvalidDateException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        campaign.setOwner(this.userService.getUserByEmail(userEmail).get());
        return new ResponseEntity(this.campaignService.register(campaign), HttpStatus.CREATED);
    }

    @GetMapping("/campaign/{campaignUrl}")
    public ResponseEntity getCampaign(@PathVariable String campaignUrl) throws NotFoundException {
        return new ResponseEntity(this.campaignService.getCampaign(campaignUrl), HttpStatus.OK);
    }

    @GetMapping("/campaign/search/{substring}")
    public ResponseEntity<List<Campaign>> searchCampaigns(@PathVariable String substring) {
        return new ResponseEntity(this.campaignService.searchCampaigns(substring, Status.valueOf("A")), HttpStatus.OK);
    }

    @GetMapping("/campaign/search/{substring}/{status}")
    public ResponseEntity<List<Campaign>> searchCampaigns(@PathVariable("substring") String substring,
                                                          @PathVariable("status") String status) {
        return new ResponseEntity(this.campaignService.searchCampaigns(substring, Status.valueOf(status)), HttpStatus.OK);
    }

    @PutMapping("/campaign/{campaignUrl}/closeCampaign")
    public ResponseEntity closeCampaign(@RequestHeader("Authorization") String header,
                                        @PathVariable("campaignUrl") String campaignUrl)
            throws ServletException, UnauthorizedException, NotFoundException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        return new ResponseEntity(this.campaignService.closeCampaign(campaignUrl, userEmail), HttpStatus.OK);
    }

    @PutMapping("/campaign/{campaignUrl}/setDeadline")
    public ResponseEntity setDeadline(@RequestHeader("Authorization") String header,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignDeadline newDeadline)
            throws InvalidDateException, UnauthorizedException, NotFoundException, ServletException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        return new ResponseEntity(this.campaignService.setDeadline(campaignUrl, newDeadline, userEmail), HttpStatus.OK);
    }

    @PutMapping("/campaign/{campaignUrl}/setGoal")
    public ResponseEntity setGoal(@RequestHeader("Authorization") String header,
                                  @PathVariable("campaignUrl") String campaignUrl,
                                  @RequestBody CampaignGoal newGoal)
            throws ServletException, UnauthorizedException, InvalidDateException, NotFoundException, InvalidGoalException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        return new ResponseEntity(this.campaignService.setGoal(campaignUrl, newGoal, userEmail), HttpStatus.OK);
    }


    @PostMapping("/campaign/{campaignUrl}/donate")
    public ResponseEntity<Campaign> donate(@RequestHeader("Authorization") String header,
                                           @PathVariable("campaignUrl") String campaignUrl,
                                           @RequestBody DonationDateValue donationDTO) throws ServletException, NotFoundException {
        String email = jwtService.getSubjectByHeader(header);
        return new ResponseEntity(this.campaignService.donate(campaignUrl, userService.getUserByEmail(email).get(), donationDTO), HttpStatus.OK);
    }


    @PostMapping("/campaign/{campaignUrl}/like")
    public ResponseEntity addLike(@RequestHeader("Authorization") String header,
                                  @PathVariable("campaignUrl") String campaignUrl,
                                  @RequestBody Like like) throws ServletException, NotFoundException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        like.setOwner(userService.getUserByEmail(userEmail).get());

        return new ResponseEntity(this.campaignService.addLike(campaignUrl, like), HttpStatus.CREATED);
    }

    @PostMapping("/campaign/{campaignUrl}/dislike")
    public ResponseEntity addDislike(@RequestHeader("Authorization") String header,
                                     @PathVariable("campaignUrl") String campaignUrl,
                                     @RequestBody Dislike dislike) throws ServletException, NotFoundException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        dislike.setOwner(userService.getUserByEmail(userEmail).get());
        return new ResponseEntity(this.campaignService.addDislike(campaignUrl, dislike), HttpStatus.CREATED);
    }
}


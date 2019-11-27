package org.ajude.controllers;

import org.ajude.dtos.*;
import org.ajude.entities.Campaign;
import org.ajude.entities.Dislike;
import org.ajude.entities.Like;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.InvalidValueException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.json.simple.JSONObject;
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
    public ResponseEntity<List<CampaignDTO>> getCampaignHomeByReceived() {
        return new ResponseEntity(this.campaignService.getCampaignHomeByRemaining(), HttpStatus.OK);
    }

    @GetMapping("/home/date")
    public ResponseEntity<List<CampaignDTO>> getCampaignHomeByDate() {
        return new ResponseEntity(this.campaignService.getCampaignHomeByDate(), HttpStatus.OK);
    }

    @GetMapping("/home/like")
    public ResponseEntity<List<CampaignDTO>> getCampaignHomeByLike() {
        return new ResponseEntity(this.campaignService.getCampaignHomeByLike(), HttpStatus.OK);
    }

    @PostMapping("/campaign/register")
    public ResponseEntity registerCampaign(@RequestHeader("Authorization") String header,
                                           @Valid @RequestBody CampaignRegister campaign)
            throws ServletException, InvalidValueException, InvalidDateException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        campaign.setOwner(this.userService.getUserByEmail(userEmail).get());
        return new ResponseEntity(this.campaignService.register(campaign), HttpStatus.CREATED);
    }

    @GetMapping("/campaign/{campaignUrl}")
    public ResponseEntity getCampaign(@PathVariable String campaignUrl) throws NotFoundException {
        return new ResponseEntity(this.campaignService.getCampaign(campaignUrl), HttpStatus.OK);
    }

    @PostMapping("/campaign/search")
    public ResponseEntity<List<Campaign>> searchCampaigns(@RequestBody JSONObject json) {
        return new ResponseEntity(this.campaignService.searchCampaigns(json.get("substring").toString()), HttpStatus.OK);
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
            throws ServletException, UnauthorizedException, InvalidDateException, NotFoundException, InvalidValueException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        return new ResponseEntity(this.campaignService.setGoal(campaignUrl, newGoal, userEmail), HttpStatus.OK);
    }


    @PostMapping("/campaign/{campaignUrl}/donate")
    public ResponseEntity<Campaign> donate(@RequestHeader("Authorization") String header,
                                           @PathVariable("campaignUrl") String campaignUrl,
                                           @RequestBody JSONObject json) throws ServletException, NotFoundException, InvalidValueException {
        String email = jwtService.getSubjectByHeader(header);
        return new ResponseEntity(this.campaignService.donate(
                campaignUrl, userService.getUserByEmail(email).get(), Double.parseDouble(json.get("value").toString())), HttpStatus.CREATED);
    }


    @PostMapping("/campaign/{campaignUrl}/like")
    public ResponseEntity addLike(@RequestHeader("Authorization") String header,
                                  @PathVariable("campaignUrl") String campaignUrl,
                                  @RequestBody Like like) throws ServletException, NotFoundException {

        String userEmail = this.jwtService.getSubjectByHeader(header);
        like.setLikeUser(userService.getUserByEmail(userEmail).get());

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


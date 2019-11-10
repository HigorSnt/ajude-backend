package org.ajude.controllers;

import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.entities.Campaign;
import org.ajude.entities.Comment;
import org.ajude.exceptions.*;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/campaign")
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

    @PostMapping("/register")
    public ResponseEntity registerCampaign(@RequestHeader("Authorization") String token,
                                           @Valid @RequestBody Campaign campaign)
            throws ServletException, InvalidGoalException, InvalidDateException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        campaign.setOwner(this.userService.getUser(userEmail).get());
        return new ResponseEntity(this.campaignService.register(campaign), HttpStatus.CREATED);
    }

    @GetMapping("/{campaignUrl}")
    public ResponseEntity getCampaign(@PathVariable String urlIdentifier) throws NotFoundException {
        return new ResponseEntity(this.campaignService.getCampaign(urlIdentifier), HttpStatus.OK);
    }

    @GetMapping("/search/{substring}")
    public ResponseEntity<List<Campaign>> searchCampaigns(@PathVariable String substring) {
        return new ResponseEntity(this.campaignService.searchCampaigns(substring, "A"), HttpStatus.OK);
    }

    @GetMapping("/search/{substring}/{status}")
    public ResponseEntity<List<Campaign>> searchCampaigns(@PathVariable("substring") String substring,
                                                          @PathVariable("status") String status) {
        return new ResponseEntity(this.campaignService.searchCampaigns(substring, status), HttpStatus.OK);
    }

    @PutMapping("/{campaignUrl}/closeCampaign")
    public ResponseEntity closeCampaign(@RequestHeader("Authorization") String token,
                                        @PathVariable("campaignUrl") String campaignUrl)
            throws ServletException, UnauthorizedException, NotFoundException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        return new ResponseEntity(this.campaignService.closeCampaign(campaignUrl, userEmail), HttpStatus.OK);
    }

    @PutMapping("/{campaignUrl}/setDeadline")
    public ResponseEntity setDeadline(@RequestHeader("Authorization") String token,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignDeadline newDeadline)
            throws InvalidDateException, UnauthorizedException, NotFoundException, ServletException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        return new ResponseEntity(this.campaignService.setDeadline(campaignUrl, newDeadline, userEmail),HttpStatus.OK);
    }

    @PostMapping("{campaignUrl}/comment/")
    public ResponseEntity<Comment> addCampaignComment(@RequestBody Comment comment,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @RequestHeader("Authorization") String token)
            throws ServletException, NotFoundException {

        String subject = this.jwtService.getSubjectByHeader(token);
        comment.setOwner(this.userService.getUser(subject).get());
        return new ResponseEntity(this.campaignService.addCampaignComment(campaign, comment), HttpStatus.OK);
    }

    @PostMapping("{campaignUrl}/comment/{id}")
    public ResponseEntity<Comment> addCommentResponse(@RequestBody Comment reply,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @PathVariable("id") Long commentId,
                                                      @RequestHeader("Authorization") String token)
            throws ServletException, CommentNotFoundException, NotFoundException {

        String subject = this.jwtService.getSubjectByHeader(token);
        reply.setOwner(this.userService.getUser(subject).get());
        return new ResponseEntity(this.campaignService.addCommentResponse(campaign, commentId, reply), HttpStatus.OK);
    }

    @PutMapping("/{campaignUrl}/setGoal")
    public ResponseEntity setGoal(@RequestHeader("Authorization") String token,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignGoal newGoal)
            throws ServletException, UnauthorizedException, InvalidDateException, NotFoundException, InvalidGoalException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        return new ResponseEntity(this.campaignService.setGoal(campaignUrl, newGoal, userEmail), HttpStatus.OK);
    }
}


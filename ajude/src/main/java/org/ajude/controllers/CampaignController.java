package org.ajude.controllers;

import org.ajude.dtos.CampaignComment;
import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.dtos.CampaignHome;
import org.ajude.entities.Campaign;
import org.ajude.entities.Comment;
import org.ajude.exceptions.*;
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

    @GetMapping("/home")
    public ResponseEntity<List<CampaignHome>> getCampaignHome() {
        return new ResponseEntity(this.campaignService.getCampaignHome(), HttpStatus.OK);
    }

    @PostMapping("/campaign/register")
    public ResponseEntity registerCampaign(@RequestHeader("Authorization") String token,
                                           @RequestBody Campaign campaign)
            throws ServletException, InvalidGoalException, InvalidDateException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
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
    public ResponseEntity closeCampaign(@RequestHeader("Authorization") String token,
                                        @PathVariable("campaignUrl") String campaignUrl)
            throws ServletException, UnauthorizedException, NotFoundException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        return new ResponseEntity(this.campaignService.closeCampaign(campaignUrl, userEmail), HttpStatus.OK);
    }

    @PutMapping("/campaign/{campaignUrl}/setDeadline")
    public ResponseEntity setDeadline(@RequestHeader("Authorization") String token,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignDeadline newDeadline)
            throws InvalidDateException, UnauthorizedException, NotFoundException, ServletException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        return new ResponseEntity(this.campaignService.setDeadline(campaignUrl, newDeadline, userEmail),HttpStatus.OK);
    }

    @PostMapping("/campaign/{campaignUrl}/comment/")
    public ResponseEntity<Comment> addCampaignComment(@RequestBody Comment comment,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @RequestHeader("Authorization") String token)
            throws ServletException, NotFoundException {

        String subject = this.jwtService.getSubjectByHeader(token);
        comment.setOwner(this.userService.getUserByEmail(subject).get());
        return new ResponseEntity(this.campaignService.addCampaignComment(campaign, comment), HttpStatus.OK);
    }

    @PostMapping("/campaign/{campaignUrl}/comment/{id}")
    public ResponseEntity<Comment> addCommentResponse(@RequestBody Comment reply,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @PathVariable("id") Long commentId,
                                                      @RequestHeader("Authorization") String token)
            throws ServletException, NotFoundException {

        String subject = this.jwtService.getSubjectByHeader(token);
        reply.setOwner(this.userService.getUserByEmail(subject).get());
        return new ResponseEntity(this.campaignService.addCommentResponse(campaign, commentId, reply), HttpStatus.OK);
    }

    @PutMapping("/campaign/{campaignUrl}/setGoal")
    public ResponseEntity setGoal(@RequestHeader("Authorization") String token,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignGoal newGoal)
            throws ServletException, UnauthorizedException, InvalidDateException, NotFoundException, InvalidGoalException {

        String userEmail = this.jwtService.getSubjectByHeader(token);
        return new ResponseEntity(this.campaignService.setGoal(campaignUrl, newGoal, userEmail), HttpStatus.OK);
    }


    @DeleteMapping("/campaign/{campaignUrl}/comment")
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String header,
                                        @RequestBody CampaignComment campaignComment) {
        try {
            String email = jwtService.getSubjectByToken(jwtService.getSubjectByHeader(header));

            if (jwtService.userHasPermission(header, email))
                return new ResponseEntity<Campaign>(campaignService.deleteComment(userService.getUserByEmail(email).get(), campaignComment), HttpStatus.OK);

        } catch (ServletException | UnauthorizedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(HttpStatus.NOT_IMPLEMENTED);
    }

}


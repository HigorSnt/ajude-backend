package org.ajude.controllers;

import org.ajude.dtos.CampaignDeadline;
import org.ajude.dtos.CampaignGoal;
import org.ajude.entities.Campaign;
import org.ajude.entities.Comment;
import org.ajude.exceptions.InvalidDateException;
import org.ajude.exceptions.InvalidGoalException;
import org.ajude.exceptions.NotFoundException;
import org.ajude.exceptions.UnauthorizedException;
import org.ajude.services.CampaignService;
import org.ajude.services.JwtService;
import org.ajude.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
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
                                           @RequestBody Campaign campaign) {

        String userEmail = null;
        try {
            userEmail = this.jwtService.getSubjectByHeader(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        campaign.setOwner(this.userService.getUser(userEmail).get());

        try {
            return new ResponseEntity(this.campaignService.register(campaign), HttpStatus.CREATED);
        } catch (InvalidDateException | InvalidGoalException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/{campaignUrl}")
    public ResponseEntity getCampaign(@PathVariable String campaign) {
        try {
            return new ResponseEntity(this.campaignService.getCampaign(campaign), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
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
                                        @PathVariable("campaignUrl") String campaignUrl) {

        String userEmail;
        try {
            userEmail = this.jwtService.getSubjectByHeader(token);
        } catch (ServletException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            return new ResponseEntity(this.campaignService.closeCampaign(campaignUrl, userEmail), HttpStatus.OK);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{campaignUrl}/setDeadline")
    public ResponseEntity setDeadline(@RequestHeader("Authorization") String token,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignDeadline newDeadline) {

        String userEmail;
        try {
            userEmail = this.jwtService.getSubjectByHeader(token);
        } catch (ServletException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            return new ResponseEntity
                    (this.campaignService.setDeadline(campaignUrl, newDeadline, userEmail),HttpStatus.OK);

        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (InvalidDateException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{campaignUrl}/comment/")
    public ResponseEntity<Comment> addCampaignComment(@RequestBody Comment comment,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @RequestHeader("Authorization") String token) {
        String subject = null;

        try {
            subject = this.jwtService.getSubjectByHeader(token);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        comment.setOwner(this.userService.getUser(subject).get());
        return new ResponseEntity(this.campaignService.addCampaignComment(campaign, comment), HttpStatus.OK);
    }

    @PostMapping("{campaignUrl}/comment/{id}")
    public ResponseEntity<Comment> addCommentResponse(@RequestBody Comment reply,
                                                      @PathVariable("campaignUrl") String campaign,
                                                      @PathVariable("id") Long commentId,
                                                      @RequestHeader("Authorization") String token) {
        String subject = null;

        try {
            subject = this.jwtService.getSubjectByHeader(token);
        } catch (ServletException e) {
            e.printStackTrace();
        }

        reply.setOwner(this.userService.getUser(subject).get());

        try {
            return new ResponseEntity(this.campaignService.addCommentResponse(campaign, commentId, reply), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{campaignUrl}/setGoal")
    public ResponseEntity setGoal(@RequestHeader("Authorization") String token,
                                      @PathVariable("campaignUrl") String campaignUrl,
                                      @RequestBody CampaignGoal newGoal){

        String userEmail;
        try {
            userEmail = this.jwtService.getSubjectByHeader(token);
        } catch (ServletException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            return new ResponseEntity(this.campaignService.setGoal(campaignUrl, newGoal, userEmail), HttpStatus.OK);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (InvalidDateException | InvalidGoalException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}

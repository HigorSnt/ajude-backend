package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.ajude.dtos.UserNameEmail;
import org.ajude.exceptions.NotFoundException;
import org.ajude.utils.Status;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue
    private Long id;
    private String shortName;
    private String urlIdentifier;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date deadline;

    @JsonIgnore
    private ZonedDateTime registerDateTime;
    private Status status;
    private Double goal;
    private Double remaining;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnore
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCampaign")
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCampaign")
    @JsonIgnore
    private List<Donation> donations;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCampaign")
    private List<Like> likeList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idCampaign")
    private List<Dislike> dislikeList;

    public Campaign(String shortName, String description, String urlIdentifier, Date deadline, Double goal, User owner) {
        this.shortName = shortName;
        this.description = description;
        this.urlIdentifier = urlIdentifier;
        this.deadline = deadline;
        this.goal = goal;
        this.owner = owner;
    }

    public Comment addComment(Comment comment) {
        this.comments.add(comment);

        return comment;
    }

    public Comment addCommentResponse(Long commentId, Comment reply) throws NotFoundException {
        Comment comment = this.comments.stream().filter(c -> c.getId().equals(commentId)).findAny().get();

        if (comment != null) {
            comment.setReply(reply);
        } else {
            throw new NotFoundException("The comment " + commentId + " was not found.");
        }

        return comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(String urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public UserNameEmail getUser() {
        return new UserNameEmail(
                this.owner.getEmail(),
                this.owner.getFirstName(),
                this.owner.getLastName(),
                this.owner.getUsername()
        );
    }

    private User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Like> getLikeList() {
        return this.likeList;
    }

    public int getNumLikes() {
        return this.likeList.size();
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public List<Dislike> getDislikeList() {
        return dislikeList;
    }

    public int getNumDislikes() {
        return this.dislikeList.size();
    }

    public void setDislikeList(List<Dislike> dislikeList) {
        this.dislikeList = dislikeList;
    }

    private void calculateRemaining() {
        Double sum = 0.0;
        for (Donation d : donations) sum += d.getValue();
        this.setRemaining(this.goal - sum);
    }

    public void setRemaining(Double remaining) {
        this.remaining = remaining;
    }

    public Double getRemaining() {
        calculateRemaining();
        return remaining;
    }

    public List<Comment> getComments() {
        List<Comment> c = new ArrayList<>(List.copyOf(this.comments));
        c.sort(Comparator.comparing(Comment::getPostedAt));


        return c;
    }

    public Comment lastCommentAdded() {
        return this.comments.get(this.comments.size() - 1);
    }

    public void deleteComment(User owner, Long idComment) {
        for (Comment comment : comments)
            if (comment.recursiveDelete(owner, idComment) == 1) break;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addDonation(Donation donation) {
        donations.add(donation);
        calculateRemaining();
    }

    public Like addLike(Like like) {

        if (this.likeList.contains(like)) {
            this.likeList.remove(like);
        } else {
            this.dislikeList.removeIf(dislike -> dislike.getOwner().equals(like.getLikeUser()));
            this.likeList.add(like);
        }

        return like;
    }

    public Dislike addDislike(Dislike dislike) {

        if (dislikeList.contains(dislike)) {
            dislikeList.remove(dislike);
        } else {
            likeList.removeIf(like -> like.getLikeUser().equals(dislike.getOwner()));
            dislikeList.add(dislike);
        }

        return dislike;
    }

    public ZonedDateTime getRegisterDateTime(){
        return this.registerDateTime;
    }

    public void setRegisterDateTime(ZonedDateTime registerDateTime) {
        this.registerDateTime = registerDateTime;
    }


    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campaign campaign = (Campaign) o;

        if (!id.equals(campaign.id)) return false;
        return shortName.equals(campaign.shortName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + shortName.hashCode();
        return result;
    }
}
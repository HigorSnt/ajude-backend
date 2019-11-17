package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.ajude.dtos.UserNameEmail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ajude.dtos.CampaignLikeDislike;
import org.ajude.exceptions.NotFoundException;
import org.ajude.utils.Status;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue
    private Long id;
    private String shortName;
    private String urlIdentifier;
    private String description;
    private Date deadline;
    private Status status;
    private Double goal;
    private Double remaining;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    @JsonIgnore
    private User owner;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Donation> donations;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Like> likeList;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Dislike> dislikeList;

    public Comment addComment(Comment comment) {
        this.comments.add(comment);

        return comment;
    }

    public Comment addCommentResponse(Long commentId, Comment reply) throws NotFoundException {
        Comment comment = this.comments.stream().filter(c -> c.getId() == commentId).findAny().get();

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

    public Double getGoal() {
        return goal;
    }

    public void setGoal(Double goal) {
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

    public int getNumLikes(){
        return this.likeList.size();
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }

    public List<Dislike> getDislikeList() {
        return dislikeList;
    }

    public int getNumDislikes(){
        return this.dislikeList.size();
    }

    public void setDislikeList(List<Dislike> dislikeList) {
        this.dislikeList = dislikeList;
    }

    private void setRemaining() {
        Double sum = 0.0;
        for(Donation d : donations) sum += d.getValue();
        this.remaining = goal - sum;
    }

    public Double getRemaining(){
        setRemaining();
        return remaining;

    public List<Comment> getComments() {
        return comments;
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

    public void addDonation (Donation donation) {
        donations.add(donation);
        setRemaining();
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

    public CampaignLikeDislike addLike(Like like) {

        if (this.likeList.contains(like)){
            this.likeList.remove(like);
        } else {
            this.dislikeList.removeIf(dislike -> dislike.getUser().equals(like.getUser()));
            this.likeList.add(like);
        }

        return new CampaignLikeDislike(this.getNumLikes(), this.getNumDislikes());
    }

    public CampaignLikeDislike addDislike(Dislike dislike) {

        if (dislikeList.contains(dislike)){
            dislikeList.remove(dislike);
        } else {
            likeList.removeIf(like -> like.getUser().equals(dislike.getUser()));
            dislikeList.add(dislike);
        }

        return new CampaignLikeDislike(this.getNumLikes(), this.getNumDislikes());
    }
}
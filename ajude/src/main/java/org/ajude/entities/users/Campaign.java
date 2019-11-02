package org.ajude.entities.users;

import java.util.Date;
import java.util.List;

public class Campaign
{
    private Long id;
    private String acronym;
    private String URLIdentifier;
    private String description;
    private Date deadline;
    private enum status {ACTIVE, CLOSED, EXPIRED, FINISHED};
    private double goal;
    private List<Donation> donations;
    private User owner;
    private List<Comment> comments;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getAcronym()
    {
        return acronym;
    }

    public void setAcronym(String acronym)
    {
        this.acronym = acronym;
    }

    public String getURLIdentifier()
    {
        return URLIdentifier;
    }

    public void setURLIdentifier(String URLIdentifier)
    {
        this.URLIdentifier = URLIdentifier;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDeadine()
    {
        return deadline;
    }

    public void setDeadine(Date deadline)
    {
        this.deadline = deadline;
    }

    public double getGoal()
    {
        return goal;
    }

    public void setGoal(double goal)
    {
        this.goal = goal;
    }

    public List<Donation> getDonations()
    {
        return donations;
    }

    public void Donate(Donation donation)
    {
        this.donations.add(donation);
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public void addComment(Comment comment)
    {
        this.comments.add(comment);
    }

    public void setStatus(String s)
    {
        //TODO
    }
}
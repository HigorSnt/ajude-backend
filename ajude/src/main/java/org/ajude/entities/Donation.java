package org.ajude.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Donation
{
    @Id
    @GeneratedValue
    private Long id;
    private Double value;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JsonIgnore
    private User donor;
    private Date date;

    public Donation(){};

    public Donation(Double value, User donor, Date date)
    {
        this.value = value;
        this.donor = donor;
        this.date  = date;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public User getDonor()
    {
        return donor;
    }

    public void setDonor(User donor)
    {
        this.donor = donor;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}

package org.ajude.entities;

import org.ajude.dtos.UserNameEmail;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Donation {

    @Id
    @GeneratedValue
    private Long id;
    private Double value;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private User donor;
    private Date date;

    public Donation() {
    }

    public Donation(Double value, User donor, Date date) {
        this.value = value;
        this.donor = donor;
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public UserNameEmail getDonor() {
        return new UserNameEmail(
                this.donor.getEmail(),
                this.donor.getFirstName(),
                this.donor.getLastName(),
                this.donor.getUsername()
        );
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

package org.ajude.dtos;

public class CampaignGoal {

    private double Goal;

    public CampaignGoal(double goal) {
        Goal = goal;
    }

    public CampaignGoal() {
    }

    public double getGoal() {
        return Goal;
    }

    public void setGoal(double goal) {
        Goal = goal;
    }
}

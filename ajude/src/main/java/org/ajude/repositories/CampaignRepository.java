package org.ajude.repositories;

import org.ajude.entities.campaigns.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {

    Campaign findByUrlIdentifier(String urlIdentifier);

    List<Campaign> findByShortNameContainingIgnoreCase(String substring, String status);
}

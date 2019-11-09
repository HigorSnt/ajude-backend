package org.ajude.repositories;

import org.ajude.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {

    Campaign findByUrlIdentifier(String urlIdentifier);

    // List<Campaign> findByShortNameContainingIgnoreCase(String substring, String status);
}

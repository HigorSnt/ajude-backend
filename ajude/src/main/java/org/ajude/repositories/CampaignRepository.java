package org.ajude.repositories;

import org.ajude.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByUrlIdentifier(String urlIdentifier);

    // List<Campaign> findByShortNameContainingIgnoreCase(String substring, String status);
}

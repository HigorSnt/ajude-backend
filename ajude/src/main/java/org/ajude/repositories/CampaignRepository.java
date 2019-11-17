package org.ajude.repositories;

import org.ajude.entities.Campaign;
import org.ajude.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByUrlIdentifier(String urlIdentifier);

    List<Campaign> findByShortNameContainingIgnoreCaseAndStatus(String substring, Status status);

    List<Campaign> findTop5ByStatusOrderByRemainingDesc(Status status);
}

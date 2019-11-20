package org.ajude.repositories;

import org.ajude.entities.Campaign;
import org.ajude.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository<T, Long> extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByUrlIdentifier(String urlIdentifier);

    List<Campaign> findByShortNameContainingIgnoreCaseAndStatus(String substring, Status status);

    List<Campaign> findTop5ByStatusOrderByRemainingDesc(Status status);

    List<Campaign> findTop5ByStatusOrderByDeadlineAsc(Status status);

    @Query(value =
            "SELECT id, deadline, description, goal, remaining, short_name, status, url_identifier, owner_email, COUNT(id) AS q " +
            "FROM (SELECT * FROM campaign WHERE status = 0) AS t LEFT JOIN like_table AS l " +
            "ON (t.id = l.id_campaign) " +
            "WHERE id_campaign IS NOT NULL " +
            "GROUP BY id " +
            "ORDER BY q DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Campaign> findTop5ByStatusOrderByLikes(Status status);
}

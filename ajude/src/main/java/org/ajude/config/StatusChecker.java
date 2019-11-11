package org.ajude.config;

import org.ajude.entities.Campaign;
import org.ajude.repositories.CampaignRepository;
import org.ajude.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class StatusChecker {


    private CampaignRepository<Long, Campaign> campaignRepository;
    //                    hours * minutes * seconds * miliseconds
    private final int interval = 24 * 60 * 60 * 1000;

    @Autowired
    public StatusChecker(CampaignRepository<Long, Campaign> campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Scheduled(fixedDelay = interval)
    public void checkStatus() {
        List<Campaign> campaigns = this.campaignRepository.findAll();

        campaigns.forEach(c -> {
            //TODO Uma campanha se torna vencida quando o deadline
            // configurado para atingir a meta chegou e a meta não foi atingida.
            // Finalmente, uma campanha é marcada como concluida
            // quando ela atingir a meta e o deadline
            if (c.getDeadline().before(Date.from(Instant.now()))) {
                c.setStatus(Status.E);

                this.campaignRepository.save(c);
            }
        });
    }
}

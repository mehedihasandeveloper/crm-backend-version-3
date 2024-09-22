package com.mehediFifo.CRM.service;

import com.mehediFifo.CRM.entity.Campaign;
import com.mehediFifo.CRM.entity.Statistics;
import com.mehediFifo.CRM.repository.CampaignRepository;
import com.mehediFifo.CRM.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampaignService {
    @Autowired
    private CampaignRepository repository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    // Creating new campaign
    public synchronized Campaign saveCampaign(Campaign campaign) {
        Campaign savedCampaign = repository.save(campaign);
        Statistics stats = statisticsRepository.findById(1L).orElse(new Statistics(0, 0, 0, 0, 0));
        stats.setTotalCampaigns(stats.getTotalCampaigns() + 1);

        // Check the campaign type and update the relevant total
        if ("Inbound".equalsIgnoreCase(campaign.getCampaignType())) {
            stats.setInboundTotal(stats.getInboundTotal() + 1);
        } else if ("Outbound".equalsIgnoreCase(campaign.getCampaignType())) {
            stats.setOutboundTotal(stats.getOutboundTotal() + 1);
        }

        statisticsRepository.save(stats);
        return savedCampaign;
    }

    // Calling all campaigns as list
    public List<Campaign> getAllCampaigns() {
        return repository.findAll();
    }

    // Calling a single campaign by ID
    public Campaign getCampaignById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Calling a single campaign by Name
    public Campaign getCampaignByName(String name) {
        return repository.findByCampaignName(name);
    }

    // Deleting a campaign by ID
    public synchronized void deleteCampaign(Long id) {

        Optional<Campaign> campaignOpt = repository.findById(id);

        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();

            // Find the current statistics
            Statistics stats = statisticsRepository.findById(1L).orElse(new Statistics(0, 0, 0, 0, 0));
            stats.setTotalCampaigns(stats.getTotalCampaigns() - 1);

            // Decrease the correct count based on campaign type
            if ("Inbound".equalsIgnoreCase(campaign.getCampaignType())) {
                stats.setInboundTotal(stats.getInboundTotal() - 1);
            } else if ("Outbound".equalsIgnoreCase(campaign.getCampaignType())) {
                stats.setOutboundTotal(stats.getOutboundTotal() - 1);
            }

            statisticsRepository.save(stats);
            repository.deleteById(id);
        }
    }

    // Updating a campaign
    public synchronized Campaign updateCampaign(Campaign campaign) {
        Campaign existingCampaign = repository.findById(campaign.getId()).orElse(campaign);
        existingCampaign.setCampaignName(campaign.getCampaignName());
        existingCampaign.setCampaignId(campaign.getCampaignId());
        existingCampaign.setStatus(campaign.getStatus());
        return repository.save(existingCampaign);
    }

    public List<Campaign> search(String campaignId, String campaignName, Integer callTarget, Boolean status) {
        List<Campaign> campaigns = new ArrayList<>();
        if (campaignId != null && !campaignId.isEmpty()) {
            campaigns.addAll(repository.findByCampaignIdContainingIgnoreCase(campaignId));
        }
        if (campaignName != null && !campaignName.isEmpty()) {
            campaigns.addAll(repository.findByCampaignNameContainingIgnoreCase(campaignName));
        }
        if (callTarget != null) {
            campaigns.addAll(repository.findByCallTarget(callTarget)); // exact match
        }
        if (status != null) {
            campaigns.addAll(repository.findByStatus(status));
        }
        return campaigns.stream().distinct().collect(Collectors.toList());
    }

    public Page<Campaign> findCampaignsWithPagination(int offset, int pageSize) {
        return repository.findAll(PageRequest.of(offset, pageSize));
    }

    public Integer getTotalCampaigns() {
        Optional<Statistics> stats = statisticsRepository.findById(1L);
        return stats.map(Statistics::getTotalCampaigns).orElse(0);
    }

    public Integer getTotalInbound() {
        Optional<Statistics> stats = statisticsRepository.findById(1L);
        return stats.map(Statistics::getInboundTotal).orElse(0);
    }

    public Integer getTotalOutbound() {
        Optional<Statistics> stats = statisticsRepository.findById(1L);
        return stats.map(Statistics::getOutboundTotal).orElse(0);
    }

}

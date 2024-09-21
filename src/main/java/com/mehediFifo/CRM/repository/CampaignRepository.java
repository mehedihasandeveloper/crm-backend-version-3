package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Collection;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Campaign findByCampaignName(String name);

    List<Campaign> findByCampaignIdContainingIgnoreCase(String campaignId);

    List<Campaign> findByCampaignNameContainingIgnoreCase(String campaignName);

    List<Campaign> findByCallTarget(Integer callTarget);  // Exact match for Integer field

    List<Campaign> findByStatus(Boolean status);  // Exact match for Boolean field
}

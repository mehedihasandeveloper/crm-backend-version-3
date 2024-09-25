package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.DataTables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DataTablesRepository extends JpaRepository<DataTables, Long> {
    DataTables findByCampaignName(String name);

    Collection<? extends DataTables> findByNameContainingIgnoreCase(String name);

    Collection<? extends DataTables> findByCampaignNameContainingIgnoreCase(String campaignName);

    Collection<? extends DataTables> findByNumberOfField(Integer numberOfField);
    @Query("SELECT d.campaignName FROM DataTables d")
    List<String> findAllCampaignNames();
}

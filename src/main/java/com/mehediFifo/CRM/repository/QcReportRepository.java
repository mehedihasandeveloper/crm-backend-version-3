package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.QcReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QcReportRepository extends JpaRepository<QcReport, Long> {

    Page<QcReport> findAllByQcInspector(String username, Pageable pageable);

    List<QcReport> findAllByCallDate(String date);

    @Query("SELECT DISTINCT c.callDate FROM QcReport c")
    List<String> findDistinctCallDate();

    List<QcReport> findByCallDateBetween(String string, String string1);

    List<QcReport> findByAgentId(String agentId);


}

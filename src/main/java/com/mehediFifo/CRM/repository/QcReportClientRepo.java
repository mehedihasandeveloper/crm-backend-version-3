package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.QcReportClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QcReportClientRepo extends JpaRepository<QcReportClient, Long> {
    Page<QcReportClient> findAllByQcInspector(String username, Pageable pageable);

    @Query("SELECT DISTINCT c.callDate FROM QcReportClient c")
    List<String> findDistinctCallDate();

    List<QcReportClient> findAllByCallDate(String date);
}

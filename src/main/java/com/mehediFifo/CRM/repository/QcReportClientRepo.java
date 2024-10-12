package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.QcReport;
import com.mehediFifo.CRM.entity.QcReportClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QcReportClientRepo extends JpaRepository<QcReportClient, Long> {
    Page<QcReportClient> findAllByQcInspector(String username, Pageable pageable);
}

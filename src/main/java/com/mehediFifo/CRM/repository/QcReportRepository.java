package com.mehediFifo.CRM.repository;

import com.mehediFifo.CRM.entity.QcReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QcReportRepository extends JpaRepository<QcReport, Long> {
}

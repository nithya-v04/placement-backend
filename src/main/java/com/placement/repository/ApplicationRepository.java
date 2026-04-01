package com.placement.repository;

import com.placement.entity.Application;
import com.placement.entity.Application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);
    List<Application> findByJobId(Long jobId);
    List<Application> findByStatus(ApplicationStatus status);
    List<Application> findByJobIdAndStatus(Long jobId, ApplicationStatus status);

    Optional<Application> findByStudentIdAndJobId(Long studentId, Long jobId);
    boolean existsByStudentIdAndJobId(Long studentId, Long jobId);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId")
    List<Application> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId AND a.status = :status")
    List<Application> findByCompanyIdAndStatus(
            @Param("companyId") Long companyId,
            @Param("status") ApplicationStatus status);

    long countByStudentId(Long studentId);
    long countByJobId(Long jobId);
    long countByStatus(ApplicationStatus status);
}

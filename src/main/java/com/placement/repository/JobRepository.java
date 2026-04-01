package com.placement.repository;

import com.placement.entity.Job;
import com.placement.entity.Job.JobStatus;
import com.placement.entity.Job.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByStatus(JobStatus status);
    List<Job> findByJobType(JobType jobType);
    List<Job> findByCompanyId(Long companyId);
    List<Job> findByCompanyIdAndStatus(Long companyId, JobStatus status);

    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN' AND j.deadline >= :today")
    List<Job> findActiveJobs(@Param("today") LocalDate today);

    @Query("SELECT j FROM Job j WHERE j.status = 'OPEN' AND j.cgpaCutoff <= :cgpa AND j.deadline >= :today")
    List<Job> findEligibleJobsForStudent(@Param("cgpa") Double cgpa, @Param("today") LocalDate today);

    long countByStatus(JobStatus status);
}

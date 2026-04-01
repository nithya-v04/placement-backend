package com.placement.repository;

import com.placement.entity.Student;
import com.placement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUser(User user);
    Optional<Student> findByUserId(Long userId);
    Optional<Student> findByRollNumber(String rollNumber);
    boolean existsByRollNumber(String rollNumber);

    List<Student> findByDepartment(String department);
    List<Student> findByIsPlaced(Boolean isPlaced);
    List<Student> findByCgpaGreaterThanEqual(Double cgpa);

    @Query("SELECT s FROM Student s WHERE s.yearOfGraduation = :year")
    List<Student> findByGraduationYear(@Param("year") Integer year);

    @Query("SELECT s FROM Student s WHERE s.cgpa >= :minCgpa AND s.department = :dept")
    List<Student> findEligibleStudents(@Param("minCgpa") Double minCgpa, @Param("dept") String dept);

    long countByIsPlaced(Boolean isPlaced);
}

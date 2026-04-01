package com.placement.service;

import com.placement.entity.Student;
import com.placement.entity.User;
import com.placement.repository.StudentRepository;
import com.placement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository    userRepository;

    public StudentService(StudentRepository studentRepository,
                          UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository    = userRepository;
    }

    // ── Reads ─────────────────────────────────────────────────────────────────

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student getMyProfile() {
    	String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }

    public List<Student> getByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }

    public List<Student> getPlacedStudents() {
        return studentRepository.findByIsPlaced(true);
    }

    public List<Student> getUnplacedStudents() {
        return studentRepository.findByIsPlaced(false);
    }

    public List<Student> getEligibleStudents(Double minCgpa, String department) {
        return studentRepository.findEligibleStudents(minCgpa, department);
    }

    public Map<String, Long> getStats() {
        long total   = studentRepository.count();
        long placed  = studentRepository.countByIsPlaced(true);
        long unplaced = studentRepository.countByIsPlaced(false);
        return Map.of("total", total, "placed", placed, "unplaced", unplaced);
    }

    // ── Writes ────────────────────────────────────────────────────────────────

    @Transactional
    public Student updateMyProfile(Student updated) {
        Student existing = getMyProfile();
        existing.setSkills(updated.getSkills());
        existing.setPhone(updated.getPhone());
        existing.setResumeUrl(updated.getResumeUrl());
        existing.setAbout(updated.getAbout());
        applyCommonUpdates(existing, updated);
        return studentRepository.save(existing);
    }

    @Transactional
    public Student updateStudent(Long id, Student updated) {
        Student existing = getStudentById(id);
        applyCommonUpdates(existing, updated);
        // Admin can also update academic fields
        if (updated.getCgpa() != null)            existing.setCgpa(updated.getCgpa());
        if (updated.getDepartment() != null)       existing.setDepartment(updated.getDepartment());
        if (updated.getYearOfGraduation() != null) existing.setYearOfGraduation(updated.getYearOfGraduation());
        if (updated.getRollNumber() != null)       existing.setRollNumber(updated.getRollNumber());
        return studentRepository.save(existing);
    }

    @Transactional
    public Student markAsPlaced(Long id) {
        Student student = getStudentById(id);
        student.setIsPlaced(true);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.delete(getStudentById(id));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void applyCommonUpdates(Student target, Student src) {
        if (src.getFirstName() != null)  target.setFirstName(src.getFirstName());
        if (src.getLastName() != null)   target.setLastName(src.getLastName());
        if (src.getPhone() != null)      target.setPhone(src.getPhone());
        if (src.getSkills() != null)     target.setSkills(src.getSkills());
        if (src.getAbout() != null)      target.setAbout(src.getAbout());
        if (src.getResumeUrl() != null)  target.setResumeUrl(src.getResumeUrl());
    }

    private User authenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}

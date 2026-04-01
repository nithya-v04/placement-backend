package com.placement.controller;

import com.placement.entity.Student;
import com.placement.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ── Student self-service ──────────────────────────────────────────────────

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getMyProfile() {
        return ResponseEntity.ok(studentService.getMyProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> updateMyProfile(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateMyProfile(student));
    }

    // ── Admin / Company reads ─────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<List<Student>> getByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(studentService.getByDepartment(department));
    }

    @GetMapping("/eligible")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<List<Student>> getEligible(
            @RequestParam Double minCgpa,
            @RequestParam String department) {
        return ResponseEntity.ok(studentService.getEligibleStudents(minCgpa, department));
    }

    // ── Admin only ────────────────────────────────────────────────────────────

    @GetMapping("/placed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getPlaced() {
        return ResponseEntity.ok(studentService.getPlacedStudents());
    }

    @GetMapping("/unplaced")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getUnplaced() {
        return ResponseEntity.ok(studentService.getUnplacedStudents());
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(studentService.getStats());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @PatchMapping("/{id}/mark-placed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> markPlaced(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.markAsPlaced(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}

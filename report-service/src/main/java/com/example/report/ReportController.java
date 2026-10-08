package com.example.report;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository repo;

    public ReportController(ReportRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Report> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Report create(@RequestBody Report body) {
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> update(@PathVariable Long id, @RequestBody Report body) {
        return repo.findById(id).map(existing -> {
            existing.copyFrom(body);
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

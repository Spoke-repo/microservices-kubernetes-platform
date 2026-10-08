package com.example.notification;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;

    public NotificationController(NotificationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Notification> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Notification create(@RequestBody Notification body) {
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(@PathVariable Long id, @RequestBody Notification body) {
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

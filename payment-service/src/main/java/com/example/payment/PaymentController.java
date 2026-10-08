package com.example.payment;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository repo;

    public PaymentController(PaymentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Payment> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment create(@RequestBody Payment body) {
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment body) {
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

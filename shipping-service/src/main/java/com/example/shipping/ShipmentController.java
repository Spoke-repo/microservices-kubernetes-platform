package com.example.shipping;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentRepository repo;

    public ShipmentController(ShipmentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Shipment> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Shipment create(@RequestBody Shipment body) {
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shipment> update(@PathVariable Long id, @RequestBody Shipment body) {
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

package com.example.inventory;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryItemController {

    private final InventoryItemRepository repo;

    public InventoryItemController(InventoryItemRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<InventoryItem> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItem create(@RequestBody InventoryItem body) {
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> update(@PathVariable Long id, @RequestBody InventoryItem body) {
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

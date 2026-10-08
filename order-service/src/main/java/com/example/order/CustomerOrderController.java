package com.example.order;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class CustomerOrderController {

    private final CustomerOrderRepository repo;

    public CustomerOrderController(CustomerOrderRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CustomerOrder> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrder create(@RequestBody CustomerOrder body) {
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerOrder> update(@PathVariable Long id, @RequestBody CustomerOrder body) {
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

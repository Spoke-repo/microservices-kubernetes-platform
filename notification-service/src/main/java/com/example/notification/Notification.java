package com.example.notification;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String message;
    private Instant sentAt = Instant.now();

    public Long getId() { return id; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }

    public void copyFrom(Notification o) {
        this.recipient = o.recipient;
        this.message = o.message;
        this.sentAt = o.sentAt;
    }
}

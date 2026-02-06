package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "LIKES")
public class Like {

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    @Getter @Setter
    private Video video;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter
    private User user;

    @Column(name = "date_created", updatable = false, nullable = false)
    @Getter
    private Instant dateCreated;

    @PrePersist
    void onCreate() {
        if (this.dateCreated == null) {
            this.dateCreated = Instant.now();
        }
    }
}

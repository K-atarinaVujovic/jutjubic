package com.jutjubiccorps.jutjubic.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "COMMENTS")
public class Comment {

    public Comment() {}

    public Comment(Video video, User user, String text, Instant dateCreated) {
        this.video = video;
        this.user = user;
        this.text = text;
        this.dateCreated = dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id", nullable = false)
    @Getter @Setter
    private Video video;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter
    private User user;

    @Column(name = "text", nullable = false, length = 1000)
    @Getter @Setter
    private String text;

    @Column(name = "dateCreated", nullable = false, updatable = false)
    @Getter
    private Instant dateCreated;

    @PrePersist
    void onCreate() {
        if (this.dateCreated == null) {
            this.dateCreated = Instant.now();
        }
    }
}

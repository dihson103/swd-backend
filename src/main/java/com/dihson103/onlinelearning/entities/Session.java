package com.dihson103.onlinelearning.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String sessionName;

    @Column(nullable = false)
    private String videoAddress;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "session")
    private List<Comment> comments;

    @OneToMany(mappedBy = "session")
    private List<Question> questions;

    @OneToMany(mappedBy = "session")
    private List<Score> scores;

}

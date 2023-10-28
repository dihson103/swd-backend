package com.dihson103.onlinelearning.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private Boolean isTrueAnswer;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}

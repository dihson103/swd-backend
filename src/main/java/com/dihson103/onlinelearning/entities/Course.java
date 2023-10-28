package com.dihson103.onlinelearning.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String courseName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime publicDate;

    @Column(nullable = false)
    private Boolean status;

    private String image;

    @OneToMany(mappedBy = "course")
    private List<Discount> discounts;

    @OneToMany(mappedBy = "course")
    private List<Enroll> enrolls;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

}

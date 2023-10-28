package com.dihson103.onlinelearning.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "News")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private Boolean status;

    @OneToMany(mappedBy = "news")
    private List<ImageNews> imageNewsList;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryNews category;

}

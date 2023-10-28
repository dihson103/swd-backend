package com.dihson103.onlinelearning.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Image_news")
public class ImageNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

}

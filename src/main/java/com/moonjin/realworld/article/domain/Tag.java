package com.moonjin.realworld.article.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }

    public static Tag of(String name) {
        return Tag.builder().name(name).build();
    }
}

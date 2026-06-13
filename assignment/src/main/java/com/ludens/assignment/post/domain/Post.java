package com.ludens.assignment.post.domain;

import com.ludens.assignment.global.common.BaseEntity;
import com.ludens.assignment.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts", indexes = {
        @Index(columnList = "author_id, created_at, id"),
        @Index(columnList = "created_at, id")
})
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    private String imagePath;

    private Post(String title, String description, User author, String imagePath) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.imagePath = imagePath;
    }

    public static Post create(String title, String description, User author, String imagePath) {
        return new Post(title, description, author, imagePath);
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }
}

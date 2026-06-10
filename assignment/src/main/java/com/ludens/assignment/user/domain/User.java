package com.ludens.assignment.user.domain;

import com.ludens.assignment.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    private LocalDateTime deletedAt;

    protected User() {}

    private User(String id, String username, String password, MemberRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static User create(String username, String encodedPassword) {
        return new User(UUID.randomUUID().toString(), username, encodedPassword, MemberRole.USER);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.username = "deleted_" + this.id;
    }
}

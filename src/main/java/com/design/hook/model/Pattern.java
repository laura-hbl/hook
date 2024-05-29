package com.design.hook.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "patterns")
public class Pattern {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String title;
    private String level;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_date")
    private String createdDate;
    private long likes;
    private String description;

    public Pattern() {
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public long getLikes() {
        return likes;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return id == pattern.id && likes == pattern.likes && Objects.equals(title, pattern.title) && Objects.equals(level, pattern.level) && Objects.equals(imageUrl, pattern.imageUrl) && Objects.equals(createdDate, pattern.createdDate) && Objects.equals(description, pattern.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, level, imageUrl, createdDate, likes, description);
    }
}

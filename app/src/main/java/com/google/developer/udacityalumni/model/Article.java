package com.google.developer.udacityalumni.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajan Maurya
 *         On 19/03/17.
 */

public class Article {

    @SerializedName("id")
    private Long  id;

    @SerializedName("title")
    private String title;

    @SerializedName("featured")
    private Boolean featured;

    @SerializedName("spotlighted")
    private Boolean spotlighted;

    @SerializedName("content")
    private String content;

    @SerializedName("feature_image")
    private String featureImage;

    @SerializedName("status")
    private String status;

    @SerializedName("slug")
    private String slug;

    @SerializedName("user")
    private User user;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("tags")
    private List<Tag> tags = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getSpotlighted() {
        return spotlighted;
    }

    public void setSpotlighted(Boolean spotlighted) {
        this.spotlighted = spotlighted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

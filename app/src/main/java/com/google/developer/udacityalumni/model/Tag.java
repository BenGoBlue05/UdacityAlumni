package com.google.developer.udacityalumni.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author Rajan Maurya
 *         On 19/03/17.
 */

public class Tag {

    @SerializedName("id")
    private Long id;

    @SerializedName("tag")
    private String tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

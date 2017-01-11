package com.google.developer.udacityalumni.notification;

import android.os.Parcel;
import android.os.Parcelable;

final class SpotlightNotificationModel implements Parcelable {

    final String title, content;
    private final long id;

    private SpotlightNotificationModel(
            String title,
            String content,
            long id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    private SpotlightNotificationModel(Parcel in) {
        title = in.readString();
        content = in.readString();
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpotlightNotificationModel> CREATOR = new Creator<SpotlightNotificationModel>() {
        @Override
        public SpotlightNotificationModel createFromParcel(Parcel in) {
            return new SpotlightNotificationModel(in);
        }

        @Override
        public SpotlightNotificationModel[] newArray(int size) {
            return new SpotlightNotificationModel[size];
        }
    };

    int getId() {
        return Long.valueOf(id).intValue();
    }

    /*
    This Builder class could be overkill for now, but may be useful if/when fucntionality is
    added to Notifications to load specific articles.
     */
    static final class Builder {

        private String title, content;
        private long id;

        Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        Builder setContent(String content) {
            this.content = content;
            return this;
        }

        Builder setId(long id) {
            this.id = id;
            return this;
        }

        SpotlightNotificationModel build() {
            return new SpotlightNotificationModel(title,content,id);
        }

    }

}

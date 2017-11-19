package com.google.developer.udacityalumni.view.slidingview;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.udacityalumni.R;
import com.squareup.picasso.Picasso;

/**
 * A {@link SlidingView} that contains place holders for an avatar image, a title/name, some content
 * and a "See More" button.
 *
 * Created by Tom Calver on 18/01/17.
 */

public final class AvatarCardAdapter implements SlidingView, View.OnClickListener {

    private String name, content;
    private Uri imageUri;
    private OnClickListener listener;

    private ImageView mAvatarImageView;
    private TextView mNameTextView, mContentTextView;

    public AvatarCardAdapter(OnClickListener listener) {
        this.listener = listener;
    }

    public AvatarCardAdapter(Options options) {
        imageUri = options.imageUri;
        name = options.name;
        content = options.content;
        listener = options.listener;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setImageUri(String imageUri) {
        final Uri uri = (imageUri == null) ? Uri.EMPTY : Uri.parse(imageUri);
        setImageUri(uri);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        Picasso.with(mAvatarImageView.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(mAvatarImageView);
    }

    public void setContent(String content) {
        this.content = content;
        mContentTextView.setText(content);
    }

    public void setName(String name) {
        this.name = name;
        mNameTextView.setText(name);
    }

    @Override
    public View onCreateView(ViewGroup parent) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final ViewGroup view =
                (ViewGroup) inflater.inflate(R.layout.avatar_sliding_card, parent, false);

        mAvatarImageView = view.findViewById(R.id.sliding_card_avatar);
        mNameTextView = view.findViewById(R.id.sliding_card_name);
        mContentTextView = view.findViewById(R.id.sliding_card_content);

        final Button seeMoreButton = view.findViewById(R.id.sliding_card_see_more);
        final ImageButton dismissButton = view.findViewById(R.id.sliding_card_dismiss);

        setImageUri(imageUri);
        setName(name);
        setContent(content);

        seeMoreButton.setOnClickListener(this);
        dismissButton.setOnClickListener(this);

        return view;
    }

    @Override
    public Parcelable getParcelableData() {
        return new Options()
                .setName(name)
                .setImageUri(imageUri)
                .setContent(content)
                .setClickListener(listener);
    }

    @Override
    public void setParcelableData(Parcelable data) {
        if(data == null) return;
        final Options options = (Options) data;
        setContent(options.content);
        setName(options.name);
        setImageUri(options.imageUri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sliding_card_see_more:
                listener.onSeeMoreClick();
                break;
            case R.id.sliding_card_dismiss:
                listener.onDismissClick();
                break;
        }
    }

    public interface OnClickListener {
        void onSeeMoreClick();
        void onDismissClick();
    }

    /**
     * {@link OnClickListener} implementations that simply Logs click events. Supplying a
     * {@link SlidingViewManager} will animate the sliding view during an {@link #onDismissClick()}
     */
    @SuppressWarnings("unused")
    public static class SimpleOnClickListener implements OnClickListener {

        @Nullable
        private final SlidingViewManager mManager;

        public SimpleOnClickListener() {
            this(null);
        }

        public SimpleOnClickListener(@Nullable SlidingViewManager manager) {
            mManager = manager;
        }

        @Override
        public void onSeeMoreClick() {
            Log.i(getClass().getSimpleName(), "See More Clicked");
        }

        @Override
        public void onDismissClick() {
            Log.i(getClass().getSimpleName(), "Dismiss Clicked");
            if (mManager != null) mManager.animate();
        }

    }

    public static final class Options implements Parcelable {

        private Uri imageUri;
        private String name;
        private String content;
        private OnClickListener listener;

        public Options() {}

        Options(Parcel in) {
            imageUri = in.readParcelable(Uri.class.getClassLoader());
            name = in.readString();
            content = in.readString();
        }

        public static final Creator<Options> CREATOR = new Creator<Options>() {
            @Override
            public Options createFromParcel(Parcel in) {
                return new Options(in);
            }

            @Override
            public Options[] newArray(int size) {
                return new Options[size];
            }
        };

        public Options setImageUri(Uri imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public Options setClickListener(@NonNull OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Options setContent(String content) {
            this.content = content;
            return this;
        }

        public Options setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(imageUri, i);
            parcel.writeString(name);
            parcel.writeString(content);
        }

        @Override
        public String toString() {
            return "Options{" +
                    "imageUri=" + imageUri +
                    ", name='" + name + '\'' +
                    ", content='" + content + '\'' +
                    ", listener=" + listener +
                    '}';
        }
    }

}
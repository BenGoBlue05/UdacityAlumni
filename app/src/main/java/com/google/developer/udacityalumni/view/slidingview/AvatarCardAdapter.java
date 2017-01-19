package com.google.developer.udacityalumni.view.slidingview;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
    private Button mSeeMoreButton;
    private ImageButton mDismissButton;

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

        mAvatarImageView = (ImageView) view.findViewById(R.id.sliding_card_avatar);
        mNameTextView = (TextView) view.findViewById(R.id.sliding_card_name);
        mContentTextView = (TextView) view.findViewById(R.id.sliding_card_content);
        mSeeMoreButton = (Button) view.findViewById(R.id.sliding_card_see_more);
        mDismissButton = (ImageButton) view.findViewById(R.id.sliding_card_dismiss);

        Picasso.with(parent.getContext())
                .load(imageUri)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(mAvatarImageView);

        mNameTextView.setText(name);
        mContentTextView.setText(content);
        mSeeMoreButton.setOnClickListener(this);
        mDismissButton.setOnClickListener(this);

        return view;
    }

    @Override
    public Parcelable getParcelableData() {
        return new Options()
                .setName(name)
                .setImageUri(imageUri)
                .setContent(content)
                .setSeeMoreClickListener(listener);
    }

    @Override
    public void setParcelableData(Parcelable data) {
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

        public Options setSeeMoreClickListener(@NonNull OnClickListener listener) {
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
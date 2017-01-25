package com.google.developer.udacityalumni.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class AlumContract {

    static final String CONTENT_AUTHORITY = "com.google.developer.udacityalumni";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_ARTICLES = "articles";
    static final String PATH_USERS = "users";
    static final String PATH_TAGS = "tags";
    static final String PATH_ARTICLE_TAGS = "article_tags";



    public static final class ArticleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARTICLES);
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_ARTICLES;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        static final String TABLE_NAME = "articles";
        public static final String COL_ARTICLE_ID = "article_id";
        public static final String COL_TITLE = "title";
        public static final String COL_SPOTLIGHTED = "spotlighted";
        public static final String COL_CONTENT = "content";
        public static final String COL_IMAGE = "image";
        public static final String COL_SLUG = "slug";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_USER_NAME = "user_name";
        public static final String COL_USER_AVATAR = "user_avatar";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_UPDATED_AT = "updated_at";
        public static final String COL_RANDOM_TAG_ID = "tag_id";
        public static final String COL_RANDOM_TAG = "tag";
        public static final String COL_BOOKMARKED = "bookmarked";
        public static final String COL_FOLLOWING_AUTHOR = "userName";


        public static Uri buildUriWithId(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class UserEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_USERS;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_USERS;
        static final String TABLE_NAME = "users";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_EMAIL = "email";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_UPDATED_AT = "updated_at";
        public static final String COL_NAME = "name";
        public static final String COL_AVATAR = "avatar";
        public static final String COL_ROLE = "role";
        public static final String COL_BIO = "bio";
        public static final String COL_PUBLIC = "public";

        public static Uri buildUriWithId(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class TagEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TAGS);
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_TAGS;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_TAGS;
        static final String TABLE_NAME = "tags";
        public static final String COL_TAG_ID = "tag_id";
        public static final String COL_TAG = "tag";

        public static Uri buildUriWithId(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    static final class ArticleTagEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TAGS);
        static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_ARTICLE_TAGS;
        static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_ARTICLE_TAGS;
        static final String TABLE_NAME = "article_tag";
        public static final String COL_TAG_ID = "tag_id";
        public static final String COL_ARTICLE_ID = "article_id";

        public static Uri buildArticleTagUri(long articleId, long tagId){
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(articleId)).appendPath(String.valueOf(tagId)).build();
        }

        public static long getArticleIdFromUri(Uri uri){
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static long getTagIdFromUri(Uri uri){
            return Long.parseLong(uri.getLastPathSegment());
        }
    }


}

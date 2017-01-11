package com.google.developer.udacityalumni.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class AlumContentProvider extends ContentProvider {

    private static final int ARTICLE = 100;
    private static final int ARTICLE_WITH_ID = 101;
    private static final int USER = 200;
    private static final int USER_WITH_ID = 201;
    private static final int TAG = 300;
    private static final int TAG_WITH_ID = 301;
    private static final int ARTICLE_TAG = 400;
    private static final int ARTICLE_TAG_WITH_BOTH_IDS = 401;
    private static final String LIMIT = "50";

    private static final String SELECTION_ARTICLE_ID = AlumContract.ArticleEntry.COL_ARTICLE_ID + "=?";
    private static final String SELECTION_USER_ID = AlumContract.UserEntry.COL_USER_ID + "=?";
    private static final String SELECTION_TAG_ID = AlumContract.TagEntry.COL_TAG_ID + "=?";
    private static final String SELECTION_ARTICLE_TAG_IDS = AlumContract.ArticleTagEntry.COL_ARTICLE_ID + " =? AND " +
            AlumContract.ArticleTagEntry.COL_TAG_ID + " =?";


    private AlumDbHelper mDbHelper;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_ARTICLES, ARTICLE);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_ARTICLES + "/#", ARTICLE_WITH_ID);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_USERS, USER);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_USERS + "/#", USER_WITH_ID);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_TAGS, TAG);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_TAGS + "/#", TAG_WITH_ID);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_ARTICLE_TAGS, ARTICLE_TAG);
        sUriMatcher.addURI(AlumContract.CONTENT_AUTHORITY, AlumContract.PATH_ARTICLE_TAGS + "/#/#", ARTICLE_TAG_WITH_BOTH_IDS);
    }

    public AlumContentProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int numRows;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {
            case ARTICLE:
                numRows = db.delete(AlumContract.ArticleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ARTICLE_WITH_ID:
                numRows = db.delete(AlumContract.ArticleEntry.TABLE_NAME, SELECTION_ARTICLE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case USER:
                numRows = db.delete(AlumContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER_WITH_ID:
                numRows = db.delete(AlumContract.UserEntry.TABLE_NAME, SELECTION_USER_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case TAG:
                numRows = db.delete(AlumContract.TagEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TAG_WITH_ID:
                numRows = db.delete(AlumContract.TagEntry.TABLE_NAME, SELECTION_TAG_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case ARTICLE_TAG:
                numRows = db.delete(AlumContract.ArticleTagEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ARTICLE_TAG_WITH_BOTH_IDS:
                numRows = db.delete(AlumContract.ArticleTagEntry.TABLE_NAME, SELECTION_ARTICLE_TAG_IDS,
                        new String[]{uri.getPathSegments().get(1), uri.getPathSegments().get(2)});
                break;
            default:
                throw new UnsupportedOperationException("UNKNOWN URI: " + uri);
        }
        if (numRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRows;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ARTICLE:
                return AlumContract.ArticleEntry.CONTENT_TYPE;
            case ARTICLE_WITH_ID:
                return AlumContract.ArticleEntry.CONTENT_ITEM_TYPE;
            case USER:
                return AlumContract.UserEntry.CONTENT_TYPE;
            case USER_WITH_ID:
                return AlumContract.UserEntry.CONTENT_ITEM_TYPE;
            case TAG:
                return AlumContract.TagEntry.CONTENT_TYPE;
            case TAG_WITH_ID:
                return AlumContract.TagEntry.CONTENT_ITEM_TYPE;
            case ARTICLE_TAG:
                return AlumContract.ArticleTagEntry.CONTENT_TYPE;
            case ARTICLE_TAG_WITH_BOTH_IDS:
                return AlumContract.ArticleTagEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("UNKNOWN URI: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri retUri;
        long id;
        switch (sUriMatcher.match(uri)) {
            case ARTICLE:
                id = db.insert(AlumContract.ArticleEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(AlumContract.ArticleEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case USER:
                id = db.insert(AlumContract.UserEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(AlumContract.UserEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case TAG:
                id = db.insert(AlumContract.TagEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(AlumContract.TagEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case ARTICLE_TAG:
                id = db.insert(AlumContract.ArticleTagEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    retUri = ContentUris.withAppendedId(AlumContract.ArticleTagEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("UNKNOWN URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new AlumDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case ARTICLE:
                cursor = db.query(AlumContract.ArticleEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder, LIMIT);
                break;
            case ARTICLE_WITH_ID:
                cursor = db.query(AlumContract.ArticleEntry.TABLE_NAME, projection, SELECTION_ARTICLE_ID,
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            case USER:
                cursor = db.query(AlumContract.UserEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_WITH_ID:
                cursor = db.query(AlumContract.UserEntry.TABLE_NAME, projection, SELECTION_USER_ID,
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            case TAG:
                cursor = db.query(AlumContract.TagEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TAG_WITH_ID:
                cursor = db.query(AlumContract.TagEntry.TABLE_NAME, projection, SELECTION_TAG_ID,
                        new String[]{uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            case ARTICLE_TAG:
                cursor = db.query(AlumContract.ArticleTagEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ARTICLE_TAG_WITH_BOTH_IDS:
                cursor = db.query(AlumContract.ArticleTagEntry.TABLE_NAME, projection, SELECTION_ARTICLE_TAG_IDS,
                        new String[]{uri.getPathSegments().get(1), uri.getLastPathSegment()}, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("UNKNOWN URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rows;
        switch (sUriMatcher.match(uri)) {
            case ARTICLE:
                rows = db.update(AlumContract.ArticleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ARTICLE_WITH_ID:
                rows = db.update(AlumContract.ArticleEntry.TABLE_NAME, values, SELECTION_ARTICLE_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case USER:
                rows = db.update(AlumContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER_WITH_ID:
                rows = db.update(AlumContract.UserEntry.TABLE_NAME, values, SELECTION_USER_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case TAG:
                rows = db.update(AlumContract.TagEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TAG_WITH_ID:
                rows = db.update(AlumContract.TagEntry.TABLE_NAME, values, SELECTION_TAG_ID,
                        new String[]{uri.getLastPathSegment()});
                break;
            case ARTICLE_TAG:
                rows = db.update(AlumContract.ArticleTagEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ARTICLE_TAG_WITH_BOTH_IDS:
                rows = db.update(AlumContract.ArticleEntry.TABLE_NAME, values, SELECTION_ARTICLE_TAG_IDS,
                        new String[]{uri.getPathSegments().get(1), uri.getLastPathSegment()});
                break;
            default:
                throw new UnsupportedOperationException("UNKNOWN URI: " + uri);
        }
        if (rows != 0) getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int retCount = 0;
        String table;
        switch (sUriMatcher.match(uri)){
            case ARTICLE:
                table = AlumContract.ArticleEntry.TABLE_NAME;
                break;
            case USER:
                table = AlumContract.UserEntry.TABLE_NAME;
                break;
            case TAG:
                table = AlumContract.TagEntry.TABLE_NAME;
                break;
            case ARTICLE_TAG:
                table = AlumContract.ArticleTagEntry.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);
        }
        db.beginTransaction();
        try{
            for (ContentValues value : values){
                long id = db.insert(table, null, value);
                if (id != -1) retCount++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return retCount;
    }
}

package com.google.developer.udacityalumni.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class AlumDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alum.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL";
    private static final String INT_NOT_NULL = " INTEGER NOT NULL";
    private static final String INT_PRIMARY_KEY= " INTEGER PRIMARY KEY";

    AlumDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + AlumContract.ArticleEntry.TABLE_NAME +    "(" +
                AlumContract.ArticleEntry._ID + INT_PRIMARY_KEY + ", " +
                AlumContract.ArticleEntry.COL_ARTICLE_ID + INT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_TITLE + TEXT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_SPOTLIGHTED + INT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_CONTENT + TEXT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_IMAGE + " TEXT, " +
                AlumContract.ArticleEntry.COL_SLUG + " TEXT, " +
                AlumContract.ArticleEntry.COL_USER_ID + INT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_USER_NAME + TEXT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_USER_AVATAR + " TEXT, " +
                AlumContract.ArticleEntry.COL_CREATED_AT + INT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_UPDATED_AT + INT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_RANDOM_TAG_ID + " INTEGER, " +
                AlumContract.ArticleEntry.COL_RANDOM_TAG + " TEXT, " +
                AlumContract.ArticleEntry.COL_BOOKMARKED + INT_NOT_NULL + ", " +
                AlumContract.ArticleEntry.COL_FOLLOWING_AUTHOR + INT_NOT_NULL +
                ");";

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + AlumContract.UserEntry.TABLE_NAME + "(" +
                AlumContract.UserEntry._ID + INT_PRIMARY_KEY + ", " +
                AlumContract.UserEntry.COL_USER_ID + INT_NOT_NULL + ", " +
                AlumContract.UserEntry.COL_EMAIL + TEXT_NOT_NULL + ", " +
                AlumContract.UserEntry.COL_CREATED_AT + INT_NOT_NULL + ", " +
                AlumContract.UserEntry.COL_UPDATED_AT + INT_NOT_NULL + ", " +
                AlumContract.UserEntry.COL_NAME + TEXT_NOT_NULL + ", " +
                AlumContract.UserEntry.COL_AVATAR + " TEXT, " +
                AlumContract.UserEntry.COL_ROLE + TEXT_NOT_NULL + ", " +
                AlumContract.UserEntry.COL_BIO + " TEXT, " +
                AlumContract.UserEntry.COL_PUBLIC + INT_NOT_NULL + ");";

        final String SQL_CREATE_TAG_TABLE = "CREATE TABLE " + AlumContract.TagEntry.TABLE_NAME + "(" +
                AlumContract.TagEntry._ID + INT_PRIMARY_KEY + ", " +
                AlumContract.TagEntry.COL_TAG_ID+ INT_NOT_NULL + ", " +
                AlumContract.TagEntry.COL_TAG + TEXT_NOT_NULL + ");";

        final String SQL_CREATE_ARTICLE_TAG_TABLE = "CREATE TABLE " + AlumContract.ArticleTagEntry.TABLE_NAME + "(" +
                AlumContract.ArticleTagEntry._ID + INT_NOT_NULL + ", " +
                AlumContract.ArticleTagEntry.COL_ARTICLE_ID + INT_NOT_NULL + ", " +
                AlumContract.ArticleTagEntry.COL_TAG_ID + INT_NOT_NULL + ", " +
                "FOREIGN KEY (" + AlumContract.ArticleTagEntry.COL_ARTICLE_ID + ") REFERENCES " +
                AlumContract.ArticleEntry.TABLE_NAME + "(" + AlumContract.ArticleEntry.COL_ARTICLE_ID + "), " +
                "FOREIGN KEY (" + AlumContract.ArticleTagEntry.COL_TAG_ID + ") REFERENCES " +
                AlumContract.TagEntry.TABLE_NAME + "(" + AlumContract.TagEntry.COL_TAG_ID + ")" + ");";

        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_TAG_TABLE);
        db.execSQL(SQL_CREATE_ARTICLE_TAG_TABLE);
    }

    private void resetTables(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + AlumContract.ArticleEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlumContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlumContract.TagEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlumContract.ArticleTagEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetTables(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetTables(db);
    }
}

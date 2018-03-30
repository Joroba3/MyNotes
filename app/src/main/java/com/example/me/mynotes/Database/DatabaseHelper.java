package com.example.me.mynotes.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.ref.WeakReference;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Database Information
    private final static String DATABASE_NAME = "MyNotesDatabase";
    private final static int DATABASE_VERSION = 1;

    //Projects {id, name, parent, color} -- parent = -1 -> null
    private final static String PROJECTS_TABLE = "projectsTable";
    private final static String KEY_PT_ID = "project_id";
    private final static String KEY_PT_NAME = "project_name";
    private final static String KEY_PT_PARENT_ID = "project_parent";
    private final static String KEY_PT_COLOR = "project_color";
    //Entries {id, project, body}
    private final static String ENTRIES_TABLE = "entriesTable";
    private final static String KEY_ET_ID = "entry_id";
    private final static String KEY_ET_PROJECT = "entry_project";
    private final static String KEY_ET_BODY = "entry_body";
    private final static String KEY_ET_CALENDAR = "entry_calendar";//TODO calendar is retrieved with regex
    //Tags {entry_id, tag}
    private final static String TAGS_TABLE = "tagsTable";
    private final static String KEY_TT_ENTRY_ID = "entry_id";
    private final static String KEY_TT_TAG = "tag";

    private static WeakReference<DatabaseHelper> INSTANCE;

    public DatabaseHelper getInstance(Context context){
        if(INSTANCE == null) INSTANCE = new WeakReference<>(new DatabaseHelper(context));
        return INSTANCE.get();
    }
    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //Create Projects Database
        db.execSQL(String.format(
                "CREATE TABLE %1$s(%2$s INTEGER PRIMARY KEY,%3$s TEXT,%4$s INTEGER,%5$s TEXT)",
                PROJECTS_TABLE,
                KEY_PT_ID, KEY_PT_NAME,
                KEY_PT_PARENT_ID, KEY_PT_COLOR));

        //Create Entries Table
        db.execSQL(String.format(
                "CREATE TABLE %1$s(%2$s INTEGER PRIMARY KEY,%3$s INTEGER,%4$s TEXT, %5$s TEXT)",
                ENTRIES_TABLE,
                KEY_ET_ID, KEY_ET_PROJECT,
                KEY_ET_BODY, KEY_ET_CALENDAR));

        //Create Tags Table
        db.execSQL(String.format(
                "CREATE TABLE %1$s(%2$s INTEGER, %3$s TEXT)",
                TAGS_TABLE,
                KEY_TT_ENTRY_ID, KEY_TT_TAG));

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}

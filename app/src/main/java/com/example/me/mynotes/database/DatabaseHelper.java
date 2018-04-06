package com.example.me.mynotes.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Database Information
    private final static String DATABASE_NAME = "MyNotesDatabase";
    private final static int DATABASE_VERSION = 1;

    //Global
    private final static int NO_PARENT = -1;

    //Projects {id, name, parent, color}
    private final static String PROJECTS_TABLE = "projectsTable";
    private final static String KEY_PT_ID = "project_id";
    private final static String KEY_PT_NAME = "project_name";
    private final static String KEY_PT_PARENT_ID = "project_parent"; //-1==null
    private final static String KEY_PT_COLOR = "project_color";

    private final static int DEFAULT_PROJECT_ID = 0;
    private final static String DEFAULT_PROJECT_NAME = "Default"; //TODO change name
    //Entries {id, project, body}
    private final static String ENTRIES_TABLE = "entriesTable";
    private final static String KEY_ET_ID = "entry_id";
    private final static String KEY_ET_PROJECT = "entry_project";
    private final static String KEY_ET_ENTRY_PARENT = "entry_parent";
    private final static String KEY_ET_PRIORITY = "entry_priority";
    private final static String KEY_ET_BODY = "entry_body";
    private final static String KEY_ET_TIME = "entry_time";//TODO calendar is retrieved with regex
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

        newProject(DEFAULT_PROJECT_ID, DEFAULT_PROJECT_NAME, NO_PARENT, "000000");//TODO for now the color is hardcoded. Change in the future.

        //Create Entries Table
        db.execSQL(String.format(
                "CREATE TABLE %1$s(%2$s INTEGER PRIMARY KEY, %3$s INTEGER, %4$s TEXT," +
                        " %5$s TEXT, %6$s TEXT, %7$s INTEGER)",
                ENTRIES_TABLE,
                KEY_ET_ID, KEY_ET_PROJECT,
                KEY_ET_BODY, KEY_ET_TIME,
                KEY_ET_ENTRY_PARENT, KEY_ET_PRIORITY));

        //Create Tags Table
        db.execSQL(String.format(
                "CREATE TABLE %1$s(%2$s INTEGER, %3$s TEXT)",
                TAGS_TABLE,
                KEY_TT_ENTRY_ID, KEY_TT_TAG));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //TODO correctly handle upgrades
    }

    /**
     * Main method for introducing new projects to the 'Projects table'.
     * @param id the id of the project. If it is left null, it will just autoincrement.
     * @param name name of the project
     * @param parent child projects will include a parent_id. If it is left null, the default value
     *               -1 will be set, meaning that it is a top project.
     * @param color a HEX string of the color.
     * */
    public void newProject(@Nullable Integer id, String name,
                           @Nullable Integer parent, String color){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(id != null) contentValues.put(KEY_PT_ID, id);
        contentValues.put(KEY_PT_NAME, name);
        contentValues.put(KEY_PT_PARENT_ID, parent != null ? parent : NO_PARENT);
        contentValues.put(KEY_PT_COLOR, color);
        db.insert(PROJECTS_TABLE, null, contentValues);

    }
    /**
     * Main method for introducing new entries to the "Entries table". This table includes entries
     * for every single project.
     * @param text body of the entry.
     * @param project project it belongs to. If left null, the entry will be included to the
     *                default project (id 0).
     * @param calendar the calendar is the way of storing dates in the database. It will include
     *                 Strings which will the be parsed with a regex.
     * */
    public void newEntry(String text, @Nullable Integer project,
                         @Nullable Integer entryParent, @Nullable String calendar){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ET_PROJECT, project != null ? project : DEFAULT_PROJECT_ID);
        contentValues.put(KEY_ET_BODY, text);
        contentValues.put(KEY_ET_ENTRY_PARENT, entryParent != null ? entryParent : NO_PARENT);
        contentValues.put(KEY_ET_TIME, calendar != null ? calendar : "");
        db.insert(ENTRIES_TABLE, null, contentValues);
    }

}

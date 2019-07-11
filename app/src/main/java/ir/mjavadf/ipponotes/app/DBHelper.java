package ir.mjavadf.ipponotes.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "IppNotes";

  private SQLiteDatabase sqLiteDatabase;

  public DBHelper(Context context) {
    super(context, DATABASE_NAME,null, DATABASE_VERSION);
    this.createTables();
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    this.sqLiteDatabase = db;
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public SQLiteDatabase get() {
    return this.getWritableDatabase();
  }

  private void createTables () {
    String query =
            " CREATE TABLE IF NOT EXISTS " +
            db.Tables.NOTES + " ( " +
            db.Notes.ID     + " INTEGER     PRIMARY KEY, " +
            db.Notes.TITLE  + " STRING,                  " +
            db.Notes.NOTE   + " STRING,                  " +
            db.Notes.MARK   + " INTEGER (1) DEFAULT (0)  " +
                              " ) ";

    this.get().execSQL(query);
  }
}

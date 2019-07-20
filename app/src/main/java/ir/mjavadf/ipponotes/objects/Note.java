package ir.mjavadf.ipponotes.objects;

import android.content.Context;
import android.database.Cursor;

import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;

public class Note {
  private String title, note;
  private long id;
  private int mark = 0;

  public Note() {}

  public Note(long id, String title, String note,  int mark) {
    this.title = title;
    this.note = note;
    this.id = id;
    this.mark = mark;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getMark() {
    return mark;
  }

  public void setMark(int mark) {
    this.mark = mark;
  }

  public static Note getNote(Context context, long id) {
    Note object = new Note();
    object.setId(id);
    DBHelper dbHelper = new DBHelper(context);
    String[] args = {id + ""};
    Cursor cursor = dbHelper.get().rawQuery(" SELECT * FROM " + db.Tables.NOTES + " WHERE " + db.Notes.ID + " = ? " , args);
    while (cursor.moveToNext()) {
      object.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE)));
      object.setNote(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE)));
      object.setMark(cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.MARK)));
    }
    cursor.close();

    return object;
  }
}

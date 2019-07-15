package ir.mjavadf.ipponotes;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.objects.Note;

public class ShowActivity extends AppCompatActivity {

  AppCompatTextView title, note;
  AppCompatImageView markToggle;
  DBHelper dbHelper;
  Note object;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show);

    init();
    readData();
  }

  private void init() {
    title = findViewById(R.id.title);
    note = findViewById(R.id.note);
    markToggle = findViewById(R.id.mark);
    markToggle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        toggleMark();
      }
    });

    dbHelper = new DBHelper(this);
  }

  private void readData() {
    object = new Note();

    if (getIntent().hasExtra(db.Notes.ID)) {
      long id = getIntent().getLongExtra(db.Notes.ID, 2);
      Cursor cursor = dbHelper.get().rawQuery(
              " SELECT * FROM " + db.Tables.NOTES +
                  " WHERE " + db.Notes.ID + " = " + id
              , null);

      while (cursor.moveToNext()) {
        object.setId(id);
        object.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE)));
        object.setNote (cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE )));
        object.setMark (cursor.getInt   (cursor.getColumnIndexOrThrow(db.Notes.MARK )));
      }

        title.setText(object.getTitle());
        note.setText(object.getNote());

        if (object.getMark() == 0)
          markToggle.setImageResource(R.drawable.ic_bookmark_deactive);
        else markToggle.setImageResource(R.drawable.ic_bookmark_active);


      cursor.close();
    }
  }

  private void toggleMark() {
    if (object.getMark() == 0) {
      object.setMark(1);
      markToggle.setImageResource(R.drawable.ic_bookmark_active);

      ContentValues values = new ContentValues();
      values.put(db.Notes.MARK, object.getMark());
      String [] whereArgs = {object.getId()+""};
      dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
    } else {
      object.setMark(0);
      markToggle.setImageResource(R.drawable.ic_bookmark_deactive);

      ContentValues values = new ContentValues();
      values.put(db.Notes.MARK, object.getMark());
      String [] whereArgs = {object.getId()+""};
      dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
    }
  }
}

package ir.mjavadf.ipponotes;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;

public class ShowActivity extends AppCompatActivity {

  AppCompatTextView title, note;
  AppCompatImageView markToggle;
  DBHelper dbHelper;

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

    dbHelper = new DBHelper(this);
  }

  private void readData() {
    if (getIntent().hasExtra(db.Notes.ID)) {
      long id = getIntent().getLongExtra(db.Notes.ID, 2);
      Cursor cursor = dbHelper.get().rawQuery(
              " SELECT * FROM " + db.Tables.NOTES +
                  " WHERE " + db.Notes.ID + " = " + id
              , null);

      while (cursor.moveToNext()) {
        title.setText(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE)));
        note.setText(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE)));

        int    mark     = cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.MARK));
        if (mark == 0)
          markToggle.setImageResource(R.drawable.ic_bookmark_deactive);
        else markToggle.setImageResource(R.drawable.ic_bookmark_active);
      }

      cursor.close();
    }
  }
}

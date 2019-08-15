package ir.mjavadf.ipponotes;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.app;
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
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    app.log("id is: " + getIntent().getIntExtra(db.Notes.ID, -1));
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
      int id = getIntent().getIntExtra(db.Notes.ID, 2);
      Cursor cursor = dbHelper.get().rawQuery(
              " SELECT * FROM " + db.Tables.NOTES +
                      " WHERE " + db.Notes.ID + " = " + id
              , null);

      while (cursor.moveToNext()) {
        object.setId(id);
        object.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE)));
        object.setNote(cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE)));
        object.setMark(cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.MARK)));
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
      String[] whereArgs = {object.getId() + ""};
      dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
    } else {
      object.setMark(0);
      markToggle.setImageResource(R.drawable.ic_bookmark_deactive);

      ContentValues values = new ContentValues();
      values.put(db.Notes.MARK, object.getMark());
      String[] whereArgs = {object.getId() + ""};
      dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_show_activity, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.edit_note:
        editNote();
        return true;
      case R.id.delete_note: {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    deleteNote();
                  }
                })
                .setNegativeButton("No", null);
        dialog.show();
        return true;
      }
      default:
        return super.onOptionsItemSelected(item);
    }

  }

  private void editNote() {
    Intent intent = new Intent(this, NoteEditorActivity.class);
    intent.putExtra(db.Notes.ID, object.getId());
    startActivity(intent);
  }

  private void deleteNote() {
    String[] whereArgs = {object.getId() + ""};
    dbHelper.get().delete(db.Tables.NOTES, db.Notes.ID + " = ? ", whereArgs);
    finish();
  }

  @Override
  protected void onResume() {
    super.onResume();
    readData();
  }
}

package ir.mjavadf.ipponotes;

import android.content.ContentValues;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.app;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.objects.Note;

public class NoteEditorActivity extends AppCompatActivity implements View.OnClickListener {

  AppCompatTextView pageTitle;
  MaterialButton cancelBTN, saveBTN;
  MaterialEditText titleET, noteET;
  DBHelper dbHelper;
  boolean hasExtra;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_editor);
    Objects.requireNonNull(getSupportActionBar()).hide();

    init();
  }

  private void init() {
    dbHelper = new DBHelper(this);
    pageTitle = findViewById(R.id.pageTitle);
    titleET = findViewById(R.id.titleET);
    noteET = findViewById(R.id.noteET);

    saveBTN = findViewById(R.id.saveBTN);
    saveBTN.setOnClickListener(this);
    cancelBTN = findViewById(R.id.cancelBTN);
    cancelBTN.setOnClickListener(this);

    hasExtra = getIntent().hasExtra(db.Notes.ID);

    if (hasExtra) {
      pageTitle.setText(getResources().getString(R.string.edit_note));
      Note object = Note.getNote(this, Objects.requireNonNull(getIntent().getExtras()).getInt(db.Notes.ID));
      titleET.setText(object.getTitle());
      noteET.setText(object.getNote());
    }
  }

  @Override
  public void onClick(View view) {
    if (view == cancelBTN)
      finish();
    else if (view == saveBTN) {
      boolean titleValidate = isInputValid(titleET);
      boolean noteValidate = isInputValid(noteET);
      if (titleValidate && noteValidate) {
        String title = Objects.requireNonNull(titleET.getText()).toString();
        String note = Objects.requireNonNull(noteET.getText()).toString();

        ContentValues values = new ContentValues();
        values.put(db.Notes.TITLE, title);
        values.put(db.Notes.NOTE, note);
        if (!hasExtra) {
          values.put(db.Notes.MARK, 0);
          int id = (int) dbHelper.get().insert(db.Tables.NOTES, null, values);
          app.log("Note Id: " + id);
        }
        else {
          dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = " +
                  Objects.requireNonNull(getIntent().getExtras()).getInt(db.Notes.ID), null);
        }
        finish();
      }
    }
  }

  private boolean isInputValid(MaterialEditText editText) {
    if (Objects.requireNonNull(editText.getText()).toString().equals("")) {
      editText.setError("%s is empty".replace("%s", editText.getHint().toString()));
      return false;
    }

    return true;
  }
}

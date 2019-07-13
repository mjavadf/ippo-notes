package ir.mjavadf.ipponotes;

import android.content.ContentValues;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.app;
import ir.mjavadf.ipponotes.app.db;

public class NoteEditorActivity extends AppCompatActivity implements View.OnClickListener {

  AppCompatTextView pageTitle;
  MaterialButton cancelBTN,  saveBTN ;
  MaterialEditText titleET,  noteET;
  DBHelper dbHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_editor);
    Objects.requireNonNull(getSupportActionBar()).hide();

    init();
  }

  private void init() {
    dbHelper = new DBHelper(this);
    pageTitle   = findViewById(R.id.pageTitle);
    titleET     = findViewById(R.id.titleET);
    noteET      = findViewById(R.id.noteET);

    saveBTN     = findViewById(R.id.saveBTN);
    saveBTN.setOnClickListener(this);
    cancelBTN   = findViewById(R.id.cancelBTN);
    cancelBTN.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view == cancelBTN)
      finish();
    else if (view == saveBTN) {
      boolean titleValidate = isInputValid(titleET);
      boolean noteValidate  = isInputValid(noteET);
      if (titleValidate && noteValidate) {
        String title = titleET.getText().toString();
        String note  = noteET.getText().toString();

        ContentValues values = new ContentValues();
        values.put(db.Notes.TITLE, title);
        values.put(db.Notes.NOTE, note);
        values.put(db.Notes.MARK, 0);
        long id = dbHelper.get().insert(db.Tables.NOTES, null, values);
        app.log("Note Id: " + id);
        finish();
      }
    }
  }

  private boolean isInputValid(MaterialEditText editText) {
    if (editText.getText().toString().equals("")) {
      editText.setError("%s is empty".replace("%s", editText.getHint().toString()));
      return false;
    }

    return true;
  }
}

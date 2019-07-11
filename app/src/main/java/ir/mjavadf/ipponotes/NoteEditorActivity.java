package ir.mjavadf.ipponotes;

import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class NoteEditorActivity extends AppCompatActivity {

  AppCompatTextView pageTitle;
  MaterialButton cancelBTN,  saveBTN ;
  MaterialEditText titleET,  noteET;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_note_editor);
    Objects.requireNonNull(getSupportActionBar()).hide();

    init();
  }

  private void init() {
    pageTitle   = findViewById(R.id.pageTitle);
    cancelBTN   = findViewById(R.id.cancelBTN);
    saveBTN     = findViewById(R.id.saveBTN);
    titleET     = findViewById(R.id.titleET);
    noteET      = findViewById(R.id.noteET);
  }
}

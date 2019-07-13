package ir.mjavadf.ipponotes;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.mjavadf.ipponotes.adapters.NotesAdapter;
import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.objects.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  RecyclerView recyclerView;
  FloatingActionButton addNote;
  NotesAdapter adapter;
  List<Note> noteList = new ArrayList<>();
  DBHelper dbHelper;

  boolean isUpdate = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Objects.requireNonNull(getSupportActionBar()).hide();

    initViews();
    isUpdate = true;
  }

  private void initViews() {
    dbHelper = new DBHelper(this);
    noteList = readData();
    recyclerView = findViewById(R.id.recyclerView);
    adapter = new NotesAdapter(noteList);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    addNote = findViewById(R.id.addNote);
    addNote.setOnClickListener(this);
  }

  private List<Note> readData() {
    List<Note> list = new ArrayList<>();

    Cursor cursor = dbHelper.get().rawQuery(" SELECT * FROM " + db.Tables.NOTES, null);
    while (cursor.moveToNext()) {
      long id = cursor.getLong(cursor.getColumnIndexOrThrow(db.Notes.ID));
      String title = cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE));
      String note  = cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE));
      int mark     = cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.MARK));

      list.add(new Note(id, title, note, mark));
    }

    cursor.close();
    return list;
  }

  @Override
  protected void onResume() {
    super.onResume();
    updateList();
  }

  private void updateList (){
    if (isUpdate) {
      noteList.clear();
      noteList.addAll(readData());
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onClick(View view) {
    if (view == addNote) {
      Intent intent = new Intent(this, NoteEditorActivity.class);
      startActivity(intent);
    }
  }
}

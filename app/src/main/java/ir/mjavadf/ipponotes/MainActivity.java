package ir.mjavadf.ipponotes;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.mjavadf.ipponotes.adapters.NotesAdapter;
import ir.mjavadf.ipponotes.adapters.NotesAdapterCard;
import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.objects.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  RecyclerView recyclerView;
  FloatingActionButton addNote;
  NotesAdapterCard adapter;
  List<Note> noteList = new ArrayList<>();
  DBHelper dbHelper;
  AppCompatImageView searchIcon;

  boolean isUpdate = false;

  /* Search Box */
  CardView searchBox;
  MaterialEditText searchText;
  AppCompatImageView closeSearchBox;

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
//    adapter = new NotesAdapter(this, noteList);
//    recyclerView.setAdapter(adapter);
//    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    StaggeredGridLayoutManager gridLayoutManager =
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    adapter = new NotesAdapterCard(this, noteList);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(gridLayoutManager);

    addNote = findViewById(R.id.addNote);
    addNote.setOnClickListener(this);

    searchIcon = findViewById(R.id.searchIcon);
    /* Search Box */
    searchIcon.setOnClickListener(this);
    searchBox = findViewById(R.id.searchBox);
    closeSearchBox = findViewById(R.id.closeSearchBox);
    closeSearchBox.setOnClickListener(this);
    searchText = findViewById(R.id.searchText);
    searchText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        noteList.clear();
        noteList.addAll(searchData(s.toString()));
        adapter.notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  private List<Note> readData() {
    List<Note> list = new ArrayList<>();
    Cursor cursor = dbHelper.get().rawQuery(" SELECT * FROM " + db.Tables.NOTES, null);
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.ID));
      String title = cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE));
      String note = cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE));
      int mark = cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.MARK));

      list.add(new Note(id, title, note, mark));
    }

    cursor.close();
    return list;
  }

  private List<Note> searchData(String text) {
    List<Note> list = new ArrayList<>();
    Cursor cursor = dbHelper.get().rawQuery(" SELECT * FROM " + db.Tables.NOTES +
            " WHERE " + db.Notes.TITLE + " LIKE '%" + text + "%' " +
            " OR    " + db.Notes.NOTE  + " LIKE '%" + text + "%' ", null);
    while (cursor.moveToNext()) {
      int id = cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.ID));
      String title = cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.TITLE));
      String note = cursor.getString(cursor.getColumnIndexOrThrow(db.Notes.NOTE));
      int mark = cursor.getInt(cursor.getColumnIndexOrThrow(db.Notes.MARK));

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

  private void updateList() {
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
    } else if (view == searchIcon) {
      searchBox.setVisibility(View.VISIBLE);
      searchText.requestFocus();
    } else if (view == closeSearchBox) {
      closeSearch();
    }
  }

  private void closeSearch() {
    if (Objects.requireNonNull(searchText.getText()).toString().equals(""))
      searchBox.setVisibility(View.GONE);
    else
      searchText.setText("");
  }

  @Override
  public void onBackPressed() {
    if (searchBox.getVisibility() == View.VISIBLE) {
      closeSearch();
    } else
      super.onBackPressed();
  }
}

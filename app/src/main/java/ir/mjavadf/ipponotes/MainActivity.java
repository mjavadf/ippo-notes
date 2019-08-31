package ir.mjavadf.ipponotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.mjavadf.ipponotes.adapters.NotesAdapter;
import ir.mjavadf.ipponotes.adapters.NotesAdapterCard;
import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.interfaces.MultiSelectionListener;
import ir.mjavadf.ipponotes.objects.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MultiSelectionListener {

  CoordinatorLayout coordinatorLayout;
  RecyclerView recyclerView;
  FloatingActionButton addNote;
  NotesAdapterCard adapter;
  List<Note> noteList = new ArrayList<>();
  List<Note> tempList = new ArrayList<>();
  DBHelper dbHelper;
  AppCompatImageView searchIcon;

  boolean isUpdate = false;

  /* Search Box */
  CardView searchBox;
  MaterialEditText searchText;
  AppCompatImageView closeSearchBox;

  /* Multi Selection Bar */
  RelativeLayout multiSelectionBar;
  AppCompatTextView selectionCount;
  AppCompatImageView deleteNotes, markNotes, unmarkNotes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Objects.requireNonNull(getSupportActionBar()).hide();

    initViews();
    isUpdate = true;
  }

  private void initViews() {
    coordinatorLayout = findViewById(R.id.myCoordinatorLayout);
    dbHelper = new DBHelper(this);
    noteList = readData();
    recyclerView = findViewById(R.id.recyclerView);
//    adapter = new NotesAdapter(this, noteList);
//    recyclerView.setAdapter(adapter);
//    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    StaggeredGridLayoutManager gridLayoutManager =
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    adapter = new NotesAdapterCard(this, noteList, this);
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

    multiSelection();
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
            " OR    " + db.Notes.NOTE + " LIKE '%" + text + "%' ", null);
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

  private void multiSelection() {
    multiSelectionBar = findViewById(R.id.multiSelectionBar);
    selectionCount = findViewById(R.id.selectionCount);
    deleteNotes = findViewById(R.id.delete_notes);
    markNotes = findViewById(R.id.mark_notes);
    unmarkNotes = findViewById(R.id.unmark_notes);

    deleteNotes.setOnClickListener(this);
    markNotes.setOnClickListener(this);
    unmarkNotes.setOnClickListener(this);
  }

  private void closeMultiSelection() {
    multiSelectionBar.setVisibility(View.GONE);
    adapter.setMultiSelection(false);
    adapter.setMultiSelectionCount(0);
    updateList();
  }

  private void markAllSelected() {
    String inString = "-1";
    for (Note object : noteList) {
      if (object.isSelected())
        inString += ", " + object.getId();
    }

    String query = " UPDATE " + db.Tables.NOTES + " SET " + db.Notes.MARK + " = 1 " +
            " WHERE " + db.Notes.ID + " IN (" + inString + ")";
    dbHelper.get().execSQL(query);
    closeMultiSelection();
  }

  private void unmarkAllSelected() {
    String inString = "-1";
    for (Note object : noteList) {
      if (object.isSelected())
        inString += ", " + object.getId();
    }

    String query = " UPDATE " + db.Tables.NOTES + " SET " + db.Notes.MARK + " = 0 " +
            " WHERE " + db.Notes.ID + " IN (" + inString + ")";
    dbHelper.get().execSQL(query);
    closeMultiSelection();
  }

  private void deleteAllSelected() {
    int count = adapter.getMultiSelectionCount();
    int[] tempIDs = new int[count];
    int c = 0; // counter
    String inString = "-1";
    for (Note object : noteList) {
      if (object.isSelected()) {
        inString += ", " + object.getId();
        tempIDs[c] = object.getId();
        c++;
      }
    }

    tempList = createTempList(tempIDs);

    String query = " DELETE FROM " + db.Tables.NOTES +
            " WHERE " + db.Notes.ID + " IN (" + inString + ")";
    dbHelper.get().execSQL(query);

    closeMultiSelection();

    Snackbar snackbar = Snackbar.make(coordinatorLayout,
            getString(R.string.snackbar_dialog).replace("%d", count + ""),
            Snackbar.LENGTH_SHORT);
    snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        undo();
      }
    });
    snackbar.show();
  }

  private void undo() {
    for (Note item : tempList) {
      ContentValues values = new ContentValues();
      values.put(db.Notes.ID, item.getId());
      values.put(db.Notes.TITLE, item.getTitle());
      values.put(db.Notes.NOTE, item.getNote());
      values.put(db.Notes.MARK, item.getMark());
      dbHelper.get().insert(db.Tables.NOTES, null, values);
      updateList();
    }
  }

  private List<Note> createTempList(int[] idList) {
    List<Note> temp = new ArrayList<>();
    for (int id : idList) {
      temp.add(Note.getNote(this, id));
    }
    return temp;
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
    } else if (view == deleteNotes) {
      deleteAllSelected();
    } else if (view == markNotes) {
      markAllSelected();
    } else if (view == unmarkNotes) {
      unmarkAllSelected();
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
    } else if (multiSelectionBar.getVisibility() == View.VISIBLE) {
      closeMultiSelection();
    } else
      super.onBackPressed();
  }

  @Override
  public void onStartSelection() {
    multiSelectionBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void onItemSelected(int position, int count) {
    selectionCount.setText(getString(R.string.selected_items).replace("%d", count + ""));
  }

  @Override
  public void onItemDeselcted(int position, int count) {
    selectionCount.setText(getString(R.string.selected_items).replace("%d", count + ""));
    if (count == 0) {
      multiSelectionBar.setVisibility(View.GONE);
    }
  }
}

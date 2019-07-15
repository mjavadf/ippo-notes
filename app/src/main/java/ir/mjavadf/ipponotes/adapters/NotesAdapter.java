package ir.mjavadf.ipponotes.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.mjavadf.ipponotes.R;
import ir.mjavadf.ipponotes.ShowActivity;
import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.objects.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

  private List<Note> objects;
  private Activity activity;
  private DBHelper dbHelper;

  public NotesAdapter(Activity activity, List<Note> objects) {
    this.objects = objects;
    this.activity = activity;
    dbHelper = new DBHelper(activity);
  }

  @NonNull
  @Override
  public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_row, viewGroup, false);
    return new NoteViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull NoteViewHolder holder, int i) {
    holder.title.setText(objects.get(i).getTitle());
    holder.note.setText(objects.get(i).getNote());
    holder.icon.setText(objects.get(i).getTitle().substring(0, 1).toUpperCase());    // character in icon is first char of title

    if (objects.get(i).getMark() == 0) {
      holder.icon.setBackgroundDrawable(activity.getDrawable(R.drawable.background_note_icon_unmarked));
    } else
      holder.icon.setBackgroundDrawable(activity.getDrawable(R.drawable.background_note_icon_marked));


  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    CardView parent;
    AppCompatTextView icon, title, note;

    NoteViewHolder(@NonNull View itemView) {
      super(itemView);

      parent = itemView.findViewById(R.id.parent);
      icon   = itemView.findViewById(R.id.icon);
      title  = itemView.findViewById(R.id.title);
      note   = itemView.findViewById(R.id.note);

      parent.setOnClickListener(this);
      icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      if (v == parent) {
        Note object = objects.get(getAdapterPosition());
        Intent intent = new Intent(activity, ShowActivity.class);
        intent.putExtra(db.Notes.ID, object.getId());
        activity.startActivity(intent);
      } else if (v == icon) toggleMark();
    }

    private void toggleMark() {
      Note object = objects.get(getAdapterPosition());
      if (object.getMark() == 0) {
        object.setMark(1);
        icon.setBackgroundDrawable(activity.getDrawable(R.drawable.background_note_icon_marked));

        ContentValues values = new ContentValues();
        values.put(db.Notes.MARK, object.getMark());
        String [] whereArgs = {object.getId()+""};
        dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
      } else {
        object.setMark(0);
        icon.setBackgroundDrawable(activity.getDrawable(R.drawable.background_note_icon_unmarked));

        ContentValues values = new ContentValues();
        values.put(db.Notes.MARK, object.getMark());
        String [] whereArgs = {object.getId()+""};
        dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
      }
    }
  }
}

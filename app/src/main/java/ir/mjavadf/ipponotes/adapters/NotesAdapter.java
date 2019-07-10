package ir.mjavadf.ipponotes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import ir.mjavadf.ipponotes.R;
import ir.mjavadf.ipponotes.objects.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

  List<Note> objects;

  public NotesAdapter(List<Note> objects) {
    this.objects = objects;
  }

  @NonNull
  @Override
  public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_row, viewGroup, false);
    return new NoteViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {

  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  public class NoteViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout parent;
    AppCompatTextView icon, title, note;
    public NoteViewHolder(@NonNull View itemView) {
      super(itemView);

      parent = itemView.findViewById(R.id.parent);
      icon   = itemView.findViewById(R.id.icon);
      title  = itemView.findViewById(R.id.title);
      note   = itemView.findViewById(R.id.note);
    }
  }
}

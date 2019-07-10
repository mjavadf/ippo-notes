package ir.mjavadf.ipponotes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import ir.mjavadf.ipponotes.R;
import ir.mjavadf.ipponotes.objects.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

  private List<Note> objects;

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
  public void onBindViewHolder(@NonNull NoteViewHolder holder, int i) {
    holder.title.setText(objects.get(i).getTitle());
    holder.note.setText(objects.get(i).getNote());
    holder.icon.setText(objects.get(i).getTitle().substring(0,1).toUpperCase());    // character in icon is first char of title
  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  class NoteViewHolder extends RecyclerView.ViewHolder {
    CardView parent;
    AppCompatTextView icon, title, note;
    NoteViewHolder(@NonNull View itemView) {
      super(itemView);

      parent = itemView.findViewById(R.id.parent);
      icon   = itemView.findViewById(R.id.icon);
      title  = itemView.findViewById(R.id.title);
      note   = itemView.findViewById(R.id.note);
    }
  }
}

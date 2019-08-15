package ir.mjavadf.ipponotes.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ir.mjavadf.ipponotes.R;
import ir.mjavadf.ipponotes.ShowActivity;
import ir.mjavadf.ipponotes.app.DBHelper;
import ir.mjavadf.ipponotes.app.db;
import ir.mjavadf.ipponotes.objects.Note;

public class NotesAdapterCard extends RecyclerView.Adapter<NotesAdapterCard.CardViewHolder> {

  private List<Note> objects;
  private Activity activity;
  private DBHelper dbHelper;

  public NotesAdapterCard(Activity activity, List<Note> objects) {
    this.objects = objects;
    this.activity = activity;
    dbHelper = new DBHelper(activity);
  }

  @NonNull
  @Override
  public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_card, viewGroup, false);
    return new NotesAdapterCard.CardViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CardViewHolder holder, int i) {
    holder.title.setText(objects.get(i).getTitle());
    holder.note.setText(objects.get(i).getNote());
  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    LinearLayout parent;
    AppCompatTextView title, note;

    public CardViewHolder(@NonNull View itemView) {
      super(itemView);

      parent = itemView.findViewById(R.id.parent);
      title = itemView.findViewById(R.id.title);
      note = itemView.findViewById(R.id.note);

      parent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Note object = objects.get(getAdapterPosition());
      Intent intent = new Intent(activity, ShowActivity.class);
      intent.putExtra(db.Notes.ID, object.getId());
      activity.startActivity(intent);
    }
  }
}

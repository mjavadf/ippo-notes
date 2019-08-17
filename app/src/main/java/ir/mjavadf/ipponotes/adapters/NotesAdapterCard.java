package ir.mjavadf.ipponotes.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    if (objects.get(i).getMark() == 1) {
      holder.parent.setBackgroundColor(activity.getResources().getColor(R.color.marked_card));
    } else {
      holder.parent.setBackgroundColor(activity.getResources().getColor(R.color.unmarked_card));
    }
  }

  @Override
  public int getItemCount() {
    return objects.size();
  }

  class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    LinearLayout parent;
    AppCompatTextView title, note;

    CardViewHolder(@NonNull View itemView) {
      super(itemView);

      parent = itemView.findViewById(R.id.parent);
      title = itemView.findViewById(R.id.title);
      note = itemView.findViewById(R.id.note);

      parent.setOnClickListener(this);
      parent.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Note object = objects.get(getAdapterPosition());
      Intent intent = new Intent(activity, ShowActivity.class);
      intent.putExtra(db.Notes.ID, object.getId());
      activity.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
      Note object = objects.get(getAdapterPosition());
      if (object.getMark() == 0) {
        object.setMark(1);

        Animation anim = AnimationUtils.loadAnimation(activity, android.R.anim.fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
          @Override
          public void onAnimationStart(Animation animation) {

          }

          @Override
          public void onAnimationEnd(Animation animation) {
            parent.setBackgroundColor(activity.getResources().getColor(R.color.marked_card));
          }

          @Override
          public void onAnimationRepeat(Animation animation) {

          }
        });
        parent.startAnimation(anim);

        ContentValues values = new ContentValues();
        values.put(db.Notes.MARK, object.getMark());
        String[] whereArgs = {object.getId() + ""};
        dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
      } else {
        object.setMark(0);

        Animation anim = AnimationUtils.loadAnimation(activity, android.R.anim.fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
          @Override
          public void onAnimationStart(Animation animation) { }

          @Override
          public void onAnimationEnd(Animation animation) {
            parent.setBackgroundColor(activity.getResources().getColor(R.color.unmarked_card));
          }

          @Override
          public void onAnimationRepeat(Animation animation) { }
        });
        parent.startAnimation(anim);

        ContentValues values = new ContentValues();
        values.put(db.Notes.MARK, object.getMark());
        String[] whereArgs = {object.getId() + ""};
        dbHelper.get().update(db.Tables.NOTES, values, db.Notes.ID + " = ? ", whereArgs);
      }
      return true;
    }
  }
}

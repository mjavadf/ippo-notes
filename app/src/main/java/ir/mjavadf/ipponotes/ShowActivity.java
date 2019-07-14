package ir.mjavadf.ipponotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

public class ShowActivity extends AppCompatActivity {

  AppCompatTextView title, note;
  AppCompatImageView markToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_show);

    initViews();
  }

  private void initViews() {
    title = findViewById(R.id.title);
    note = findViewById(R.id.note);
    markToggle = findViewById(R.id.mark);
  }
}

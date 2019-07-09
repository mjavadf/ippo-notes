package ir.mjavadf.ipponotes;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  FloatingActionButton addNote;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();

    initViews();
  }

  private void initViews() {
    recyclerView = findViewById(R.id.recyclerView);
    addNote = findViewById(R.id.addNote);
  }
}

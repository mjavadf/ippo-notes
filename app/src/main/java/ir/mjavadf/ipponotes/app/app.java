package ir.mjavadf.ipponotes.app;

import android.util.Log;
import android.widget.Toast;

public class app {
  public static final String TAG = "IppoNotes";

  public static void log(String msg) {
    Log.e(TAG, msg);
  }

  public static void toast(String msg) {
    Toast.makeText(Application.getContext(),msg, Toast.LENGTH_SHORT).show();
  }
}

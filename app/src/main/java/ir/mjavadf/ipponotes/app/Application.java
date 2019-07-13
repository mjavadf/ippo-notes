package ir.mjavadf.ipponotes.app;

import android.content.Context;

public class Application extends android.app.Application {
  Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;
  }

  public Context getContext() {
    return context;
  }
}

package ir.mjavadf.ipponotes.interfaces;

public interface MultiSelectionListener {
  void onStartSelection();
  void onItemSelected(int position, int count);
  void onItemDeselcted(int position, int count);
}

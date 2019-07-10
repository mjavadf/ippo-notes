package ir.mjavadf.ipponotes.objects;

public class Note {
  private String title, note;
  private int id;
  private int mark = 0;

  public Note() {}

  public Note(String title, String note, int id, int mark) {
    this.title = title;
    this.note = note;
    this.id = id;
    this.mark = mark;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMark() {
    return mark;
  }

  public void setMark(int mark) {
    this.mark = mark;
  }
}

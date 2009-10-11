package project.mooncycle;

import java.util.Date;

public class Record {

  protected String woman;
  protected Date date;

  Record(String woman, Date date) {
    this.woman = woman;
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  public String toString() {
    return "" + woman + ":" + date + "/" + hashCode();
  }

}
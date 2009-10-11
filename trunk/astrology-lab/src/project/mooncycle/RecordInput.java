package project.mooncycle;

import java.util.Date;
import java.util.List;

public class RecordInput extends Record {

  private Record record;
  private List<Record> list;

  public RecordInput(List<Record> list, Record record) {
    super(record.woman, record.getDate());
    this.list = list;
    this.record = record;
  }

  public void setDate(Date date) {
    this.date = date;
    record.date = date;

    list.set(list.indexOf(this), record);
  }

}
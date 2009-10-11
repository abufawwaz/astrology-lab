package project.mooncycle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import open.osiva.core.Center;

public class RecordList {

  // A local cache for the filtered records by user
  private transient List<Record> records = new ArrayList<Record>();

  public List<Record> getRecords() {
    return records;
  }

  public void recordThisDay() {
    add(new Record(Center.getUser(), new Date()));
  }

  public void add(Record record) {
    Data.records.add(record);
    records.add(record);
  }

  public void remove(Record record) {
    Data.records.remove(record);
    records.remove(record);
  }

  public void edit(Record record) {
    int index = records.indexOf(record);
    if (index >= 0) {
      records.set(index, new RecordInput(records, record));
    }
  }

}
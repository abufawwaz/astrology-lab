package project.mooncycle;

import java.util.HashSet;

public class StatisticsOverview {

  public int getParticipantsCount() {
    HashSet<String> participants = new HashSet<String>();

    for (Record r: Data.getAllRecords()) {
      participants.add(r.woman);
    }

    return participants.size();
  }

  public int getRecordsCount() {
    return Data.getAllRecords().size();
  }

}
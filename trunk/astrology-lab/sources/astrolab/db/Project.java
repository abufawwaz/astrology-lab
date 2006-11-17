package astrolab.db;

import java.util.Date;

public class Project {

  private int laboratory;
  private ProjectType type;
  private Date startTime;
  private int name;
  private int description;

  Project(int name, int laboratory, String type, Date started, int description) {
    this.name = name;
    this.laboratory = laboratory;
    this.type = ProjectType.get(type);
    this.startTime = started;
    this.description = description;
  }

  public int getId() {
    return name;
  }

  public int getLaboratory() {
    return laboratory;
  }

  public ProjectType getType() {
    return type;
  }

  public Date getStart() {
    return startTime;
  }

  public String getName() {
    return Text.getText(name);
  }

  public String getDescription() {
    return Text.getText(description);
  }
}
package astrolab.project;

public class ProjectDataKey {

  private String name;
  private String type;

  public ProjectDataKey(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

}
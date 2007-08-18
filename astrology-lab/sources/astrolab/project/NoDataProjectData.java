package astrolab.project;

import astrolab.astronom.SpacetimeEvent;

public class NoDataProjectData extends ProjectData {

  private NoDataProject project;

  NoDataProjectData(NoDataProject project) {
    super(project);
    this.project = project;
  }

  public int getSize() {
    return 1;
  }

  public SpacetimeEvent getFromTime() {
    return project.getMinTime();
  }

  public SpacetimeEvent getToTime() {
    return project.getMaxTime();
  }

  public Object get(String key) {
    return 0;
  }

  public double getNumeric(String key) {
    return 0;
  }

  public String getString(String key) {
    return null;
  }

  public SpacetimeEvent getTime() {
    return getToTime();
  }

  public boolean begin() {
    return false;
  }

  public boolean move() {
    return false;
  }

  public boolean previous() {
    return false;
  }

  public void close() {  }
}

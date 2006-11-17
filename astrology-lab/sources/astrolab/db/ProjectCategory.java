package astrolab.db;

public class ProjectCategory {

  private int id;

  private ProjectCategory(int id) {
    this.id = id;
  }

  public String toString() {
  	return "Category: " + id;
  }

  static ProjectCategory get(int id) {
    return new ProjectCategory(id);
  }

}
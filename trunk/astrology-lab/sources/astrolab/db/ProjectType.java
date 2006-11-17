package astrolab.db;

public final class ProjectType {

  public final static ProjectType WHITE = new ProjectType("white");
  public final static ProjectType RED = new ProjectType("red");
  public final static ProjectType BLACK = new ProjectType("black");

  private String type;

  private ProjectType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  static ProjectType get(String type) {
    switch (type.charAt(0)) {
      case 'w': return WHITE;
      case 'r': return RED;
      case 'b': return BLACK;
    }
    return WHITE;
  }

  public String toString() {
    return type;
  }

}
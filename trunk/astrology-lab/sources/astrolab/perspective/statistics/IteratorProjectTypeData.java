package astrolab.perspective.statistics;

import java.util.HashSet;

import astrolab.criteria.Criterion;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.formula.FormulaeBase;
import astrolab.project.Project;
import astrolab.project.ProjectDataKey;
import astrolab.web.server.Request;

public class IteratorProjectTypeData extends IteratorProjectData {

  private static HashSet<String> UNNACCEPTABLE_KEYS = new HashSet<String>();
  static {
    UNNACCEPTABLE_KEYS.add("subject_id");
    UNNACCEPTABLE_KEYS.add("location");

    for (Integer planet: Criterion.TYPES.get(Criterion.TYPE_PLANET)) {
      UNNACCEPTABLE_KEYS.add(Text.getDescriptiveId(planet));
    }
  }

  protected IteratorProjectTypeData(Project project) {
    super(project);

    ensureBase();
    readData();
  }

  public String getType() {
    try {
      Object type = data.getObject(1);
      if (type instanceof Number) {
        return Text.getText(((Number) type).intValue());
      } else {
        return type.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "?";
    }
  }

  public int getValue() {
    try {
      return data.getInt(2);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  private void ensureBase() {
    String current = getBase().getText();

    if ((current == null) || UNNACCEPTABLE_KEYS.contains(current)) {
      for (ProjectDataKey key: project.getKeys()) {
        if (!UNNACCEPTABLE_KEYS.contains(key.getName())) {
          super.base = new FormulaeBase(0, project.getId(), Request.getCurrentRequest().getUser(), key.getName());
          return;
        }
      }
    }
  }

  private void readData() {
    StringBuffer buffer = new StringBuffer("SELECT ");

    buffer.append(normalizeKey(getBase().getText()));
    buffer.append(", COUNT(");
    buffer.append(normalizeKey(getBase().getText()));
    buffer.append(")");

    fillTableSelect(buffer, 1);
//  fillTimeSelect(buffer);
    buffer.append(" TRUE"); // TODO: needed only while time selection is off

    buffer.append(" GROUP BY ");
    buffer.append(normalizeKey(getBase().getText()));
    buffer.append(" LIMIT 20");

    this.data = Database.executeQuery(buffer.toString());

    try {
      if (this.data.last()) {
        if (this.data.getRow() >= 20) {
          this.data = null;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
package astrolab.perspective.statistics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import astrolab.criteria.Criterion;
import astrolab.db.Database;
import astrolab.db.Text;
import astrolab.formula.Element;
import astrolab.formula.FormulaeSeries;
import astrolab.project.Project;
import astrolab.project.ProjectDataKey;
import astrolab.web.server.Request;

public class IteratorProjectZodiacData extends IteratorProjectData {

  private int currentIndex;
  private int currentBaseValue;

  private static HashSet<String> UNNACCEPTABLE_KEYS = new HashSet<String>();
  static {
    UNNACCEPTABLE_KEYS.add("time");
    UNNACCEPTABLE_KEYS.add("event_time");
    UNNACCEPTABLE_KEYS.add("subject_id");
    UNNACCEPTABLE_KEYS.add("location");

    for (Integer planet: Criterion.TYPES.get(Criterion.TYPE_PLANET)) {
      UNNACCEPTABLE_KEYS.add(Text.getDescriptiveId(planet));
    }
  }

  protected IteratorProjectZodiacData(Project project) {
    super(project);

    ensureBase();
    ensureSeries();
    readData();
  }

  public int getBaseValue() {
    try {
      return data.getInt("base_key");
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public double[] getValue(String key) {
    if (currentIndex == currentBaseValue) {
      return new double[] {
          internalGetValue("min_" + key),
          internalGetValue("avg_" + key),
          internalGetValue("max_" + key)
      };
    } else {
      return null;
    }
  }

  private double internalGetValue(String key) {
    try {
      Object raw = data.getObject(key);
      if (raw instanceof Number) {
        return ((Number) raw).doubleValue();
      }
    } catch (SQLException e) {
    }

    return 0;
  }

  public boolean begin() {
    boolean begin = super.begin();

    if (begin) {
      currentIndex = 0;
      currentBaseValue = getBaseValue();
    }

    return begin;
  }

  public boolean move() {
    try {
      if (currentIndex >= currentBaseValue) {
        boolean hasNext = super.move();
        if (hasNext) {
          currentBaseValue = getBaseValue();
        }
        return hasNext;
      } else {
        return true;
      }
    } finally {
      currentIndex++;
    }
  }

  private void ensureBase() {
    super.base = StatisticsFormulae.getFormulae();
  }

  private void ensureSeries() {
    if ((series == null) || (series.length == 0)) {
      int user = Request.getCurrentRequest().getUser();
      ArrayList<FormulaeSeries> list = new ArrayList<FormulaeSeries>();

      for (ProjectDataKey key: project.getKeys()) {
        if (!UNNACCEPTABLE_KEYS.contains(key.getName())) {
          list.add(new FormulaeSeries(0, project.getId(), user, key.getName(), 0, "black"));
        }
      }
      series = list.toArray(new FormulaeSeries[0]);
    }
  }

  private void readData() {
    StringBuffer buffer = new StringBuffer("SELECT ");

    Element[] baseKeys = StatisticsFormulae.getFormulae().getElements();

    if (baseKeys.length == 0) {
      // no data
      return;
    }

    buffer.append(normalizeKey(StatisticsFormulae.getFormulae().getSQL()));
    buffer.append(" as base_key");

//    for (int serie = 0; serie < series.length; serie++) {
int serie = 0;
      String seriesName = series[serie].getText();
      StatisticsFormulaeCorrection[] correction = StatisticsFormulaeCorrection.readCorrections(serie);
      String seriesFormulae = fillSeries(series[serie].getSQL(), (correction != null) ? correction : null);
seriesFormulae = " abs(( " + seriesFormulae.replace("p1.", "p2.") + " / " + seriesFormulae + " ) * 100 - 100)";
      buffer.append(", avg(");
      buffer.append(seriesFormulae);
      buffer.append(") as avg_");
      buffer.append(seriesName);
      buffer.append(", min(");
      buffer.append(seriesFormulae);
      buffer.append(") as min_");
      buffer.append(seriesName);
      buffer.append(", max(");
      buffer.append(seriesFormulae);
      buffer.append(") as max_");
      buffer.append(seriesName);
//    }

      fillTableSelect(buffer, 2);
//    fillTimeSelect(buffer);
buffer.append(" TRUE"); // TODO: needed only while time selection is off
buffer.append(" AND TIMESTAMPADD(HOUR,24,p1.time) = p2.time");

    buffer.append(" GROUP BY base_key");

    this.data = Database.executeQuery(buffer.toString());
  }

  //      buffer.append(" - (54.6 + SIN(RADIANS(project_sunspots.Jupiter - 45)) * 25) - (COS(RADIANS(project_sunspots.Uranus * 8)) * 30)) + 50 as avg_");

  private final String fillSeries(String series, StatisticsFormulaeCorrection[] corrections) {
    String result = series;
    for (StatisticsFormulaeCorrection correction: corrections) {
      result += " - " + correction.getSQL();
    }
    return normalizeKey(result);
  }
}
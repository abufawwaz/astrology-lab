package open.osiva.core.view;

import java.util.ArrayList;
import java.util.HashMap;

import open.osiva.core.controller.Controller;
import open.osiva.core.controller.ControllerCollection;
import open.osiva.core.controller.ControllerRegistry;
import open.osiva.core.view.html.CustomizedView;
import open.osiva.core.view.template.AppearanceTemplate;
import open.osiva.core.view.template.Chunk;
import open.osiva.core.view.template.ScriptChunk;
import open.osiva.core.view.template.TextChunk;
import open.osiva.core.view.template.ViewChunk;

class ViewCustomizedAppearance {

  private static HashMap<String, AppearanceTemplate> APPEARANCE = new HashMap<String, AppearanceTemplate>();

  static {
    ArrayList<Chunk> chunks;
    AppearanceTemplate view;

    chunks = new ArrayList<Chunk>();
    chunks.add(new ScriptChunk("<table border='0' cellspacing='20'>"));
    chunks.add(new ScriptChunk("<tr><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsOverview"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("</table>"));
    view = new AppearanceTemplate(chunks);
    APPEARANCE.put("project.mooncycle.ProjectMoonCycle", view);

    chunks = new ArrayList<Chunk>();
    chunks.add(new ScriptChunk("<div style='border: 1px solid DDDDFF;'>"));
    chunks.add(new ScriptChunk("<table border='0' width='100%'>"));
    chunks.add(new ScriptChunk("<tr><td colspan='2' style='background-color: DDDDFF;'>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsOverview*label"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td colspan='2'>"));
    chunks.add(new TextChunk("The project is at full speed!"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td nowrap>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsOverview.getParticipantsCount*label"));
    chunks.add(new ScriptChunk(":</td><td width='90%'>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsOverview.getParticipantsCount"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td nowrap>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsOverview.getRecordsCount*label"));
    chunks.add(new ScriptChunk(":</td><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsOverview.getRecordsCount"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("</table>"));
    chunks.add(new ScriptChunk("</div>"));
    view = new AppearanceTemplate(chunks);
    APPEARANCE.put("project.mooncycle.ProjectMoonCycle.statisticsOverview", view);

    chunks = new ArrayList<Chunk>();
    chunks.add(new ScriptChunk("<div style='border: 1px solid DDDDFF;'>"));
    chunks.add(new ScriptChunk("<table border='0' cellspacing='3'>"));
    chunks.add(new ScriptChunk("<tr><td colspan='2' style='background-color: DDDDFF;'>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou*label"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records*label"));
    chunks.add(new ScriptChunk("</td><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.forecast*label"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records"));
    chunks.add(new ScriptChunk("</td><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.forecast"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("</table>"));
    chunks.add(new ScriptChunk("</div>"));
    view = new AppearanceTemplate(chunks);
    APPEARANCE.put("project.mooncycle.ProjectMoonCycle.statisticsForYou", view);

    chunks = new ArrayList<Chunk>();
    chunks.add(new ScriptChunk("<table border='0'>"));
    chunks.add(new ScriptChunk("<tr><td colspan='3'>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records.getRecords"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("<tr><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records.recordThisDay"));
    chunks.add(new ScriptChunk("</td><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records.remove"));
    chunks.add(new ScriptChunk("</td><td>"));
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records.edit"));
    chunks.add(new ScriptChunk("</td></tr>"));
    chunks.add(new ScriptChunk("</table>"));
    view = new AppearanceTemplate(chunks);
    APPEARANCE.put("project.mooncycle.ProjectMoonCycle.statisticsForYou.records", view);

    chunks = new ArrayList<Chunk>();
    chunks.add(new ViewChunk("project.mooncycle.ProjectMoonCycle.statisticsForYou.records.getRecords.?.getDate"));
    view = new AppearanceTemplate(chunks);
    APPEARANCE.put("project.mooncycle.ProjectMoonCycle.statisticsForYou.records.getRecords.?", view);
  }

  public static CustomizedView getCustomizedView(String id) {
    Controller controller = ControllerRegistry.get(id);
    Controller parent = ControllerRegistry.getParent(id);
    String templateId = null;
    AppearanceTemplate template;

    if (parent instanceof ControllerCollection) {
      templateId = parent.getId() + Controller.PATH_DELIMITER + "?";
      template = APPEARANCE.get(templateId);
    } else {
      template = APPEARANCE.get(id);
    }

    return (template != null) ? new CustomizedView(controller, templateId, template) : null;
  }

}
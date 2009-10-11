package open.osiva.core.view.html;

import java.util.ArrayList;
import java.util.List;

import open.osiva.core.controller.Controller;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.template.AppearanceTemplate;
import open.osiva.core.view.template.Chunk;
import open.osiva.core.view.template.ViewChunk;

public class CustomizedView extends AbstractView {

  String templateId;
  AppearanceTemplate pageBuilder;

  public CustomizedView(Controller controller, String templateId, AppearanceTemplate template) {
    super(controller);

    this.templateId = templateId;
    if (templateId == null) {
      this.pageBuilder = template;
    } else {
      this.pageBuilder = new AppearanceTemplate(clone(template.getChunks(), templateId, controller.getId()));
    }
  }

  protected void populateContents(PageSourceBuilder page) {
    pageBuilder.populateView(page);
  }

  private static List<Chunk> clone(List<Chunk> chunks, String templateId, String realId) {
    ArrayList<Chunk> newChunks = new ArrayList<Chunk>();

    for (Chunk c: chunks) {
      if (c instanceof ViewChunk) {
        ViewChunk v = (ViewChunk) c;

        if (v.id.startsWith(templateId)) {
          String newId = realId + v.id.substring(templateId.length());
          newChunks.add(new ViewChunk(newId));
        } else {
          newChunks.add(c);
        }
      } else {
        newChunks.add(c);
      }
    }

    return newChunks;
  }

}
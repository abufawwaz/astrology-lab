package open.osiva.core.view.template;

import java.util.List;

import open.osiva.core.controller.Controller;
import open.osiva.core.controller.ControllerRegistry;
import open.osiva.core.view.PageSourceBuilder;
import open.osiva.core.view.View;

public class AppearanceTemplate implements View {

  private List<Chunk> chunks;

  public AppearanceTemplate(List<Chunk> chunks) {
    this.chunks = chunks;
  }

  public List<Chunk> getChunks() {
    return chunks;
  }

  public void populateView(PageSourceBuilder page) {
    for (Chunk chunk: chunks) {
      if (chunk instanceof ScriptChunk) {
        page.append(((ScriptChunk) chunk).script);
      } else if (chunk instanceof TextChunk) {
        // TODO: Localize text
        page.append(((TextChunk) chunk).text);
      } else if (chunk instanceof ViewChunk) {
        Controller child = ControllerRegistry.get(((ViewChunk) chunk).id);
        child.getView().populateView(page);
      }
    }
  }
}
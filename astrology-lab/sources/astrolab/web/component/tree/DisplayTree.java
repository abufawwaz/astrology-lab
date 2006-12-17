package astrolab.web.component.tree;

import java.util.HashSet;

import astrolab.db.RecordIterator;
import astrolab.web.HTMLDisplay;
import astrolab.web.server.Request;
import astrolab.web.server.content.LocalizedStringBuffer;

public abstract class DisplayTree extends HTMLDisplay {

  private int id;
  private String choiceId;

  protected DisplayTree(int id) {
    this.id = id;
    this.choiceId = "_tree-" + id;
  }

  protected int getId() {
    return id;
  }

  public String getChoiceId() {
    return choiceId;
  }

  public abstract TreeObject getRoot();

  public void fillBodyContent(Request request, LocalizedStringBuffer buffer) {
    RecordIterator iterator;
    TreeObject selectionRoot = getRoot();

    try {
      String selectedText = request.get(choiceId);
      TreeObject seletedNode = (selectedText != null) ? selectionRoot.getById(Integer.parseInt(selectedText)) : selectionRoot;

      selectionRoot = seletedNode;

      if (seletedNode.getId() > 0) {
        try {
          TreeObject parentNode = seletedNode.getParent();
          if (parentNode != null) {
            buffer.append("<a href=\"?_d=");
            buffer.append(id);
            buffer.append("&amp;");
            buffer.append("_reload=0");
            buffer.append("&amp;");
            buffer.append(choiceId);
            buffer.append("=");
            buffer.append(parentNode.getId());
            buffer.append("\"><i>&lt;&lt; ");
            if (parentNode.getId() > 0) {
              buffer.append(parentNode.getText(false));
            }
            buffer.append("</i></a>");
            buffer.append(" / ");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        buffer.append(seletedNode.toString());
        buffer.append(": <br />");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    iterator = selectionRoot.iterateSubTrees();
    HashSet subtrees = new HashSet();

    while (iterator.hasNext()) {
      TreeObject child = (TreeObject) iterator.next();
      subtrees.add(new Integer(child.getId()));

      buffer.append(" <a href=\"javascript:if (parent != top) parent.select(new Array('");
      buffer.append(child.getId());
      buffer.append("', '");
      buffer.append(child.getText(false));
      buffer.append("', false)); window.location.href='?_d=");
      buffer.append(id);
      buffer.append("&amp;");
      buffer.append(choiceId);
      buffer.append("=");
      buffer.append(child.getId());
      buffer.append("'\">");
      buffer.append(child.getText(true));
      buffer.append("</a><br />");
    }

    iterator = selectionRoot.iterateChildren();
    while (iterator.hasNext()) {
      TreeObject child = (TreeObject) iterator.next();

      if (!subtrees.contains(new Integer(child.getId()))) {
        buffer.append(" <a href=\"javascript:if (parent != top) parent.select(new Array('");
        buffer.append(child.getId());
        buffer.append("', '");
        buffer.append(child.getText(false));
        buffer.append("', true))\">");
        buffer.append(child.getText(true));
        buffer.append("</a><br />");
      }
    }
  }

}
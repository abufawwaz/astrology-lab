package astrolab.web.component.tree;

import astrolab.db.RecordIterator;

public interface TreeObject {

  public int getId();

  public TreeObject getById(int id);

  public TreeObject getParent();

  public RecordIterator iterateChildren();

  public RecordIterator iterateSubTrees();

  public String getText();

  public String getDescription();

}

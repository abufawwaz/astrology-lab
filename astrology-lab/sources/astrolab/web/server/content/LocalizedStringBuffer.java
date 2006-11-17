package astrolab.web.server.content;

import astrolab.db.Text;

public class LocalizedStringBuffer {

  private StringBuffer buffer = new StringBuffer();

  public void append(String text) {
    buffer.append(text);
  }

  public void append(int text) {
    buffer.append(text);
  }

  public void append(double text) {
    buffer.append(text);
  }

  public void localize(String text) {
    append(bbcode(Text.getText(text)));
  }

  public void localize(int id) {
    append(bbcode(Text.getText(id)));
  }

  public void signature(int user) {
    append("<table width='100%'><tr><td width='30%'></td><td width='40%'><hr /></td><td align='right'><span style='font-size:8px;font-style:italic;color:blue'>");
    localize(user);
    append("</span></td></tr></table>");
  }

  public String toString() {
    return buffer.toString();
  }

  private final static String bbcode(String text) {
    return replace(text, "\n", "<br />");
  }

  private final static String replace(String text, String from, String to) {
    int skip = from.length();
    int previous = 0;
    int index = text.indexOf(from);
    if (index >= 0) {
      StringBuffer result = new StringBuffer();

      do {
        result.append(text.substring(previous, index));
        result.append(to);
        previous = index + skip;
        index = text.indexOf(from, previous);
      } while (index > 0);
      result.append(text.substring(previous));

      return result.toString();
    } else {
      return text;
    }
  }

}

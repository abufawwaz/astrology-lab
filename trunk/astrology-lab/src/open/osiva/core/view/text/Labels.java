package open.osiva.core.view.text;

public class Labels {

  public final static String format(String label) {
    StringBuilder result = new StringBuilder();
    boolean firstChar = true;

    for (char c: label.toCharArray()) {
      if (Character.isUpperCase(c)) {
        result.append(' ');
      }

      if (firstChar) {
        result.append(Character.toUpperCase(c));
        firstChar = false;
      } else {
        result.append(c);
      }
    }

    return result.toString().trim();
  }

}
package open.osiva.core.view;

public class PageSourceBuilder {

   private StringBuilder source = new StringBuilder();

   private final static String NEW_LINE = "\r\n";
   private final static String SINGLE_TAB = "\t";

   public int indent = 0;

   public void append(Object text) {
     source.append(text);
   }

   public void startLine(Object text) {
     source.append(NEW_LINE);
     for (int i = 0; i < indent; i++) {
       source.append(SINGLE_TAB);
     }

     source.append(text);
   }

   public void goIn() {
     indent++;
   }

   public void goOut() {
     indent--;
   }

   public String toString() {
     return source.toString();
   }
}
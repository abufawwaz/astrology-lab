package open.osiva.core.view;

import open.osiva.core.controller.Controller;
import open.osiva.core.view.html.ControllerView;
import open.osiva.core.view.html.ModifiableNumberView;
import open.osiva.core.view.html.ReadonlyPrimitiveView;

public class ViewFactory {

  public static View createView(Controller controller) {
    View result = ViewCustomizedAppearance.getCustomizedView(controller.getId());

    if (result != null) {
      return result;
    }

    if (!controller.isModifiable() && isPrimary(controller.getModel())) {
      return new ReadonlyPrimitiveView(controller);
    } else if (controller.isModifiable() && (controller.getModel() instanceof Number)) {
      return new ModifiableNumberView(controller);
    } else if (isPrimary(controller.getModel())) {
      // Fall back all primary objects to being read-only
      return new ReadonlyPrimitiveView(controller);
    } else {
      return new ControllerView(controller);
    }
  }

  private final static boolean isPrimary(Object model) {
    Class<?> classs = model.getClass();

    return classs.isPrimitive() || classs.getName().startsWith("java");
  }

}
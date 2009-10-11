package open.osiva.core.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;

import open.osiva.core.log.Log;
import open.osiva.core.persistency.Persistence;

public class ControllerFactory {

  private final static ClassLoader LOADER = ControllerFactory.class.getClassLoader();

  public static Controller createController(String service) {
    try {
      String root = findRootController(service);
      Object data = LOADER.loadClass(root).newInstance();

      Persistence.read(root, data);

      createController(root, data);
    } catch (Exception e) {
      Log.log("Unable to create controller for " + service + " due to " + e);
    }

    return ControllerRegistry.get(service);
  }

  public static String findRootController(String service) {
    while ((service.indexOf('.') > 0) && LOADER.getResource(service.replace('.', '/') + ".class") == null) {
      service = service.substring(0, service.lastIndexOf('.'));
    }
    return service;
  }

  public static Controller createController(String id, Object model) {
    Controller result = new Controller(id, model);

    createControllerTree(result);

    return result;
  }

  static void createControllerTree(Controller controller) {
    Object model = controller.getModel();

    if ((model != null) && !isPrimary(model)) {
      HashSet<String> displayed = new HashSet<String>();
  
      for (Field field: model.getClass().getFields()) {
        if (createControllerForField(controller.getId(), model, field)) {
          displayed.add(field.getName().toLowerCase());
        }
      }
  
      for (Method method: model.getClass().getMethods()) {
        if (method.getDeclaringClass() == Object.class) {
          continue;
        }
        if (displayed.contains(method.getName().substring(3).toLowerCase())) {
          continue;
        }
  
        if (method.getName().startsWith("get")) {
          createControllerForGetterMethod(controller.getId(), model, method);
        } else if (method.getParameterTypes().length == 0) {
          createControllerForActionMethod(controller.getId(), model, method);
        } else {
          createControllerForCommandMethod(controller.getId(), model, method);
        }
      }
    }
  }

  private final static boolean createControllerForField(String controllerId, Object model, Field field) {
    // The field is displayed either if it is public or ...
    boolean displayed = Modifier.isPublic(field.getModifiers());

    if (displayed) {
      String id = controllerId + Controller.PATH_DELIMITER + field.getName();

      new ControllerLabel(id, field.getName());

      ControllerField controllerField = new ControllerField(id, field);
      createControllerTree(controllerField);
    }

    return displayed;
  }

  private final static void createControllerForGetterMethod(String controllerId, Object model, Method method) {
    String id = controllerId + Controller.PATH_DELIMITER + method.getName();
    Controller getter;

    new ControllerLabel(id, method.getName().substring(3));

    if (Collection.class.isAssignableFrom(method.getReturnType())) {
      getter = new ControllerCollectionGetterMethod(id, method);
    } else {
      getter = new ControllerGetterMethod(id, method);
    }

    createControllerTree(getter);
  }

  private final static void createControllerForActionMethod(String controllerId, Object model, Method method) {
    String id = controllerId + Controller.PATH_DELIMITER + method.getName();
    Controller result = new ControllerActionMethod(id, method);

    createControllerTree(result);
  }

  private final static void createControllerForCommandMethod(String controllerId, Object model, Method method) {
    String id = controllerId + Controller.PATH_DELIMITER + method.getName();
    Controller result = new ControllerCommandMethod(id, method);

    createControllerTree(result);
  }

  private final static boolean isPrimary(Object model) {
    Class<?> classs;

    if (model instanceof Method) {
      classs = ((Method) model).getReturnType();
    } else if (model instanceof Field) {
      classs = ((Field) model).getType();
    } else {
      classs = model.getClass();
    }

    return classs.isPrimitive() || classs.getName().startsWith("java");
  }

}
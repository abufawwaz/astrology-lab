
// Global object to hold drag information.
var dragObj = null;
var areaObj = null;

var controls = new Array();

function manage_control() {
  for (var i = 0; i < controls.length; i++) {
    try {
      if (controls[i].initialize) {
        controls[i].initialize()
      }
    } catch (e) {
      controls.splice(i, 1)
    }
  }
}

function control_mouse_move() {
  if (top.dragObj) {
    for (var i = 0; i < controls.length; i++) {
      try {
        if (controls[i].div_document.body) controls[i].div_document.body.style.cursor = 'move'
        if (controls[i].divelement) controls[i].divelement.style.cursor = 'move'
      } catch (e) {
        controls.splice(i, 1)
      }
    }
  }
}

function control_mouse_up() {
  if (dragObj && areaObj) {
    areaObj.control(dragObj);
  }
  areaObj = null;
  dragObj = null;

  for (var i = 0; i < controls.length; i++) {
    try {
      if (controls[i].div_document) {
        controls[i].div_document.onmouseup = null
        if (controls[i].div_document.body) controls[i].div_document.body.style.cursor = 'auto'
        if (controls[i].divelement) controls[i].divelement.style.cursor = 'auto'
      }
      if (controls[i].mouse_out) {
        controls[i].mouse_out(controls[i])
      }
    } catch (e) {
      controls.splice(i, 1)
    }
  }
}

function control_mouse_down(event, object) {
  dragObj = object

  manage_control()

  if (window && window.event && window.event.returnValue) {
    window.event.returnValue = false;
  }
  if (event && event.preventDefault) {
    event.preventDefault();
  }

  for (var i = 0; i < controls.length; i++) {
    try {
      if (controls[i].div_document) {
        controls[i].div_document.onmousemove = control_mouse_move
        controls[i].div_document.onmouseup = control_mouse_up
      }
      if (controls[i].mouse_in) {
        controls[i].mouse_in(controls[i])
      }
    } catch (e) {
      controls.splice(i, 1)
    }
  }
  control_mouse_move()
}
 
 function Controllable(in_document, in_div_id, in_acceptedTypes, in_function_control, in_function_action) {
   this.div_document = in_document;
   this.div_id = in_div_id;
   this.acceptedTypes = in_acceptedTypes;
   this.action = in_function_action;
   this.on_control = in_function_control;

   this.is_over = false;
   this.divelement = null;
   this.controller = null;

   this.initialize = function() {
     if (this.div_id && this.div_document.getElementById(this.div_id) && !this.divelement) {
       this.divelement = this.div_document.getElementById(this.div_id)
       var controller =  this.divelement
       controller.controller = this
       controller.onmouseover = function() { controller.controller.mouse_over(controller.controller) }
       controller.onmouseout = function() { controller.controller.mouse_out(controller.controller) }
     }
   }

   this.control = function() {
     if (top.dragObj) {
       this.controller = top.dragObj;
       this.controller.control(this);

       if (this.on_control) this.on_control(this.controller.command)
     }
   }

   this.isTypeAccepted = function() {
     if (top.dragObj) {
       return (this.acceptedTypes.indexOf(top.dragObj.type) >= 0);
     }
   }

   this.mouse_over = function(control) {
     if (control.isTypeAccepted()) {
       control.is_over = true
       areaObj = control
       control.divelement.style.border = '2px dashed red'
     }
   }

   this.mouse_in = function(control) {
     if (!control.is_over && control.isTypeAccepted()) {
       if (control.divelement) control.divelement.style.border = control.is_over ? '2px dashed red' : '2px dashed green'
     }
   }

   this.mouse_out = function(control) {
     control.is_over = false
     if (areaObj == control) areaObj = null
     if (control.divelement) control.divelement.style.border = control.isTypeAccepted() ? '2px dashed green' : '1px dashed gray'
   }

   // register the object with the global list
   controls[controls.length] = this
 }

 function Controller(in_document, in_type, in_div_id, in_command) {
   this.div_document = in_document;
   this.div_id = in_div_id;
   this.command = in_command;
   this.type = (in_type) ? in_type : "Object";
   this.controlled = new Array();
   this.divelement = null;

   this.initialize = function() {
     if (this.div_id && this.div_document.getElementById(this.div_id) && !this.divelement) {
       this.divelement = this.div_document.getElementById(this.div_id)
       var controller = this.divelement
       controller.controller = this
       controller.onmousedown = function(event) { control_mouse_down(event, controller.controller) }
       controller.onmouseover = function() { controller.style.border = '2px dashed green' }
       controller.onmouseout = function() { controller.style.border = 'none' }
     }
   }

   this.control = function(object) {
     if (this.controlled.indexOf(object) < 0) {
       this.controlled[this.controlled.length] = object;
     }
   }

   this.action = function() {
     for (i = 0; i < this.controlled.length; i++) {
       if (this.controlled[i].action) this.controlled[i].action();
     }
   }

   // register the object with the global list
   controls[controls.length] = this
 }
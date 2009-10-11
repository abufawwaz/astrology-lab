var selection
var selectionType
var originalBackground
var controls = new Array()

function select(componentId, type) {
  if (getControllersOfType(type).length == 0) {
    return
  }

  if (selection) {
    deselect()
  }

  if (document.getElementById(componentId)) {
    selection = componentId
    selectionType = type
    originalBackground = document.getElementById(componentId).style.background
    document.getElementById(componentId).style.background = "#DDFFDD"

    var controls = getControllersOfType(selectionType)
    for (var i = 0; i < controls.length; i++) {
      c = document.getElementById(controls[i])
      if (c) {
        c.disabled = false
      }
    }
  }
}

function deselect() {
  if (selection) {
    document.getElementById(selection).style.background = originalBackground
  } 
  selection = null

  var controls = getControllersOfType(selectionType)
  for (var i = 0; i < controls.length; i++) {
    c = document.getElementById(controls[i])
    if (c) {
      c.disabled = true
    }
  }
}

function registerSelectionControl(controlId, type) {
  var controls = getControllersOfType(type)
  controls[controls.length] = controlId
}

function getControllersOfType(type) {
  var result = controls[type]
  if (!result) result = (controls[type] = new Array())
  return result
}
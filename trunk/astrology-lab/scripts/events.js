// public methods

function registerListener(win, eventType, listenerFunction) {
  return getEventType(eventType).registerListener(listenerFunction, win)
}

function unregisterListener(eventType, listener) {
  getEventType(eventType).unregisterListener(listener)
}

function fireEvent(win, eventType, message) {
  getEventType(eventType).fireEvent(win, message)
}

// internal functions

var events = new Array();

function EventListener(listenerFunction, listenerWindow) {
  this.handle = listenerFunction;
  this.owner = listenerWindow;
}

function EventType() {
  var listeners = new Array();

  this.registerListener = function(listenerFunction, listenerWindow) {
    var listener = new EventListener(listenerFunction, listenerWindow)
    listeners[listeners.length] = listener
    return listener
  }

  this.unregisterListener = function(listener) {
    for (var i = 0; i < listeners.length; i++) {
      if (listeners[i].owner.location.href == listener.owner.location.href) {
        listeners.splice(i, 1)
      }
    }
  }

  this.fireEvent = function(win, message) {
    for (var i = 0; i < listeners.length; i++) {
      try {
        if (listeners[i] && (listeners[i].owner != win)) {
          listeners[i].handle((typeof(message) == "function") ? message() : message, listeners[i].owner.frameIndex)
        }
      } catch (e) {
        listeners.splice(i, 1)
      }
    }
  }

}

function getEventType(eventType) {
  var knownEventType = events[eventType]
  if (!knownEventType) {
    knownEventType = new EventType()
    events[eventType] = knownEventType
  }
  return knownEventType
}

// public methods

function registerListener(eventType, listenerFunction) {
  return getEventType(eventType).registerListener(listenerFunction)
}

function unregisterListener(eventType, listener) {
  getEventType(eventType).unregisterListener(listener)
}

function fireEvent(eventType, message) {
  getEventType(eventType).fireEvent(message)
}

// internal functions

var events = new Array();

function EventListener(listenerFunction) {
  this.handle = listenerFunction;
}

function EventType() {
  var listeners = new Array();

  this.registerListener = function(listenerFunction) {
    var listener = new EventListener(listenerFunction)
    listeners[listeners.length] = listener
    return listener
  }

  this.unregisterListener = function(listener) {
    for (var i = 0; i < listeners.length; i++) {
      if (listeners[i] == listener) {
        listeners.splice(i, 1)
      }
    }
  }

  this.fireEvent = function(message) {
    for (var i = 0; i < listeners.length; i++) {
      try {
        if (listeners[i]) {
          listeners[i].handle((typeof(message) == "function") ? message() : message)
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

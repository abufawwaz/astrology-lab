// public methods

function registerListener(eventType, listenerFunction) {
  getEventType(eventType).registerListener(listenerFunction)
}

function fireEvent(eventType, message) {
  getEventType(eventType).fireEvent(message)
}

// internal functions

var events = new Array();

function EventType() {
  var listeners = new Array();

  this.registerListener = function(listenerFunction) {
    listeners[listeners.length] = listenerFunction
  }

  this.fireEvent = function(message) {
    for (var i = 0; i < listeners.length; i++) {
      try {
        if (listeners[i]) {
          listeners[i](message)
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

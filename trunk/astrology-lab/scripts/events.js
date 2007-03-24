// public methods

function registerListener(eventType, listenerDocument, listenerFunction) {
  getEventType(eventType).registerListener(listenerDocument, listenerFunction)
}

function unregisterListener(eventType, listenerDocument) {
  getEventType(eventType).unregisterListener(listenerDocument)
}

function fireEvent(eventType, message) {
  getEventType(eventType).fireEvent(message)
}

// internal functions

var events = new Array();

function EventType() {
  var documents = new Array();
  var listeners = new Array();

  this.registerListener = function(listenerDocument, listenerFunction) {
    documents[documents.length] = listenerDocument
    listeners[listeners.length] = listenerFunction
  }

  this.unregisterListener = function(listenerDocument) {
    for (var i = 0; i < documents.length; i++) {
      if (documents[i] == listenerDocument) {
        listeners.splice(i, 1)
        documents.splice(i, 1)
      }
    }
  }

  this.fireEvent = function(message) {
    for (var i = 0; i < listeners.length; i++) {
      try {
        if (listeners[i] && documents[i]) {
          if (typeof(message) == "function") {
            listeners[i](message())
          } else {
            listeners[i](message)
          }
        }
      } catch (e) {
        listeners.splice(i, 1)
        documents.splice(i, 1)
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

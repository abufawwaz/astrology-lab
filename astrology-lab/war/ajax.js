//
// The first section deals with sending the request
//

function call(actionId, argumentId) {
  var service = actionId.substring(0, actionId.lastIndexOf('.'))
  var command = actionId.substring(actionId.lastIndexOf('.') + 1)

  var args = ""
  if (argumentId) {
    args = argumentId;
  }

  postAjaxRequest("/" + actionId.replace(/\./g, '/'), args, getResult)
}

function createAjaxRequest() {
  var httprequest = false

  if (window.XMLHttpRequest) { // if Mozilla, Safari etc
    httprequest = new XMLHttpRequest()
    if (httprequest.overrideMimeType) httprequest.overrideMimeType('text/xml; charset=UTF-8')
  } else if (window.ActiveXObject){ // if IE
    try {
      httprequest=new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
      try {
        httprequest=new ActiveXObject("Microsoft.XMLHTTP");
      } catch (e) {}
    }
  }

  return httprequest
}

function postAjaxRequest(url, request, handle_result_function) {
  var handle = handle_result_function
  var ajax = createAjaxRequest()

  if (ajax) {
    ajax.onreadystatechange = function() {
      if (ajax.readyState == 4) {
        handle(ajax)
      }
    }

    var parameters = "action=" + encodeURIComponent(request)

    ajax.open('POST', url, true)
    ajax.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
    ajax.setRequestHeader("Content-length", parameters.length)
    ajax.send(parameters)
  } else {
    handle(null)
  }
}

//
// The next section deals with parsing the response
//

function getResult(result) {
  // refresh only if the response shows change in information!
  if (result && result.responseText) {
    var data = result.responseText
    var id
    var start
    var end = 0

    while (true) {
      start = data.indexOf("<div", end)
      if (start < 0) return
      end = getEndOfDiv(data, start)

      var i = data.indexOf("id='", start) + 4
      id = data.substring(i, data.indexOf("'", i))
      document.getElementById(id).outerHTML = data.substring(start, end)
    }
  }
}

function getEndOfDiv(text, offset) {
  var level = 0
  var i = offset + 4
  var i1
  var i2

  while (level >= 0) {
    i1 = text.indexOf("<div", i)
    i2 = text.indexOf("</div>", i)

    if (i2 < i1 || i1 < 0) {
      i = i2 + 6
      level--
    } else {
      i = i1 + 4
      level++
    }
  }

  return i
}
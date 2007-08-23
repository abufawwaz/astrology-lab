  var current_path = ""
  var controlView = -1;

  function select(selection) {
   var s=selection[0]
   for (var i = 1; i < selection.length; i++) {
     s += ":" + selection[i]
   }
   top.frames['view'].location.href=frames['view'].location.pathname + "?_s=" + s
   top.frames['menu'].location.href="menu.html?_s=" + s + "&_p=" + current_path
  }

var mousex = 0;
var mousey = 0;
var grabx = 0;
var graby = 0;
var orix = 0;
var oriy = 0;
var oriwidth = 0;
var oriheight = 0;
var elex = 0;
var eley = 0;
var maxZindex = 20;

var dragobjid = null
var grabtype;

function falsefunc() { return false; } // used to block cascading events

function init() {
  document.onmousemove = update; // update(event) implied on NS, update(null) implied on IE
  update();

  addPane('text', './menu.html', false, 150, 0, 10, 0)
  addPane('account', './root.html?_d=56', false, 500)
}

function update(e) {
  if (!e) e = window.event; // works on IE, but not NS (we rely on NS passing us the event)

  if (e) { 
    if (e.pageX || e.pageY) { // this doesn't work on IE6!! (works on FF,Moz,Opera7)
      mousex = e.pageX;
      mousey = e.pageY;
    } else if (e.clientX || e.clientY) { // works on IE6,FF,Moz,Opera7
      mousex = e.clientX + document.body.scrollLeft;
      mousey = e.clientY + document.body.scrollTop;
    }  
  }
}

function grab(_dragobjid, _grabtype) {
  dragobjid = _dragobjid
  grabtype = _grabtype

  grabx = mousex;
  graby = mousey;

  dragobj = document.getElementById('_obj_id_' + _dragobjid)
  elex = orix = dragobj.offsetLeft;
  eley = oriy = dragobj.offsetTop;

  dragobj.style.zIndex = ++maxZindex; // move it to the top

  handleobj = document.getElementById('_obj_' + _grabtype + '_handle_id_' + _dragobjid)
  handleobj.style.top = (graby - oriy - 15).toString(10) + 'px';
  handleobj.style.height = '30px';

  document.onmousemove = drag;
  document.onmouseup = drop;
  document.onmousedown = falsefunc; // in NS this prevents cascading of events, thus disabling text selection

  frameobj = document.getElementById('_obj_frame_id_' + _dragobjid)
  oriwidth = frameobj.width;
  oriheight = frameobj.height;

  update();
}

function drag(e) {// parameter passing is important for NS family\
  if (dragobjid) {
    dragobj = document.getElementById('_obj_id_' + dragobjid)
    if (grabtype == 'm') {
      // move
      elex = orix + (mousex-grabx);
      eley = oriy + (mousey-graby);
      dragobj.style.left = (elex).toString(10) + 'px';
      dragobj.style.top  = (eley).toString(10) + 'px';
    } else {
      // resize
      frameobj = document.getElementById('_obj_frame_id_' + dragobjid)

      temp = oriwidth * (mousex - orix) / (grabx - orix)
      frameobj.width = (temp > 100) ? temp : 100

      temp = mousey - oriy - 15
      frameobj.height = (temp > 100) ? temp : 100

      document.getElementById('_obj_r_handle_id_' + dragobjid).style.top = frameobj.height.toString(10) + 'px';
    }
  }
  update(e);
  return false; // in IE this prevents cascading of events, thus text selection is disabled
}

function drop() {
  if (dragobjid) {
    dragobj = document.getElementById('_obj_id_' + dragobjid)
    handleobj.style.top = (grabtype == 'm') ? '0px' : '99%';
    handleobj.style.height = '5px';
    handleobj = null;
    dragobjid = null;
  }
  update();
  document.onmousemove = update;
  document.onmouseup = null;
  document.onmousedown = null;   // re-enables text selection on NS
}

function addPane(_id, _src, _new, _width, _height, _x, _y) {
 if (_new) {
   _id += Math.random() * 255
 } else if (document.getElementById('_obj_id_' + _id)) {
   // the pane is already opened
   return
 }

 var _div = document.createElement("div");
 _div.setAttribute('id', '_obj_id_' + _id)
 _div.style.position='absolute'
 _div.style.left=(_x) ? _x+'px' : '200px'
 if (_y) _div.style.top=_y+'px'
 _div.style.backgroundColor='transparent'
 var _table = document.createElement("table");
 _table.border=0
 _table.style.margin=0
 _table.style.padding=0
 _div.appendChild(_table)
 var _tbody = document.createElement("tbody");
 _table.appendChild(_tbody)

 var _tr1 = document.createElement("tr");
 _tbody.appendChild(_tr1)
 var _td1 = document.createElement("td");
 _tr1.appendChild(_td1)
 var _handle = document.createElement("div");
 _handle.setAttribute('id', '_obj_m_handle_id_' + _id)
 _handle.style.position='absolute'
 _handle.style.opacity=0.5
 _handle.style.backgroundColor='#DDDDFF'
 _handle.style.top='0px'
 _handle.style.left='0px'
 _handle.style.width='100%'
 _handle.style.height='5px'
 _handle.style.margin='0px'
 _handle.style.padding='0px'
 _handle.style.fontSize=0
 _handle.style.cursor='move'
 _handle.onmousedown=function() { grab(_id, 'm')}
 _td1.appendChild(_handle)

 var _close = document.createElement("a");
 _close.setAttribute('href','#')
 _close.style.color='black'
 _close.style.textDecoration='none'
 _close.style.fontSize=7
 _td1.style.align='right'
 _close.onclick=function() { document.getElementById("_root_div_id").removeChild(document.getElementById('_obj_id_' + _id)); }
 _td1.appendChild(_close)
 var _close_text = document.createTextNode("X")
 _close.appendChild(_close_text)

 var _tr2 = document.createElement("tr");
 _tbody.appendChild(_tr2)
 var _td2 = document.createElement("td");
 _tr2.appendChild(_td2)
 var _iframe = document.createElement("iframe")
 _iframe.allowtransparency='true'
 _iframe.setAttribute('id', '_obj_frame_id_' + _id)
 _iframe.allowTransparency='true'
 _iframe.setAttribute('style', 'backgroundColor:transparent')
 _iframe.border=0
 _iframe.frameBorder=0
 _iframe.marginWidth=0
 _iframe.marginHeight=0
 _iframe.width=(_width) ? _width : 500
 _iframe.height=(_height) ? _height : 500
 _td2.appendChild(_iframe)

 var _tr3 = document.createElement("tr");
 _tbody.appendChild(_tr3)
 var _td3 = document.createElement("td");
 _tr3.appendChild(_td3)
 var _resize_handle = document.createElement("div");
 _resize_handle.setAttribute('id', '_obj_r_handle_id_' + _id)
 _resize_handle.style.position='absolute'
 _resize_handle.style.opacity=0.5
 _resize_handle.style.backgroundColor='#DDDDFF'
 _resize_handle.style.top= '99%'
 _resize_handle.style.left='0px'
 _resize_handle.style.width='100%'
 _resize_handle.style.height='5px'
 _resize_handle.style.margin='0px'
 _resize_handle.style.padding='0px'
 _resize_handle.style.fontSize=0
 _resize_handle.style.cursor='nw-resize'
 _resize_handle.onmousedown=function() { grab(_id, 'r')}
 _td3.appendChild(_resize_handle)

 document.getElementById("_root_div_id").appendChild(_div);
 _iframe.src=_src
}

  function refreshAllPanes(exceptPane) {
    for (frame = 0; frame < top.window.frames.length; frame++) {
      if (top.window.frames[frame] != exceptPane) {
        isPost = top.window.frames[frame].location.pathname == '/post.html'
        if (isPost) {
          top.window.frames[frame].location.href = top.window.frames[frame].location.href
        } else {
          top.window.frames[frame].location.reload()
        }
      }
    }
  }
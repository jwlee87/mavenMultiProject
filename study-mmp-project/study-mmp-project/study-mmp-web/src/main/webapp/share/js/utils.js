
var UTILS = {
	"openWin" : function(sURL, sWindowName, w, h, sScroll, reSize, status) {
		// 화면 중앙으로 Popup 띄우기.. 스크롤바는 옵션..
		// ex)
		//		openWin("test.asp", "winTest", 400, 300);			☞ 스크롤바 없음
		//		openWin("test.asp", "winTest", 400, 300, "yes");	☞ 스크롤바 있음
		//		openWin("test.asp", "winTest", 400, 300, "auto");	☞ 스크롤바 자동

		//var _sWindowName = typeof(sWindowName) != "undefined" ? sWindowName : "";
		
		var x = (screen.width - w) / 2;
		var y = (screen.height - h) / 2;

		if (sScroll==null) sScroll = "no";
		if (screen.width >= 800 && screen.height >= 600 ) sScroll = "yes"; // 해상도 800*600일 때 스크롤 생기도록 함 
		
		var sOption = "";
		sOption = sOption + "toolbar=no, channelmode=no, location=no, directories=no, menubar=no";
		sOption = sOption + ", scrollbars=" + sScroll + ", resizable=" + reSize + ", status=" + status +", left=" + x + ", top=" + y + ", width=" + w + ", height=" + h;

		//var win = controlOpenWindow(sURL, sWindowName, sOption);
		var win = window.open(sURL, sWindowName, sOption);

		try {
			win.focus();
		} catch(e) {}

		return win;
	},
		
	"numberKeyPress" : function(e) {
	    var key;
	
	    if(window.event)
	         key = window.event.keyCode; //IE
	    else
	         key = e.which; //firefox
		
	    // backspace or delete or tab
	    var event; 
	    if (key == 0 || key == 8 || key == 46 || key == 9){
	    	event = e || window.event;
		    if (typeof event.stopPropagation != "undefined") {
		        event.stopPropagation();
		    } else {
		        event.cancelBubble = true;
		    }	
			return ;
		}
	
	    if (key < 48 || (key > 57 && key < 96) || key > 105) {
	    	e.preventDefault ? e.preventDefault() : e.returnValue = false;
	    }
	},	

	"isNumber" : function(num) {
		return (/^[0-9]+$/).test(num);
	},
	
	"isEmpty" : function(obj) {
		return jQuery.trim(obj.val()) == "";
	},
	
	"equalsVal" : function(obj1 , obj2){
		return obj1.val() == obj2.val();
	},
	 
	"isValidEmail" : function(obj){
		return this.isValidEmailValue(obj.val());
	},

	"isValidEmailValue" : function(value) {
	    var reg= /^[a-zA-Z0-9._%+-]+@[A-Za-z0-9.-]+\.[a-zA-Z]{2,4}$/i;
		return reg.test(value);
	},
	
	"isValidUrl" : function(value) {
		var expUrl = /^(https?\:\/\/)?(([-\w]+)[.])+(asia|biz|cc|cn|com|de|eu|in|info|jobs|jp|kr|mobi|mx|name|net|nz|org|travel|tv|tw|uk|us)(\/([\&\=\?\w]*))*$/i;
		return expUrl.test(value);
	},

	//문자 길이 체크
	"getStringLength" : function(str) {
		//var retCode = 0;
		var strLength = 0;
		
		for (var i = 0; i < str.length; i++) {
			var code = str.charCodeAt(i);
			var ch = str.substr(i,1).toUpperCase();
			
			code = parseInt(code);
			
			if ((ch < "0" || ch > "9") && (ch < "A" || ch > "Z") && ((code > 255) || (code < 0)))
				strLength = strLength + 2;
			else
				strLength = strLength + 1;
		}
		return strLength;
	},
	
	"cut" : function(str, len, ellipsis) {
		try {
			if(str == null || str == "") return str;

			var strLen = str.length;
			var byteLen = 0;
			var chr = null;
			var newStr = "";
			   
			for (var i = 0; i < strLen; i++) {
			  	chr = str.charAt(i);
			  	byteLen += (escape(chr).length > 4 ? 2 : 1);
			   	if(byteLen >= len) {
			   		return (str == newStr) ? str : newStr + ellipsis;
			   	}
			   	newStr += chr;
			}
			return (str == newStr) ? str : newStr + ellipsis;
		} catch(e) {
			return str;
		}
	}

};

// trim
if (!String.prototype.trim) {
	String.prototype.trim = function () {
		return this.replace(/^\s+|\s+$/g, '');
	};
}

// encodeURIComponent for IE5.0 
if (typeof(encodeURIComponent) != "function") {
    encodeURIComponent = function (s) {
        function toHex (n) {
            var hexchars = "0123456789ABCDEF";
            return  "%" + hexchars.charAt(n>>4) + hexchars.charAt(n&0xF);
        }
        var es  = "" ;
        for (var i = 0; i < s.length;) {
            var c = s.charCodeAt(i++);
            if ((c&0xF800) == 0xD800) {
                var sc = s.charCodeAt(i++);
                c = ((c-0xD800)<<10) + (sc-0xDC00) + 0x10000;
            }
            if (!(c&~0x7F)) {
                if ((c>=65&&c<=90) || (c>=97&&c<=122) || (c>=48&&c<=57) || (c>=45&&c<=46) || c==95 || c==33 || c==126 || (c>=39&&c<=42))
                	es += String.fromCharCode(c) ;
                else
                	es += toHex(c) ;
            }
            else if (!(c&~0x7FF))
            	es += toHex(0xC0+(c>>6)) + toHex(c&0x3F) ;
            else if (!(c&~0xFFFF))
            	es += toHex(0xE0+(c>>12)) + toHex(0x80+(c>>6&0x3F)) + toHex(0x80+(c&0x3F)) ;
            else    
            	es += toHex(0xF0+(c>>18)) + toHex(0x80+(c>>12&0x3F)) + toHex(0x80+(c>>6&0x3F)) + toHex(0x80+(c&0x3F)) ;
        }
        return  es ;
    };
}

Number.prototype.format = function(){
    if(this==0) return 0;
 
    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (this + '');
 
    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');
 
    return n;
};

String.prototype.format = function() {
    var formatted = this;
    for( var arg in arguments ) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};

$.fn.serializeJSON=function() {
	  var json = {};
	  jQuery.map(jQuery(this).serializeArray(), function(n, i) {
	    var _ = n.name.indexOf('[');
	    if (_ > -1) {
	      var o = json;
	      _name = n.name.replace(/\]/gi, '').split('[');
	      for (var i=0, len=_name.length; i<len; i++) {
	        if (i == len-1) {
	          if (o[_name[i]]) {
	            if (typeof o[_name[i]] == 'string') {
	              o[_name[i]] = [o[_name[i]]];
	            }
	            o[_name[i]].push(n.value);
	          }
	          else o[_name[i]] = n.value || '';
	        }
	        else o = o[_name[i]] = o[_name[i]] || {};
	      }
	    }
	    else {
	      if (json[n.name] !== undefined) {
	        if (!json[n.name].push) {
	          json[n.name] = [json[n.name]];
	        }
	        json[n.name].push(n.value || '');
	      }
	      else json[n.name] = n.value || '';      
	    }
	  });
	  return json;
	};	
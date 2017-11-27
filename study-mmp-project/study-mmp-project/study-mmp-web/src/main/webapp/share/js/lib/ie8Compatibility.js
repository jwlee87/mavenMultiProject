/**
 * IE8에서 지원되지 않는 함수 목록
 * 
 * IE8에서 지원되지 않는 함수들은 이곳에 선언한뒤 require.config.js에서 의존시킵니다. 
 */
if (!Array.indexOf) {
  Array.prototype.indexOf = function (obj, start) {
    for (var i = (start || 0); i < this.length; i++) {
      if (this[i] == obj) {
        return i;
      }
    }
  }
}
if (!Object.keys) {
  Object.keys = function(obj) {
    var keys = [];

    for (var i in obj) {
      if (obj.hasOwnProperty(i)) {
        keys.push(i);
      }
    }

    return keys;
  };
}
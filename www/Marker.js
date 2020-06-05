var Marker = tabris.NativeObject.extend('com.eclipsesource.maps.Marker', tabris.Widget);

var EVENT_TYPES = ['tap', 'move'];

Marker.prototype._listen = function (name, listening) {
  if (EVENT_TYPES.indexOf(name) > -1) {
    this._nativeListen(name, listening);
  }
  else {
    tabris.Widget.prototype._listen.call(this, name, listening);
  }
};

Map.prototype._trigger = function (name, event) {
  tabris.Widget.prototype._trigger.call(this, name, event);
};

Marker.prototype.dispose = function () {
  if (this._map) {
    this._map.removeMarker(this);
  }
  this._dispose();
};

Marker.prototype.moveTo = function (values) {
  if (!values.to) throw new Error(values.to + ' is not an array');
  if (values.animate && !values.duration) throw new Error(values.duration + ' is not an integer');
  this._nativeCall('moveTo', values);
};

tabris.NativeObject.defineProperties(Marker.prototype, {
  alpha: { type: 'number', default: 1 },
  position: {
    type: {
      convert(value) {
        if (!Array.isArray(value)) {
          throw new Error(value + ' is not an array');
        }
        return Object.freeze(value);
      }
    },
    nocache: true
  },
  image: { type: 'ImageValue', default: null }
});

module.exports = Marker;

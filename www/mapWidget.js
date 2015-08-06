tabris.registerWidget("ESMap", {
  _properties: {
    liteMode: {type: "boolean", default: false},
    center: "array",
    zoom: "float",
    mapType: {type: ["choice", ["none", "hybrid", "normal", "satellite", "terrain"]]}
  },
  _events: {
    tap: {
      trigger: function(event) {this.trigger("tap", this, event.latLng);}
    },
    longpress: {
      trigger: function(event) {this.trigger("longpress", this, event.latLng);}
    },
    ready: {
      trigger: function() {this.trigger("ready", this);}
    },
    pan: {
      trigger: function(event) {this.trigger("pan", this, event.latLng);}
    }
  },
  animateCamera: function(centerLatLng, zoom) {
    // TODO check types
    this._nativeCall("animateCamera", {
      "centerLatLng": centerLatLng,
      "zoom": zoom
    });
  },
  animateCameraToBounds: function(northWestLatLng, southEastLatLng) {
    // TODO check types
    this._nativeCall("animateCameraToBounds", {
      "northWestLatLng": northWestLatLng,
      "southEastLatLng": southEastLatLng
    });
  },
  animateCameraToPointGroup: function(latLngPointGroup) {
    // TODO check types
    this._nativeCall("animateCameraToPointGroup", {
      "latLngPointGroup": latLngPointGroup
    });
  },
  getMinZoomLevel: function() {
    return this._nativeCall("getMinZoomLevel", {});
  },
  getMaxZoomLevel: function() {
    return this._nativeCall("getMaxZoomLevel", {});
  }
});

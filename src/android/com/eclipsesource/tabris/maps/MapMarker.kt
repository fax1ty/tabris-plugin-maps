package com.eclipsesource.tabris.maps

import android.animation.ObjectAnimator
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MapMarker {

  var position: LatLng? = null
    set(value) {
      field = value
      marker?.let { it.position = value }
    }

  var title: String? = null
    set(value) {
      field = value
      marker?.let { it.title = value }
    }

  var icon: BitmapDescriptor? = null
    set(value) {
      field = value
      marker?.setIcon(value)
    }

  var snippet: String? = null
    set(value) {
      field = value
      marker?.let { it.snippet = value }
    }

  var marker: Marker? = null
    set(value) {
      field = value
      value?.let {
        it.position = position
        it.title = title
        it.setIcon(icon)
        it.snippet = snippet
      }
    }

  var mapId: String? = null

  var animator: ObjectAnimator? = null

}

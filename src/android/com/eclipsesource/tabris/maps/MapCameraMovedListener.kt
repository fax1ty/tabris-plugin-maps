package com.eclipsesource.tabris.maps

import com.eclipsesource.tabris.android.Scope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition

class MapCameraMovedListener(private val mapHolderView: MapHolderView, private val scope: Scope)
    : GoogleMap.OnCameraMoveListener {

    override fun onCameraMove() {
        mapHolderView.googleMap.cameraPosition?.let {
            notifyCameraMoveEvent(it)
        }
    }

    private fun notifyCameraMoveEvent(cameraPosition: CameraPosition) {
        scope.remoteObject(mapHolderView)?.notify("cameraMoved", "camera", mapOf(
                "position" to listOf(cameraPosition.target.latitude, cameraPosition.target.longitude)
        ))
    }
}
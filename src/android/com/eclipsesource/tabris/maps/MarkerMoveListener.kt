package com.eclipsesource.tabris.maps

import android.animation.ValueAnimator
import com.eclipsesource.tabris.android.Scope

class MarkerMoveListener(private val mapMarker: MapMarker, private val scope: Scope) : ValueAnimator.AnimatorUpdateListener {

    override fun onAnimationUpdate(value: ValueAnimator?) {
        val mapId = mapMarker.mapId
        val pos = mapMarker.marker?.position!!
        scope.objectRegistry.find<MapMarker>()
                .find { it.marker?.id == mapMarker.marker?.id && it.mapId == mapId }
                .let { scope.remoteObject(it)?.notify("tap", "position", listOf(pos.latitude, pos.longitude)) }
    }

}

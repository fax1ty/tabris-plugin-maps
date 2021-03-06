package com.eclipsesource.tabris.maps

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.util.Log
import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.ObjectHandler
import com.eclipsesource.tabris.android.Property
import com.eclipsesource.tabris.android.internal.ktx.toList
import com.eclipsesource.v8.V8Object
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MarkerHandler(private val scope: ActivityScope) : ObjectHandler<MapMarker> {

    override val type = "com.eclipsesource.maps.Marker"

    override val properties: List<Property<MapMarker, *>> = listOf(
            MarkerImageProperty(scope),
            MarkerPositionProperty(),
            MarkerSnippetProperty,
            MarkerTitleProperty
    )

    override fun listen(id: String, mapMarker: MapMarker, event: String, listen: Boolean) {
        super.listen(id, mapMarker, event, listen)
        when (event) {
            "move" -> {
                if (listen) attachOnMoveListener(mapMarker)
                else removeOnMoveListener(mapMarker)
            }
        }
    }

    private fun attachOnMoveListener(mapMarker: MapMarker) {
        mapMarker.animator?.addUpdateListener(MarkerMoveListener(mapMarker, scope))
    }

    private fun removeOnMoveListener(mapMarker: MapMarker) {
        mapMarker.animator?.removeAllListeners()
    }

    override fun call(mapMarker: MapMarker, method: String, properties: V8Object) = when (method) {
        "moveTo" -> moveTo(mapMarker, properties)
        // @fax1ty
        else -> null
    }

    private fun moveTo(mapMarker: MapMarker, properties: V8Object) {
        val toPos = properties.getArray("to").toList<Double>()
        val animate = properties.getBoolean("animate")
        val duration = properties.getInteger("duration")
        if (!animate) mapMarker.position = LatLng(toPos[0], toPos[1])
        else {
            MarkerAnimation.animateMarkerTo(mapMarker, LatLng(toPos[0], toPos[1]), duration, LatLngInterpolator.Spherical())
        }
    }

    override fun create(id: String, properties: V8Object) = MapMarker()

    override fun destroy(mapMarker: MapMarker) {
        mapMarker.marker?.remove()
    }

}

object MarkerAnimation {
    fun animateMarkerTo(mapMarker: MapMarker, finalPosition: LatLng, duration: Int, latLngInterpolator: LatLngInterpolator) {
        val typeEvaluator = TypeEvaluator { fraction: Float, a: LatLng?, b: LatLng? -> latLngInterpolator.interpolate(fraction, a!!, b!!) }
        val property = android.util.Property.of(Marker::class.java, LatLng::class.java, "position")
        mapMarker.animator = ObjectAnimator.ofObject(mapMarker.marker, property, typeEvaluator, finalPosition)
        val animator = mapMarker.animator!!
        animator.duration = duration.toLong()
        animator.addUpdateListener { Log.v("fuck", "gg / ${mapMarker.marker?.position?.latitude} ${mapMarker.marker?.position?.longitude}") }
        animator.start()
    }
}

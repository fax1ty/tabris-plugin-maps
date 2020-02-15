package com.eclipsesource.tabris.maps

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import com.eclipsesource.tabris.android.ActivityScope
import com.eclipsesource.tabris.android.ObjectHandler
import com.eclipsesource.tabris.android.Property
import com.eclipsesource.tabris.android.internal.ktx.toList
import com.eclipsesource.v8.V8Object
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class MarkerHandler(scope: ActivityScope) : ObjectHandler<MapMarker> {

    override val type = "com.eclipsesource.maps.Marker"

    override val properties: List<Property<MapMarker, *>> = listOf(
            MarkerImageProperty(scope),
            MarkerPositionProperty(),
            MarkerSnippetProperty,
            MarkerTitleProperty
    )

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
            MarkerAnimation.animateMarkerTo(mapMarker.marker, LatLng(toPos[0], toPos[1]), duration, LatLngInterpolator.Spherical())
        }
    }

    override fun create(id: String, properties: V8Object) = MapMarker()

    override fun destroy(mapMarker: MapMarker) {
        mapMarker.marker?.remove()
    }

}

object MarkerAnimation {
    fun animateMarkerTo(marker: Marker?, finalPosition: LatLng, duration: Int, latLngInterpolator: LatLngInterpolator) {
        val typeEvaluator = TypeEvaluator { fraction: Float, a: LatLng?, b: LatLng? -> latLngInterpolator.interpolate(fraction, a!!, b!!) }
        val property = android.util.Property.of(Marker::class.java, LatLng::class.java, "position")
        val animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition)
        animator.duration = duration.toLong()
        animator.start()
    }
}

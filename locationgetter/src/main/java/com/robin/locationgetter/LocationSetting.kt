package com.robin.locationgetter

import android.app.Activity
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

class LocationSetting(fragment: PermissionHelper, settingCallback: SettingCallback) {

    private val REQUEST_CHECK_SETTINGS = 2
    private var fragment = fragment
    var settingCallback = settingCallback

    interface SettingCallback {

        fun setSettingStatus(status: Boolean)


    }


    fun setResult(requestCode: Int, resultCode: Int) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                settingCallback.setSettingStatus(true)

            } else
                settingCallback.setSettingStatus(false)

        }

    }

    fun checkLocationSetting(locationRequest: LocationRequest) {

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(fragment.getActivity() as Activity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(fragment.getActivity() as Activity) {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            settingCallback.setSettingStatus(true)
        }

        task.addOnFailureListener(fragment.getActivity() as Activity) { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().

                    fragment.startIntentSenderForResult(
                        e.getResolution().getIntentSender()
                        , REQUEST_CHECK_SETTINGS, null
                        , 0, 0, 0, null
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }

            }
        }


    }


}
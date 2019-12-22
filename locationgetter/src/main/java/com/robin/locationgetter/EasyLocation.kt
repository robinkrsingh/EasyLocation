package com.robin.locationgetter

import android.app.Activity
import android.location.Location
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*

class EasyLocation(activity: Activity, easyLocationCallBack: EasyLocationCallBack) {

    private val TAG = "location"
    private var mTrackingLocation: Boolean = true
    private var mLocationCallback: LocationCallback? = null


    private var mFusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            activity
        )


    interface EasyLocationCallBack {

        fun permissionDenied()

        fun locationSettingFailed()

        fun getLocation(location: Location)

    }


    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private val locationRequest: LocationRequest
        get() {
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return locationRequest
        }

    init {

        // Initialize the location callbacks.
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {


                locationResult?.lastLocation?.let { easyLocationCallBack.getLocation(it) }

            }
        }


        var mHeadlessFrag = (activity as FragmentActivity).supportFragmentManager
            .findFragmentByTag(TAG) as PermissionHelper?
        if (mHeadlessFrag == null) {
            mHeadlessFrag =
                PermissionHelper.newInstance(object : PermissionHelper.PermissionListener {


                    override fun fetchLocation() {
                        mTrackingLocation = true
                        mFusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            mLocationCallback!!, null
                        )


                    }

                    override fun permissionStatus(permissionValue: Boolean) {

                        if (permissionValue)
                            fetchLocation()
                        else
                            easyLocationCallBack.permissionDenied()


                    }

                    override fun settingStatus(settingValue: Boolean) {


                        if (settingValue)
                            mHeadlessFrag?.getPermissionStatus()
                        else
                            easyLocationCallBack.locationSettingFailed()


                    }


                })

            activity.supportFragmentManager.beginTransaction().add(mHeadlessFrag, TAG)
                .commit()

            mHeadlessFrag.setLocationRequest(locationRequest)


        }
    }


}
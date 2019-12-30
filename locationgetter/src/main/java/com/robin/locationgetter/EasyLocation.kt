/*
 * Copyright 2019 Robin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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


        var permissionHelper = (activity as FragmentActivity).supportFragmentManager
            .findFragmentByTag(TAG) as PermissionHelper?
        if (permissionHelper == null) {
            permissionHelper =
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
                            permissionHelper?.getPermissionStatus()
                        else
                            easyLocationCallBack.locationSettingFailed()


                    }

                    override fun stopLocationUpdates() {

                        mFusedLocationClient.removeLocationUpdates(mLocationCallback)

                    }


                })

            activity.supportFragmentManager.beginTransaction().add(permissionHelper, TAG)
                .commit()

            permissionHelper.setLocationRequest(locationRequest)


        }
    }


}
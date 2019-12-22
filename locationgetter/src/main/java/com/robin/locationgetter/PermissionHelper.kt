package com.robin.locationgetter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationRequest


class PermissionHelper(permissionListener: PermissionListener) : Fragment(),
    LocationSetting.SettingCallback {

    val REQUEST_LOCATION_PERMISSION = 1

    lateinit var activity: Activity

    lateinit var locationSetting: LocationSetting

    var permissionListener = permissionListener

    private lateinit var locationRequest: LocationRequest

    interface PermissionListener {
        fun fetchLocation()

        fun permissionStatus(permissionValue: Boolean)

        fun settingStatus(settingValue: Boolean)


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        checkLocationSetting(locationRequest)


    }


    fun setLocationRequest(locationRequest: LocationRequest) {
        this.locationRequest = locationRequest
    }

    private fun checkLocationSetting(locationRequest: LocationRequest) {

        locationSetting = LocationSetting(this@PermissionHelper, this)
        locationSetting.checkLocationSetting(locationRequest)

    }

    fun getPermissionStatus() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {

            permissionListener.fetchLocation()


        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {

            this.activity = activity
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnHeadlineSelectedListener")
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {

            when {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                grantResults.isEmpty() -> {
                    Log.i("permission", "User interaction was cancelled.")

                    permissionListener.permissionStatus(false)
                }
                // Permission granted.
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {

                    permissionListener.permissionStatus(true)
                }

                else -> {

                    permissionListener.permissionStatus(false)

                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        locationSetting.setResult(requestCode, resultCode)


    }


    companion object {
        fun newInstance(permissionListener: PermissionListener): PermissionHelper {


            return PermissionHelper(permissionListener)
        }
    }


    override fun setSettingStatus(status: Boolean) {

        permissionListener.settingStatus(status)
    }


}
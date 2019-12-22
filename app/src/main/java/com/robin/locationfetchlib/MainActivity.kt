package com.robin.locationfetchlib

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.robin.locationgetter.EasyLocation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        EasyLocation(this@MainActivity, object: EasyLocation.EasyLocationCallBack{
            override fun permissionDenied() {

                Log.i("Location_", "permission  denied")
                tvLocation.text = "Permission  denied"

            }

            override fun locationSettingFailed() {

                Log.i("Location_", "setting failed")
                tvLocation.text = "Setting failed"

            }

            override fun getLocation(location: Location) {

                Log.i("Location_lat_lng"," latitude ${location?.latitude} longitude ${location?.longitude}")

                tvLocation.text = " latitude ${location?.latitude}, longitude ${location?.longitude}"


            }
        })



    }



}



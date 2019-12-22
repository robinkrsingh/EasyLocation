# EasyLocation
[![](https://jitpack.io/v/robinkrsingh/easylocation.svg)](https://jitpack.io/#robinkrsingh/easylocation)

Location library to fetch location and location updates very easily. It handles location setting and permission by itself.


Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.robinkrsingh:easylocation:0.1.0'
Example:
     
     
     EasyLocation(this@MainActivity, object: EasyLocation.EasyLocationCallBack{
            override fun permissionDenied() {

                Log.i("Location", "permission  denied")
           

            }

            override fun locationSettingFailed() {

                Log.i("Location", "setting failed")
               

            }

            override fun getLocation(location: Location) {

                Log.i("Location_lat_lng"," latitude ${location?.latitude} longitude ${location?.longitude}")

                

            }
        })

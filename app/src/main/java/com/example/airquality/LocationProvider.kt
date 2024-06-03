package com.example.airquality

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

// 디바이스 내 시스템 위치 정보 서비스에 접근하기 위해서 locationmanager를 사용한다.
class LocationProvider(val context: Context) {

    // nullpointerexception 오류 방지를 위해서 null 값으로 선언한다.
    private var location: Location? = null
    private var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    private fun getLocation(): Location? {
        try {
            // MainActivity에서는 context가 있었기 때문에 context를 따로 명시하지 않았다.
            // LocationProvider는 context가 없기 때문에 context를 인수로 받아와서 getSystemService를 사용한다.
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // GPS 위성을 이용하여 위치 정보를 얻을지, 이동통신사 망 정보를 이용하여 위치 정보를 얻을지.
            var gpsLocation: Location? = null
            var networkLocation: Location? = null

            // GPS or Network가 활성화되었는지 확인한다.

            // locationManager 인수를 null로 선언해놓고 try문에서 locationManager를 가져오지 못한다면
            // catch문으로 빠져서 exception을 발생시키기 위해 locationManager 뒤에 !!를 붙인다.(binding null safety랑 같은 원리)
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            // 디바이스 시스템 내 위치 정보에 접근하는 권한이 허용되었는지 확인한다.
            // GPS와 Network 둘 중에 하나라도 불가능하면 서비스 이용이 불가하다.
            if (!isGPSEnabled && !isNetworkEnabled) {
                return null
            } else {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null
                }

                if (isNetworkEnabled) {
                    networkLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                if (isGPSEnabled) {
                    gpsLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }

                // gpsLocation과 networkLocation 둘 중에 위치값이 더 정확한 값으로 설정한다.
                if (gpsLocation != null &&
                    networkLocation != null) {
                    location = if (gpsLocation.accuracy > networkLocation.accuracy) {
                        gpsLocation
                    } else {
                        networkLocation
                    }
                } else { // 둘 중에 하나라도 null값이 있다면 accuracy의 우위를 따지지 않고 null값이 아닌 것으로 설정한다.
                    if (gpsLocation != null) {
                        location = gpsLocation
                    }
                    if (networkLocation != null) {
                        location = networkLocation
                    }
                }
            }
        }catch (e : Exception) {
            e.printStackTrace()
        }

        return location
    }

    // MainActivity에서 locationProvider를 통해 위도, 경도 정보를 가져가려고 할 때 사용된다.
    fun getLocationLatitude(): Double {
        return location?.latitude ?: 0.0
    }

    fun getLocationLongitude(): Double {
        return location?.longitude ?: 0.0
    }
}
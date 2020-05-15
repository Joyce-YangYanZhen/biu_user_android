package com.noplugins.keepfit.userplatform.activity

import android.Manifest
import android.content.Context
import android.util.Log
import android.os.Bundle
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import com.ycuwq.datepicker.time.HourAndMinDialogFragment
import com.ycuwq.datepicker.time.WeightPickerDialogFragment
import kotlinx.android.synthetic.main.activity_gaode_test.*

class GaodeTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaode_test)

        tv_click.setOnClickListener {
            val hour = HourAndMinDialogFragment()
            hour.setOnDateChooseListener { startHour, startMinute, endHour, endMinute ->
                tv_click.setText("$startHour $startMinute $endHour $endMinute")
            }

            hour.show(supportFragmentManager,"HourAndMinDialogFragment")
            //
        }
    }


}

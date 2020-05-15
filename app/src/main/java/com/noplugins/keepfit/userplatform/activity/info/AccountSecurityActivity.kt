package com.noplugins.keepfit.userplatform.activity.info

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.SpUtils
import kotlinx.android.synthetic.main.activity_account_security.*

class AccountSecurityActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_account_security)
        tv_now_phone.text = SpUtils.getString(applicationContext,AppConstants.PHONE)
    }

    override fun doBusiness(mContext: Context?) {
        back_btn.clickWithTrigger {
            finish()
        }
        rl_update_phone.clickWithTrigger{
            val intent = Intent(this,UpdatePhoneActivity::class.java)
            startActivity(intent)
        }
        rl_setting_pwd.clickWithTrigger {
            val intent = Intent(this,SettingPwdActivity::class.java)
            startActivity(intent)
        }
        rl_update_pwd.clickWithTrigger {
            val intent = Intent(this,UpdatePasswordActivity::class.java)
            startActivity(intent)
        }
    }

}

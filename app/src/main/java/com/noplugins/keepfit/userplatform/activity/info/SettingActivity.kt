package com.noplugins.keepfit.userplatform.activity.info

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.LoginActivity
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.PublicPopControl
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.jpush.TagAliasOperatorHelper
import com.noplugins.keepfit.userplatform.util.ActivityCollectorUtil
import com.noplugins.keepfit.userplatform.util.SpUtils
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {
    override fun initBundle(parms: Bundle?) {

    }

    override fun initView() {
        setContentView(R.layout.activity_setting)
     }

    override fun doBusiness(mContext: Context?) {
        back_btn.clickWithTrigger {
            finish()
        }
        rl_mine_info.clickWithTrigger {
            val intent = Intent(this,InformationActivity::class.java)
            startActivity(intent)

        }
        rl_account.clickWithTrigger {
            val intent = Intent(this,AccountSecurityActivity::class.java)
            startActivity(intent)

        }
        rl_about.clickWithTrigger {
            val intent = Intent(this,AboutActivity::class.java)
            startActivity(intent)

        }
        rl_risk.clickWithTrigger {

        }
        rl_xieyi.clickWithTrigger {
            val intent = Intent(this,XieYiActivity::class.java)
            startActivity(intent)

        }
        rl_quit.clickWithTrigger {

            toPhone()
        }
//        back_btn.clickWithTrigger {
//
//        }
    }


    /**
     * 拨打电话
     */
    var m_dialog: Dialog? = null
    private fun toPhone() {

        PublicPopControl.alert_dialog_center(this) { view, popup ->
            val content = view.findViewById<TextView>(R.id.pop_content)
            val title = view.findViewById<TextView>(R.id.pop_title)
            title.setText("确认登出？")
            content.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.cancel_btn)
                .setOnClickListener {
                    popup.dismiss()
                }
            view.findViewById<LinearLayout>(R.id.sure_btn)
                .setOnClickListener {
                    //退出
                    SpUtils.putString(applicationContext,AppConstants.TOKEN,"")
                    SpUtils.putString(applicationContext,AppConstants.PHONE,"")
                    SpUtils.putString(applicationContext,AppConstants.USER_NAME,"")
                    SpUtils.putInt(applicationContext,AppConstants.PROMOTIONS,-1)
                    SpUtils.putString(applicationContext,AppConstants.IS_SET_ALIAS,"")

                    val tagAliasBean = TagAliasOperatorHelper.TagAliasBean()
                    tagAliasBean.alias = ""
                    TagAliasOperatorHelper.getInstance().handleAction(
                        applicationContext,
                        TagAliasOperatorHelper.sequence, tagAliasBean
                    )

                    startActivity(intent)
                    ActivityCollectorUtil.finishAllActivity()
                    popup.dismiss()
                }
        }
    }

}

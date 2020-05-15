package com.noplugins.keepfit.userplatform.mvc_demo

import com.noplugins.keepfit.userplatform.bean.LoginBean
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import java.util.HashMap

class LoginModel {

    fun PasswordLoginModel(
        mvC_LoginActivity: MVC_LoginActivity,
        params: HashMap<String, Any>,
        callback: Login_Password_CallBack
    ) {
        Network.getInstance("登录", mvC_LoginActivity)
            .login(
                params, ProgressSubscriber("登录", object : SubscriberOnNextListener<Bean<LoginBean>> {
                    override fun onNext(result: Bean<LoginBean>) {
                        callback.Success(result)
                    }

                    override fun onError(error: String) {
                        callback.Failed(error)
                    }
                }, mvC_LoginActivity, true)
            )
    }


}









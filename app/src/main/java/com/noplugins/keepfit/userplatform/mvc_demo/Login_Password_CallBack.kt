package com.noplugins.keepfit.userplatform.mvc_demo

import com.noplugins.keepfit.userplatform.bean.LoginBean
import com.noplugins.keepfit.userplatform.util.net.entity.Bean

interface Login_Password_CallBack {
    fun Success(t: Bean<LoginBean>)

    fun Failed(error: String)

}

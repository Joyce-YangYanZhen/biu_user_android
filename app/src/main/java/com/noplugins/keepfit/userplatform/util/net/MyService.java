package com.noplugins.keepfit.userplatform.util.net;


import com.noplugins.keepfit.userplatform.bean.*;
import com.noplugins.keepfit.userplatform.entity.*;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import okhttp3.RequestBody;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * Created by limengtao on 2017/3/17.
 */

public interface MyService {


    /**
     * 获取验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("getVerifyCodeNew")
    Observable<Bean<String>> get_yanzhengma(@Body RequestBody json);

    /**
     * 验证验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("verifyCode")
    Observable<Bean<LoginBean>> check_yanzhengma(@Body RequestBody json);

    /**
     * 修改密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("setPassword")
    Observable<Bean<Object>> setPassword(@Body RequestBody json);

    /**
     * 修改密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("settingPassword")
    Observable<Bean<Object>> settingPassword(@Body RequestBody json);

    /**
     * 修改手机号
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("modificationPhone")
    Observable<Bean<Object>> modificationPhone(@Body RequestBody json);
    /**
     *  获取个人信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("personalData")
    Observable<Bean<InformationBean>> personalData(@Body RequestBody json);

    /**
     *  获取个人信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("setPersonalData")
    Observable<Bean<Object>> setPersonalData(@Body RequestBody json);

    /**
     * 登录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("passwordLogin")
    Observable<Bean<LoginBean>> login(@Body RequestBody json);

    /**
     * 完善资料
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("completeData")
    Observable<Bean<Object>> completeData(@Body RequestBody json);

    /**
     * 权益列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("equityAffirm")
    Observable<Bean<Object>> equityAffirm(@Body RequestBody json);

    /**
     * 权益列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("verifyRights")
    Observable<Bean<Object>> verifyRights(@Body RequestBody json);

    /**
     * 场馆列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findArea")
    Observable<Bean<ChangguanBean>> findArea(@Body RequestBody json);

    /**
     * 场馆详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findAreaDetails")
    Observable<Bean<ChangguanDetailBean>> findAreaDetails(@Body RequestBody json);

    /**
     * 场馆收藏
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("userCollect")
    Observable<Bean<Object>> userCollect(@Body RequestBody json);
    /**
     * 获取时间段 价格
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findPriceArea")
    Observable<Bean<Object>> findPriceArea(@Body RequestBody json);

    /**
     * 场馆团课
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findCourse")
    Observable<Bean<Object>> findCourse(@Body RequestBody json);

    /**
     * 私教列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findteacherList")
    Observable<Bean<PrivateBean>> findteacherList(@Body RequestBody json);

    /**
     * 私教列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findAllTeacher")
    Observable<Bean<Object>> findAllTeacher(@Body RequestBody json);

    /**
     * 私教详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findteacherDetail")
    Observable<Bean<PrivateDetailBean>> findteacherDetail(@Body RequestBody json);

    /**
     * 私教收藏
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("userCollectTeacher")
    Observable<Bean<Object>> userCollectTeacher(@Body RequestBody json);

    /**
     * 私教-课程详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("courseDetail")
    Observable<Bean<Object>> courseDetail(@Body RequestBody json);

    /**
     * 教练可选时间段
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findTeacherTime")
    Observable<Bean<Object>> findTeacherTime(@Body RequestBody json);

    /**
     * 获取地址
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findAllCity")
    Observable<Bean<Object>> findAllCity(@Body RequestBody json);

    /**
     * 获取搜索历史
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("searchHistory")
    Observable<Bean<Object>> searchHistory(@Body RequestBody json);

    /**
     * 清空搜索历史
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("deleteSearchHistory")
    Observable<Bean<Object>> deleteSearchHistory(@Body RequestBody json);

    /**
     * 获取搜索结果
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("searchData")
    Observable<Bean<Object>> searchData(@Body RequestBody json);

    /**
     * 生成订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("confirmOrder")
    Observable<Bean<OrderReult>> confirmOrder(@Body RequestBody json);

    /**
     * 支付-生成订单数据
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("orderPay")
    Observable<Bean<Object>> orderPay(@Body RequestBody json);

    /**
     * 团课
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findCourseList")
    Observable<Bean<TeamBean>> findCourseList(@Body RequestBody json);

    /**
     * 团课详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findCourseListDetail")
    Observable<Bean<TeamDetailBean>> findCourseListDetail(@Body RequestBody json);

    /**
     * 根据订单编号获取订单支付结果
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("getFinalPayResult")
    Observable<Bean<OrderResultBean>> getFinalPayResult(@Body RequestBody json);

    //下面是场馆端的 数据
    //////////////////////////
    //////////////////////////


    /**
     * 修改密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("resetPassword")
    Observable<Bean<Object>> update_password(@FieldMap Map<String, String> map);

    /**
     * 修改密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("choiceRole")
    Observable<Bean<Object>> select_role(@FieldMap Map<String, String> map);


    /**
     * 获取七牛token
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getPicUrl")
    Observable<Bean<Object>> get_qiniu_url(@FieldMap Map<String, String> map);

    /**
     * 提交审核资料
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("submitAudit")
    Observable<Bean<Object>> submit_information(@Body RequestBody json);

    /**
     * 获取审核状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getAuditResult")
    Observable<Bean<Object>> get_check_status(@FieldMap Map<String, String> map);

    /**
     * 获取审核状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST("dayView")
    Observable<Bean<Object>> get_class_resource(@FieldMap Map<String, String> map);

    /**
     * 获取审核状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("addClass")
    Observable<Bean<Object>> add_class(@Body RequestBody map);

    /**
     * 获取审核状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST("resourceList")
    Observable<Bean<Object>> class_list(@FieldMap Map<String, Object> map);

    /**
     * 获取审核状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("getMaxPerson")
    Observable<Bean<Object>> get_max_num(@Body RequestBody json);

    /**
     * 获取审核状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("dayViewList")
    Observable<Bean<Object>> get_month_view(@Body RequestBody json);

    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("selectMessageByType")
    Observable<Bean<MessageDateEntity>> zhanghu_message_list(@Body RequestBody json);
    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("afterSportFace")
    Observable<Bean<Object>> pinglun_biaoqian(@Body RequestBody json);

    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("beforeSportFace")
    Observable<Bean<Object>> beforeSportFace(@Body RequestBody json);


    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findTeacherLabel")
    Observable<Bean<List<LabelEntity>>> get_biaoqian(@Body RequestBody json);
    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("getOrderItemResult")
    Observable<Bean<CheckInEntity>> get_check_in_date(@Body RequestBody json);

    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findTeacherLabel")
    Observable<Bean<Object>> check_in_set_biaoqian(@Body RequestBody json);

    /**
     * 日志消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("myOrderList")
    Observable<Bean<OrderEntity>> get_order_list(@Body RequestBody json);
    /**
     * 取消订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("cancelOrder")
    Observable<Bean<Object>> cancel_order(@Body RequestBody json);
    /**
     * 取消订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findOrderData")
    Observable<Bean<OrderDetailEntity>> order_detail(@Body RequestBody json);

    /**
     * 获取收藏的场馆
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("myAreaAndTeacherCollect")
    Observable<Bean<CollectionChangGuanEntity>> get_collection_changgaun(@Body RequestBody json);
    /**
     * 获取收藏的私教
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("myAreaAndTeacherCollect")
    Observable<Bean<CollectionSIjiaoEntity>> get_collection_sijiao(@Body RequestBody json);
    /**
     * 取消订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("deleteOrder")
    Observable<Bean<Object>> delete_order(@Body RequestBody json);


    /**
     * 获取审核状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("agreeApply")
    Observable<Bean<Object>> agreeApply(@Body RequestBody json);

    /**
     * 获取申请详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("classDetail")
    Observable<Bean<Object>> get_shenqing_detail(@Body RequestBody json);

    /**
     * 获取申请详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("alreadyRead")
    Observable<Bean<Object>> change_status(@Body RequestBody json);

    /**
     * 获取申请详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("messageTotalCount")
    Observable<Bean<Object>> get_message_all(@Body RequestBody json);

    /**
     * 获取申请详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("getFreeTeacher")
    Observable<Bean<Object>> get_teacher_list(@FieldMap Map<String, Object> map);

    /**
     * 邀请
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("inviteTeacher")
    Observable<Bean<Object>> invite(@Body RequestBody json);

    /**
     * 取消邀请
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("cancelInvite")
    Observable<Bean<Object>> cancel_invite(@Body RequestBody json);

    /**
     * 获取用户/产品 统计
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("statistics")
    Observable<Bean<Object>> get_statistics(@Body RequestBody json);

    /**
     * 取消邀请
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("leagueClass")
    Observable<Bean<Object>> class_detail(@Body RequestBody json);

    /**
     * 修改密码
     *
     * @return 是否修改成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("modificationPassWord")
    Observable<Bean<Object>> update_my_password(@Body RequestBody json);

    /**
     * 设置高低峰时段
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("setHighAndLowTime")
    Observable<Bean<Object>> setHighAndLowTime(@Body RequestBody json);

    /**
     * 设置高低峰时段
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("teacherDetail")
    Observable<Bean<Object>> teacherDetail(@Body RequestBody json);


    /**
     * 批量绑定用户
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("bindingRole")
    Observable<Bean<Object>> bindingRole(@Body RequestBody json);

    /**
     * 获取已绑定列表
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findBindingRoles")
    Observable<Bean<Object>> findBindingRoles(@Body RequestBody json);

    /**
     * 批量绑定教练
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("gymBinding")
    Observable<Bean<Object>> bindingTeacher(@Body RequestBody json);

    /**
     * 获取已绑定列表
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findBindingTeachers")
    Observable<Bean<Object>> findBindingTeachers(@Body RequestBody json);

    /**
     * 产品反馈
     *
     * @return 是否设置成功
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("feedBackData")
    Observable<Bean<Object>> feedback(@Body RequestBody json);

    /**
     * 获取我的账户
     *
     * @return 账户信息
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("searchWallet")
    Observable<Bean<Object>> searchWallet(@Body RequestBody json);

    /**
     * 获取我的账单列表
     *
     * @return 账户信息
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("searchWalletDetail")
    Observable<Bean<Object>> searchWalletDetail(@Body RequestBody json);

    /**
     * 我的资料
     **/
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("myself")
    Observable<Bean<Object>> user_information(@Body RequestBody json);

    /**
     * 获取日历
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("yearView")
    Observable<Bean<Object>> get_rili(@Body RequestBody json);
    /**
     * 获取课程
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("dayView")
    Observable<Bean<Object>> get_class_resource_date(@Body RequestBody json);

    /**
     * 消息未读变已读接口
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("messageRead")
    Observable<Bean<Object>> messageRead(@Body RequestBody json);

    /**
     * 运动日志详情
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findSportLogDetail")
    Observable<Bean<Object>> yundongrizh_detail(@Body RequestBody json);
    /**
     * 删除运动日志
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("deleteSportLog")
    Observable<Bean<Object>> shanchu_yundong_rizhi(@Body RequestBody json);
    /**
     * 删除运动日志
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findSportLog")
    Observable<Bean<Object>> yundong_rizhi_liebiao(@Body RequestBody json);

    /**
     * 修改日志详情
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("updateSportLog")
    Observable<Bean<Object>> update_rizhi_detail(@Body RequestBody json);
    /**
     * 修改日志详情
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("userBodyData")
    Observable<Bean<Object>> get_body_resource(@Body RequestBody json);
    /**
     * 修改进阶数据
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("updateUserBody")
    Observable<Bean<Object>> update_jinjie_resource(@Body RequestBody json);

    /**
     * 教练时间判重
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("findTeacherBusyTime")
    Observable<Bean<Object>> findTeacherBusyTime(@Body RequestBody json);


    /**
     * 活动地址
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("getIndexCoupon")
    Observable<Bean<PromotionsBean>> getIndexCoupon(@Body RequestBody json);

    /**
     * 匹配用户最优优惠券
     *
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("matchUserCoupon")
    Observable<Bean<CouponListBean>> matchUserCoupon(@Body RequestBody json);


}

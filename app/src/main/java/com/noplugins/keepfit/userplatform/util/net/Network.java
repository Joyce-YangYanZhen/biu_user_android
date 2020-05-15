package com.noplugins.keepfit.userplatform.util.net;


import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noplugins.keepfit.userplatform.bean.*;
import com.noplugins.keepfit.userplatform.entity.*;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.interceptor.LogInterceptor;
import com.orhanobut.logger.Logger;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.http.Headers;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by shiyujia02 on 2017/8/16.
 */
public class Network {
    public static final int DEFAULT_TIMEOUT = 15;
    private static Network mInstance;
    public MyService service;
    public TestService testService;
    public CoachService coach_service;
    public static String token = "";
    private static String MRTHOD_NAME = "";
    Retrofit retrofit, coach_retrofit, get_test_retrofit;

    public String get_user_url(String str) {
        if (str.equals("test")) {
            return "http://testapi.noplugins.com/api/cust-service/custuser/";
        } else if (str.equals("api2")) {
            return "http://api2.noplugins.com/api/cust-service/custuser/";
        } else if (str.equals("local")) {
            return "http://192.168.1.45:8888/api/cust-service/custuser/";
        } else {
            return "http://kft.ahcomg.com/api/cust-service/custuser/";
        }
    }

    public String get_changguan_url(String str) {
        if (str.equals("test")) {
            return "http://testapi.noplugins.com/api/gym-service/";
        } else if (str.equals("api2")) {
            return "http://api2.noplugins.com/api/gym-service/";
        } else if (str.equals("local")){
            return "http://192.168.1.45:8080/api/gym-service/";
        } else {
            return "http://kft.ahcomg.com/api/gym-service/";
        }
    }

    public String get_coach_url(String str) {
        if (str.equals("test")) {
            return "http://testapi.noplugins.com/api/coach-service/coachuser/";
        } else if (str.equals("api2")) {
            return "http://api2.noplugins.com/api/coach-service/coachuser/";
        } else {
            return "http://kft.ahcomg.com/api/coach-service/coachuser/";
        }
    }

    //获取单例
    public static Network getInstance(String method, Context context) {
        MRTHOD_NAME = method;
        if (context != null) {
            if ("".equals(SpUtils.getString(context, AppConstants.TOKEN))) {
                token = "";
                Logger.e(method + "没有添加token");
            } else {
                token = SpUtils.getString(context, AppConstants.TOKEN);
                Logger.e(method + "添加的token:" + token);
            }
            if (mInstance == null) {
                synchronized (Network.class) {
                    mInstance = new Network(method, context);
                }
            }
        }
        return mInstance;
    }


    private Network(String method, Context context) {
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())//去掉okhttp https证书验证
                .addInterceptor(new LogInterceptor(method))//添加日志拦截器
                .addInterceptor(new Interceptor() {//添加token
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("token", token)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//超时处理
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(get_user_url("test"))//设置请求网址根部
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        get_test_retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(get_changguan_url("test"))//设置请求网址根部
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        coach_retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(get_coach_url("test"))//设置请求网址根部
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        coach_service = coach_retrofit.create(CoachService.class);
        service = retrofit.create(MyService.class);
        testService = get_test_retrofit.create(TestService.class);

    }


    private static class TrustAllManager
            implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType)
                throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType)
                throws CertificateException {
        }
    }

    /**
     * 版本判断
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription update_version(Map<String, Object> params, Subscriber<Bean<VersionEntity>> subscriber) {
        return coach_service.update_version(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription get_yanzhengma(Map<String, Object> params, Subscriber<Bean<String>> subscriber) {
        return service.get_yanzhengma(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 验证验证码
     *
     * @param subscriber
     * @return
     */
    public Subscription check_yanzhengma(Map<String, Object> params, Subscriber<Bean<LoginBean>> subscriber) {
        return service.check_yanzhengma(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 登录
     *
     * @param subscriber
     * @return
     */
    public Subscription login(Map<String, Object> params , Subscriber<Bean<LoginBean>> subscriber) {
        return service.login(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 设置密码
     *
     * @param subscriber
     * @return
     */
    public Subscription setPassword(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.setPassword(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取权益列表
     *
     * @param subscriber
     * @return
     */
    public Subscription equityAffirm(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.equityAffirm(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 确认权益
     *
     * @param subscriber
     * @return
     */
    public Subscription verifyRights(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.verifyRights(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }




    /**
     * 完善资料
     *
     * @param subscriber
     * @return
     */
    public Subscription completeData(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.completeData(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取场馆列表
     *
     * @param subscriber
     * @return
     */
    public Subscription findArea(Map<String, Object> params, Subscriber<Bean<ChangguanBean>> subscriber) {
        return service.findArea(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取场馆详情
     *
     * @param subscriber
     * @return
     */
    public Subscription findAreaDetails(Map<String, Object> params, Subscriber<Bean<ChangguanDetailBean>> subscriber) {
        return service.findAreaDetails(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 场馆收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription userCollect(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.userCollect(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取时间段价格
     *
     * @param subscriber
     * @return
     */
    public Subscription findPriceArea(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findPriceArea(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 场馆团课
     *
     * @param subscriber
     * @return
     */
    public Subscription findCourse(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findCourse(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 私教列表
     *
     * @param subscriber
     * @return
     */
    public Subscription findteacherList(Map<String, Object> params, Subscriber<Bean<PrivateBean>> subscriber) {
        return service.findteacherList(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 私教列表
     *
     * @param subscriber
     * @return
     */
    public Subscription findAllTeacher(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findAllTeacher(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 私教详情
     *
     * @param subscriber
     * @return
     */
    public Subscription findteacherDetail(Map<String, Object> params, Subscriber<Bean<PrivateDetailBean>> subscriber) {
        return service.findteacherDetail(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 私教收藏
     *
     * @param subscriber
     * @return
     */
    public Subscription userCollectTeacher(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.userCollectTeacher(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 私教-课程详情
     *
     * @param subscriber
     * @return
     */
    public Subscription courseDetail(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.courseDetail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 教练可选时间段
     *
     * @param subscriber
     * @return
     */
    public Subscription findTeacherTime(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findTeacherTime(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取地址
     *
     * @param subscriber
     * @return
     */
    public Subscription findAllCity(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findAllCity(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取搜索历史
     *
     * @param subscriber
     * @return
     */
    public Subscription searchHistory(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.searchHistory(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 清空搜索历史
     *
     * @param subscriber
     * @return
     */
    public Subscription deleteSearchHistory(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.deleteSearchHistory(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取搜索结果
     *
     * @param subscriber
     * @return
     */
    public Subscription searchData(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.searchData(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 生成订单
     *
     * @param subscriber
     * @return
     */
    public Subscription confirmOrder(Map<String, Object> params, Subscriber<Bean<OrderReult>> subscriber) {
        return service.confirmOrder(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 支付宝-生成订单数据
     *
     * @param subscriber
     * @return
     */
    public Subscription orderPay(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.orderPay(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 团课
     *
     * @param subscriber
     * @return
     */
    public Subscription findCourseList(Map<String, Object> params, Subscriber<Bean<TeamBean>> subscriber) {
        return service.findCourseList(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 团课详情
     *
     * @param subscriber
     * @return
     */
    public Subscription findCourseListDetail(Map<String, Object> params, Subscriber<Bean<TeamDetailBean>> subscriber) {
        return service.findCourseListDetail(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 设置密码
     *
     * @param subscriber
     * @return
     */
    public Subscription settingPassword(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.settingPassword(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改手机号
     *
     * @param subscriber
     * @return
     */
    public Subscription modificationPhone(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.modificationPhone(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取个人信息
     *
     * @param subscriber
     * @return
     */
    public Subscription personalData(RequestBody params, Subscriber<Bean<InformationBean>> subscriber) {
        return service.personalData(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取个人信息
     *
     * @param subscriber
     * @return
     */
    public Subscription setPersonalData(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.setPersonalData(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 获取个人信息
     *
     * @param subscriber
     * @return
     */
    public Subscription getFinalPayResult(RequestBody params, Subscriber<Bean<OrderResultBean>> subscriber) {
        return service.getFinalPayResult(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    ///////////////////////////////////////////////////////////////
    /////
    //////////////////////////////////////////////////////////////


    /**
     * 修改密码
     *
     * @param subscriber
     * @return
     */
    public Subscription submit_password(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return service.update_password(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 消息列表
     *
     * @param subscriber
     * @return
     */
    public Subscription zhanghu_message_list(Map<String, Object> params, Subscriber<Bean<MessageDateEntity>> subscriber) {
        return service.zhanghu_message_list(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * checkout评论
     *
     * @param subscriber
     * @return
     */
    public Subscription pinglun_biaoqian(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.pinglun_biaoqian(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * check in评论
     *
     * @param subscriber
     * @return
     */
    public Subscription beforeSportFace(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.beforeSportFace(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取标签
     *
     * @param subscriber
     * @return
     */
    public Subscription get_biaoqian(Map<String, Object> params, Subscriber<Bean<List<LabelEntity>>> subscriber) {
        return service.get_biaoqian(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取check in数据
     *
     * @param subscriber
     * @return
     */
    public Subscription get_check_in_date(Map<String, Object> params, Subscriber<Bean<CheckInEntity>> subscriber) {
        return service.get_check_in_date(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 设置check in标签
     *
     * @param subscriber
     * @return
     */
    public Subscription check_in_set_biaoqian(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.check_in_set_biaoqian(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取订单列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_order_list(Map<String, Object> params, Subscriber<Bean<OrderEntity>> subscriber) {
        return service.get_order_list(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 取消订单
     *
     * @param subscriber
     * @return
     */
    public Subscription cancel_order(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.cancel_order(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 订单详情
     *
     * @param subscriber
     * @return
     */
    public Subscription order_detail(Map<String, Object> params, Subscriber<Bean<OrderDetailEntity>> subscriber) {
        return service.order_detail(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取收藏场馆列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_collection_changgaun(Map<String, Object> params, Subscriber<Bean<CollectionChangGuanEntity>> subscriber) {
        return service.get_collection_changgaun(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取收藏场馆列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_collection_sijiao(Map<String, Object> params, Subscriber<Bean<CollectionSIjiaoEntity>> subscriber) {
        return service.get_collection_sijiao(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除订单
     *
     * @param subscriber
     * @return
     */
    public Subscription delete_order(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.delete_order(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    private RequestBody retuen_json_params(Map<String, Object> params) {
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        Logger.e(MRTHOD_NAME + "->请求参数打印：->" + json_params);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        return requestBody;
    }


    /**
     * 获取消息总数
     *
     * @param subscriber
     * @return
     */
    public Subscription get_message_all(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.get_message_all(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取教练列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_teacher_list(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.get_teacher_list(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取教练列表
     *
     * @param subscriber
     * @return
     */
    public Subscription invite(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.invite(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取教练列表
     *
     * @param subscriber
     * @return
     */
    public Subscription cancel_invite(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.cancel_invite(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取统计信息
     *
     * @param subscriber
     * @return
     */
    public Subscription get_statistics(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.get_statistics(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改密码
     *
     * @param subscriber
     * @return
     */
    public Subscription update_my_password(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.update_my_password(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 设置高低峰时段
     *
     * @param subscriber
     * @return
     */
    public Subscription setHighAndLowTime(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.setHighAndLowTime(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取课程详情
     *
     * @param subscriber
     * @return
     */
    public Subscription class_detail(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.class_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取课程详情
     *
     * @param subscriber
     * @return
     */
    public Subscription teacherDetail(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.teacherDetail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 批量绑定角色
     *
     * @param subscriber
     * @return
     */
    public Subscription binding_role(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.bindingRole(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取角色列表
     *
     * @param subscriber
     * @return
     */
    public Subscription findBindingRoles(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findBindingRoles(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 批量绑定教练
     *
     * @param subscriber
     * @return
     */
    public Subscription binding_teacher(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.bindingTeacher(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取教练列表
     *
     * @param subscriber
     * @return
     */
    public Subscription findBindingTeachers(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.findBindingTeachers(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 产品反馈
     *
     * @param subscriber
     * @return
     */
    public Subscription feedback(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.feedback(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我的账户
     *
     * @param subscriber
     * @return
     */
    public Subscription searchWallet(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.searchWallet(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取我的账单列表
     *
     * @param subscriber
     * @return
     */
    public Subscription searchWalletDetail(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.searchWalletDetail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 个人资料
     *
     * @param subscriber
     * @return
     */
    public Subscription user_information(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.user_information(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 个人资料
     *
     * @param subscriber
     * @return
     */
    public Subscription get_rili(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.get_rili(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取课程列表
     *
     * @param subscriber
     * @return
     */
    public Subscription get_class_resource_date(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.get_class_resource_date(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 消息未读变已读接口
     *
     * @param subscriber
     * @return
     */
    public Subscription messageRead(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.messageRead(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 运动日志详情
     *
     * @param subscriber
     * @return
     */
    public Subscription yundongrizh_detail(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.yundongrizh_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除运动日志
     *
     * @param subscriber
     * @return
     */
    public Subscription shanchu_yundong_rizhi(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.shanchu_yundong_rizhi(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 运动日志列表
     *
     * @param subscriber
     * @return
     */
    public Subscription yundong_rizhi_liebiao(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.yundong_rizhi_liebiao(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改日志详情
     *
     * @param subscriber
     * @return
     */
    public Subscription update_rizhi_detail(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.update_rizhi_detail(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获得身体数据
     *
     * @param subscriber
     * @return
     */
    public Subscription get_body_resource(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.get_body_resource(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改进阶数据
     *
     * @param subscriber
     * @return
     */
    public Subscription update_jinjie_resource(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return service.update_jinjie_resource(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //    /**
//     * 数据字典
//     * @param params
//     * @param subscriber
//     * @return
//     */
    public Subscription get_types(Map<String, Object> params, Subscriber<Bean<List<DictionaryeBean>>> subscriber) {
        return testService.get_types(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //微信测试 weixinPay
    public Subscription weixinPay(RequestBody params, Subscriber<Bean<Object>> subscriber) {
        return testService.weixinPay(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取七牛token
     *
     * @param subscriber
     * @return
     */
    public Subscription get_qiniu_token(Map<String, String> params, Subscriber<Bean<Object>> subscriber) {
        return testService.get_qiniu_token(params)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 查询教练的上课时间
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription findTeacherBusyTime(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return service.findTeacherBusyTime(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 活动地址 1.1
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription getIndexCoupon(Map<String, Object> params, Subscriber<Bean<PromotionsBean>> subscriber) {
        return service.getIndexCoupon(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 匹配用户最优优惠券 1.1
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription matchUserCoupon(Map<String, Object> params, Subscriber<Bean<CouponListBean>> subscriber) {
        return service.matchUserCoupon(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * test 银联
     *
     * @param params
     * @param subscriber
     * @return
     */
    public Subscription unionPay(Map<String, Object> params, Subscriber<Bean<Object>> subscriber) {
        return testService.unionPay(retuen_json_params(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }



    private interface TestService {
        //wx

        /**
         * 修改进阶数据
         */
        @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
        @POST("weixinPay")
        Observable<Bean<Object>> weixinPay(@Body RequestBody json);

        /**
         * 数据字典
         *
         * @return
         */
        @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
        @POST("searchDict")
        Observable<Bean<List<DictionaryeBean>>> get_types(@Body RequestBody json);

        /**
         *  ceshi
         * @return
         */
        @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
        @POST("unionPay")
        Observable<Bean<Object>> unionPay(@Body RequestBody json);

        /**
         * 获取七牛token
         *
         * @return
         */
        @FormUrlEncoded
        @POST("getPicToken")
        Observable<Bean<Object>> get_qiniu_token(@FieldMap Map<String, String> map);

    }

}

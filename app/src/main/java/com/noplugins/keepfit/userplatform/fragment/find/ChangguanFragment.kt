package com.noplugins.keepfit.userplatform.fragment.find

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andview.refreshview.XRefreshView
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.changguan.ChaungguanDetailActivity
import com.noplugins.keepfit.userplatform.adapter.ChangguanFindAdapter
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter
import com.noplugins.keepfit.userplatform.base.BaseFragment
import com.noplugins.keepfit.userplatform.bean.ChangguanBean
import com.noplugins.keepfit.userplatform.callback.MessageEvent
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.pop.SpinnerPopWindow
import kotlinx.android.synthetic.main.fragment_find_changguan.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set
import kotlin.collections.toMutableList


class ChangguanFragment : BaseFragment() {

    var newView: View? = null
    var data: ArrayList<ChangguanBean.ChangguanEntity> = ArrayList()
    var adapter: ChangguanFindAdapter? = null

    var currentLat:Double = 0.00
    var currentLon:Double = 0.00

    var isPinfen = false
    var isYongchi = false
    var changguanType = 0

    private var is_not_more = true

    private var isDialog = true

    var page:Int = 1
    var maxPage = -1

    var formType = -1 //如果是从我的收藏里取消的
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (newView == null) {
            newView = inflater.inflate(R.layout.fragment_find_changguan, container, false)
            EventBus.getDefault().register(this)
        }
        currentLat = SpUtils.getString(context,AppConstants.LAT,"121.473701").toDouble()
        currentLon = SpUtils.getString(context,AppConstants.LON,"31.230416").toDouble()

        return newView

    }

    override fun onFragmentFirstVisible() {
        initview()
        Log.d("CHangGuan","onActivityCreated")
        page = 1
        requestChuangguan()
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        super.onFragmentVisibleChange(isVisible)
        requestChuangguan()
    }

    /**
     * eventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(data: MessageEvent) {
//        if (currentLat == data.currentLat && currentLon == data.currentLon){
//            return
//        }
        if (data.currentLat==0.0){
            return
        }
        currentLat = SpUtils.getString(context,AppConstants.LAT).toDouble()
        currentLon = SpUtils.getString(context,AppConstants.LON).toDouble()
        page = 1
        requestChuangguan()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(data1: String) {
       if (data1 == "场馆收藏了"){
           formType =3
       }
    }

    override fun onResume() {
        super.onResume()
        if (formType == 3){
            page = 1
            formType = -1
            requestChuangguan()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CHangGuan","onViewCreated")


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    private fun showPopwindow(pop: SpinnerPopWindow<String>, view: TextView){
        pop.width = view.width
        pop.showAsDropDown(view)

    }
    private var popWindow: SpinnerPopWindow<String>? = null
    private fun initview() {

        val listClass = resources.getStringArray(R.array.identify_types).toMutableList()
        popWindow = SpinnerPopWindow(context,
            listClass,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                main_occupation.setText(listClass.get(position))
                changguanType = if (position == listClass.size -1){
                -1
            } else {
                position + 1
            }
                isDialog = true
                page = 1
                requestChuangguan()
                popWindow!!.dismiss()
            })
        main_occupation.setOnClickListener {
            showPopwindow(popWindow!!,main_occupation)

        }

        tv_pingfen.setOnClickListener {
            isPinfen = if (!isPinfen){
                tv_pingfen.setTextColor(resources.getColor(R.color.btn_text_color))
                true
            } else {
                tv_pingfen.setTextColor(resources.getColor(R.color.color_6D7278))
                false
            }
            isDialog = true
            page = 1
            requestChuangguan()
        }
        iv_to_top.setOnClickListener {
            if (data.size > 0){
                rv_list.smoothScrollToPosition(0)
            }
        }

        tv_yongchi.setOnClickListener {
            isYongchi = if (!isYongchi){
                tv_yongchi.setTextColor(resources.getColor(R.color.btn_text_color))
                true
            } else {
                tv_yongchi.setTextColor(resources.getColor(R.color.color_6D7278))
                false
            }
            isDialog = true
            page = 1
            requestChuangguan()
        }

        rv_list.layoutManager = LinearLayoutManager(context)
        adapter = ChangguanFindAdapter(data)
        rv_list.adapter = adapter
        val view = LayoutInflater.from(context).inflate(R.layout.enpty_view, rv_list, false)
        val txet = view.findViewById<TextView>(R.id.tv_empty_tips)
        val iv  = view.findViewById<ImageView>(R.id.iv_empty_view)
        iv.setImageResource(R.drawable.no_list_data)
        txet.text = "木有搜索到相关内容哇～"
        adapter!!.emptyView = view
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            if (BaseUtils.isFastClick()){
                if ( data.size>=position+1){
                    val intent = Intent(activity,
                        ChaungguanDetailActivity::class.java)
                    intent.putExtra("areaNum",data[position].areaNum)
                    startActivity(intent)
                }
            }

        }



        xrefreshview.setEnableLoadMore(true)
        xrefreshview.setEnableRefresh(true)
        xrefreshview.setOnRefreshListener {
            //下拉刷新
            Handler().postDelayed({
                page = 1
                //填写刷新数据的网络请求，一般page=1，List集合清空操作
//                data.clear()
                isDialog = false
                requestChuangguan()
                if (xrefreshview!=null){
                    xrefreshview.finishRefresh()
                }

                xrefreshview.setEnableLoadMore(true)
            }, 1000)//2000是刷新的延时，使得有个动画效果
        }
        xrefreshview.setOnLoadMoreListener {
            //上拉加载
            Handler().postDelayed({
                page += 1
                isDialog = false
                requestChuangguan()

            }, 1000)//2000是刷新的延时，使得有个动画效果
            xrefreshview.finishLoadMore()
        }
    }

    /**
     * 请求场馆数据
     */
    private fun requestChuangguan(){
        val params = HashMap<String, Any>()
        params["longitude"] = currentLon
        params["latitude"] = currentLat
        params["page"] = page

        params["userNum"] = SpUtils.getString(activity,AppConstants.USER_NAME)
        if (isPinfen) {
            params["grade"] = 1
        }
        if (isYongchi){
            params["haveSwim"] = 1
        }
        if (changguanType > 0){
            params["type"] = changguanType
        }

        if (SpUtils.getString(activity,AppConstants.PROVINCE,"") != ""){
            params["province"] = SpUtils.getString(activity,AppConstants.PROVINCE)
        }
        if (SpUtils.getString(activity,AppConstants.CITY,"") != ""){
            params["city"] = SpUtils.getString(activity,AppConstants.CITY)
        }
        if (SpUtils.getString(activity,AppConstants.DISTRICT,"") != ""){
            params["district"] = SpUtils.getString(activity,AppConstants.DISTRICT)
        }

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)


        val subscription = Network.getInstance("获取场馆列表", activity)
            .findArea(params,
                ProgressSubscriber("",object : SubscriberOnNextListener<Bean<ChangguanBean>> {
                        override fun onNext(result: Bean<ChangguanBean>) {
                            tv_cg_num.text = "共${result.data.totalNum}家场馆"
                            maxPage = result.data.maxPage
//                            if (result.data.areaList.size == 0){
//                                xrefreshview.setEnableLoadMore(false)
//                            }
//                            if (page == maxPage) {
//                                xrefreshview.setEnableLoadMore(false)
//                            } else {
//                                xrefreshview.setEnableLoadMore(true)
//                            }
                            if (page == 1) {
                                update(result.data)
                            } else {
                                add(result.data)
                            }
                        }

                        override fun onError(error: String) {
//                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    }, activity, false
                )
            )
    }

    private fun update(bean:ChangguanBean){
        data.clear()
        data.addAll(bean.areaList)
        adapter!!.notifyDataSetChanged()

    }

    private fun add(bean:ChangguanBean){
        data.addAll(bean.areaList)
        adapter!!.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(title: String): ChangguanFragment {
            val fragment = ChangguanFragment()
            val args = Bundle()
            args.putString("home_fragment_title", title)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStop() {
        super.onStop()

    }
    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}

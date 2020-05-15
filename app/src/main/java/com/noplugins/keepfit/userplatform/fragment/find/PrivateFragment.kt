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
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter
import com.noplugins.keepfit.userplatform.adapter.PrivateFindAdapter
import com.noplugins.keepfit.userplatform.base.BaseFragment
import com.noplugins.keepfit.userplatform.bean.DictionaryeBean
import com.noplugins.keepfit.userplatform.bean.PrivateBean
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
import kotlinx.android.synthetic.main.fragment_find_private.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set
import kotlin.collections.toMutableList

class PrivateFragment : BaseFragment() {
    var newView: View? = null

    var private_class_type = -1
    var private_year_type = -1
    var private_sex_type = -1

    var currentLat: Double = 0.00
    var currentLon: Double = 0.00

    var page = 1
    private var is_not_more = true

    var adapter: PrivateFindAdapter? = null
    var data: ArrayList<PrivateBean.GenTeacherListBean> = ArrayList()

    var skillType = -1
    var yaerSelect = -1
    var sexSelect = -1
    var maxPage = -1

    var formType = -1 //如果是从我的收藏里取消的

    private var isDialog = true
    companion object {
        fun newInstance(title: String): PrivateFragment {
            val fragment = PrivateFragment()
            val args = Bundle()
            args.putString("home_fragment_title", title)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (newView == null) {
            newView = inflater.inflate(R.layout.fragment_find_private, container, false)
            EventBus.getDefault().register(this)
            currentLat = SpUtils.getString(context,AppConstants.LAT,"121.473701").toDouble()
            currentLon = SpUtils.getString(context,AppConstants.LON,"31.230416").toDouble()
        }
        return newView

    }


    override fun onFragmentFirstVisible() {

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){

        }
    }

    /**
     * eventBus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(data: MessageEvent) {
//        if (currentLat == data.currentLat && currentLon == data.currentLon) {
//            return
//        }
        if (data.currentLat==0.0){
            return
        }
        currentLat = SpUtils.getString(context,AppConstants.LAT).toDouble()
        currentLon = SpUtils.getString(context,AppConstants.LON).toDouble()
        page = 1
        requestPrivate()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(data1 : String) {
        if (data1 == "收藏了私教"){
            formType = 3
        }

    }

    override fun onResume() {
        super.onResume()
        if (formType == 3){
            page = 1
            formType = -1
            requestPrivate()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initAdapter()
        requestDoc()
        page = 1
        requestPrivate()
    }


    private fun showPopwindow(pop: SpinnerPopWindow<String>, view: TextView){
        pop.width = view.width
        pop.showAsDropDown(view)

    }
    private var popWindow2: SpinnerPopWindow<String>? = null
    private var popWindow3: SpinnerPopWindow<String>? = null
    private var popWindow: SpinnerPopWindow<String>? = null
    private fun initView() {

        val listYear = resources.getStringArray(R.array.private_year_types).toMutableList()
        popWindow2 = SpinnerPopWindow(context,
            listYear,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                private_year.setText(listYear.get(position))

                popWindow2!!.dismiss()
                yaerSelect = if (position == listYear.size -1){
                    -1
                } else {
                    position + 1
                }
                page = 1
                requestPrivate()
                popWindow2!!.dismiss()
            })
        private_year.setOnClickListener {
            showPopwindow(popWindow2!!,private_year)

        }

        val listSex = resources.getStringArray(R.array.private_sex_types).toMutableList()
        popWindow3 = SpinnerPopWindow(context,
            listSex,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                private_sex.setText(listSex.get(position))
                popWindow3!!.dismiss()
                sexSelect = if (position == listSex.size -1){
                -1
            } else {
                position
            }
                page = 1
                requestPrivate()
                popWindow3!!.dismiss()
            })
        private_sex.setOnClickListener {
            showPopwindow(popWindow3!!,private_sex)

        }

    }

    private fun initAdapter() {
        rv_list.layoutManager = LinearLayoutManager(context)
        adapter = PrivateFindAdapter(data)
        rv_list.adapter = adapter
        val view = LayoutInflater.from(context).inflate(R.layout.enpty_view, rv_list, false)
        val txet = view.findViewById<TextView>(R.id.tv_empty_tips)
        val iv  = view.findViewById<ImageView>(R.id.iv_empty_view)
        iv.setImageResource(R.drawable.no_list_data)
        txet.text = "木有搜索到相关内容哇～"
        adapter!!.emptyView = view

        adapter!!.setOnItemClickListener { adapter, view, position ->
           if (BaseUtils.isFastClick()){
               val intent = Intent(
                   activity,
                   PrivateDetailActivity::class.java
               )
               intent.putExtra("teacherNum", data[position].teacherNum)
               startActivity(intent)
           }
        }


        xrefreshview.setEnableLoadMore(true)
        xrefreshview.setEnableRefresh(true)
        xrefreshview.setOnRefreshListener {
            //下拉刷新
            Handler().postDelayed({
                page = 1
                //填写刷新数据的网络请求，一般page=1，List集合清空操作
                requestPrivate()
                xrefreshview.finishRefresh()
                isDialog = false
                xrefreshview.setEnableLoadMore(true)
            }, 1000)//2000是刷新的延时，使得有个动画效果
        }
        xrefreshview.setOnLoadMoreListener {
            //上拉加载
            if (page < maxPage){
                page += 1
                isDialog = false
                requestPrivate()
            }
            xrefreshview.finishLoadMore()

        }

    }

    private fun requestDoc(){
        val params = HashMap<String, Any>()
        params["object"] = 7
        val subscription = Network.getInstance("获取数据字典", activity)
            .get_types(params,ProgressSubscriber("",object:SubscriberOnNextListener<Bean<List<DictionaryeBean>>>{
                override fun onError(error: String?) {
                    Toast.makeText(activity,"获取私教课程类型失败",Toast.LENGTH_SHORT).show()
                }

                override fun onNext(bean: Bean<List<DictionaryeBean>>) {
                    val listClass:MutableList<String> = ArrayList()
                    bean.data.forEach {
                        listClass.add(it.name)
                    }
                    listClass.add("全部")
                    popWindow = SpinnerPopWindow(context,
                        listClass,
                        PopUpAdapter.OnItemClickListener { _, _, position ->
                            if (listClass.get(position).length > 4){
                                private_class.text =  listClass.get(position).substring(0,3)+"..."
                            } else{
                                private_class.text = listClass.get(position)
                            }

                            skillType= if (position == listClass.size -1){
                                -1
                            } else {
                                position + 1
                            }
                            page = 1
                            requestPrivate()
                            popWindow!!.dismiss()
                        })
                    private_class.setOnClickListener {
                        showPopwindow(popWindow!!,private_class)

                    }
                }

            }, activity, false
            )
            )
    }
    private fun requestPrivate(){
        val params = HashMap<String, Any>()
        params["page"] = page
        params["longitude"] = currentLon
        params["latitude"] = currentLat
        params["userNum"] = SpUtils.getString(activity, AppConstants.USER_NAME)
        if (skillType > 0){
            params["skill"] = skillType
        }
        if (sexSelect > -1){
            params["sex"] = sexSelect
        }
        if (yaerSelect > -1){
            params["year"] = yaerSelect
        }


        if (SpUtils.getString(activity,AppConstants.PROVINCE,"") != ""){
            params["provincecode"] = SpUtils.getString(activity,AppConstants.PROVINCE)
        }
        if (SpUtils.getString(activity,AppConstants.CITY,"") != ""){
            params["citycode"] = SpUtils.getString(activity,AppConstants.CITY)
        }
        if (SpUtils.getString(activity,AppConstants.DISTRICT,"") != ""){
            params["districtcode"] = SpUtils.getString(activity,AppConstants.DISTRICT)
        }

        val subscription = Network.getInstance("获取教练列表", activity)
            .findteacherList(params,ProgressSubscriber("",object:SubscriberOnNextListener<Bean<PrivateBean>>{
                override fun onError(error: String?) {
                    Toast.makeText(activity,"私教获取失败",Toast.LENGTH_SHORT).show()
                }

                override fun onNext(bean: Bean<PrivateBean>) {
                    tv_private_num.text = "共${bean.data.totalNum}位私教"
                    maxPage = bean.data.maxPage
                    if (page >= maxPage) {
                        xrefreshview.setEnableLoadMore(false)
                    } else {
                        xrefreshview.setEnableLoadMore(true)
                    }
                    if (bean.data.totalNum <= 10){
                        xrefreshview.setEnableLoadMore(false)
                    }
                    if (page == 1) {
                        update(bean.data)
                    } else {
                        xrefreshview.finishLoadMore()
                        add(bean.data)
                    }
                }

            }, activity, isDialog
                )
            )
    }

    private fun update(bean: PrivateBean) {
        data.clear()
        data.addAll(bean.genTeacherList)
        adapter!!.notifyDataSetChanged()

    }

    private fun add(bean: PrivateBean) {
        data.addAll(bean.genTeacherList)
        adapter!!.notifyDataSetChanged()
    }


    override fun onStop() {
        super.onStop()

    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

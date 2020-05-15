package com.noplugins.keepfit.userplatform.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andview.refreshview.XRefreshView
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.changguan.ChaungguanDetailActivity
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity
import com.noplugins.keepfit.userplatform.activity.team.TeamDetailActivity
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter
//import com.noplugins.keepfit.userplatform.adapter.SearchAdapter
import com.noplugins.keepfit.userplatform.adapter.SearchDataAdapter
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.SearchBean
import com.noplugins.keepfit.userplatform.bean.SearchHistoryBean
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.global.clickWithTrigger
import com.noplugins.keepfit.userplatform.util.ActivityCollectorUtil
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.pop.SpinnerPopWindow
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_search.*
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : BaseActivity() {

    var adapter: SearchDataAdapter?=null
    var list1 : MutableList<SearchBean.AreaBean> = ArrayList()
    var searchWord = ArrayList<String>()

    var page = 1
    var maxPage = -1
    var type = 1
    private var is_not_more = true
    override fun initBundle(parms: Bundle?) {

    }

    var empty :View ?= null
    override fun initView() {
        setContentView(R.layout.activity_search)
        val layoutManager = LinearLayoutManager(this)
        rv_search_list.layoutManager = layoutManager

        empty= LayoutInflater.from(this).inflate(R.layout.enpty_view, null, false)
        (empty!!.findViewById(R.id.iv_empty_view) as ImageView).setBackgroundResource(R.drawable.no_list_data)
        (empty!!.findViewById(R.id.tv_empty_tips) as TextView).text = "木有搜索到相关内容哇~"

        initAdapter()
        requestSearchHistory()

    }

    private fun initAdapter(){

        adapter = SearchDataAdapter(list1)

        rv_search_list.adapter = adapter
//        rv_search_list.setEmptyView(empty)
        val view = LayoutInflater.from(this).inflate(R.layout.enpty_view, rv_search_list, false)
        val txet = view.findViewById<TextView>(R.id.tv_empty_tips)
        val iv  = view.findViewById<ImageView>(R.id.iv_empty_view)
        iv.setImageResource(R.drawable.no_list_data)
        txet.text = "木有搜索到相关内容哇～"
        adapter!!.emptyView = view
        if (adapter!!.itemCount>0){
            ll_search_history.visibility = View.GONE
        }
        adapter!!.setOnItemChildClickListener  { adapter, view, position ->

            Log.d("Tag","adapter.getItemViewType(position):"+adapter.getItemViewType(position))
            when(adapter.getItemViewType(position)){
                1->{
                    val intent = Intent(this, ChaungguanDetailActivity::class.java)
                    intent.putExtra("areaNum", list1[position].areaNum)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this, PrivateDetailActivity::class.java)
                    intent.putExtra("teacherNum", list1[position].teacherNum)
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(this, TeamDetailActivity::class.java)
                    intent.putExtra("courseNum", list1[position].courseNum)
                    startActivity(intent)
                }
            }
        }



//设置是否可以下拉刷新

        xrefreshview.pullRefreshEnable = true

        //设置是否可以上拉加载

        xrefreshview.pullLoadEnable = true
        xrefreshview.setAutoRefresh(false)

        xrefreshview.setMoveForHorizontal(true)

        xrefreshview.setAutoLoadMore(false)

        xrefreshview.setOnRecyclerViewScrollListener(object : RecyclerView.OnScrollListener() {
        })

        xrefreshview.setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {
            override fun onRefresh(isPullDown: Boolean) {
                Handler().postDelayed({
                    page = 1
                    //填写刷新数据的网络请求，一般page=1，List集合清空操作
                    list1.clear()
                    requestSearchData()
                    xrefreshview.stopRefresh()
                }, 1000)//2000是刷新的延时，使得有个动画效果
            }

            override fun onLoadMore(isSilence: Boolean) {
                Handler().postDelayed({
                    page = page + 1
                    if (page > maxPage){
                        xrefreshview.setLoadComplete(true)
                    } else{
                        requestSearchData()
                    }
                    xrefreshview.stopLoadMore(true)
                }, 1000)//2000是刷新的延时，使得有个动画效果
            }
        })
    }

    private fun requestSearchHistory(){
        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("获取搜索历史", applicationContext)
            .searchHistory(
                requestBody,
                ProgressSubscriberNew(
                    SearchHistoryBean::class.java,
                    object : GsonSubscriberOnNextListener<SearchHistoryBean> {
                        override fun on_post_entity(code: SearchHistoryBean, get_message_id: String) {
                            searchWord.addAll(code.history)
                            initSearchHistory()
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "获取搜索历史失败：$error")
                            Toast.makeText(applicationContext, "获取搜索历史失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun requestSearchData(){
        if(edit_search.text.toString() == ""){
            Toast.makeText(applicationContext, "搜索内容不能为空！", Toast.LENGTH_SHORT).show()

            return
        }

        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        params["data"] = edit_search.text.toString()
        params["longitude"] = SpUtils.getString(applicationContext, AppConstants.LON).toDouble()
        params["latitude"] = SpUtils.getString(applicationContext, AppConstants.LAT).toDouble()
        params["page"] = page
        params["type"] = type
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("搜索内容", applicationContext)
            .searchData(
                requestBody,
                ProgressSubscriberNew(
                    SearchBean::class.java,
                    object : GsonSubscriberOnNextListener<SearchBean> {
                        override fun on_post_entity(code: SearchBean, get_message_id: String) {
                            if (page == 1){
                                list1.clear()
                            }
                            maxPage = code.maxPage
                            if (page == maxPage){
                                xrefreshview.setLoadComplete(true)
                            } else {
                                xrefreshview.setLoadComplete(false)
                            }
                            ll_search_history.visibility = View.GONE
                            list1.addAll(code.list)
                            adapter!!.notifyDataSetChanged()
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "搜索失败：$error")
                            Toast.makeText(applicationContext, "搜索失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    private fun requestDeletedHistory(){
        val params = HashMap<String, Any>()
        params["userNum"] = SpUtils.getString(applicationContext, AppConstants.USER_NAME)
        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)

        subscription = Network.getInstance("清空搜索历史", applicationContext)
            .deleteSearchHistory(
                requestBody,
                ProgressSubscriberNew(
                    String::class.java,
                    object : GsonSubscriberOnNextListener<String> {
                        override fun on_post_entity(code: String, get_message_id: String) {
                            ll_search_history.visibility = View.GONE
                            searchWord.clear()
                        }
                    },
                    object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Logger.e("", "清空搜索历史失败：$error")
                            Toast.makeText(applicationContext, "清空搜索历史失败！", Toast.LENGTH_SHORT).show()
                        }
                    },
                    this,
                    true
                )
            )
    }

    //
    private fun initSearchHistory(){
        xrefreshview.visibility = View.GONE
        if (searchWord.size <= 0){
            ll_search_history.visibility = View.GONE
            return
        }
        ll_search_history.visibility = View.VISIBLE
        val layoutParams =
            ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(30, 30, 30, 30)
        for (i in 0 until searchWord.size){
            val paramItemView = layoutInflater.inflate(R.layout.adapter_search_histroy, fl_search_history,false)
            val keyWordTv = paramItemView.findViewById<TextView>(R.id.tv_content)
            keyWordTv.setPadding(35,5,35,5)
            keyWordTv.text = searchWord[i]
            keyWordTv.setOnClickListener {
                edit_search.setText(searchWord[i])
            }
            fl_search_history.addView(paramItemView, layoutParams)
        }



    }

    private var popWindow: SpinnerPopWindow<String>? = null
    private fun showPopwindow(pop: SpinnerPopWindow<String>,view:View){
        pop.width = view.width
        pop.showAsDropDown(view)

    }
    override fun doBusiness(mContext: Context?) {
        val listClass = resources.getStringArray(R.array.select_type_date).toMutableList()
        popWindow = SpinnerPopWindow(this,
            listClass,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                private_year.text = listClass[position]

                popWindow!!.dismiss()
                type = position+1
//                requestSearchData()
            })
        ff_select_type.clickWithTrigger {
            showPopwindow(popWindow!!,ff_select_type)
        }
        iv_delete_edit.clickWithTrigger {
            edit_search.setText("")
        }
        back_btn.clickWithTrigger {
            finish()
        }
        iv_delete_all.clickWithTrigger {
            toCleanAll()
        }
        edit_search.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH){
                    Log.d("EditorInfo","当前点击了")
                    list1.clear()
                    requestSearchData()
                    xrefreshview.visibility = View.VISIBLE
                    return false
                }
                Log.d("EditorInfo","当前点击了qita")
                return true
            }

        })

        edit_search.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

//                if (p0!!.isNotEmpty()){
//                    list1.clear()
//                    requestSearchData()
//                    xrefreshview.visibility = View.VISIBLE
//                } else {
//                    list1.clear()
//                    adapter!!.notifyDataSetChanged()
//                    ll_search_history.visibility = View.VISIBLE
//                    xrefreshview.visibility = View.GONE
////                    requestSearchHistory()
//                }
            }

        })

    }

    /**
     * 拨打电话
     */
    var m_dialog: Dialog? = null
    private fun toCleanAll() {
        val factory = LayoutInflater.from(this)
        val view = factory.inflate(R.layout.dialog_to_back, null)
        m_dialog = Dialog(this, R.style.transparentFrameWindowStyle2)
        m_dialog!!.setContentView(
            view,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
        )
        val window = m_dialog!!.window
        // 设置显示动画
        window!!.setWindowAnimations(R.style.main_menu_animstyle)
        val wl = window.attributes
        wl.x = 0
        wl.y = 0
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT
        // 设置显示位置
        m_dialog!!.onWindowAttributesChanged(wl)
        // 设置点击外围解散
        m_dialog!!.setCanceledOnTouchOutside(true)
        m_dialog!!.show()

        val dismiss_tv = view.findViewById<TextView>(R.id.dismiss_tv)
        val tv_phone = view.findViewById<TextView>(R.id.tv_phone)
        tv_phone.text = "清空所有搜索历史"
        dismiss_tv.clickWithTrigger {
            requestDeletedHistory()
            m_dialog!!.dismiss() }

        val phone_tv = view.findViewById<TextView>(R.id.phone_tv)
        phone_tv.clickWithTrigger {
            m_dialog!!.dismiss()
        }
    }



}

package com.noplugins.keepfit.userplatform.activity.changguan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andview.refreshview.XRefreshView
import com.google.gson.Gson
import com.noplugins.keepfit.userplatform.R
import com.noplugins.keepfit.userplatform.activity.teahcher.PrivateDetailActivity
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter
import com.noplugins.keepfit.userplatform.adapter.PrivateFindAdapter
import com.noplugins.keepfit.userplatform.base.BaseActivity
import com.noplugins.keepfit.userplatform.bean.OrderBean
import com.noplugins.keepfit.userplatform.bean.PrivateBean
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.pop.SpinnerPopWindow
import kotlinx.android.synthetic.main.activity_changguan_private_list.*
import okhttp3.RequestBody
import java.io.Serializable
import java.util.HashMap

class ChangguanPrivateListActivity :BaseActivity() {
    var page = 1
    private var is_not_more = true

    var adapter: PrivateFindAdapter? = null
    var data: ArrayList<PrivateBean.GenTeacherListBean> = ArrayList()

    var skillType = -1
    var yaerSelect = -1
    var sexSelect = -1
    override fun initBundle(parms: Bundle?) {
    }

    override fun initView() {
        setContentView(R.layout.activity_changguan_private_list)

        init()
        initAdapter()

        requestPrivate()

    }

    override fun doBusiness(mContext: Context?) {

        back_btn.setOnClickListener {
            finish()
        }
     }


    private fun showPopwindow(pop: SpinnerPopWindow<String>, view: TextView){
        pop.width = view.width
        pop.showAsDropDown(view)

    }
    private var popWindow2: SpinnerPopWindow<String>? = null
    private var popWindow3: SpinnerPopWindow<String>? = null
    private var popWindow: SpinnerPopWindow<String>? = null
    private fun init() {

        val listClass = resources.getStringArray(R.array.private_class_types).toMutableList()
        popWindow = SpinnerPopWindow(this,
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


        val listYear = resources.getStringArray(R.array.private_year_types).toMutableList()
        popWindow2 = SpinnerPopWindow(this,
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
        popWindow3 = SpinnerPopWindow(this,
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
        rv_list.layoutManager = LinearLayoutManager(this)
        adapter = PrivateFindAdapter(data)
        rv_list.adapter = adapter
        val view = LayoutInflater.from(this).inflate(R.layout.enpty_view, rv_list, false)
        val txet = view.findViewById<TextView>(R.id.tv_empty_tips)
        val iv  = view.findViewById<ImageView>(R.id.iv_empty_view)
        iv.setImageResource(R.drawable.no_list_data)
        txet.text = "木有搜索到相关内容哇～"
        adapter!!.emptyView = view

        adapter!!.setOnItemClickListener { adapter, view, position ->
            if (BaseUtils.isFastClick()){
                val orderBean = intent.getSerializableExtra("order") as OrderBean
                val intent = Intent(
                    this,
                    PrivateDetailActivity::class.java
                )


                intent.putExtra("teacherNum",data[position].teacherNum)
                intent.putExtra("from",1)
                intent.putExtra("order",orderBean as Serializable)
                startActivity(intent)
            }
        }


        //设置是否可以下拉刷新

        xrefreshview.pullRefreshEnable = true

        //设置是否可以上拉加载

        xrefreshview.pullLoadEnable = true

        xrefreshview.setMoveForHorizontal(true)

        xrefreshview.setAutoLoadMore(false)

        xrefreshview.setOnRecyclerViewScrollListener(object : RecyclerView.OnScrollListener() {
        })

        xrefreshview.setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {
            override fun onRefresh(isPullDown: Boolean) {
                Handler().postDelayed({
                    page = 1
                    //填写刷新数据的网络请求，一般page=1，List集合清空操作
                    data.clear()
                    requestPrivate()
                    xrefreshview.stopRefresh()
                }, 1000)//2000是刷新的延时，使得有个动画效果
            }

            override fun onLoadMore(isSilence: Boolean) {
                Handler().postDelayed({
                    page = page + 1
                    requestPrivate()
                    //填写加载更多的网络请求，一般page++
//                        //没有更多数据时候
                    if (is_not_more) {
                        xrefreshview.setLoadComplete(true)
                    } else {
                        //刷新完成必须调用此方法停止加载
                        xrefreshview.stopLoadMore(true)
                    }
                }, 1000)//2000是刷新的延时，使得有个动画效果
            }
        })
    }

    private fun requestPrivate(){
        val params = HashMap<String, Any>()
        params["page"] = page
        params["areaNum"] = intent.getStringExtra("areaNum")
        params["userNum"] = SpUtils.getString(this, AppConstants.USER_NAME)
        if (skillType > 0){
            params["skill"] = skillType
        }
        if (sexSelect > -1){
            params["sex"] = sexSelect
        }
        if (yaerSelect > -1){
            params["year"] = yaerSelect
        }

        val json = Gson().toJson(params)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json)


        val subscription = Network.getInstance("获取教练列表", this)
            .findAllTeacher(requestBody,
                ProgressSubscriberNew(PrivateBean::class.java,
                    GsonSubscriberOnNextListener<PrivateBean> { bean, get_message_id ->
                        tv_private_num.text = "共${bean.totalNum}位私教"
                        if (page == 1){
                            update(bean)
                        } else {
                            add(bean)
                        }
                        is_not_more = bean.teacherList.size <= 0


                    }, object : SubscriberOnNextListener<Bean<Any>> {
                        override fun onNext(result: Bean<Any>) {

                        }

                        override fun onError(error: String) {
                            Toast.makeText(applicationContext, "获取教练列表失败！", Toast.LENGTH_SHORT).show()
                        }
                    }, this, true
                )
            )
    }

    private fun update(bean: PrivateBean) {
        data.clear()
        data.addAll(bean.teacherList)
        adapter!!.notifyDataSetChanged()

    }

    private fun add(bean: PrivateBean) {
        data.addAll(bean.teacherList)
        adapter!!.notifyDataSetChanged()
    }


}

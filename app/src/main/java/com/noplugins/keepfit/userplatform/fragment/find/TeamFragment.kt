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
import com.noplugins.keepfit.userplatform.activity.changguan.BillActivity
import com.noplugins.keepfit.userplatform.activity.team.TeamDetailActivity
import com.noplugins.keepfit.userplatform.adapter.ChangguanDetailTimeAdapter
import com.noplugins.keepfit.userplatform.adapter.PopUpAdapter
import com.noplugins.keepfit.userplatform.adapter.TeamFindAdapter
import com.noplugins.keepfit.userplatform.base.BaseFragment
import com.noplugins.keepfit.userplatform.bean.OrderBean
import com.noplugins.keepfit.userplatform.bean.TeamBean
import com.noplugins.keepfit.userplatform.bean.TimeWeekBean
import com.noplugins.keepfit.userplatform.callback.MessageEvent
import com.noplugins.keepfit.userplatform.global.AppConstants
import com.noplugins.keepfit.userplatform.util.BaseUtils
import com.noplugins.keepfit.userplatform.util.SpUtils
import com.noplugins.keepfit.userplatform.util.data.DateHelper
import com.noplugins.keepfit.userplatform.util.net.Network
import com.noplugins.keepfit.userplatform.util.net.entity.Bean
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener
import com.noplugins.keepfit.userplatform.util.ui.pop.SpinnerPopWindow
import kotlinx.android.synthetic.main.fragment_find_team.*
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class TeamFragment : BaseFragment() {
    var newView: View? = null
    var timeData: ArrayList<TimeWeekBean> = ArrayList()
    var timeAdapter: ChangguanDetailTimeAdapter? = null

    var skillType = -1
    var yaerSelect = -1
    var sexSelect = -1
    var timeSelect = -1
    var currentLat: Double = 0.00
    var currentLon: Double = 0.00

    var time = ""
    var page = 1
    var maxPage = -1

    var startTime = ""
    var endTime = ""
    private var isDialog = false

    private var is_not_more = true

    var adapter: TeamFindAdapter? = null
    var data: ArrayList<TeamBean.CourseListBean> = ArrayList()

    private var orderList: MutableList<OrderBean> = ArrayList()

    companion object {
        fun newInstance(title: String): TeamFragment {
            val fragment = TeamFragment()
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
            newView = inflater.inflate(R.layout.fragment_find_team, container, false)
            EventBus.getDefault().register(this)
        }
        return newView

    }

    override fun onFragmentFirstVisible() {
        currentLat = SpUtils.getString(context,AppConstants.LAT,"121.473701").toDouble()
        currentLon = SpUtils.getString(context,AppConstants.LON,"31.230416").toDouble()
        initView()
        initTimeAdapter()
        initAdapter()
        requestTeam()
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        super.onFragmentVisibleChange(isVisible)
        //


    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "进来了")
        if (time!= ""){
            page = 1
            requestTeam()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
        currentLat = SpUtils.getString(context, AppConstants.LAT).toDouble()
        currentLon = SpUtils.getString(context, AppConstants.LON).toDouble()
        if (time!= ""){
            page = 1
            requestTeam()
        }

    }

    private fun initTimeAdapter() {
        val layoutManager = LinearLayoutManager(context)
        //调整RecyclerView的排列方向
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv_date_select.layoutManager = layoutManager
        timeData.clear()
        for (i in 0..13) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, +i)
            val time = TimeWeekBean()
            time.time = "${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.DATE)}"
            time.week = DateHelper.getWeek(calendar.get(Calendar.DAY_OF_WEEK))
            val month = if (calendar.get(Calendar.MONTH) + 1 > 9) {
                "${calendar.get(Calendar.MONTH) + 1}"
            } else {
                "0${calendar.get(Calendar.MONTH) + 1}"
            }

            val day = if (calendar.get(Calendar.DATE) > 9) {
                calendar.get(Calendar.DATE)
            } else {
                "0${calendar.get(Calendar.DATE)}"
            }

            time.nowDate = "${calendar.get(Calendar.YEAR)}-$month-$day"

            timeData.add(time)
        }

        time = timeData[0].nowDate
        timeAdapter = ChangguanDetailTimeAdapter(context, timeData)
        rv_date_select.adapter = timeAdapter

        timeAdapter!!.setmOnItemClickListener { view, obj, position ->
            Log.d("FFFFFFF", "FFFFF:$obj")
            time = obj as String
            page = 1
            requestTeam()
        }

    }

    private fun initAdapter() {
        rv_list.layoutManager = LinearLayoutManager(context)
        adapter = TeamFindAdapter(data)
        rv_list.adapter = adapter
        val view = LayoutInflater.from(context).inflate(R.layout.enpty_view, rv_list, false)
        val txet = view.findViewById<TextView>(R.id.tv_empty_tips)
        val iv = view.findViewById<ImageView>(R.id.iv_empty_view)
        iv.setImageResource(R.drawable.no_list_data)
        txet.text = "木有搜索到相关内容哇～"
        adapter!!.emptyView = view
        adapter!!.setOnItemChildClickListener { adapter, view, position ->
            if (BaseUtils.isFastClick()) {
                //需要传递给订单的信息
                orderList.clear()
                val orderBean = OrderBean()
                orderBean.areaNum = data[position].gymAreaNum
                orderBean.areaName = data[position].areaName
                orderBean.address = data[position].address
                orderBean.custUserNum = SpUtils.getString(activity, AppConstants.USER_NAME)
                orderBean.courseType = 1
                orderBean.startTime = time + " " + DateHelper.getDateByLong(data[position].startTime)
                orderBean.endTime = time + " " + DateHelper.getDateByLong(data[position].endTime)
                orderBean.courseNum = data[position].courseNum
                orderBean.coachUserNum = data[position].genTeacherNum
                orderBean.courseName = data[position].courseName
                orderBean.coachUserName = data[position].teacherName
                orderBean.price = data[position].finalPrice
                orderBean.logo = data[position].imgUrl
                orderBean.difficulty = difficultyToStr(data[position].difficulty)
                orderList.add(orderBean)

                when (view.id) {
                    R.id.tv_pay -> {
                        if (data[position].fullPerson == 1) {
                            return@setOnItemChildClickListener
                        }
                        val intent = Intent(
                            activity,
                            BillActivity::class.java
                        )

                        intent.putExtra("list", orderList as Serializable)
                        intent.putExtra("type", 3)
                        startActivity(intent)
                    }
                    else -> {
                        val intent = Intent(
                            activity,
                            TeamDetailActivity::class.java
                        )
                        intent.putExtra("list", orderList as Serializable)
                        intent.putExtra("courseNum", data[position].courseNum)
                        startActivity(intent)
                    }
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
                data.clear()
                isDialog = false
                requestTeam()
                xrefreshview.finishRefresh()
                is_not_more = false
                xrefreshview.setEnableLoadMore(true)
            }, 1000)//2000是刷新的延时，使得有个动画效果
        }
        xrefreshview.setOnLoadMoreListener {
            //上拉加载
            Handler().postDelayed({
                page += 1
                isDialog = false
                requestTeam()
                xrefreshview.finishLoadMore()

            }, 1000)//2000是刷新的延时，使得有个动画效果
        }
    }

    private var popWindow: SpinnerPopWindow<String>? = null
    private var popWindow2: SpinnerPopWindow<String>? = null
    private var popWindow3: SpinnerPopWindow<String>? = null
    private var popWindow4: SpinnerPopWindow<String>? = null
    private fun showPopwindow(pop: SpinnerPopWindow<String>, view: TextView) {
        pop.width = view.width
        pop.showAsDropDown(view)

    }

    private fun initView() {
        val listClass = resources.getStringArray(R.array.team_xlmb).toMutableList()
        popWindow = SpinnerPopWindow(context,
            listClass,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                if (listClass.get(position).length > 4) {
                    private_class.text = listClass.get(position).substring(0, 3) + "..."
                } else {
                    private_class.text = listClass.get(position)
                }
                popWindow!!.dismiss()

                skillType = if (position == listClass.size - 1) {
                    -1
                } else {
                    position + 1
                }
                isDialog = true
                page = 1
                requestTeam()
                popWindow!!.dismiss()
            })
        private_class.setOnClickListener {
            showPopwindow(popWindow!!, private_class)

        }

        val listYear = resources.getStringArray(R.array.team_class_type).toMutableList()
        popWindow2 = SpinnerPopWindow(context,
            listYear,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                //                private_year.setText(listYear.get(position))
                if (listYear.get(position).length > 4) {
                    private_year.text = listYear.get(position).substring(0, 3) + "..."
                } else {
                    private_year.text = listYear.get(position)
                }
                popWindow2!!.dismiss()
                yaerSelect = if (position == listYear.size - 1) {
                    -1
                } else {
                    position + 1
                }
                isDialog = true
                page = 1
                requestTeam()
                popWindow2!!.dismiss()
            })
        private_year.setOnClickListener {
            showPopwindow(popWindow2!!, private_year)


        }

        val listSex = resources.getStringArray(R.array.team_xlqd).toMutableList()
        popWindow3 = SpinnerPopWindow(context,
            listSex,
            PopUpAdapter.OnItemClickListener { _, _, position ->
                private_sex.setText(listSex.get(position))
                popWindow3!!.dismiss()
                sexSelect = if (position == listSex.size - 1) {
                    -1
                } else {
                    position + 1
                }
                isDialog = true
                page = 1
                requestTeam()
                popWindow3!!.dismiss()
            })
        private_sex.setOnClickListener {
            showPopwindow(popWindow3!!, private_sex)

        }

        val listTime = resources.getStringArray(R.array.team_time).toMutableList()
        popWindow4 = SpinnerPopWindow(context,
            listTime,
            PopUpAdapter.OnItemClickListener { _, obj, position ->
                team_time.setText(listTime.get(position))
                popWindow4!!.dismiss()
                if (position == listTime.size - 1) {
                    timeSelect = -1
                } else {
                    timeSelect = position + 1
                    val arr = listTime.get(position).split("–")
                    startTime = arr[0]
                    endTime = arr[1]
                }

                isDialog = true
                page = 1
                requestTeam()
                popWindow4!!.dismiss()
            })
        team_time.setOnClickListener {
            showPopwindow(popWindow4!!, team_time)

        }


    }

    private fun requestTeam() {
        val params = HashMap<String, Any>()
        params["page"] = page

        params["date"] = time
        params["longitude"] = currentLon
        params["latitude"] = currentLat
        if (skillType > 0) {
            params["target"] = skillType
        }
        if (sexSelect > -1) {
            params["difficulty"] = sexSelect
        }
        if (yaerSelect > -1) {
            params["classType"] = yaerSelect
        }
        if (startTime != "" && timeSelect != -1) {
            params["start"] = startTime
            params["end"] = endTime
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

        val subscription = Network.getInstance("获取团课列表", activity)
            .findCourseList(
                params, ProgressSubscriber(
                    "获取团课列表",
                    object : SubscriberOnNextListener<Bean<TeamBean>> {
                        override fun onNext(bean: Bean<TeamBean>) {
                            tv_private_num.text = "共${bean.data.totalCount}节团课"
                            maxPage = bean.data.maxPage
                            if (page >= maxPage) {
                                xrefreshview.setEnableLoadMore(false)
                            } else {
                                xrefreshview.setEnableLoadMore(true)
                            }
                            if (page == 1) {
                                update(bean.data)
                            } else {
                                add(bean.data)
                            }
                        }

                        override fun onError(error: String?) {
                            Toast.makeText(activity, "团课获取失败", Toast.LENGTH_SHORT).show()
                        }

                    }, activity, isDialog
                )
            )
    }

    private fun update(bean: TeamBean) {
        data.clear()
        if (bean.courseList.size > 0) {
            data.addAll(bean.courseList)

        }
        adapter!!.notifyDataSetChanged()


    }

    private fun add(bean: TeamBean) {
        data.addAll(bean.courseList)
        adapter!!.notifyDataSetChanged()
    }


    private fun difficultyToStr(id: Int): String {
        var str = ""
        when (id) {
            1 -> {
                str = "容易"
            }
            2 -> {
                str = "中等"
            }
            3 -> {
                str = "难"
            }
        }
        return str
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

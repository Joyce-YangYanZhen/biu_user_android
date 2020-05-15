package com.noplugins.keepfit.userplatform.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.CheckCodeActivity;
import com.noplugins.keepfit.userplatform.activity.DailyTravelDetailActivity;
import com.noplugins.keepfit.userplatform.entity.DateViewEntity;
import com.noplugins.keepfit.userplatform.entity.LabelEntity;
import com.noplugins.keepfit.userplatform.fragment.TimeLinFormatFragment;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriber;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import com.umeng.commonsdk.debug.I;
import org.greenrobot.eventbus.EventBus;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeClassAdapter extends BaseRecyclerAdapter<RecyclerView.ViewHolder> implements EasyPermissions.PermissionCallbacks {
    private List<DateViewEntity.CourseListBean> list;
    private Context context;
    private static final int EMPTY_VIEW = 2;
    private static final int TYPE_YOUTANG = 1;
    public static final int PERMISSION_STORAGE_CODE = 10001;
    public static final String PERMISSION_STORAGE_MSG = "需要电话权限才能联系客服哦";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.CALL_PHONE};
    public TimeLinFormatFragment timeLinFormatFragment;
    private int select_biaoqing = 0;
    private int select_biaoqing_bf = 0;
    private List<LabelEntity> labelEntities = new ArrayList<>();
    private TagAdapter tagAdapter;

    public TimeClassAdapter(List<DateViewEntity.CourseListBean> mlist, Context mcontext, TimeLinFormatFragment mtimeLinFormatFragment) {
        list = mlist;
        timeLinFormatFragment = mtimeLinFormatFragment;
        context = mcontext;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {

        YouYangViewHolder youYangViewHolder = new YouYangViewHolder(view, false);
        return youYangViewHolder;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        RecyclerView.ViewHolder holder = getViewHolderByViewType(viewType, parent);
        return holder;
    }

    RecyclerView.ViewHolder holder = null;

    private RecyclerView.ViewHolder getViewHolderByViewType(int viewType, ViewGroup parent) {
        View item_view = null;
        if (viewType == EMPTY_VIEW) {
            item_view = LayoutInflater.from(context).inflate(R.layout.time_class_null, parent, false);
            holder = new EmptyViewHolder(item_view, false);
        } else if (viewType == TYPE_YOUTANG) {
            item_view = LayoutInflater.from(context).inflate(R.layout.time_class_item, parent, false);
            holder = new YouYangViewHolder(item_view, true);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder view_holder, int position, boolean isItem) {
        if (view_holder instanceof YouYangViewHolder) {
            YouYangViewHolder holder = (YouYangViewHolder) view_holder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            DateViewEntity.CourseListBean courseListBean = list.get(position);

            if (courseListBean.getCourseType() == 1) {//团课
                holder.sijiao.setBackgroundResource(R.drawable.zi_bg);
                holder.class_type.setText("团课服务");
                holder.call_img.setVisibility(View.GONE);
                holder.ll_name.setVisibility(View.VISIBLE);
                holder.teacher_name_tv.setText("团操-" + courseListBean.getCoachUserName());
                holder.tvCgRoomName.setVisibility(View.VISIBLE);
                holder.tvCgRoomName.setText(courseListBean.getAreaName()+" | "+courseListBean.getPlaceName());
            }
            if (courseListBean.getCourseType() == 2) {//私教
                holder.sijiao.setBackgroundResource(R.drawable.bg_lv);
                holder.class_type.setText("私教服务");
                holder.ll_name.setVisibility(View.VISIBLE);
                //教练名字
                holder.teacher_name_tv.setText("教练-" + courseListBean.getCoachUserName());
                holder.call_img.setVisibility(View.VISIBLE);//只有私教才能打电话
                holder.tvCgRoomName.setVisibility(View.VISIBLE);
                holder.tvCgRoomName.setText(courseListBean.getAreaName()+" | "+courseListBean.getPlaceName());
            }
            if (courseListBean.getCourseType() == 3) {
                holder.call_img.setVisibility(View.GONE);
                holder.sijiao.setBackgroundResource(R.drawable.huang_bg);
                holder.class_type.setText("场馆服务");
                holder.ll_name.setVisibility(View.GONE);
                holder.teacher_name_tv.setText("场馆-" + courseListBean.getAreaName());
                holder.tvCgRoomName.setVisibility(View.GONE);
            }

            //时间
            holder.time_tv.setText(courseListBean.getClassStart());
            //课程名称
            holder.class_name.setText(courseListBean.getCourseName());
            //开始时间结束时间
            holder.start_end_tv.setText(courseListBean.getClassDur());

            //地址
            holder.adress_tv.setText(courseListBean.getAddress());

            holder.tuanke_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseUtils.isFastClick()) {
                        if (!TextUtils.isEmpty(courseListBean.getSportsNum())) {
                            Intent intent = new Intent(context, DailyTravelDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("detail_id", courseListBean.getSportsNum());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "没有日志~", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            //是否过期
            if (courseListBean.getDayViewPast() == 1) {//已结束
                holder.status_icon.setImageResource(R.drawable.over_icon);
                holder.erweima_icon.setImageResource(R.drawable.erweima_guoqi);
                holder.erweima_icon.setVisibility(View.GONE);
                holder.erweima_icon.setClickable(false);

                holder.ll_weijieshu.setVisibility(View.VISIBLE);
                //是否有团课日志
                if (courseListBean.getSprotLog() == 1) {//有团课日志
                    holder.tuanke_img.setVisibility(View.VISIBLE);
                } else {
                    holder.tuanke_img.setVisibility(View.GONE);
                }
                if (courseListBean.getBeforeFace() == 0 && courseListBean.getAfterFace() == 0) {
                    holder.ll_tips.setVisibility(View.VISIBLE);
                    holder.ll_biaoqin.setVisibility(View.GONE);
                } else {
                    holder.ll_tips.setVisibility(View.GONE);
                    holder.ll_biaoqin.setVisibility(View.VISIBLE);
                }

            } else if (courseListBean.getDayViewPast() == 2) {//进行中
                holder.status_icon.setImageResource(R.drawable.jinxingzhong);//进行中
                holder.erweima_icon.setImageResource(R.drawable.erweima_normal);
                holder.erweima_icon.setClickable(true);
                holder.ll_weijieshu.setVisibility(View.VISIBLE);

                //进行中 没有进行checkIn
                if (courseListBean.getCheckIn() == 0) {
                    holder.erweima_icon.setVisibility(View.VISIBLE);
                    holder.erweima_icon.setClickable(true);
                } else {
                    holder.erweima_icon.setVisibility(View.INVISIBLE);
                    holder.erweima_icon.setClickable(false);
                }
                //进行中不能跳转到运动日志
                holder.tuanke_layout.setVisibility(View.INVISIBLE);
                holder.tuanke_layout.setClickable(false);

                //holder.ll_sportEnd.setVisibility(View.INVISIBLE);
                holder.ll_sportEnd.setClickable(false);

                if (courseListBean.getBeforeFace() == 0) {
                    holder.ll_tips.setVisibility(View.VISIBLE);
                    holder.ll_biaoqin.setVisibility(View.GONE);
                } else {
                    holder.ll_tips.setVisibility(View.GONE);
                    holder.ll_biaoqin.setVisibility(View.VISIBLE);
                }
            } else if (courseListBean.getDayViewPast() == 3) {//未开始
                holder.status_icon.setImageResource(R.drawable.weikaishi_icon);//未开始
                holder.erweima_icon.setImageResource(R.drawable.erweima_normal);
                holder.erweima_icon.setClickable(true);
                holder.ll_weijieshu.setVisibility(View.GONE);

                if (courseListBean.getBeforeFace() == 0) {
                    holder.ll_tips.setVisibility(View.VISIBLE);
                    holder.ll_biaoqin.setVisibility(View.GONE);
                } else {
                    holder.ll_tips.setVisibility(View.GONE);
                    holder.ll_biaoqin.setVisibility(View.VISIBLE);
                }
                if (courseListBean.getCheckIn() == 1) {
                    holder.erweima_icon.setVisibility(View.INVISIBLE);
                    holder.erweima_icon.setClickable(false);
                } else {
                    if (courseListBean.getCheckIn() == 0) {
                        holder.erweima_icon.setVisibility(View.VISIBLE);
                        holder.erweima_icon.setClickable(true);
                    }
                }
            }
            //打电话
            holder.call_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("tag", "phone:" + courseListBean.getCoachPhone());
                    if (courseListBean.getCoachPhone() != null) {
                        call_pop(holder, courseListBean.getCoachPhone());
                    }

                }
            });


            holder.erweima_icon.setImageResource(R.drawable.erweima_normal);


            holder.erweima_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BaseUtils.isFastClick()) {
                        Intent intent = new Intent(context, CheckCodeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("order_number", courseListBean.getCustOrderItemNum() + "");
                        bundle.putSerializable("order", courseListBean);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
            });
            if (courseListBean.getText() == 0) {//没有评价过
                holder.pinglun_img.setVisibility(View.VISIBLE);
            } else {//评价过了
                holder.pinglun_img.setVisibility(View.GONE);
            }
            holder.pinglun_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exit_popwindow(holder.pinglun_img, courseListBean, courseListBean.getCourseType(), courseListBean.getBeforeFace());
                }
            });
            //设置运动前表情
            if (courseListBean.getBeforeFace() == 0 || courseListBean.getBeforeFace() == -1) {//没有
                holder.ll_sportStart.setVisibility(View.INVISIBLE);
            } else {
                if (courseListBean.getBeforeFace() == 1) {
                    holder.yundongqian_img.setImageResource(R.drawable.xiaolian1);
                } else if (courseListBean.getBeforeFace() == 2) {
                    holder.yundongqian_img.setImageResource(R.drawable.xiaolian2);
                } else if (courseListBean.getBeforeFace() == 3) {
                    holder.yundongqian_img.setImageResource(R.drawable.xiaolian3);
                } else if (courseListBean.getBeforeFace() == 4) {
                    holder.yundongqian_img.setImageResource(R.drawable.xiaolian4);
                } else if (courseListBean.getBeforeFace() == 5) {
                    holder.yundongqian_img.setImageResource(R.drawable.xiaolian5);
                }
                holder.ll_sportStart.setVisibility(View.VISIBLE);

            }
            //设置运动后表情
            if (courseListBean.getAfterFace() == 0 || courseListBean.getAfterFace() == -1) {//没有
                holder.ll_sportEnd.setVisibility(View.INVISIBLE);
            } else {
                if (courseListBean.getAfterFace() == 1) {
                    holder.yundonghou_img.setImageResource(R.drawable.xiaolian1);
                } else if (courseListBean.getAfterFace() == 2) {
                    holder.yundonghou_img.setImageResource(R.drawable.xiaolian2);
                } else if (courseListBean.getAfterFace() == 3) {
                    holder.yundonghou_img.setImageResource(R.drawable.xiaolian3);
                } else if (courseListBean.getAfterFace() == 4) {
                    holder.yundonghou_img.setImageResource(R.drawable.xiaolian4);
                } else if (courseListBean.getAfterFace() == 5) {
                    holder.yundonghou_img.setImageResource(R.drawable.xiaolian5);
                }
                holder.ll_sportEnd.setVisibility(View.VISIBLE);

            }


        }
    }

    private void exit_popwindow(ImageView pinglun_img, DateViewEntity.CourseListBean courseListBean, int class_type, int start) {
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.exit_pop, null);
        Dialog m_dialog = new Dialog(context, R.style.transparentFrameWindowStyle2);
        m_dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        Window window = m_dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 设置显示位置
        m_dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        m_dialog.setCanceledOnTouchOutside(true);
        m_dialog.show();
        /**操作*/
        LinearLayout biaoqian_layout = view.findViewById(R.id.biaoqian_layout);

        if (class_type == 3) {//"馆" 类型 不显示标签
            biaoqian_layout.setVisibility(View.GONE);
        } else {
            biaoqian_layout.setVisibility(View.VISIBLE);
        }

        TextView cancel_tv = view.findViewById(R.id.cancel_tv);
        TextView sure_tv = view.findViewById(R.id.sure_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
            }
        });
        sure_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_dialog.dismiss();
                select_biaoqing_net(pinglun_img, courseListBean);

            }
        });
        ImageView img_set = view.findViewById(R.id.img_set);
        ImageView img_start = view.findViewById(R.id.img_start);
        ImageView xiaolian1 = view.findViewById(R.id.xiaolian1);
        ImageView xiaolian2 = view.findViewById(R.id.xiaolian2);
        ImageView xiaolian3 = view.findViewById(R.id.xiaolian3);
        ImageView xiaolian4 = view.findViewById(R.id.xiaolian4);
        ImageView xiaolian5 = view.findViewById(R.id.xiaolian5);

        if (start != 0) {
            select_biaoqing_bf = start;
            switch (start) {
                case 1:
                    img_start.setImageResource(R.drawable.xiaolian1);
                    break;
                case 2:
                    img_start.setImageResource(R.drawable.xiaolian2);
                    break;
                case 3:
                    img_start.setImageResource(R.drawable.xiaolian3);
                    break;
                case 4:
                    img_start.setImageResource(R.drawable.xiaolian4);
                    break;
                case 5:
                    img_start.setImageResource(R.drawable.xiaolian5);
                    break;
            }
        }
        xiaolian1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (select_biaoqing_bf == 0 || select_biaoqing_bf == -1) {
                    select_biaoqing_bf = 1;
                    img_start.setImageResource(R.drawable.xiaolian1);
                } else {
                    img_set.setImageResource(R.drawable.xiaolian1);
                    select_biaoqing = 1;
                }

            }
        });
        xiaolian2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_biaoqing_bf == 0 || select_biaoqing_bf == -1) {
                    select_biaoqing_bf = 2;
                    img_start.setImageResource(R.drawable.xiaolian2);
                } else {
                    img_set.setImageResource(R.drawable.xiaolian2);
                    select_biaoqing = 2;
                }

            }
        });
        xiaolian3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_biaoqing_bf == 0 || select_biaoqing_bf == -1) {
                    select_biaoqing_bf = 3;
                    img_start.setImageResource(R.drawable.xiaolian3);
                } else {
                    img_set.setImageResource(R.drawable.xiaolian3);
                    select_biaoqing = 3;
                }

            }
        });
        xiaolian4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_biaoqing_bf == 0 || select_biaoqing_bf == -1) {
                    select_biaoqing_bf = 4;
                    img_start.setImageResource(R.drawable.xiaolian4);
                } else {
                    img_set.setImageResource(R.drawable.xiaolian4);
                    select_biaoqing = 4;
                }

            }
        });
        xiaolian5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select_biaoqing_bf == 0 || select_biaoqing_bf == -1) {
                    select_biaoqing_bf = 5;
                    img_start.setImageResource(R.drawable.xiaolian5);
                } else {
                    img_set.setImageResource(R.drawable.xiaolian5);
                    select_biaoqing = 1;
                }

            }
        });
        GridView gridView = view.findViewById(R.id.grid_view);

        init_label_date(courseListBean, gridView);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LabelEntity labelEntity = labelEntities.get(i);
                if (labelEntity.isCheck()) {
                    labelEntity.setCheck(false);
                } else {
                    labelEntity.setCheck(true);
                }
                tagAdapter.notifyDataSetChanged();

            }
        });
    }

    private void init_label_date(DateViewEntity.CourseListBean courseListBean, GridView gridView) {
        Map<String, Object> params = new HashMap<>();
        Subscription subscription = Network.getInstance("获取教练标签", context)
                .get_biaoqian(params,
                        new ProgressSubscriber<>("获取教练标签", new SubscriberOnNextListener<Bean<List<LabelEntity>>>() {
                            @Override
                            public void onNext(Bean<List<LabelEntity>> result) {
                                labelEntities = result.getData();
                                tagAdapter = new TagAdapter(context, labelEntities);
                                gridView.setAdapter(tagAdapter);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, context, true));
    }

    private void select_biaoqing_net(ImageView pinglun_img, DateViewEntity.CourseListBean courseListBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("ordNum", courseListBean.getCustOrderNum());
        if (select_biaoqing > 0) {
            params.put("afterFace", select_biaoqing);
        }
        if (select_biaoqing_bf > 0) {
            params.put("beforeFace", select_biaoqing_bf);
        }
        params.put("teacherNum", courseListBean.getCoachUserNum());
        StringBuffer label_str = new StringBuffer();
        for (int i = 0; i < labelEntities.size(); i++) {
            LabelEntity labelEntity = labelEntities.get(i);
            if (i == labelEntities.size() - 1) {
                if (labelEntity.isCheck()) {
                    label_str.append(labelEntity.getValue());
                }
            } else {
                if (labelEntity.isCheck()) {
                    label_str.append(labelEntity.getValue() + ",");
                }
            }
        }
        if (label_str.length() > 0) {
            String label_str_all = label_str.toString();
            String last_str = label_str_all.substring(label_str_all.length() - 1, label_str_all.length());
            if (last_str.equals(",")) {
                label_str_all = label_str_all.substring(0, label_str_all.length() - 1);
            }
            params.put("lable", label_str_all);
        }

        Subscription subscription = Network.getInstance("评论课程", context)
                .pinglun_biaoqian(params,
                        new ProgressSubscriber<>("评论课程", new SubscriberOnNextListener<Bean<Object>>() {
                            @Override
                            public void onNext(Bean<Object> result) {
                                pinglun_img.setVisibility(View.GONE);
                                Toast.makeText(context, "记录成功啦~", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post("update_biaoqin");
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }, context, true));
    }


    private void call_pop(YouYangViewHolder holder, final String phone) {
        CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.call_pop)
                .setBackGroundLevel(0.5f)//0.5f
                .setAnimationStyle(R.style.main_menu_animstyle)
                .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT)
                .setOutSideTouchable(true).create();
        popupWindow.showAsDropDown(holder.call_img);

        /**设置逻辑*/
        View view = popupWindow.getContentView();
        LinearLayout cancel_layout = view.findViewById(R.id.cancel_layout);
        LinearLayout sure_layout = view.findViewById(R.id.sure_layout);
        TextView phoneDialog = view.findViewById(R.id.to_phone);
        phoneDialog.setText("确认拨打" + phone + "?");

        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSimple(phone);
                popupWindow.dismiss();
            }
        });


    }

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    public void initSimple(String phone) {
        if (hasStoragePermission(context)) {
            //有权限
            callPhone(phone);
        } else {
            //申请权限
            EasyPermissions.requestPermissions(timeLinFormatFragment, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
        }
    }

    public void callPhone(String phoneNum) {
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent1.setData(data);
        timeLinFormatFragment.startActivity(intent1);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }

    public static boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(timeLinFormatFragment, perms)) {
            new AppSettingsDialog.Builder(timeLinFormatFragment)
                    .setTitle("提醒")
                    .setRationale("需要电话权限才能联系客服哦")
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, context);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getAdapterItemViewType(int position) {
        if (list.size() == 0) {
            return EMPTY_VIEW;
        } else {
            return TYPE_YOUTANG;
        }
    }


    @Override
    public int getAdapterItemCount() {
        return list.size() > 0 ? list.size() : 1;
    }


    public void setData(List<DateViewEntity.CourseListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public EmptyViewHolder(View item_view, boolean isItem) {
            super(item_view);
            if (isItem) {
                this.view = item_view;
            }
        }
    }


    public class YouYangViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public LinearLayout sijiao, ll_tips, ll_weijieshu, ll_biaoqin;
        public TextView class_type, time_tv, class_name, start_end_tv, teacher_name_tv, adress_tv,tvCgRoomName;
        public ImageView erweima_icon, status_icon, call_img, pinglun_img, yundongqian_img, yundonghou_img;
        public ImageView tuanke_img;
        public RelativeLayout tuanke_layout;
        public LinearLayout ll_name, ll_sportEnd, ll_sportStart;

        public YouYangViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.view = itemView;
                ll_name = view.findViewById(R.id.ll_name);
                ll_biaoqin = view.findViewById(R.id.ll_biaoqin);
                ll_tips = view.findViewById(R.id.ll_tips);
                ll_weijieshu = view.findViewById(R.id.ll_weijieshu);
                ll_sportEnd = view.findViewById(R.id.ll_sportEnd);
                ll_sportStart = view.findViewById(R.id.ll_sportStart);
                sijiao = view.findViewById(R.id.sijiao);
                class_type = view.findViewById(R.id.class_type);
                erweima_icon = view.findViewById(R.id.erweima_icon);
                status_icon = view.findViewById(R.id.status_icon);
                time_tv = view.findViewById(R.id.time_tv);
                class_name = view.findViewById(R.id.class_name);
                start_end_tv = view.findViewById(R.id.start_end_tv);
                teacher_name_tv = view.findViewById(R.id.teacher_name_tv);
                adress_tv = view.findViewById(R.id.adress_tv);
                tuanke_img = view.findViewById(R.id.tuanke_img);
                tuanke_layout = view.findViewById(R.id.tuanke_layout);
                call_img = view.findViewById(R.id.call_img);
                pinglun_img = view.findViewById(R.id.pinglun_img);
                yundongqian_img = view.findViewById(R.id.yundongqian_img);
                yundonghou_img = view.findViewById(R.id.yundonghou_img);
                tvCgRoomName = view.findViewById(R.id.tvCgRoomName);
            }
        }
    }
}

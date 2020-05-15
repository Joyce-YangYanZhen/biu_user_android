package com.noplugins.keepfit.userplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.lxj.xpopup.core.BasePopupView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.adapter.DailyTagAdapter;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.callback.PopViewCallBack;
import com.noplugins.keepfit.userplatform.entity.SportEntity;
import com.noplugins.keepfit.userplatform.entity.TagEntity;
import com.noplugins.keepfit.userplatform.entity.UpdateTravalEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.global.PublicPopControl;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.data.StringsHelper;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import okhttp3.RequestBody;
import rx.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyTravelDetailActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.update_xinqing_btn)
    ImageView update_xinqing_btn;
    @BindView(R.id.yundonghou_img)
    ImageView yundonghou_img;
    @BindView(R.id.yundonghou_layout)
    RelativeLayout yundonghou_layout;
    @BindView(R.id.update_tice_btn)
    ImageView update_tice_btn;
    @BindView(R.id.shengao_edit)
    EditText shengao_edit;
    @BindView(R.id.tizhong_edit)
    EditText tizhong_edit;
    @BindView(R.id.tizhi_edit)
    EditText tizhi_edit;
    @BindView(R.id.bmi_edit)
    EditText bmi_edit;
    @BindView(R.id.yaotunbi_edit)
    EditText yaotunbi_edit;
    @BindView(R.id.daixie_edit)
    EditText daixie_edit;
    @BindView(R.id.grid_view)
    GridView grid_view;
    @BindView(R.id.shanchu_btn)
    ImageView shanchu_btn;
    @BindView(R.id.save_btn)
    TextView save_btn;
    @BindView(R.id.class_name)
    TextView class_name;
    @BindView(R.id.class_time)
    TextView class_time;
    @BindView(R.id.address_tv)
    TextView address_tv;
    @BindView(R.id.ruchang_time)
    TextView ruchang_time;
    @BindView(R.id.lichang_time)
    TextView lichang_time;
    @BindView(R.id.ruhang_biaoqing)
    ImageView ruhang_biaoqing;
    @BindView(R.id.yundong_before_layout)
    RelativeLayout yundong_before_layout;
    @BindView(R.id.class_content)
    TextView class_content;
    @BindView(R.id.yundongqian_tv)
    TextView yundongqian_tv;
    @BindView(R.id.yundonghou_tv)
    TextView yundonghou_tv;
    @BindView(R.id.lin4)
    LinearLayout lin4;
    @BindView(R.id.lin5)
    LinearLayout lin5;

    private String detail_id = "";
    private int normal_biaoqing;
    private int normal_check_out_biaoqing;

    @Override
    public void initBundle(Bundle parms) {
        if (null != parms) {
            detail_id = parms.getString("detail_id");
        }
    }

    @Override
    public void initView() {
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentLayout(R.layout.activity_daily_travel_detail);
        ButterKnife.bind(this);
        isShowTitle(false);
    }

    @Override
    public void doBusiness(Context mContext) {
        init_date();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.is_check_code_back = true;
                finish();
            }
        });

        update_xinqing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (normal_biaoqing == -1) {//表示没有运动前表情
                    select_biaoqing_pop(false);

                } else {
                    select_biaoqing_pop(true);

                }
            }
        });

        //修改体测数据
        update_tice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_btn.setVisibility(View.VISIBLE);
                update_tice_btn.setVisibility(View.GONE);

                shengao_edit.setEnabled(true);
                StringsHelper.setSelecttion(shengao_edit);//设置光标显示的位置在最后
                tizhong_edit.setEnabled(true);
                tizhi_edit.setEnabled(true);
                bmi_edit.setEnabled(false);
                yaotunbi_edit.setEnabled(true);
                daixie_edit.setEnabled(true);
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_btn.setVisibility(View.GONE);
                update_tice_btn.setVisibility(View.VISIBLE);

                shengao_edit.setEnabled(false);
                tizhong_edit.setEnabled(false);
                tizhi_edit.setEnabled(false);
                bmi_edit.setEnabled(false);
                yaotunbi_edit.setEnabled(false);
                daixie_edit.setEnabled(false);

                change_status(-1, true);
            }
        });


        //删除日志
        shanchu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_pop();
            }
        });

    }

    private void init_date() {
        Map<String, Object> params = new HashMap<>();
        params.put("sportsNum", detail_id);
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "运动日志详情参数：" + json_params);
        Subscription subscription = Network.getInstance("运动日志详情", this)
                .yundongrizh_detail(requestBody, new ProgressSubscriberNew<>(SportEntity.class, new GsonSubscriberOnNextListener<SportEntity>() {
                    @Override
                    public void on_post_entity(SportEntity entity, String s) {
                        Log.e("运动日志详情成功", entity + "运动日志详情成功");
                        init_date_value(entity);

                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("运动日志详情失败", "运动日志详情失败:" + error);
                        Toast.makeText(DailyTravelDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }

    private void init_date_value(SportEntity sportEntity) {
        SportEntity.CourseDetailBean courseDetailBean = sportEntity.getCourseDetail();
        if (courseDetailBean.getCourseType() == 1 || courseDetailBean.getCourseType() == 3) {
            lin4.setVisibility(View.GONE);
            lin5.setVisibility(View.GONE);
        } else {
            lin4.setVisibility(View.VISIBLE);
            lin5.setVisibility(View.VISIBLE);
        }

        class_name.setText(courseDetailBean.getCourseName());
        class_time.setText(courseDetailBean.getBiuTime());
        address_tv.setText(courseDetailBean.getAreaAddress());
        //设置标签
        if (null != courseDetailBean.getLableList() && courseDetailBean.getLableList().size() > 0) {
            List<TagEntity> strings = new ArrayList<>();
            for (int i = 0; i < courseDetailBean.getLableList().size(); i++) {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setTag(courseDetailBean.getLableList().get(i));
                strings.add(tagEntity);
            }
            DailyTagAdapter tagAdapter = new DailyTagAdapter(this, strings);
            grid_view.setAdapter(tagAdapter);
            grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (BaseUtils.isFastClick()) {

                    }
                }
            });
        }

        //设置心情数据
        SportEntity.MoodDetailBean moodDetailBean = sportEntity.getMoodDetail();
        ruchang_time.setText(moodDetailBean.getInRoom() + "入场");
        lichang_time.setText(moodDetailBean.getOutRoom() + "离场");

        if (moodDetailBean.getSportsBefore() == 0) {//入场表情
            yundong_before_layout.setVisibility(View.INVISIBLE);
        } else if (moodDetailBean.getSportsBefore() == 1) {
            yundong_before_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(true, 1, 0);
        } else if (moodDetailBean.getSportsBefore() == 2) {
            yundong_before_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(true, 2, 0);

        } else if (moodDetailBean.getSportsBefore() == 3) {
            yundong_before_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(true, 3, 0);

        } else if (moodDetailBean.getSportsBefore() == 4) {
            yundong_before_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(true, 4, 0);

        } else if (moodDetailBean.getSportsBefore() == 5) {
            yundong_before_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(true, 5, 0);

        } else {//等于-1的话就影藏
            yundong_before_layout.setVisibility(View.INVISIBLE);
        }
        if (moodDetailBean.getSportsAfter() == 0) {//离场表情
            yundonghou_layout.setVisibility(View.INVISIBLE);
        } else if (moodDetailBean.getSportsAfter() == 1) {
            yundonghou_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(false, 0, 1);

        } else if (moodDetailBean.getSportsAfter() == 2) {
            yundonghou_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(false, 0, 2);

        } else if (moodDetailBean.getSportsAfter() == 3) {
            yundonghou_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(false, 0, 3);

        } else if (moodDetailBean.getSportsAfter() == 4) {
            yundonghou_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(false, 0, 4);

        } else if (moodDetailBean.getSportsAfter() == 5) {
            yundonghou_layout.setVisibility(View.VISIBLE);
            set_ruchang_lichang_layout(false, 0, 5);

        } else {
            yundonghou_layout.setVisibility(View.INVISIBLE);
        }

        class_content.setText(sportEntity.getCourseDesDetail());
        normal_biaoqing = moodDetailBean.getSportsBefore();
        normal_check_out_biaoqing = moodDetailBean.getSportsAfter();
        //设置体测数据
        if (null != sportEntity.getTestDetail()) {
            SportEntity.TestDetailBean testDetailBean = sportEntity.getTestDetail();
            shengao_edit.setText(testDetailBean.getStature() + "");
            tizhong_edit.setText(testDetailBean.getWeight() + "");
            tizhi_edit.setText(testDetailBean.getFinalBodyfat() + "");
            bmi_edit.setText(testDetailBean.getFinalBmi() + "");
            yaotunbi_edit.setText(testDetailBean.getFinalwaistratebuttocks() + "");
            daixie_edit.setText(testDetailBean.getFinalKcal() + "");
        }
    }


    private void set_ruchang_lichang_layout(boolean is_set_ruchang, int ruchang_img_id, int lichang_img_id) {
        if (is_set_ruchang) {//设置入场
            if (ruchang_img_id == 1) {
                ruhang_biaoqing.setImageResource(R.drawable.xiaolian1);
                yundongqian_tv.setText("心情不错，来一组运动让自己充满元气～");
            } else if (ruchang_img_id == 2) {
                ruhang_biaoqing.setImageResource(R.drawable.xiaolian2);
                yundongqian_tv.setText("棒呆，好心情有利于运动哦！");
            } else if (ruchang_img_id == 3) {
                yundongqian_tv.setText("平平淡淡的生活，就用健身来调剂吧！");
                ruhang_biaoqing.setImageResource(R.drawable.xiaolian3);
            } else if (ruchang_img_id == 4) {
                yundongqian_tv.setText("摸了摸一整块腹肌，看来健身迫在眉睫了");
                ruhang_biaoqing.setImageResource(R.drawable.xiaolian4);
            } else if (ruchang_img_id == 5) {
                yundongqian_tv.setText("来健身发泄一下，丢掉坏心情");
                ruhang_biaoqing.setImageResource(R.drawable.xiaolian5);
            }
        } else {
            if (lichang_img_id == 1) {
                yundonghou_tv.setText("收拾好心情，生活才会更加精彩哦");
                yundonghou_img.setImageResource(R.drawable.xiaolian1);
            } else if (lichang_img_id == 2) {
                yundonghou_tv.setText("今日份运动目标轻松达成～好身材在向我招手");
                yundonghou_img.setImageResource(R.drawable.xiaolian2);
            } else if (lichang_img_id == 3) {
                yundonghou_img.setImageResource(R.drawable.xiaolian3);
                yundonghou_tv.setText("运动是一种习惯，坚持下去总会有收获");
            } else if (lichang_img_id == 4) {
                yundonghou_img.setImageResource(R.drawable.xiaolian4);
                yundonghou_tv.setText("抱抱你，不要沮丧，运动也是好好爱自己");
            } else if (lichang_img_id == 5) {
                yundonghou_img.setImageResource(R.drawable.xiaolian5);
                yundonghou_tv.setText("今天没有燃烧的卡路里，下次再找你们算账!");

            }
            if (yundong_before_layout.getVisibility() == View.VISIBLE && yundonghou_layout.getVisibility() == View.VISIBLE) {
                update_xinqing_btn.setVisibility(View.GONE);
            }

        }
    }


    private void delete_pop() {

        PublicPopControl.alert_dialog_center(this, new PopViewCallBack() {
            @Override
            public void return_view(View view, BasePopupView popup) {
                TextView content = view.findViewById(R.id.pop_content);
                TextView title = view.findViewById(R.id.pop_title);
                title.setText("确认删除日志？");
                content.setText("删除后将无法恢复，您确认要删除吗？");
                view.findViewById(R.id.cancel_btn)
                        .setOnClickListener(v -> popup.dismiss());
                view.findViewById(R.id.sure_btn)
                        .setOnClickListener(v -> {
                            popup.dismiss();
                            delete_daily();
                        });
            }
        });


    }

    private void delete_daily() {
        Map<String, Object> params = new HashMap<>();
        params.put("sportsNum", detail_id);
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "删除运动日志：" + json_params);
        Subscription subscription = Network.getInstance("删除运动日志", this)
                .shanchu_yundong_rizhi(requestBody, new ProgressSubscriberNew<>(SportEntity.class, new GsonSubscriberOnNextListener<SportEntity>() {
                    @Override
                    public void on_post_entity(SportEntity entity, String s) {
                        Log.e("删除运动日志成功", entity + "删除运动日志成功");
                        Toast.makeText(DailyTravelDetailActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("删除运动日志失败", "删除运动日志失败:" + error);
                        Toast.makeText(DailyTravelDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }

    private void select_biaoqing_pop(boolean is_update_yundonghou_biaoqing) {
        CommonPopupWindow popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.select_biaoqing_layout)
                .setBackGroundLevel(1)//0.5f
                .setAnimationStyle(R.style.AnimHorizontal)
                .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT)
                .setOutSideTouchable(true).create();
        popupWindow.showAsDropDown(update_xinqing_btn);

        /**设置逻辑*/
        View view = popupWindow.getContentView();
        ImageView xiaolian1 = view.findViewById(R.id.xiaolian1);
        ImageView xiaolian2 = view.findViewById(R.id.xiaolian2);
        ImageView xiaolian3 = view.findViewById(R.id.xiaolian3);
        ImageView xiaolian4 = view.findViewById(R.id.xiaolian4);
        ImageView xiaolian5 = view.findViewById(R.id.xiaolian5);
        xiaolian1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                change_status(1, is_update_yundonghou_biaoqing);
            }
        });
        xiaolian2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                change_status(2, is_update_yundonghou_biaoqing);

            }
        });
        xiaolian3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                change_status(3, is_update_yundonghou_biaoqing);

            }
        });
        xiaolian4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                change_status(4, is_update_yundonghou_biaoqing);

            }
        });
        xiaolian5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();

                change_status(5, is_update_yundonghou_biaoqing);

            }
        });


    }

    private void change_status(int i, boolean is_update_yundonghou_biaoqing) {
        UpdateTravalEntity updateTravalEntity = new UpdateTravalEntity();
        updateTravalEntity.setSportsNum(detail_id);
        if (i == -1) {//修改的是体侧
            UpdateTravalEntity.TestBean testBean = new UpdateTravalEntity.TestBean();
            int tizhong;
            if (tizhong_edit.getText().toString().equals("")) {
                tizhong = 0;
            } else {
                tizhong = Integer.valueOf(tizhong_edit.getText().toString());
            }
            testBean.setWeight(tizhong);
            //去掉百分号
            if (TextUtils.isEmpty(shengao_edit.getText().toString())) {
                shengao_edit.setText("0");
            }
            if (TextUtils.isEmpty(yaotunbi_edit.getText().toString())) {
                yaotunbi_edit.setText("0");
            }
            if (TextUtils.isEmpty(daixie_edit.getText().toString())) {
                daixie_edit.setText("0");
            }
            if (TextUtils.isEmpty(tizhi_edit.getText().toString())) {
                tizhi_edit.setText("0");
            }
            testBean.setFinalBodyfat(Double.valueOf(tizhi_edit.getText().toString()));
            testBean.setStature(Integer.valueOf(shengao_edit.getText().toString()));
            testBean.setFinalwaistratebuttocks(Double.valueOf(yaotunbi_edit.getText().toString()));
            testBean.setFinalKcal(Double.valueOf(daixie_edit.getText().toString()));
            testBean.setFinalBmi(Double.valueOf(bmi_edit.getText().toString()));
            updateTravalEntity.setTest(testBean);
        } else {
            UpdateTravalEntity.MoodBean moodBean = new UpdateTravalEntity.MoodBean();
            if (is_update_yundonghou_biaoqing) {
                moodBean.setSportsAfter(i);
                moodBean.setSportsBefore(normal_biaoqing);
                updateTravalEntity.setMood(moodBean);
            } else {
                moodBean.setSportsAfter(normal_check_out_biaoqing);
                moodBean.setSportsBefore(i);
                updateTravalEntity.setMood(moodBean);
            }
        }
        Gson gson = new Gson();
        String json_params = gson.toJson(updateTravalEntity);
        String json = new Gson().toJson(updateTravalEntity);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "修改日志详情参数：" + json_params);
        Subscription subscription = Network.getInstance("修改日志详情", this)
                .update_rizhi_detail(requestBody, new ProgressSubscriberNew<>(Object.class, new GsonSubscriberOnNextListener<Object>() {
                    @Override
                    public void on_post_entity(Object entity, String s) {
                        Log.e("修改日志详情成功", entity + "修改日志详情成功");
                        if (is_update_yundonghou_biaoqing) {
                            yundonghou_layout.setVisibility(View.VISIBLE);
                            set_ruchang_lichang_layout(false, 0, i);
                        } else {
                            yundong_before_layout.setVisibility(View.VISIBLE);
                            set_ruchang_lichang_layout(true, i, 0);
                        }
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("修改日志详情失败", "修改日志详情失败:" + error);
                        Toast.makeText(DailyTravelDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }, this, true));
    }


}

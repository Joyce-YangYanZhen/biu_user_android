package com.noplugins.keepfit.userplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.gson.Gson;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.base.BaseActivity;
import com.noplugins.keepfit.userplatform.entity.BodyResourceEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.data.StringsHelper;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.LineView;
import okhttp3.RequestBody;
import rx.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BodyResouceActivity extends BaseActivity {
    @BindView(R.id.base_information_edit)
    ImageView base_information_edit;
    @BindView(R.id.shengao_edit)
    EditText shengao_edit;
    @BindView(R.id.tizhong_edit)
    EditText tizhong_edit;
    @BindView(R.id.update_jinjie_btn)
    ImageView update_jinjie_btn;
    @BindView(R.id.tizhilv_edit)
    EditText tizhilv_edit;
    @BindView(R.id.yaotunbi_edit)
    EditText yaotunbi_edit;
    @BindView(R.id.jichudaxie_edit)
    EditText jichudaxie_edit;
    @BindView(R.id.bmi_edit)
    EditText bmi_edit;
    @BindView(R.id.save_btn)
    TextView save_btn;
    @BindView(R.id.save_jinjie_btn)
    TextView save_jinjie_btn;
    @BindView(R.id.tizhong_chart)
    LineView tizhong_chart;
    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.tizhilv_chart)
    LineView tizhilv_chart;

    String assessNum = "";

    private String[] yValues = new String[]{"48", "49", "55.5", "56.4", "58.0", "56.4", "55.5", "49"};
    private String[] yAisx = new String[]{"60", "58", "56", "54", "52", "50", "48"};
    //private String[] yAisx = new String[]{"125","100","75", "50", "25", "0"};

    private String[] xAisx = new String[]{"8/01", "8/02", "8/03", "8/04", "8/05", "8/06", "8/07", "8/08"};
    List<BodyResourceEntity.DataBean> dataBeans = new ArrayList<>();

    @Override
    public void initBundle(Bundle parms) {

    }

    @Override
    public void initView() {
        setContentLayout(R.layout.activity_body_resouce);
        ButterKnife.bind(this);
        isShowTitle(false);
    }

    private void initMessageDate(boolean is_update) {
        Map<String, Object> params = new HashMap<>();
        if ("".equals(SpUtils.getString(this, AppConstants.USER_NAME))) {

        } else {
            String user_id = SpUtils.getString(this, AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        params.put("year", "2019");
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "获取身体数据参数：" + json_params);
        Subscription subscription = Network.getInstance("获取身体数据", this)
                .get_body_resource(requestBody, new ProgressSubscriberNew<>(BodyResourceEntity.class, new GsonSubscriberOnNextListener<BodyResourceEntity>() {
                    @Override
                    public void on_post_entity(BodyResourceEntity entity, String s) {
                        if (dataBeans.size() > 0) {
                            dataBeans.clear();
                        }
                        dataBeans.addAll(entity.getData());
                        Log.e("获取身体数据成功", entity + "身体数据成功" + dataBeans.size());
                        if (dataBeans.size() > 0) {
                            set_date_value(entity);
                        }
                        if (is_update) {
                            //刷新整个activity
                            Intent intent = new Intent(BodyResouceActivity.this, BodyResouceActivity.class);
                            startActivity(intent);
                            finish();//关闭自己
                            overridePendingTransition(0, 0);
                        }

                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("身体数据失败", "身体数据失败:" + error);
                    }
                }, this, true));
    }


    @Override
    public void doBusiness(Context mContext) {
        initMessageDate(false);

        //修改基础信息
        base_information_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_btn.setVisibility(View.VISIBLE);
                base_information_edit.setVisibility(View.GONE);

                shengao_edit.setEnabled(true);
                StringsHelper.setSelecttion(shengao_edit);//设置光标显示的位置在最后
                tizhong_edit.setEnabled(true);

            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_value()) {
                    save_btn.setVisibility(View.GONE);
                    base_information_edit.setVisibility(View.VISIBLE);
                    shengao_edit.setEnabled(false);
                    tizhong_edit.setEnabled(false);
                    update_jinjie_date(true);
                }
            }
        });

        //修改进阶数据
        update_jinjie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_jinjie_btn.setVisibility(View.VISIBLE);
                update_jinjie_btn.setVisibility(View.GONE);
                tizhilv_edit.setEnabled(true);
                StringsHelper.setSelecttion(tizhilv_edit);//设置光标显示的位置在最后
                yaotunbi_edit.setEnabled(true);
                jichudaxie_edit.setEnabled(true);
                bmi_edit.setEnabled(false);

            }
        });
        save_jinjie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_value()) {
                    save_jinjie_btn.setVisibility(View.GONE);
                    update_jinjie_btn.setVisibility(View.VISIBLE);
                    tizhilv_edit.setEnabled(false);
                    yaotunbi_edit.setEnabled(false);
                    jichudaxie_edit.setEnabled(false);
                    bmi_edit.setEnabled(false);
                    update_jinjie_date(true);
                }
            }
        });


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tizhong_edit.addTextChangedListener(tizhong_textWatcher);
        shengao_edit.addTextChangedListener(tizhong_textWatcher);

    }

    private boolean check_value() {
        if (TextUtils.isEmpty(shengao_edit.getText())) {
            Toast.makeText(getApplicationContext(), "身高不可为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(tizhong_edit.getText())) {
            Toast.makeText(getApplicationContext(), "体重不可为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(yaotunbi_edit.getText())) {
            Toast.makeText(getApplicationContext(), "腰臀比不可为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(tizhilv_edit.getText())) {
            Toast.makeText(getApplicationContext(), "体脂率不可为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(jichudaxie_edit.getText())) {
            Toast.makeText(getApplicationContext(), "基础代谢不可为空", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    TextWatcher tizhong_textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (tizhong_edit.getText().length() > 0 && shengao_edit.getText().length() > 0) {
                double height = Double.parseDouble(shengao_edit.getText().toString()) / 100;
                double weight = Double.parseDouble(tizhong_edit.getText().toString());
                double BMI = weight / (height * height);
                bmi_edit.setText(BMI + "");
            }

        }
    };

    private void update_jinjie_date(boolean is_update) {
        Map<String, Object> params = new HashMap<>();
        params.put("userNum", SpUtils.getString(this, AppConstants.USER_NAME));
        params.put("assessNum", assessNum);
        if (tizhilv_edit.getText().length() > 0) {
            params.put("finalBodyfat", tizhilv_edit.getText().toString());
        }
        if (yaotunbi_edit.getText().length() > 0) {
            params.put("finalwaistratebuttocks", yaotunbi_edit.getText().toString());
        }
        if (jichudaxie_edit.getText().length() > 0) {
            params.put("finalKcal", jichudaxie_edit.getText().toString());
        }
        if (shengao_edit.getText().length() > 0) {
            params.put("stature", shengao_edit.getText().toString());
        }
        if (tizhong_edit.getText().length() > 0) {
            params.put("weight", tizhong_edit.getText().toString());
        }
        if (bmi_edit.getText().length() > 0) {
            params.put("finalBmi", bmi_edit.getText().toString());
        }
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e(TAG, "修改进阶参数：" + json_params);
        Subscription subscription = Network.getInstance("修改进阶", this)
                .update_jinjie_resource(requestBody, new ProgressSubscriberNew<>(Object.class, new GsonSubscriberOnNextListener<Object>() {
                    @Override
                    public void on_post_entity(Object object, String s) {
                        initMessageDate(is_update);
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("修改进阶失败", "修改进阶失败:" + error);
                    }
                }, this, false));


    }

    private void init_tizhi_chart(float[] data, String[] lables) {
        String yTitle = "体脂率 %";
        tizhilv_chart.setData(data);
        tizhilv_chart.setLables(lables);
        tizhilv_chart.setxTitle("");
        tizhilv_chart.setyTitle(yTitle);
        tizhilv_chart.setDataFactor(10);
    }

    private void init_tizhong_chart(float[] weight_data, String[] lables) {
        String yTitle = "体重 kg";
        tizhong_chart.setData(weight_data);
        tizhong_chart.setLables(lables);
        tizhong_chart.setxTitle("");
        tizhong_chart.setyTitle(yTitle);
        tizhong_chart.setDataFactor(10);
    }

    private void set_date_value(BodyResourceEntity entity) {
        BodyResourceEntity.DataBean bodyResourceEntity = entity.getData().get(entity.getData().size() - 1);
        shengao_edit.setText(bodyResourceEntity.getStature() + "");
        tizhong_edit.setText(bodyResourceEntity.getWeight() + "");
        tizhilv_edit.setText(bodyResourceEntity.getFinalBodyfat() + "");
        yaotunbi_edit.setText(bodyResourceEntity.getFinalwaistratebuttocks() + "");
        jichudaxie_edit.setText(bodyResourceEntity.getFinalKcal() + "");
        bmi_edit.setText(bodyResourceEntity.getFinalBmi() + "");

        float[] weight_data = new float[entity.getData().size()];
        String[] lables = new String[entity.getData().size()];
        float[] tizhi_data = new float[entity.getData().size()];

        for (int i = 0; i < entity.getData().size(); i++) {
            weight_data[i] = Float.valueOf(entity.getData().get(i).getWeight() + "");
            tizhi_data[i] = Float.valueOf(entity.getData().get(i).getFinalBodyfat() + "");
            lables[i] = entity.getData().get(i).getDayview();
        }

        assessNum = entity.getData().get(entity.getData().size() - 1).getAssessNum();

        init_tizhi_chart(tizhi_data, lables);

        init_tizhong_chart(weight_data, lables);

        //设置bmi
        if (tizhong_edit.getText().length() > 0 && shengao_edit.getText().length() > 0) {
            double height = Double.parseDouble(shengao_edit.getText().toString()) / 100;
            double weight = Double.parseDouble(tizhong_edit.getText().toString());
            double BMI = weight / (height * height);
            bmi_edit.setText(BMI + "");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}

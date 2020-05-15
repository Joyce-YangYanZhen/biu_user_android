package com.noplugins.keepfit.userplatform.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lxj.xpopup.core.BasePopupView;
import com.noplugins.keepfit.userplatform.R;
import com.noplugins.keepfit.userplatform.activity.BodyResouceActivity;
import com.noplugins.keepfit.userplatform.activity.CollectionActivity;
import com.noplugins.keepfit.userplatform.activity.OrderManageActivity;
import com.noplugins.keepfit.userplatform.activity.TravelActivity;
import com.noplugins.keepfit.userplatform.activity.info.InformationActivity;
import com.noplugins.keepfit.userplatform.activity.info.ProductAdviceActivity;
import com.noplugins.keepfit.userplatform.activity.info.SettingActivity;
import com.noplugins.keepfit.userplatform.adapter.MineTagAdapter;
import com.noplugins.keepfit.userplatform.base.BaseFragment;
import com.noplugins.keepfit.userplatform.callback.PopViewCallBack;
import com.noplugins.keepfit.userplatform.entity.TagEntity;
import com.noplugins.keepfit.userplatform.entity.UserInformationEntity;
import com.noplugins.keepfit.userplatform.global.AppConstants;
import com.noplugins.keepfit.userplatform.global.PublicPopControl;
import com.noplugins.keepfit.userplatform.util.BaseUtils;
import com.noplugins.keepfit.userplatform.util.SpUtils;
import com.noplugins.keepfit.userplatform.util.net.Network;
import com.noplugins.keepfit.userplatform.util.net.entity.Bean;
import com.noplugins.keepfit.userplatform.util.net.progress.GsonSubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.net.progress.ProgressSubscriberNew;
import com.noplugins.keepfit.userplatform.util.net.progress.SubscriberOnNextListener;
import com.noplugins.keepfit.userplatform.util.ui.pop.CommonPopupWindow;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MineFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.grid_view)
    GridView grid_view;
    @BindView(R.id.touxiang_image)
    CircleImageView touxiang_image;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.sex_img)
    ImageView sex_img;
    @BindView(R.id.qianming_tv)
    TextView qianming_tv;
    @BindView(R.id.call_kefu)
    LinearLayout call_kefu;
    @BindView(R.id.travel_daily_btn)
    LinearLayout travel_daily_btn;
    @BindView(R.id.body_resouce)
    LinearLayout body_resouce;
    @BindView(R.id.order_manage_btn)
    LinearLayout order_manage_btn;
    @BindView(R.id.mine_collection)
    LinearLayout mine_collection;
    @BindView(R.id.advise_btn)
    LinearLayout advise_btn;
    @BindView(R.id.ll_info)
    LinearLayout ll_info;

    @BindView(R.id.btn_setting)
    LinearLayout btn_setting;
    private View view;
    public static final int PERMISSION_STORAGE_CODE = 10001;
    public static final String PERMISSION_STORAGE_MSG = "需要电话权限才能联系客服哦";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.CALL_PHONE};


    public static MineFragment newInstance(String title) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString("home_fragment_title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
            ButterKnife.bind(this, view);//绑定黄牛刀
            EventBus.getDefault().register(this);
            initView();
        }
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent( String data) {
        if (data.equals("修改了个人信息")){
            initDate();
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible){
            initDate();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Glide.with(getActivity()).load(SpUtils.getString(getActivity(),AppConstants.LOGO))
////            .placeholder(R.drawable.logo_gray)
//                .error(R.drawable.logo_gray)
//                .into(touxiang_image);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    public void initSimple() {
        if (hasStoragePermission(getActivity())) {
            //有权限
            callPhone("4006836895");
        } else {
            //申请权限
            EasyPermissions.requestPermissions(getActivity(), PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
        }
    }

    /**
     * 是否有电话权限
     *
     * @param context
     * @return
     */
    public static boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA);
    }

    private void initView() {
        ll_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
        });
        call_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call_pop();
            }
        });
        travel_daily_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TravelActivity.class);
                startActivity(intent);
            }
        });
        body_resouce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BodyResouceActivity.class);
                startActivity(intent);
            }
        });
        order_manage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderManageActivity.class);
                startActivity(intent);
            }
        });
        mine_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
            }
        });
        advise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProductAdviceActivity.class);
                startActivity(intent);
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }



    private void call_pop() {

        PublicPopControl.alert_call_phone_dialog_center(getActivity(), new PopViewCallBack() {
            @Override
            public void return_view(View view, BasePopupView popup) {
                TextView title = view.findViewById(R.id.to_phone);
                title.setText("确认拨打 4006836895？");
                view.findViewById(R.id.cancel_btn)
                        .setOnClickListener(v -> popup.dismiss());
                view.findViewById(R.id.sure_btn)
                        .setOnClickListener(v -> {
                            popup.dismiss();
                            initSimple();
                        });
            }
        });


    }


    private void initDate() {
        Map<String, Object> params = new HashMap<>();
        if ("".equals(SpUtils.getString(getActivity(), AppConstants.USER_NAME))) {

        } else {
            String user_id = SpUtils.getString(getActivity(), AppConstants.USER_NAME);
            params.put("userNum", user_id);//用户编号
        }
        Gson gson = new Gson();
        String json_params = gson.toJson(params);
        String json = new Gson().toJson(params);//要传递的json
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Log.e("个人资料", "个人资料参数：" + json_params);
        Subscription subscription = Network.getInstance("个人资料", getActivity())
                .user_information(requestBody, new ProgressSubscriberNew<>(UserInformationEntity.class, new GsonSubscriberOnNextListener<UserInformationEntity>() {
                    @Override
                    public void on_post_entity(UserInformationEntity entity, String s) {
                        Log.e("个人资料成功", entity + "个人资料成功");
                        setInformation(entity);
                    }
                }, new SubscriberOnNextListener<Bean<Object>>() {
                    @Override
                    public void onNext(Bean<Object> result) {

                    }

                    @Override
                    public void onError(String error) {
                        Log.e("个人资料失败", "个人资料失败:" + error);
                    }
                }, getActivity(), false));
    }

    private void setInformation(UserInformationEntity entity) {
        Glide.with(getActivity()).load(entity.getLogo())
                .error(R.drawable.logo_gray)
                .into(touxiang_image);
        user_name.setText(entity.getNickname());
        if (entity.getSex() == 0) {//女
            Glide.with(getActivity()).load(R.drawable.women_icon).into(sex_img);
        } else {//男
            Glide.with(getActivity()).load(R.drawable.man_icon).into(sex_img);
        }
        qianming_tv.setText(entity.getSignature());
        //设置标签
        List<TagEntity> strings = new ArrayList<>();
        if (entity.getLableList() != null){
            for (int i = 0; i < entity.getLableList().size(); i++) {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setTag(entity.getLableList().get(i));
                strings.add(tagEntity);
            }
        }

        MineTagAdapter tagAdapter = new MineTagAdapter(getActivity(), strings);
        grid_view.setAdapter(tagAdapter);
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (BaseUtils.isFastClick()){

                }
            }
        });


    }

    /**
     * 权限设置页面回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());
    }

    /**
     * 拒绝权限
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("提醒")
                    .setRationale("需要电话权限才能联系客服哦")
                    .build()
                    .show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

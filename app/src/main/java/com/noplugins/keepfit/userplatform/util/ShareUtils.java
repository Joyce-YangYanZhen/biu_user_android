package com.noplugins.keepfit.userplatform.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import com.noplugins.keepfit.userplatform.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

public class ShareUtils {
    /**
     * 分享
     */
    public static void share( Activity context, Bitmap bitmap,SHARE_MEDIA share_media){
        UMImage image = new UMImage(context, bitmap);
        //SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,, SHARE_MEDIA.SINA
        new ShareAction(context)
                .withMedia(image)
                .setPlatform(share_media)//分享平台
                .setCallback(null)
                .share();
    }


    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}

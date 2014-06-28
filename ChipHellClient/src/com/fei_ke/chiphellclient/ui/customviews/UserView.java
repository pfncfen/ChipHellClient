
package com.fei_ke.chiphellclient.ui.customviews;

import android.content.Context;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fei_ke.chiphellclient.R;
import com.fei_ke.chiphellclient.bean.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * 用户视图
 * 
 * @author fei-ke
 * @2014-6-16
 */
@EViewGroup(R.layout.layout_user)
public class UserView extends FrameLayout {

    @ViewById(R.id.imageView_avatar)
    ImageView imageViewAvatar;

    @ViewById(R.id.textView_name)
    TextView textViewName;

    @ViewById(R.id.textView_info)
    TextView textViewInfo;

    public static UserView newInstance(Context context) {
        return UserView_.build(context);
    }

    public UserView(Context context) {
        super(context);
    }

    public void bindValue(User user) {
        ImageLoader.getInstance().displayImage(user.getAvatarUrl(), imageViewAvatar);
        textViewName.setText(user.getName());
        textViewInfo.setText(Html.fromHtml(user.getInfo()));
    }

}

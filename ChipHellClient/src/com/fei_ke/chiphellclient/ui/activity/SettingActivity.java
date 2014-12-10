
package com.fei_ke.chiphellclient.ui.activity;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.TextUtils;
import android.view.MenuItem;

import com.fei_ke.chiphellclient.R;
import com.fei_ke.chiphellclient.utils.GlobalSetting;
import com.fei_ke.chiphellclient.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashSet;
import java.util.Set;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackPreferenceActivity;

/**
 * 设置页面
 *
 * @author fei-ke
 * @2014年6月28日
 */
public class SettingActivity extends SwipeBackPreferenceActivity implements OnPreferenceChangeListener {

    private MultiSelectListPreference mSwipeEdge;
    private EditTextPreference mForumAddress;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        getSwipeBackLayout().setEdgeTrackingEnabled(GlobalSetting.getSwipeBackEdge());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.open_source_notices);
        BaseActivity.initActionBar(this);
        getListView().setBackgroundResource(R.color.background_light);

        setSwipeEdge();
        setForumAddress();
    }

    private void setForumAddress() {
        mForumAddress = (EditTextPreference) findPreference(GlobalSetting.FORUM_ADDRESS);
        String forumAddress = GlobalSetting.getForumAddress();
        mForumAddress.setSummary(forumAddress);
        mForumAddress.setText(forumAddress);
        mForumAddress.setOnPreferenceChangeListener(this);
    }

    private void setSwipeEdge() {
        mSwipeEdge = (MultiSelectListPreference) findPreference(GlobalSetting.SWIPE_BACK_EDGE);
        int edge = GlobalSetting.getSwipeBackEdge();

        Set<String> edges = new HashSet<String>();
        StringBuilder summary = new StringBuilder();
        if ((edge & SwipeBackLayout.EDGE_LEFT) != 0) {
            edges.add(String.valueOf(SwipeBackLayout.EDGE_LEFT));
            summary.append(getResources().getString(R.string.swipe_edge_left)).append(" ");
        }
        if ((edge & SwipeBackLayout.EDGE_RIGHT) != 0) {
            edges.add(String.valueOf(SwipeBackLayout.EDGE_RIGHT));
            summary.append(getResources().getString(R.string.swipe_edge_right)).append(" ");
        }
        if ((edge & SwipeBackLayout.EDGE_BOTTOM) != 0) {
            edges.add(String.valueOf(SwipeBackLayout.EDGE_BOTTOM));
            summary.append(getResources().getString(R.string.swipe_edge_bottom)).append(" ");
        }
        mSwipeEdge.setValues(edges);
        mSwipeEdge.setSummary(summary.toString());
        mSwipeEdge.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSwipeEdge) {
            Set<String> newValues = (Set<String>) newValue;
            int edge = 0;
            StringBuilder summary = new StringBuilder();
            for (String value : newValues) {
                switch (Integer.parseInt(value)) {
                    case SwipeBackLayout.EDGE_LEFT:
                        edge |= SwipeBackLayout.EDGE_LEFT;
                        summary.append(getResources().getString(R.string.swipe_edge_left)).append(" ");
                        break;
                    case SwipeBackLayout.EDGE_RIGHT:
                        edge |= SwipeBackLayout.EDGE_RIGHT;
                        summary.append(getResources().getString(R.string.swipe_edge_right)).append(" ");
                        break;
                    case SwipeBackLayout.EDGE_BOTTOM:
                        edge |= SwipeBackLayout.EDGE_BOTTOM;
                        summary.append(getResources().getString(R.string.swipe_edge_bottom)).append(" ");
                        break;
                }
            }
            GlobalSetting.putSwipeBackEdge(edge);
            mSwipeEdge.setSummary(summary.toString());
            return true;
        } else if (preference == mForumAddress) {
            String newAddress = (String) newValue;
            if (TextUtils.isEmpty(newAddress)) {
                newAddress = GlobalSetting.DEFAULT_FORUM_ADDRESS;
            }
            if (!newAddress.startsWith("http")) {
                newAddress = "http://" + newAddress;
            }
            if (!newAddress.endsWith("/")) {
                newAddress += "/";
            }
            GlobalSetting.setForumAddress(newAddress);
            mForumAddress.setSummary(newAddress);
            ToastUtil.show(this, "需重新启动应用生效");
            return true;
        }
        return false;
    }

}

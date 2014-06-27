
package com.fei_ke.chiphellclient.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.fei_ke.chiphellclient.R;
import com.umeng.analytics.MobclickAgent;

import me.imid.swipebacklayout.lib.app.SwipeBackPreferenceActivity;

public class SoftwareNoticesActivity extends SwipeBackPreferenceActivity{
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.software_notices);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.open_source_notices);
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

}

 
package cn.lanqiushe.ui;

import cn.lanqiushe.manager.TitleManager;

import cn.lanqiushe.R;
import android.os.Bundle;

public class SetAboutUsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.layout_aboutus);
     TitleManager.showTitle(this, null, R.string.app_name, R.string.title_back, 0);
    	super.onCreate(savedInstanceState);
    }
}

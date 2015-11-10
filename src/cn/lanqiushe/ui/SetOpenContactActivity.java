 
package cn.lanqiushe.ui;

import cn.lanqiushe.manager.TitleManager;

import cn.lanqiushe.R;
import android.os.Bundle;

public class SetOpenContactActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	TitleManager.showTitle(this, null, R.string.title_input_pwd, R.string.title_back,
				 R.string.title_next);
    	super.onCreate(savedInstanceState);
    }
}

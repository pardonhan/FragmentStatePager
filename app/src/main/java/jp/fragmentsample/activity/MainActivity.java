package jp.fragmentsample.activity;

import java.util.ArrayList;

import jp.fragmentsample.R;
import jp.fragmentsample.fragment.ButtonSwitchFragment;
import jp.fragmentsample.fragment.EmptyFragment;
import jp.fragmentsample.fragment.LabelListFragment;
import jp.fragmentsample.model.DummyGenerator;
import jp.fragmentsample.utils.DebugLog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        FragmentManager manager = getFragmentManager();
        // viewpager
        final ViewPager pager = (ViewPager)findViewById(R.id.pager);

        final MyAdapter1 adapter = new MyAdapter1(manager, this, pager);
   }
    

    private class MyAdapter1 extends FragmentPagerAdapter {

    	private final Context context;
    	private final ViewPager pager;


		public MyAdapter1(FragmentManager fm, Context context, ViewPager pager) {
			super(fm);
			this.context = context;
			this.pager = pager;
			this.pager.setAdapter(this);
		}
		

		@Override
		public Fragment getItem(int position) {
			DebugLog.v(position);
			if (position == 0) {
				DebugLog.i("Tab-0");
				return new ButtonSwitchFragment();
			}else if (position == 1){
				DebugLog.i("Tab-1");
				return new LabelListFragment(DummyGenerator.getLabelList());
			}else{
				DebugLog.i("Tab-2");
				return new EmptyFragment(Color.WHITE);
			}
		}

		@Override
		public int getCount() {
			DebugLog.v("getCount()");
			return 3;
		}

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return super.onOptionsItemSelected(item);
    }
}
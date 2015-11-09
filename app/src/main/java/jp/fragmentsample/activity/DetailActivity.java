package jp.fragmentsample.activity;

import java.util.ArrayList;
import java.util.List;

import jp.fragmentsample.R;
import jp.fragmentsample.fragment.DetailFragment;
import jp.fragmentsample.model.Item;
import jp.fragmentsample.utils.DebugLog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DetailActivity extends Activity {

	public final static String TAG = DetailActivity.class.getSimpleName();
	public final static String KEY_LIST = "key_list";
	public final static String KEY_POSITION = "key_position";
	
	ArrayList<Item> list;
	private List<DetailFragment> fragments;
	
	public DetailActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);
		DebugLog.e(TAG);
		//获取传递的数据
		Intent intent = getIntent();
		list = (ArrayList<Item>)intent.getSerializableExtra(KEY_LIST);
		
		fragments = new ArrayList<DetailFragment>();
		for (Item item : list) {
			DebugLog.i(item.getLabel());
			fragments.add(new DetailFragment(item));
		}
		
		final ViewPager pager = (ViewPager)findViewById(R.id.pager);

		pager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
			@Override
			public int getCount() {
				return list.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				return fragments.get(position);
			}
		});
		pager.setCurrentItem(intent.getIntExtra(KEY_POSITION, 0));
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detail_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

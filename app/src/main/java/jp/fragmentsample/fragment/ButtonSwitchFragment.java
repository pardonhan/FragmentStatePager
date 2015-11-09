package jp.fragmentsample.fragment;

import jp.fragmentsample.R;
import jp.fragmentsample.model.DummyGenerator;
import jp.fragmentsample.utils.DebugLog;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

public class ButtonSwitchFragment extends Fragment {
	
	public static final String TAG = ButtonSwitchFragment.class.getSimpleName();


	private View view;
	private int indexFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DebugLog.e("ButtonSwitchFragment");
		DebugLog.e(TAG);
		view = null;
		indexFragment = 0;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.buttonswitch_fragment, container, false);
		DebugLog.i("indexFragment->"+indexFragment);
		changeFragment(indexFragment);
		return view;
	}
	
	public void changeFragment(int index) {
		FragmentManager fragMgr = getFragmentManager();
		FragmentTransaction fragTrs = fragMgr.beginTransaction();
		
		indexFragment = index;
		switch (indexFragment) {
			case 0:
				DebugLog.e("changeFragment---index-->0");
				fragTrs.replace(R.id.lyt_main_fragment_container, new ItemListFragment(DummyGenerator.getItemNumberList(), true));
				break;
			case 1:
				DebugLog.e("changeFragment---index-->1");
				fragTrs.replace(R.id.lyt_main_fragment_container, new ItemListFragment(DummyGenerator.getItemAlphabetList(), true));
				break;
			case 2:
				DebugLog.e("changeFragment---index-->2");
				fragTrs.replace(R.id.lyt_main_fragment_container, new EmptyFragment(Color.WHITE));
				break;
		}
		fragTrs.commit();
	}

}

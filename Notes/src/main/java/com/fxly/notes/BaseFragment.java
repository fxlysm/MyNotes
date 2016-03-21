package com.fxly.notes;

import android.support.v4.app.Fragment;
import com.squareup.leakcanary.RefWatcher;
import com.fxly.notes.helpers.AnalyticsHelper;


public class BaseFragment extends Fragment {


	@Override
	public void onStart() {
		super.onStart();
		AnalyticsHelper.trackScreenView(getClass().getName());
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		//		Leaks 内存泄漏监控
//		RefWatcher refWatcher = OmniNotes.getRefWatcher();
//		refWatcher.watch(this);
	}

}

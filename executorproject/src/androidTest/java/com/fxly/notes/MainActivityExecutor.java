package com.fxly.notes;

import android.app.Activity;
import com.robotium.recorder.executor.Executor;

@SuppressWarnings("rawtypes")
public class MainActivityExecutor extends Executor {

	@SuppressWarnings("unchecked")
	public MainActivityExecutor() throws Exception {
		super((Class<? extends Activity>) Class.forName("com.fxly.notes.MainActivity"),  "com.fxly.notes.R.id.", new android.R.id(), false, false, "1458183631293");
	}

	public void setUp() throws Exception { 
		super.setUp();
	}
}

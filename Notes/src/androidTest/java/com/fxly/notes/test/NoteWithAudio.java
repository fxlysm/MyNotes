package com.fxly.notes.test;

import com.fxly.notes.MainActivity;
import com.fxly.notes.R;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class NoteWithAudio extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;
  	
  	public NoteWithAudio() {
		super(MainActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}

	public void testRun() {
        //Wait for activity: 'it.feio.android.omninotes.MainActivity'
		solo.waitForActivity(com.fxly.notes.MainActivity.class, 2000);
		solo.sleep(2000);//2秒
        //Set default small timeout to 19121 milliseconds
		Timeout.setSmallTimeout(19121);
        //Click on (1028.1432, 1752.0874)
		solo.clickOnScreen(1028.1432F, 1752.0874F);
        //Click on ImageView
		solo.clickLongOnView(solo.getView(R.id.fab_expand_menu_button));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(R.id.menu_attachment));
        //Click on Record
		solo.clickOnView(solo.getView(R.id.recording));
        //Click on Stop
		solo.clickOnView(solo.getView(R.id.recording));
        //Click on 0:04
		solo.clickInList(1, 0);
        //Click on 0:04
		solo.clickInList(1, 0);
        //Click on ImageView
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
	}
}

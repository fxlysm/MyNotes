package com.fxly.notes.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;

import com.fxly.notes.MainActivity;
import com.fxly.notes.R;
import com.fxly.notes.db.DbHelper;
import com.fxly.notes.utils.Navigation;


public class NoteWithAudioAndCategory extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;

  	public NoteWithAudioAndCategory() {
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

	public void testCategory() {
        //Wait for activity: 'it.feio.android.omninotes.MainActivity'
		solo.waitForActivity(MainActivity.class, 2000);
        //Set default small timeout to 19121 milliseconds
		Timeout.setSmallTimeout(19121);
        //Click on (1028.1432, 1752.0874)
		solo.clickOnScreen(1028.1432F, 1752.0874F);
        //Click on ImageView
		solo.clickLongOnView(solo.getView(com.fxly.notes.R.id.fab_expand_menu_button));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.fxly.notes.R.id.menu_attachment));
        //Click on Record
		solo.clickOnView(solo.getView(com.fxly.notes.R.id.recording));
        //Click on Stop
		solo.clickOnView(solo.getView(com.fxly.notes.R.id.recording));
		//Click on Category menu
		solo.clickOnView(solo.getView(com.fxly.notes.R.id.menu_category));
		//Add new category
		solo.clickOnView(solo.getView(com.afollestad.materialdialogs.R.id.buttonDefaultPositive));
		// Enter category name
		solo.enterText((EditText) solo.getView(R.id.category_title), "cat123");
		// Save category
		solo.clickOnView(solo.getView(com.fxly.notes.R.id.save));
		//Click on up navigation
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
		//Open navigation drawer
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
		//Select category in navigation drawer
		solo.clickInList(0, 2);
		solo.sleep(800);
		assertTrue(Navigation.checkNavigation(Navigation.CATEGORY));
		//Open navigation drawer
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
		//Select notes in navigation drawer
		solo.clickInList(0, 1);
		solo.sleep(800);
		assertTrue(Navigation.checkNavigation(Navigation.NOTES));

		// Category deletion

		int categoriesCount = DbHelper.getInstance().getCategories().size();
		//Open navigation drawer
		solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
		//Long click on first category
		solo.clickLongInList(0, 2);
		//Delete category
		solo.clickOnView(solo.getView(com.fxly.notes.R.id.delete));
		//Confirm deletion
		solo.clickOnView(solo.getView(com.afollestad.materialdialogs.R.id.buttonDefaultPositive));
		solo.sleep(500);
		//Checks categories number decreased
		assertEquals(DbHelper.getInstance().getCategories().size(), categoriesCount - 1);
	}
}

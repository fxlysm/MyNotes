/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fxly.notes.async;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import com.fxly.notes.MainActivity;
import com.fxly.notes.R;
import com.fxly.notes.async.bus.NavigationUpdatedEvent;
import com.fxly.notes.models.NavigationItem;
import com.fxly.notes.models.adapters.NavDrawerAdapter;
import com.fxly.notes.models.misc.DynamicNavigationLookupTable;
import com.fxly.notes.models.views.NonScrollableListView;
import com.fxly.notes.utils.Constants;
import com.fxly.notes.utils.Navigation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainMenuTask extends AsyncTask<Void, Void, List<NavigationItem>> {

    private final WeakReference<Fragment> mFragmentWeakReference;
    private final MainActivity mainActivity;
    @Bind(R.id.drawer_nav_list) NonScrollableListView mDrawerList;
    @Bind(R.id.drawer_tag_list) NonScrollableListView mDrawerCategoriesList;


    public MainMenuTask(Fragment mFragment) {
        mFragmentWeakReference = new WeakReference<>(mFragment);
        this.mainActivity = (MainActivity) mFragment.getActivity();
        ButterKnife.bind(this, mFragment.getView());
    }


    @Override
    protected List<NavigationItem> doInBackground(Void... params) {
        return buildMainMenu();
    }


    @Override
    protected void onPostExecute(final List<NavigationItem> items) {
        if (isAlive()) {
            mDrawerList.setAdapter(new NavDrawerAdapter(mainActivity, items));
            mDrawerList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
				String navigation = mFragmentWeakReference.get().getResources().getStringArray(R.array
						.navigation_list_codes)[items.get(position).getArrayIndex()];
				if (mainActivity.updateNavigation(navigation)) {
					mDrawerList.setItemChecked(position, true);
					if (mDrawerCategoriesList != null)
						mDrawerCategoriesList.setItemChecked(0, false); // Called to force redraw
					mainActivity.getIntent().setAction(Intent.ACTION_MAIN);
					EventBus.getDefault().post(new NavigationUpdatedEvent(mDrawerList.getItemAtPosition(position)));
				}
			});
            mDrawerList.justifyListViewHeightBasedOnChildren();
        }
    }


    private boolean isAlive() {
        return mFragmentWeakReference.get() != null
                && mFragmentWeakReference.get().isAdded()
                && mFragmentWeakReference.get().getActivity() != null
                && !mFragmentWeakReference.get().getActivity().isFinishing();
    }


    private List<NavigationItem> buildMainMenu() {
        if (!isAlive()) {
            return new ArrayList<>();
        }

        String[] mNavigationArray = mainActivity.getResources().getStringArray(R.array.navigation_list);
        TypedArray mNavigationIconsArray = mainActivity.getResources().obtainTypedArray(R.array.navigation_list_icons);
        TypedArray mNavigationIconsSelectedArray = mainActivity.getResources().obtainTypedArray(R.array
                .navigation_list_icons_selected);

        final List<NavigationItem> items = new ArrayList<>();
        for (int i = 0; i < mNavigationArray.length; i++) {
            if (!checkSkippableItem(i)) {
                NavigationItem item = new NavigationItem(i, mNavigationArray[i], mNavigationIconsArray.getResourceId(i,
                        0), mNavigationIconsSelectedArray.getResourceId(i, 0));
                items.add(item);
            }
        }
        return items;
    }


    private boolean checkSkippableItem(int i) {
        boolean skippable = false;
        SharedPreferences prefs = mainActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS);
        boolean dynamicMenu = prefs.getBoolean(Constants.PREF_DYNAMIC_MENU, true);
        DynamicNavigationLookupTable dynamicNavigationLookupTable = null;
        if (dynamicMenu) {
            dynamicNavigationLookupTable = DynamicNavigationLookupTable.getInstance();
        }
        switch (i) {
            case Navigation.REMINDERS:
                if (dynamicMenu && dynamicNavigationLookupTable.getReminders() == 0)
                    skippable = true;
                break;
            case Navigation.UNCATEGORIZED:
                boolean showUncategorized = prefs.getBoolean(Constants.PREF_SHOW_UNCATEGORIZED, false);
                if (!showUncategorized || (dynamicMenu && dynamicNavigationLookupTable.getUncategorized() == 0))
                    skippable = true;
                break;
            case Navigation.ARCHIVE:
                if (dynamicMenu && dynamicNavigationLookupTable.getArchived() == 0)
                    skippable = true;
                break;
            case Navigation.TRASH:
                if (dynamicMenu && dynamicNavigationLookupTable.getTrashed() == 0)
                    skippable = true;
                break;
        }
        return skippable;
    }

}
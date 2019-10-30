package com.project.jewelmart.swarnsarita.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.project.jewelmart.swarnsarita.fragments.HomeFragment;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragments = new ArrayList<>();
	private Fragment currentFragment;

	public DemoViewPagerAdapter(FragmentManager fm) {
		super(fm);
       // Fragment fragment=badgenew HomeFragment();
		fragments.clear();
		fragments.add(new HomeFragment());
		/*fragments.add(badgenew CartFragment());
		fragments.add(badgenew AccountFragment());
		fragments.add(badgenew OrderFragment());
		fragments.add(DemoFragment.newInstance(4));*/
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		if (getCurrentFragment() != object) {
			currentFragment = ((Fragment) object);
		}
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public Fragment getCurrentFragment() {
		return currentFragment;
	}
}
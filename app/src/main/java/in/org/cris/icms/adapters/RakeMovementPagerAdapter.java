package in.org.cris.icms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 23/6/17.
 */
public class RakeMovementPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public RakeMovementPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

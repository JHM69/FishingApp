package com.jhm69.farhad_fishingapp.Adapters;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jhm69.farhad_fishingapp.fragments.CategoryFragment;
import com.jhm69.farhad_fishingapp.fragments.MyPostFragment;
import com.jhm69.farhad_fishingapp.fragments.VideosFragment;
import com.jhm69.farhad_fishingapp.fragments.UserPostFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new MyPostFragment();
        } else if(position == 1){
            return new UserPostFragment();
        } else if(position == 2){
			return new CategoryFragment();
		} else{
            return new VideosFragment();
        }
	}



    @Override
    public int getCount() {
        return 4;
    }
}

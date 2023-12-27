package com.example.punekar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.punekar.IntroScreenItems;
import com.example.punekar.R;

import java.util.List;

public class IntroScreenViewPagerAdapter extends PagerAdapter {

    private List<IntroScreenItems> mScreenList;
    private Context mContext;

    public IntroScreenViewPagerAdapter(List<IntroScreenItems> mScreenList, Context mContext) {
        this.mScreenList = mScreenList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutview = inflater.inflate(R.layout.intro_layout_screen, null);

        TextView title = layoutview.findViewById(R.id.intro_title);
        TextView description = layoutview.findViewById(R.id.intro_description);
        ImageView img = layoutview.findViewById(R.id.intro_display);

        title.setText(mScreenList.get(position).getTitle());
        description.setText(mScreenList.get(position).getDescription());
        img.setImageResource(mScreenList.get(position).getImage());

        container.addView(layoutview);
        return layoutview;

    }

    @Override
    public int getCount() {
        return mScreenList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

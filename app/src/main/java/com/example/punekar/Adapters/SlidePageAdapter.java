package com.example.punekar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.punekar.Models.Slide;
import com.example.punekar.R;

import java.util.List;

public class SlidePageAdapter extends PagerAdapter {

    private Context mcontext;
    private List<Slide> mlst;

    public SlidePageAdapter(Context mcontext, List<Slide> mlst) {
        this.mcontext = mcontext;
        this.mlst = mlst;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slidelayout = layoutInflater.inflate(R.layout.slide_items,null);

        ImageView slideImage = slidelayout.findViewById(R.id.slide_image);
        TextView slideTitle = slidelayout.findViewById(R.id.slide_title);

        slideImage.setImageResource(mlst.get(position).getImage());
        slideTitle.setText(mlst.get(position).getTitle());

        container.addView(slidelayout);
        return slidelayout;
    }

    @Override
    public int getCount() {
        return mlst.size();
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

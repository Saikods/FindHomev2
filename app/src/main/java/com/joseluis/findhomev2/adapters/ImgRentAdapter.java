package com.joseluis.findhomev2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.pojo.Rents;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.BitSet;

public class ImgRentAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> images;
    private LayoutInflater layoutInflater;

    public ImgRentAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.img_rents, container, false);
        ImageView img = view.findViewById(R.id.img);


        Picasso.get().load(images.get(position)).into(img);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

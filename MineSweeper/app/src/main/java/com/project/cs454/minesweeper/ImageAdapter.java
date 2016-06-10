package com.project.cs454.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Arrays;

/**
 * Created by christoph on 5/6/16.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private Integer[] mThumbIds;






            /*
            {
            R.drawable.number_1, R.drawable.number_2,
            R.drawable.number_3, R.drawable.number_4,
            R.drawable.number_5, R.drawable.number_6,
            R.drawable.number_7, R.drawable.number_8,
            R.drawable.empty, R.drawable.flag,
            R.drawable.hidden, R.drawable.mine
            };
            */

    public ImageAdapter(Context c, int row, int col) {
        mContext = c;
        mThumbIds = new Integer[row * col];
        Arrays.fill(mThumbIds, R.drawable.hidden);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(135, 135));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;;
        }
        imageView.setImageResource(mThumbIds[position]);
        imageView.setTag(new Integer(0));
        return imageView;
    }


}
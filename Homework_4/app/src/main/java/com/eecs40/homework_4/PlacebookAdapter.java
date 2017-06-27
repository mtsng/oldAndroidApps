package com.eecs40.homework_4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Michael on 6/4/2015.
 */
public class PlacebookAdapter extends ArrayAdapter<PlacebookEntry> {
    private final Context context;
    private ArrayList<PlacebookEntry> mPlacebookEntries;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public PlacebookAdapter(Context context, ArrayList<PlacebookEntry> pbe){
        super(context, R.layout.row_layout, pbe);
        this.context = context;
        this.mPlacebookEntries = pbe;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate layouts
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        //Place TextView
        TextView textView = (TextView) rowView.findViewById(R.id.row_txtPlace);
        //Place Description TextView
        TextView textView1 = (TextView) rowView.findViewById(R.id.row_txtPlaceDesc);
        //Image icon
        ImageView imageView = (ImageView) rowView.findViewById(R.id.row_image_view);
        textView.setText(mPlacebookEntries.get(position).getName());
        textView1.setText(mPlacebookEntries.get(position).getDescript());
        //if photoPath exists display image
        if(mPlacebookEntries.get(position).getPhotoPath() != null) {
            File imageFile = new File(mPlacebookEntries.get(position).getPhotoPath());
            //Log.i("H", mPlacebookEntries.get(position).getPhotoPath());
            if (imageFile.exists()) {
                //Log.i("True", "It entered");
                Bitmap b = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                //scales image to icon size
                Bitmap scaled = scaleBitmap(b);
                imageView.setImageBitmap(scaled);
            }
        }
        //else use default image
        else {
            imageView.setImageResource(R.drawable.btn_rating_star_off_normal);
        }
        return rowView;
    }
    private Bitmap scaleBitmap(Bitmap b){
        //handles strange bug where bitmap is null
        if(b == null){
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.btn_rating_star_off_pressed, options);
        }
        else {
            return Bitmap.createScaledBitmap(b, 70, 70, true);
        }
    }
}
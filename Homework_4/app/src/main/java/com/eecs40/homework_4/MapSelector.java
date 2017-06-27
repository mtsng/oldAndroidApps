package com.eecs40.homework_4;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by Michael on 5/27/2015.
 */
public class MapSelector {
        public static final int REQUEST_PLACE_PICKER = 1003;
        private Context context;
        private MainActivity main;

        public MapSelector(Context context, MainActivity main){
            this.context = context;
            this.main = main;
        }

        public void launchPlacePicker () {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Context context = main.getApplicationContext();
            try {
                main.startActivityForResult(builder.build(context), REQUEST_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                Toast.makeText(context, "PlacePicker Error", Toast.LENGTH_SHORT).show();
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(context, "PlacePicker Error - Play Services Not Available", Toast.LENGTH_SHORT).show();
            }
        }

}

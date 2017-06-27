package com.eecs40.homework_4;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    public static final String VIEW_ALL_KEY = "com.eecs40.homework_4.EXTRA_VIEW_ALL" ;
    private static final int REQUEST_VIEW_ALL = 1005;
    private ArrayList <PlacebookEntry> mPlacebookEntries = new ArrayList<>();
    private ImageButton mBtnSnapshot;
    private ImageButton mBtnPlacePicker;
    private ImageButton mBtnSpeechtoText;
    private ImageButton mBtnLocation;
    private GoogleApiClient mGoogleApiClient;
    private CameraInput camera;
    private MapSelector select;
    private SpeechInput speech;
    private Location location;
    private boolean editMode = false;
    private String photoPath;
    private int id = 0;
    public static boolean deleting = false;
    //for location coordinates
    private double mLat;
    private double mLong;
    private double mAlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camera = new CameraInput(getBaseContext(), this);
        select = new MapSelector(getBaseContext(), this);
        speech = new SpeechInput(getBaseContext(), this);
        //inflates layout
        setContentView(R.layout.activity_main);

        mBtnSnapshot = (ImageButton) findViewById(R.id.button_snapshot);
        mBtnPlacePicker = (ImageButton) findViewById(R.id.button_place_picker);
        mBtnSpeechtoText = (ImageButton) findViewById(R.id.button_speak);
        mBtnLocation = (ImageButton) findViewById(R.id.button_location);

        initGoogleApi();

        mBtnSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.dispatchTakePictureIntent();
                photoPath = camera.getPhotoPath();
            }
        });
        mBtnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.launchPlacePicker();
            }
        });

        mBtnSpeechtoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech.dispatchSpeechInputIntent();
            }
        });

        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setLocation()) {
                    dispathLocation();
                }
            }
        });
    }

    // Call dispatch View All Places () when its menu command is selected .
    private void dispatchViewAllPlaces () {
        if(editMode){
            EditText editText1 = (EditText) findViewById(R.id.txtPlaceContent);
            EditText editText2 = (EditText) findViewById(R.id.edit_place_desc);
            String name = editText1.getText().toString();
            String description = editText2.getText().toString();
            if(HistoryActivity.selectedItem == -1){
                Toast.makeText(this, "Edit Error", Toast.LENGTH_SHORT).show();
            }
            else {
                mPlacebookEntries.get(HistoryActivity.selectedItem).name = name;
                mPlacebookEntries.get(HistoryActivity.selectedItem).description = description;
                mPlacebookEntries.get(HistoryActivity.selectedItem).photoPath = photoPath;
            }
        }

        Intent intent = new Intent(this, HistoryActivity.class);
        intent.putParcelableArrayListExtra(VIEW_ALL_KEY, mPlacebookEntries);
        try {
            startActivityForResult(intent, REQUEST_VIEW_ALL);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, "Activity Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void AddPlace(CameraInput c, int i){
        editMode = false;
        EditText editText1 = (EditText) findViewById(R.id.txtPlaceContent);
        EditText editText2 = (EditText) findViewById(R.id.edit_place_desc);
        String name = editText1.getText().toString();
        String description = editText2.getText().toString();
        String photoPath = c.getPhotoPath();
        PlacebookEntry pbe = new PlacebookEntry(i, name, description, photoPath);
        mPlacebookEntries.add(pbe);
    }

    private void dispathLocation(){
        TextView tvLa = (TextView) findViewById(R.id.txtGpsLatitudeContent);
        TextView tvLo = (TextView) findViewById(R.id.txtGpsLongitudeContent);
        TextView tvAl = (TextView) findViewById(R.id.txtGpsAltitudeContent);
        tvLa.setText(Double.toString(mLat));
        tvLo.setText(Double.toString(mLong));
        tvAl.setText(Double.toString(mAlt));
    }

    private boolean setLocation(){
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location != null){
            mLat = location.getLatitude();
            mLong = location.getLongitude();
            mAlt = location.getAltitude();
            return true;
        }
        else{
            Toast.makeText(this, "Location Cannot be Found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void resetText(){
        EditText editText1 = (EditText) findViewById(R.id.txtPlaceContent);
        EditText editText2 = (EditText) findViewById(R.id.edit_place_desc);
        editText1.setText(null);
        editText2.setText(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: return true;
            case R.id.action_new_place:
                AddPlace(camera, id);
                camera = new CameraInput(getBaseContext(), this);
                resetText();
                id++;
                return true;
            case R.id.action_view_all :
                dispatchViewAllPlaces();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    // Call init Google Api() from MainActivity.onCreate ()
    private void initGoogleApi () {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.i("H", "RequestCode: "+requestCode);
        //Log.i("H", "ResultCode: "+resultCode);

        //handles up-button back to main activity - passes back edited array
        if (requestCode == REQUEST_VIEW_ALL && data != null) {
            resetText();
            mPlacebookEntries = data.getParcelableArrayListExtra(VIEW_ALL_KEY);
            if(resultCode == HistoryActivity.RESULT_EDIT){
                editMode = true;
                EditText editText1 = (EditText) findViewById(R.id.txtPlaceContent);
                EditText editText2 = (EditText) findViewById(R.id.edit_place_desc);
                //handles potentially improper index
                if(HistoryActivity.selectedItem == -1){
                    Toast.makeText(this, "Editing Error - Please Try Again", Toast.LENGTH_SHORT).show();
                }
                else {
                    editText1.setText(mPlacebookEntries.get(HistoryActivity.selectedItem).name);
                    editText2.setText(mPlacebookEntries.get(HistoryActivity.selectedItem).description);
                    photoPath = mPlacebookEntries.get(HistoryActivity.selectedItem).photoPath;
                }
            }
            else{editMode = false;}
        }
        //handle back-button (i.e. lower arrow) use for improper deleting
        if(resultCode != RESULT_OK && deleting && resultCode != HistoryActivity.RESULT_EDIT){
            Toast.makeText(this, "Entry Not Deleted - Please Use Upper Button", Toast.LENGTH_LONG).show();
            deleting = false;
        }
        //handle camera results
        if(requestCode == CameraInput.REQUEST_IMAGE_CAPTURE){
            if(resultCode != RESULT_OK){
                Toast.makeText(this, "Capture Mode Exited", Toast.LENGTH_SHORT).show();
            }
        }
        //handle placepicker results
        if(requestCode == MapSelector.REQUEST_PLACE_PICKER && resultCode == RESULT_OK){
            Place place = PlacePicker.getPlace(data, this);
            /*String toastMsg = String.format("Place: %s", place.getName());
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            EditText editText1 = (EditText) findViewById(R.id.txtPlaceContent);
            editText1.setText(place.getName());
        }
        //handle voice-to-text results
        if(requestCode == SpeechInput.REQUEST_SPEECH_INPUT){
            if(resultCode == RESULT_OK){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                EditText editText1 = (EditText) findViewById(R.id.txtPlaceContent);
                EditText editText2 = (EditText) findViewById(R.id.edit_place_desc);
                //checks which textbox to fill in
                if(editText1.hasFocus()) {
                    editText1.setText(result.get(0));
                }
                else{
                    editText2.setText(result.get(0));
                }
            }
            else{
                Toast.makeText(this, "Speech-to-Text Exited", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Required google method
    }

    @Override
    public void onConnectionSuspended(int cause) {
        //Required google method
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, "Error - Connection Failure", Toast.LENGTH_SHORT).show();
    }
}

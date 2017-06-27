package com.eecs40.homework_4;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Michael on 5/27/2015.
 */
public class PlacebookEntry  implements Parcelable{
    private final long id;
    public String name;
    public String description;
    public String photoPath;

    public PlacebookEntry(int i, String n, String d, String p){
        this.id = i;
        this.name = n;
        this.description = d;
        this.photoPath = p;
    }

    public String getName(){
        return name;
    }

    public String getDescript(){
        return description;
    }

    public String getPhotoPath(){
        return photoPath;
    }

    public PlacebookEntry(Parcel source) {
        this.id = source.readLong() ;
        this.name = source.readString() ;
        this.description = source.readString() ;
        this.photoPath = source.readString() ;
        //....
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name.toString ());
        dest.writeString(this.description);
        dest.writeString(this.photoPath);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator <PlacebookEntry> CREATOR = new Parcelable.Creator<PlacebookEntry>() {
        @Override
        public PlacebookEntry createFromParcel(Parcel source) {
            return new PlacebookEntry(source);
        }
        @Override
        public PlacebookEntry[] newArray(int size) {
            return new PlacebookEntry[size];
        }
    };

}

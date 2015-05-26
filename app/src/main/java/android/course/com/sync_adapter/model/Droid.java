package android.course.com.sync_adapter.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class Droid implements Parcelable {
    private String title;
    private String id;

    public Droid(String id, String title) {
        this.id = id;
        this.title = title;
    }

    private Droid(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static final Parcelable.Creator<Droid> CREATOR = new Parcelable.Creator<Droid>() {
        public Droid createFromParcel(Parcel in) {
            return new Droid(in);
        }

        public Droid[] newArray(int size) {
            return new Droid[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(id);
        dest.writeString(title);
    }
}

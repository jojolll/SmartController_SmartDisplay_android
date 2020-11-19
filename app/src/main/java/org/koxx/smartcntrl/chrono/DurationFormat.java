package org.koxx.smartcntrl.chrono;


import android.os.Parcel;
import android.os.Parcelable;

class DurationFormat implements Parcelable {
    int count;
    long main_time;
    long diff;


    public DurationFormat(int count, long main_time, long diff) {
        this.main_time = main_time;
        this.diff = diff;
        this.count = count;
    }

    @Override
    public String toString() {
        return count + "       +" + (int) diff / 1000 + "    " + ChronometerTimeOn.formatToSeconds(main_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeLong(this.main_time);
        dest.writeLong(this.diff);
    }

    protected DurationFormat(Parcel in) {
        this.count = in.readInt();
        this.main_time = in.readLong();
        this.diff = in.readLong();
    }

    public static final Parcelable.Creator<DurationFormat> CREATOR = new Parcelable.Creator<DurationFormat>() {
        @Override
        public DurationFormat createFromParcel(Parcel source) {
            return new DurationFormat(source);
        }

        @Override
        public DurationFormat[] newArray(int size) {
            return new DurationFormat[size];
        }
    };
}


package org.koxx.smartlcd.chrono;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

public class ChronometerTimeOn implements Parcelable {
    public static ChronometerTimeOn chrono;
    static Boolean milli = true;

    public static ChronometerTimeOn getChrono() {
        return chrono;
    }

    public static void setChrono(ChronometerTimeOn chrono) {
        ChronometerTimeOn.chrono = chrono;
    }

    public long global;
    long son_baslama;
    Boolean stop;
    Boolean isStarted;
    long currentTime;
    int nbDurations;
    long toplam_durma;
    LinkedList<DurationFormat> durations;

    public ChronometerTimeOn() {
        global = 0;
        son_baslama = 0;
        stop = true;
        isStarted = false;
        currentTime = 0;
        nbDurations = 0;
        toplam_durma = 0;
        durations = new LinkedList<DurationFormat>();
    }

    public void Start() {
        isStarted = true;
        stop = false;
        currentTime = System.currentTimeMillis();
        son_baslama = currentTime;
    }

    public void Stop() {
        stop = true;
        nbDurations++;
        DurationFormat son;
        if (nbDurations == 1) {
            son = new DurationFormat(nbDurations, global, System.currentTimeMillis() - currentTime);
        } else {
            son = new DurationFormat(nbDurations, global, System.currentTimeMillis() - son_baslama);
        }

        durations.add(son);
    }

    public void CountUp() {
        if (isStarted) {
            if (stop == false) {
                global = System.currentTimeMillis() - currentTime - toplam_durma;
            } else {
                toplam_durma = System.currentTimeMillis() - currentTime - global;
            }
        }
    }

    public void Reset() {
        global = 0;
        toplam_durma = 0;
        stop = true;
        isStarted = false;
        while (durations.size() != 0) {
            durations.remove(0);
        }
        currentTime = 0;
        nbDurations = 0;
        son_baslama = 0;
    }

    public static String formatToSeconds(long current) {
        long hour = current / 3600000;
        long minute = (current % 3600000) / 60000;
        long second = (current - (hour * 3600000) - (minute * 60000)) / 1000;
        String format = hour + ":" + ((minute < 10) ? "0" : "") + minute + ":"+ ((second < 10) ? "0" : "")  + second;
        return format;
    }

    public void Continue() {
        stop = false;
        son_baslama = System.currentTimeMillis();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.global);
        dest.writeLong(this.son_baslama);
        dest.writeValue(this.stop);
        dest.writeValue(this.isStarted);
        dest.writeLong(this.currentTime);
        dest.writeInt(this.nbDurations);
        dest.writeLong(this.toplam_durma);
        dest.writeList(this.durations);
    }

    protected ChronometerTimeOn(Parcel in) {
        this.global = in.readLong();
        this.son_baslama = in.readLong();
        this.stop = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isStarted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.currentTime = in.readLong();
        this.nbDurations = in.readInt();
        this.toplam_durma = in.readLong();
        this.durations = new LinkedList<DurationFormat>();
        in.readList(this.durations, DurationFormat.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChronometerTimeOn> CREATOR = new Parcelable.Creator<ChronometerTimeOn>() {
        @Override
        public ChronometerTimeOn createFromParcel(Parcel source) {
            return new ChronometerTimeOn(source);
        }

        @Override
        public ChronometerTimeOn[] newArray(int size) {
            return new ChronometerTimeOn[size];
        }
    };
}



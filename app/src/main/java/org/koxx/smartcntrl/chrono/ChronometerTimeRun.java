package org.koxx.smartcntrl.chrono;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

public class ChronometerTimeRun implements Parcelable {
    public static ChronometerTimeRun chrono;
    static Boolean milli = true;

    public static ChronometerTimeRun getChrono() {
        return chrono;
    }

    public static void setChrono(ChronometerTimeRun chronoTimeOn) {
        ChronometerTimeRun.chrono = chronoTimeOn;
    }

    public long global;
    long son_baslama;
    Boolean stop;
    public Boolean isStarted;
    long ilk_oynatma;
    int nbDurations;
    long toplam_durma;
    LinkedList<DurationFormat> durations;

    public ChronometerTimeRun() {
        global = 0;
        son_baslama = 0;
        stop = true;
        isStarted = false;
        ilk_oynatma = 0;
        nbDurations = 0;
        toplam_durma = 0;
        durations = new LinkedList<DurationFormat>();
    }

    public void Start() {
        isStarted = true;
        stop = false;
        ilk_oynatma = System.currentTimeMillis();
        son_baslama = ilk_oynatma;
    }

    public void Stop() {
        stop = true;
        nbDurations++;
        DurationFormat son;
        if (nbDurations == 1) {
            son = new DurationFormat(nbDurations, global, System.currentTimeMillis() - ilk_oynatma);
        } else {
            son = new DurationFormat(nbDurations, global, System.currentTimeMillis() - son_baslama);
        }

        durations.add(son);
    }

    public void CountUp() {
        if (isStarted) {
            if (stop == false) {
                global = System.currentTimeMillis() - ilk_oynatma - toplam_durma;
            } else {
                toplam_durma = System.currentTimeMillis() - ilk_oynatma - global;
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
        ilk_oynatma = 0;
        nbDurations = 0;
        son_baslama = 0;
    }

    public static String formatToSeconds(long current) {
        long hour = current / 3600000;
        long minute = (current % 3600000) / 60000;
        long second = (current - (hour * 3600000) - (minute * 60000)) / 1000;
        String format = hour + " : " + minute + " : " + second;
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
        dest.writeLong(this.ilk_oynatma);
        dest.writeInt(this.nbDurations);
        dest.writeLong(this.toplam_durma);
        dest.writeList(this.durations);
    }

    protected ChronometerTimeRun(Parcel in) {
        this.global = in.readLong();
        this.son_baslama = in.readLong();
        this.stop = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isStarted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.ilk_oynatma = in.readLong();
        this.nbDurations = in.readInt();
        this.toplam_durma = in.readLong();
        this.durations = new LinkedList<DurationFormat>();
        in.readList(this.durations, DurationFormat.class.getClassLoader());
    }

    public static final Creator<ChronometerTimeRun> CREATOR = new Creator<ChronometerTimeRun>() {
        @Override
        public ChronometerTimeRun createFromParcel(Parcel source) {
            return new ChronometerTimeRun(source);
        }

        @Override
        public ChronometerTimeRun[] newArray(int size) {
            return new ChronometerTimeRun[size];
        }
    };

}

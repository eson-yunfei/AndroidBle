package org.e.ble.scan;

import android.os.Parcel;
import android.os.Parcelable;

public class ScanCfg implements Parcelable {


    private final long mPowerSaveScanInterval;
    private final long mPowerSaveRestInterval;

    private long mReportDelayMillis;

    public long getReportDelayMillis() {
        return mReportDelayMillis;
    }

    private ScanCfg(long reportDelayMillis,
                    long powerSaveScanInterval, long powerSaveRestInterval) {
        mReportDelayMillis = reportDelayMillis;
        mPowerSaveScanInterval = powerSaveScanInterval;
        mPowerSaveRestInterval = powerSaveRestInterval;
    }

    private ScanCfg(Parcel in) {
        mReportDelayMillis = in.readLong();
        mPowerSaveScanInterval = in.readLong();
        mPowerSaveRestInterval = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mReportDelayMillis);
        dest.writeLong(mPowerSaveScanInterval);
        dest.writeLong(mPowerSaveRestInterval);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScanCfg>
            CREATOR = new Creator<ScanCfg>() {
        @Override
        public ScanCfg[] newArray(int size) {
            return new ScanCfg[size];
        }

        @Override
        public ScanCfg createFromParcel(Parcel in) {
            return new ScanCfg(in);
        }
    };

    public boolean hasPowerSaveMode() {
        return mPowerSaveRestInterval > 0 && mPowerSaveScanInterval > 0;
    }

    public long getPowerSaveRest() {
        return mPowerSaveRestInterval;
    }

    public long getPowerSaveScan() {
        return mPowerSaveScanInterval;
    }

    public static final class Builder {
        private long mReportDelayMillis = 0;
        private long mPowerSaveRestInterval = 0;
        private long mPowerSaveScanInterval = 0;


//        public Builder setReportDelay(long reportDelay) {
//            mReportDelayMillis = reportDelay;
//            return this;
//        }


        public Builder setPowerSave(final long scanInterval, final long restInterval) {
            if (scanInterval <= 0 || restInterval <= 0) {
                throw new IllegalArgumentException("scanInterval and restInterval must be > 0");
            }
            mPowerSaveScanInterval = scanInterval;
            mPowerSaveRestInterval = restInterval;
            return this;
        }

        public ScanCfg build() {
            return new ScanCfg(mReportDelayMillis,
                    mPowerSaveScanInterval, mPowerSaveRestInterval);
        }
    }
}

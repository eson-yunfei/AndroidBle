package org.e.ble.scan.support;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

public class ScanResult implements Parcelable {
	// Remote bluetooth device.
	private BluetoothDevice mDevice;

	// Scan record, including advertising data and scan response data.
	@Nullable
	private ScanRecord mScanRecord;

	// Received signal strength.
	private int mRssi;

	// Device timestamp when the result was last seen.
	private long mTimestampNanos;

	/**
	 * Constructor of scan result.
	 *
	 * @param device Remote bluetooth device that is found.
	 * @param scanRecord Scan record including both advertising data and scan response data.
	 * @param rssi Received signal strength.
	 * @param timestampNanos Device timestamp when the scan result was observed.
	 */
	public ScanResult(BluetoothDevice device, @Nullable ScanRecord scanRecord, int rssi,
					  long timestampNanos) {
		mDevice = device;
		mScanRecord = scanRecord;
		mRssi = rssi;
		mTimestampNanos = timestampNanos;
	}

	private ScanResult(Parcel in) {
		readFromParcel(in);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (mDevice != null) {
			dest.writeInt(1);
			mDevice.writeToParcel(dest, flags);
		} else {
			dest.writeInt(0);
		}
		if (mScanRecord != null) {
			dest.writeInt(1);
			dest.writeByteArray(mScanRecord.getBytes());
		} else {
			dest.writeInt(0);
		}
		dest.writeInt(mRssi);
		dest.writeLong(mTimestampNanos);
	}

	private void readFromParcel(Parcel in) {
		if (in.readInt() == 1) {
			mDevice = BluetoothDevice.CREATOR.createFromParcel(in);
		}
		if (in.readInt() == 1) {
			mScanRecord = ScanRecord.parseFromBytes(in.createByteArray());
		}
		mRssi = in.readInt();
		mTimestampNanos = in.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Returns the remote bluetooth device identified by the bluetooth device address.
	 */
	public BluetoothDevice getDevice() {
		return mDevice;
	}

	/**
	 * Returns the scan record, which is a combination of advertisement and scan response.
	 */
	@Nullable
	public ScanRecord getScanRecord() {
		return mScanRecord;
	}

	/**
	 * Returns the received signal strength in dBm. The valid range is [-127, 127].
	 */
	public int getRssi() {
		return mRssi;
	}

	/**
	 * Returns timestamp since boot when the scan record was observed.
	 */
	public long getTimestampNanos() {
		return mTimestampNanos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mDevice, mRssi, mScanRecord, mTimestampNanos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ScanResult other = (ScanResult) obj;
		return Objects.equals(mDevice, other.mDevice) && (mRssi == other.mRssi) &&
				Objects.equals(mScanRecord, other.mScanRecord)
				&& (mTimestampNanos == other.mTimestampNanos);
	}

	@Override
	public String toString() {
		return "ScanResult{" + "mDevice=" + mDevice + ", mScanRecord="
				+ Objects.toString(mScanRecord) + ", mRssi=" + mRssi + ", mTimestampNanos="
				+ mTimestampNanos + '}';
	}

	public static final Creator<ScanResult> CREATOR = new Creator<ScanResult>() {
		@Override
		public ScanResult createFromParcel(Parcel source) {
			return new ScanResult(source);
		}

		@Override
		public ScanResult[] newArray(int size) {
			return new ScanResult[size];
		}
	};
}

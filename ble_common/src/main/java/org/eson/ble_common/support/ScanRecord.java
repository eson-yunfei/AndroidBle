package org.eson.ble_common.support;

import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanRecord {

	private static final String TAG = "ScanRecord";

	private static final int DATA_TYPE_FLAGS = 0x01;
	private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 0x02;
	private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 0x03;
	private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 0x04;
	private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 0x05;
	private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 0x06;
	private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 0x07;
	private static final int DATA_TYPE_LOCAL_NAME_SHORT = 0x08;
	private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 0x09;
	private static final int DATA_TYPE_TX_POWER_LEVEL = 0x0A;
	private static final int DATA_TYPE_SERVICE_DATA = 0x16;
	private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xFF;

	private final int mAdvertiseFlags;

	@Nullable
	private final List<ParcelUuid> mServiceUuids;

	private final SparseArray<byte[]> mManufacturerSpecificData;

	private final Map<ParcelUuid, byte[]> mServiceData;

	// Transmission power level(in dB).
	private final int mTxPowerLevel;

	// Local name of the Bluetooth LE device.
	private final String mDeviceName;

	// Raw bytes of scan record.
	private final byte[] mBytes;

	public int getAdvertiseFlags() {
		return mAdvertiseFlags;
	}

	public List<ParcelUuid> getServiceUuids() {
		return mServiceUuids;
	}

	public SparseArray<byte[]> getManufacturerSpecificData() {
		return mManufacturerSpecificData;
	}

	@Nullable
	public byte[] getManufacturerSpecificData(int manufacturerId) {
		return mManufacturerSpecificData.get(manufacturerId);
	}

	public Map<ParcelUuid, byte[]> getServiceData() {
		return mServiceData;
	}

	@Nullable
	public byte[] getServiceData(ParcelUuid serviceDataUuid) {
		if (serviceDataUuid == null) {
			return null;
		}
		return mServiceData.get(serviceDataUuid);
	}

	public int getTxPowerLevel() {
		return mTxPowerLevel;
	}

	@Nullable
	public String getDeviceName() {
		return mDeviceName;
	}

	/**
	 * Returns raw bytes of scan record.
	 */
	public byte[] getBytes() {
		return mBytes;
	}

	private ScanRecord(List<ParcelUuid> serviceUuids,
					   SparseArray<byte[]> manufacturerData,
					   Map<ParcelUuid, byte[]> serviceData,
					   int advertiseFlags, int txPowerLevel,
					   String localName, byte[] bytes) {
		mServiceUuids = serviceUuids;
		mManufacturerSpecificData = manufacturerData;
		mServiceData = serviceData;
		mDeviceName = localName;
		mAdvertiseFlags = advertiseFlags;
		mTxPowerLevel = txPowerLevel;
		mBytes = bytes;
	}

	/* package */ static ScanRecord parseFromBytes(byte[] scanRecord) {
		if (scanRecord == null) {
			return null;
		}

		int currentPos = 0;
		int advertiseFlag = -1;
		List<ParcelUuid> serviceUuids = new ArrayList<>();
		String localName = null;
		int txPowerLevel = Integer.MIN_VALUE;

		SparseArray<byte[]> manufacturerData = new SparseArray<>();
		Map<ParcelUuid, byte[]> serviceData = new HashMap<>();

		try {
			while (currentPos < scanRecord.length) {
				// length is unsigned int.
				int length = scanRecord[currentPos++] & 0xFF;
				if (length == 0) {
					break;
				}
				// Note the length includes the length of the field type itself.
				int dataLength = length - 1;
				// fieldType is unsigned int.
				int fieldType = scanRecord[currentPos++] & 0xFF;
				switch (fieldType) {
					case DATA_TYPE_FLAGS:
						advertiseFlag = scanRecord[currentPos] & 0xFF;
						break;
					case DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL:
					case DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE:
						parseServiceUuid(scanRecord, currentPos,
								dataLength, BluetoothUuid.UUID_BYTES_16_BIT, serviceUuids);
						break;
					case DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL:
					case DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE:
						parseServiceUuid(scanRecord, currentPos, dataLength,
								BluetoothUuid.UUID_BYTES_32_BIT, serviceUuids);
						break;
					case DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL:
					case DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE:
						parseServiceUuid(scanRecord, currentPos, dataLength,
								BluetoothUuid.UUID_BYTES_128_BIT, serviceUuids);
						break;
					case DATA_TYPE_LOCAL_NAME_SHORT:
					case DATA_TYPE_LOCAL_NAME_COMPLETE:
						localName = new String(
								extractBytes(scanRecord, currentPos, dataLength));
						break;
					case DATA_TYPE_TX_POWER_LEVEL:
						txPowerLevel = scanRecord[currentPos];
						break;
					case DATA_TYPE_SERVICE_DATA:
						// The first two bytes of the service data are service data UUID in little
						// endian. The rest bytes are service data.
						int serviceUuidLength = BluetoothUuid.UUID_BYTES_16_BIT;
						byte[] serviceDataUuidBytes = extractBytes(scanRecord, currentPos,
								serviceUuidLength);
						ParcelUuid serviceDataUuid = BluetoothUuid.parseUuidFrom(
								serviceDataUuidBytes);
						byte[] serviceDataArray = extractBytes(scanRecord,
								currentPos + serviceUuidLength, dataLength - serviceUuidLength);
						serviceData.put(serviceDataUuid, serviceDataArray);
						break;
					case DATA_TYPE_MANUFACTURER_SPECIFIC_DATA:
						// The first two bytes of the manufacturer specific data are
						// manufacturer ids in little endian.
						int manufacturerId = ((scanRecord[currentPos + 1] & 0xFF) << 8) +
								(scanRecord[currentPos] & 0xFF);
						byte[] manufacturerDataBytes = extractBytes(scanRecord, currentPos + 2,
								dataLength - 2);
						manufacturerData.put(manufacturerId, manufacturerDataBytes);
						break;
					default:
						// Just ignore, we don't handle such data type.
						break;
				}
				currentPos += dataLength;
			}

			if (serviceUuids.isEmpty()) {
				serviceUuids = null;
			}
			return new ScanRecord(serviceUuids, manufacturerData, serviceData,
					advertiseFlag, txPowerLevel, localName, scanRecord);
		} catch (Exception e) {
			Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
			// As the record is invalid, ignore all the parsed results for this packet
			// and return an empty record with raw scanRecord bytes in results
			return new ScanRecord(null, null, null, -1, Integer.MIN_VALUE, null, scanRecord);
		}
	}

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ScanRecord that = (ScanRecord) o;

    if (mAdvertiseFlags != that.mAdvertiseFlags) {
      return false;
    }
    if (mTxPowerLevel != that.mTxPowerLevel) {
      return false;
    }
    if (mServiceUuids != null ? !mServiceUuids.equals(that.mServiceUuids) : that.mServiceUuids != null) {
      return false;
    }
    if (mManufacturerSpecificData != null ? !mManufacturerSpecificData.equals(
        that.mManufacturerSpecificData) : that.mManufacturerSpecificData != null) {
      return false;
    }
    if (mServiceData != null ? !mServiceData.equals(that.mServiceData)
        : that.mServiceData != null) {
      return false;
    }
    if (mDeviceName != null ? !mDeviceName.equals(that.mDeviceName) : that.mDeviceName != null) {
      return false;
    }
    return Arrays.equals(mBytes, that.mBytes);
  }

  @Override public int hashCode() {
    int result = mAdvertiseFlags;
    result = 31 * result + (mServiceUuids != null ? mServiceUuids.hashCode() : 0);
    result = 31 * result + (mManufacturerSpecificData != null ? mManufacturerSpecificData.hashCode() : 0);
    result = 31 * result + (mServiceData != null ? mServiceData.hashCode() : 0);
    result = 31 * result + mTxPowerLevel;
    result = 31 * result + (mDeviceName != null ? mDeviceName.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(mBytes);
    return result;
  }

  @Override
	public String toString() {
		return "ScanRecord [mAdvertiseFlags=" + mAdvertiseFlags + ", mServiceUuids=" + mServiceUuids
				+ ", mManufacturerSpecificData=" + BluetoothLeUtils.toString(mManufacturerSpecificData)
				+ ", mServiceData=" + BluetoothLeUtils.toString(mServiceData)
				+ ", mTxPowerLevel=" + mTxPowerLevel + ", mDeviceName=" + mDeviceName + "]";
	}

	// Parse service UUIDs.
	private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength,
										int uuidLength, List<ParcelUuid> serviceUuids) {
		while (dataLength > 0) {
			byte[] uuidBytes = extractBytes(scanRecord, currentPos,
					uuidLength);
			serviceUuids.add(BluetoothUuid.parseUuidFrom(uuidBytes));
			dataLength -= uuidLength;
			currentPos += uuidLength;
		}
		return currentPos;
	}

	// Helper method to extract bytes from byte array.
	private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
		byte[] bytes = new byte[length];
		System.arraycopy(scanRecord, start, bytes, 0, length);
		return bytes;
	}
}

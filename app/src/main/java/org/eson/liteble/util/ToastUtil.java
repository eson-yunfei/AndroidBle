package org.eson.liteble.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by xiaoyunfei on 2016/9/25.
 */

public class ToastUtil {

	private static Toast toast;

	/**
	 * 短时间显示  Toast
	 *
	 * @param context
	 * @param sequence
	 */
	public static void showShort(Context context, CharSequence sequence) {

		show(context, sequence, Toast.LENGTH_SHORT);
	}

	/**
	 * 短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {

		show(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 自定义显示时间
	 *
	 * @param context
	 * @param sequence
	 * @param duration
	 */
	public static void show(Context context, CharSequence sequence, int duration) {
		if (toast == null) {
			toast = Toast.makeText(context, sequence, duration);
		} else {
			toast.setText(sequence);
		}
		toast.show();

	}

	public static void show(Context context, int resource, int duration) {
		if (toast == null) {
			toast = Toast.makeText(context, resource, duration);
		} else {
			toast.setText(resource);
		}
		toast.show();

	}

	/**
	 * 隐藏toast
	 */
	public static void hideToast() {
		if (toast != null) {
			toast.cancel();
		}
	}

}

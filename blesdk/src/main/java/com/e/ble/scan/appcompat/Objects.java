/*
 * Copyright (c) 2017. xiaoyunfei
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.e.ble.scan.appcompat;

import java.util.Arrays;

/* package */ class Objects {

	/**
	 * Returns true if both arguments are null,
	 * the result of {@link Arrays#equals} if both arguments are primitive arrays,
	 * the result of {@link Arrays#deepEquals} if both arguments are arrays of reference types,
	 * and the result of {@link #equals} otherwise.
	 */
	static boolean deepEquals(Object a, Object b) {
		if (a == null || b == null) {
			return a == b;
		} else if (a instanceof Object[] && b instanceof Object[]) {
			return Arrays.deepEquals((Object[]) a, (Object[]) b);
		} else if (a instanceof boolean[] && b instanceof boolean[]) {
			return Arrays.equals((boolean[]) a, (boolean[]) b);
		} else if (a instanceof byte[] && b instanceof byte[]) {
			return Arrays.equals((byte[]) a, (byte[]) b);
		} else if (a instanceof char[] && b instanceof char[]) {
			return Arrays.equals((char[]) a, (char[]) b);
		} else if (a instanceof double[] && b instanceof double[]) {
			return Arrays.equals((double[]) a, (double[]) b);
		} else if (a instanceof float[] && b instanceof float[]) {
			return Arrays.equals((float[]) a, (float[]) b);
		} else if (a instanceof int[] && b instanceof int[]) {
			return Arrays.equals((int[]) a, (int[]) b);
		} else if (a instanceof long[] && b instanceof long[]) {
			return Arrays.equals((long[]) a, (long[]) b);
		} else if (a instanceof short[] && b instanceof short[]) {
			return Arrays.equals((short[]) a, (short[]) b);
		}
		return a.equals(b);
	}

	/**
	 * Null-safe equivalent of {@code a.equals(b)}.
	 */
	static boolean equals(Object a, Object b) {
		return (a == null) ? (b == null) : a.equals(b);
	}

	/**
	 * Convenience wrapper for {@link Arrays#hashCode}, adding varargs.
	 * This can be used to compute a hash code for an object's fields as follows:
	 * {@code Objects.hash(a, b, c)}.
	 */
	static int hash(Object... values) {
		return Arrays.hashCode(values);
	}

	/**
	 * Returns "null" for null or {@code o.toString()}.
	 */
	static String toString(Object o) {
		return (o == null) ? "null" : o.toString();
	}
}

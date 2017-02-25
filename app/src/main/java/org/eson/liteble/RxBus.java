package org.eson.liteble;

import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/25
 * @说明：
 */

public class RxBus {

	private final Subject<Object>
			rxBus = PublishSubject.create().toSerialized();

	private RxBus() {

	}

	public static RxBus getInstance() {
		return RxBusHolder.instance;
	}

	public static class RxBusHolder {
		private static final RxBus instance = new RxBus();
	}

	public void send(Bundle o) {
		rxBus.onNext(o);
	}

	public Observable<Object> toObserverable() {
		return rxBus;
	}

}

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

package org.eson.liteble.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import com.e.ble.BuildConfig;

import org.eson.liteble.R;
import org.eson.liteble.activity.base.ViewBindActivity;
import org.eson.liteble.databinding.ActivityAboutBinding;

/**
 * @package_name org.eson.liteble.activity
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/10/12.
 * @description
 */

public class AboutActivity extends ViewBindActivity {


    private ActivityAboutBinding aboutBinding;

    @Override
    protected View getBindViewRoot() {
        aboutBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        return aboutBinding.getRoot();
    }
    @Override
    protected void onProcess() {
        String appVersion = getVersionName(this) + "(" + getVersionCode(this) + ")";
        aboutBinding.appVerTV.setText(getString(R.string.app_version, appVersion));
        String sdkVersion = BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")";
        aboutBinding.sdkVerTV.setText(getString(R.string.sdk_version, sdkVersion));
    }



    //获取版本号
    private String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    //获取版本号
    private int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }
}

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

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.shon.mvvm.base.ui.BaseBindingActivity;

import org.eson.liteble.R;
import org.eson.liteble.databinding.ActivityAboutBinding;

/**
 * @package_name org.eson.liteble.activity
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/10/12.
 * @description
 */

public class AboutActivity extends BaseBindingActivity<ActivityAboutBinding> {

    @Override
    public void onProcess(Bundle bundle) {

        String appVersion = formatVersion(getVersionName(), getVersionCode());
//        String sdkVersion = formatVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);


        binding.appVerTV.setText(getString(R.string.app_version, appVersion));
//        viewBinding.sdkVerTV.setText(getString(R.string.sdk_version, sdkVersion));

    }


    /**
     * 格式化 版本
     *
     * @param versionName versionName
     * @param versionCode versionCode
     * @return 格式化文字
     */
    private String formatVersion(String versionName, int versionCode) {
        return versionName + "（ " + versionCode + " ) ";
    }


    /**
     * 获取版本 名称
     *
     * @return versionName
     */
    private String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取版本号
     *
     * @return versionCode
     */
    private int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }


}

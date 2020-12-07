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
package org.eson.liteble.activity

import android.content.pm.PackageManager
import android.os.Bundle
import com.shon.bluetooth.BuildConfig
import com.shon.mvvm.base.ui.BaseBindingActivity
import org.eson.liteble.R
import org.eson.liteble.databinding.ActivityAboutBinding

/**
 * Created by xiaoyunfei on 2017/10/12.
 *
 * update by xiao yun fei on 2020/12/07.
 * @description
 */
class AboutActivity : BaseBindingActivity<ActivityAboutBinding?>() {
    override fun onProcess(bundle: Bundle?) {


        val appVersion = formatVersion(getVersionName(), getVersionCode())
        val sdkVersion = formatVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
        binding?.brandTv?.text = getString(R.string.brand, android.os.Build.BRAND)
        binding?.modelTv?.text = getString(R.string.model, android.os.Build.MODEL)
        binding?.systemVersionTv?.text = getString(R.string.system_version, android.os.Build.VERSION.RELEASE)
        binding?.appVerTV?.text = getString(R.string.app_version, appVersion)
        binding?.sdkVerTV?.text = getString(R.string.sdk_version, sdkVersion)
    }

    /**
     * 格式化 版本
     *
     * @param versionName versionName
     * @param versionCode versionCode
     * @return 格式化文字
     */
    private fun formatVersion(versionName: String, versionCode: Int): String {
        return "$versionName（ $versionCode ) "
    }

    /**
     * 获取版本 名称
     *
     * @return versionName
     */
    private fun getVersionName(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }


    /**
     * 获取版本号
     *
     * @return versionCode
     */
    private fun getVersionCode(): Int {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }
}
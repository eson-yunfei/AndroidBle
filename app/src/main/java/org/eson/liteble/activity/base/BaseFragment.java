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

package org.eson.liteble.activity.base;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eson.liteble.util.LogUtil;

/**
 * @package_name org.eson.liteble.activity.fragment
 * @name ${name}
 * <p>
 * Created by xiaoyunfei on 2017/5/5.
 * @description
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutID(), container, false);
        initViews();
        initViewsListener();
        onProcess();
        return rootView;
    }


    protected abstract int getLayoutID();

    protected abstract void initViews();

    protected void initViewsListener() {

    }


    public void onBleStateChange(String mac, int state) {

    }

    protected void onProcess() {
    }


    @Override
    public void onClick(View v) {

    }

    protected <T extends View> T findView(int viewId) {
        return (T) rootView.findViewById(viewId);
    }



    /**
     * 导航返回
     */
    protected void navigateBack() {
        View view = getView();
        if (view == null) {
            return;
        }
        NavController navController = Navigation.findNavController(view);
        try {
            navController.popBackStack();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 导航到下一个页面
     *
     * @param resId  resId
     * @param bundle 参数
     */
    protected void navigateNext(@IdRes int resId, @Nullable Bundle bundle) {
        View view = getView();
        if (view == null) {
            return;
        }
        try {

            NavController navController = Navigation.findNavController(view);
            if (bundle != null) {
                navController.navigate(resId, bundle);
            } else {
                navController.navigate(resId);
            }
        } catch (Exception e) {
            LogUtil.e("navigateNext  error ::::: " + e.getMessage());
            e.printStackTrace();
        }

    }

}

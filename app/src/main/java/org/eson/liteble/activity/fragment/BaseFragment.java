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

package org.eson.liteble.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


}

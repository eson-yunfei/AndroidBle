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
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

/**
 * @作者 xiaoyunfei
 * @日期: 2017/2/27
 * @说明：
 */

abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Context mContext = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getRootLayout());
        mContext = this;

        initView();
        initViewListener();

        process(savedInstanceState);
    }

    protected void initView() {

    }

    protected void initViewListener() {
    }

    protected void process(Bundle savedInstanceState) {

    }

    protected abstract int getRootLayout();

    @Override
    public void onClick(View v) {

    }


    protected <T extends View> T findView(int viewId) {
        return (T) findViewById(viewId);
    }
}

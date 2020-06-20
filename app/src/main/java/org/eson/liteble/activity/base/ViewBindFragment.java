package org.eson.liteble.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:20
 * Package name : org.eson.liteble.activity.base
 * Des :
 */
public abstract class ViewBindFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return getView(inflater,container);
    }

    protected abstract View getView(LayoutInflater inflater, ViewGroup container);
}

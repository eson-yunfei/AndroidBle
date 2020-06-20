package org.eson.liteble.activity.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eson.liteble.activity.base.ViewBindFragment;
import org.eson.liteble.databinding.ActivityDetailBinding;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/20 14:12
 * Package name : org.eson.liteble.activity.fragment
 * Des :
 */
class ServiceListFragment extends ViewBindFragment {

    private ActivityDetailBinding detailBinding;
    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        detailBinding = ActivityDetailBinding.inflate(inflater,container,false);
        return detailBinding.getRoot();
    }
}

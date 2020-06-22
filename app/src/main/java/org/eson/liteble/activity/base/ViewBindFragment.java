package org.eson.liteble.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.eson.liteble.util.LogUtil;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListener();

        onProcess();
    }



    protected abstract View getView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initListener();

    protected void onProcess() {

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

//package org.eson.liteble.activity.base;
//
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewbinding.ViewBinding;
//
///**
// * Auth : xiao.yunfei
// * Date : 2020/7/9 16:50
// * Package name : org.eson.liteble.activity.base
// * Des :
// */
//public abstract class IBaseActivity<VB extends ViewBinding> extends AppCompatActivity {
//
//    protected VB viewBinding;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        viewBinding = getViewBing();
//        setContentView(viewBinding.getRoot());
//        onProcess();
//    }
//
//
//
//    protected abstract VB getViewBing();
//
//    protected abstract void onProcess();
//
//}

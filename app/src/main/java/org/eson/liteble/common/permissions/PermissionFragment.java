//package org.eson.liteble.common.permissions;
//
//import android.annotation.TargetApi;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//
//import org.eson.log.LogUtils;
//
//import java.util.ArrayList;
//
///**
// * Auth : xiao.yunfei
// * Date : 2020/6/29 10:06
// * Package name : org.eson.liteble.common.permissions
// * Des :
// */
//public class PermissionFragment extends Fragment {
//    private static final int PERMISSIONS_REQUEST_CODE = 88;
//    private OnRequestPermissionCallback permissionCallback;
//
//    PermissionFragment() {
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    void requestPermissions(@NonNull String[] permissions) {
//        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode != PERMISSIONS_REQUEST_CODE) {
//            return;
//        }
//        LogUtils.e("onRequestPermissionsResult = ");
//        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
//
//        for (int i = 0; i < permissions.length; i++) {
//            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
//        }
//
//        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
//    }
//
//    private void onRequestPermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale) {
//        ArrayList<Boolean> grantedArray = new ArrayList<>();
//        for (int i = 0, size = permissions.length; i < size; i++) {
//            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
//            LogUtils.e("granted = " + granted);
//            if (granted) {
//                grantedArray.add(granted);
//            }
//        }
//        if (permissionCallback == null) {
//            return;
//        }
//        if (grantedArray.size() == grantResults.length) {
//            permissionCallback.onRequest(true);
//        } else {
//            permissionCallback.onRequest(false);
//        }
//    }
//
//    public void setOnRequestCallBack(@NonNull OnRequestPermissionCallback onRequestPermissionCallback) {
//        this.permissionCallback = onRequestPermissionCallback;
//    }
//}

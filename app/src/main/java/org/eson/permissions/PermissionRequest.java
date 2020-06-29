package org.eson.permissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Auth : xiao.yunfei
 * Date : 2020/6/29 10:42
 * Package name : org.eson.permissions
 * Des :
 */
public class PermissionRequest {


    private static final String TAG = PermissionRequest.class.getName();
    private PermissionFragment rxPermissionsFragment;


    public PermissionRequest(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        createPermissionFragment(fragmentManager);
    }

    public PermissionRequest(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        createPermissionFragment(fragmentManager);

    }


    public void  requestPermissions(@NonNull String... permissions){
        if (rxPermissionsFragment != null){
            rxPermissionsFragment.requestPermissions(permissions);
        }
    }


    private void createPermissionFragment(@NonNull final FragmentManager fragmentManager) {
        synchronized (PermissionRequest.class) {
            if (rxPermissionsFragment == null) {
                rxPermissionsFragment = getPermissionsFragment(fragmentManager);
            }
        }
    }

    private PermissionFragment getPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        PermissionFragment rxPermissionsFragment = findRxPermissionsFragment(fragmentManager);
        boolean isNewInstance = rxPermissionsFragment == null;
        if (isNewInstance) {
            rxPermissionsFragment = new PermissionFragment();
            fragmentManager
                    .beginTransaction()
                    .add(rxPermissionsFragment, TAG)
                    .commitNow();
        }
        return rxPermissionsFragment;
    }

    private PermissionFragment findRxPermissionsFragment(@NonNull final FragmentManager fragmentManager) {
        return (PermissionFragment) fragmentManager.findFragmentByTag(TAG);
    }

    public void setOnRequestCallBack(OnRequestPermissionCallback onRequestPermissionCallback) {
        if (rxPermissionsFragment != null && onRequestPermissionCallback != null){
            rxPermissionsFragment.setOnRequestCallBack(onRequestPermissionCallback);
        }
    }
}

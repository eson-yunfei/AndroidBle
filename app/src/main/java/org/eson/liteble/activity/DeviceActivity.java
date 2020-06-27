package org.eson.liteble.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.e.tool.ble.BleTool;
import com.e.tool.ble.bean.ConnectResult;

import org.eson.liteble.R;
import org.eson.liteble.util.LogUtil;

public class DeviceActivity extends AppCompatActivity {
    private ConnectResult connectBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        connectBt = (ConnectResult) getIntent().getSerializableExtra("connectBt");
        setupNavigation(connectBt);
    }

    private void setupNavigation(ConnectResult connectBt) {
        if (connectBt == null) {
            return;
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.connected_device_view);

        if (navHostFragment == null) {
            return;
        }
        NavController controller = navHostFragment.getNavController();
        NavGraph graph = controller.getGraph();
        NavArgument argument = new NavArgument.Builder()
                .setDefaultValue(connectBt)
                .build();
        graph.addArgument("connectBt", argument);

        controller.setGraph(graph);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.e("DeviceActivity -->> onBackPressed");

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("DeviceActivity -->> onPause");
        BleTool.getInstance().getController()
                .disConnect(connectBt.getAddress());
    }
}
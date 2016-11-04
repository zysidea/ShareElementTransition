package com.example.zys.shareelementtransition;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Bundle mTmpReenterState;

    private MainFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setExitSharedElementCallback(mCallback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container,mFragment,"main").commit();

    }


    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mTmpReenterState != null) {
                int startingPosition = mTmpReenterState.getInt(Constant.EXTRA_START_POSITION);
                int currentPosition = mTmpReenterState.getInt(Constant.EXTRA_CURRENT_POSITION);
                if (startingPosition != currentPosition) {
                    View newSharedElement=mFragment.getNewSharedElement(currentPosition);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(Constant.TRANSITION_NAME +currentPosition);
                        sharedElements.clear();
                        sharedElements.put(Constant.TRANSITION_NAME +currentPosition, newSharedElement);
                    }
                }
                mTmpReenterState = null;
            } else {
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                if (navigationBar != null) {
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }
                if (statusBar != null) {
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }
        }
    };

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        mTmpReenterState=data.getExtras();
        int startPosition = mTmpReenterState.getInt(Constant.EXTRA_START_POSITION);
        int currentPosition = mTmpReenterState.getInt(Constant.EXTRA_CURRENT_POSITION);
        mFragment.rennter(startPosition,currentPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

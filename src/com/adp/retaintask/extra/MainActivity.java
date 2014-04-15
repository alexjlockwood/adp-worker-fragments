package com.adp.retaintask.extra;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.adp.retaintask.R;

/**
 * MainActivity starts a UI fragment which will display the screen's UI.
 *
 * To run this MainActivity instead of the one in the
 * <code>com.adp.retaintask</code> package, you will need to manually change the
 * default launcher Activity in the <code>AndroidManifest.xml</code> file.
 */
public class MainActivity extends FragmentActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final boolean DEBUG = true; // Set this to false to disable logs.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (DEBUG) Log.i(TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.add(android.R.id.content, new UIFragment()).commit();
    }
  }

  /************************/
  /***** OPTIONS MENU *****/
  /************************/

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_trigger_config_change:
        // Simulate a configuration change. Only available on
        // Honeycomb and above.
        recreate();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /************************/
  /***** LOGS & STUFF *****/
  /************************/

  @Override
  protected void onStart() {
    if (DEBUG) Log.i(TAG, "onStart()");
    super.onStart();
  }

  @Override
  protected void onResume() {
    if (DEBUG) Log.i(TAG, "onResume()");
    super.onResume();
  }

  @Override
  protected void onPause() {
    if (DEBUG) Log.i(TAG, "onPause()");
    super.onPause();
  }

  @Override
  protected void onStop() {
    if (DEBUG) Log.i(TAG, "onStop()");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    if (DEBUG) Log.i(TAG, "onDestroy()");
    super.onDestroy();
  }

}

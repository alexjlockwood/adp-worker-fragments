package com.adp.retaintask.extra;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.adp.retaintask.R;

/**
 * This Activity starts a UIFragment which will display the screen's UI.
 *
 * To run this MainActivity instead of the one in the
 * <code>com.adp.retaintask</code> package, you will need to manually change the
 * default launcher Activity in the <code>AndroidManifest.xml</code> file.
 */
public class MainActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.add(android.R.id.content, new UIFragment()).commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  /************************/
  /***** OPTIONS MENU *****/
  /************************/

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
}
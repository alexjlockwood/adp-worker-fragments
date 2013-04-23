package com.adp.retaintask.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.adp.retaintask.R;

/**
 * The MainActivity's only responsibility is to instantiate and display the
 * UiFragment to the screen. This activity will be destroyed and re-created on
 * configuration changes.
 */
public class MainActivity extends FragmentActivity {
  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "+++ onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.add(android.R.id.content, new UiFragment()).commit();
    }
    Log.i(TAG, "--- onCreate(Bundle)");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    Log.i(TAG, "+++ onCreateOptionsMenu(Menu)");
    getMenuInflater().inflate(R.menu.activity_main, menu);
    Log.i(TAG, "--- onCreateOptionsMenu(Menu)");
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.i(TAG, "+++ onOptionsItemSelected(MenuItem)");
    switch (item.getItemId()) {
      case R.id.menu_change_font_size:
        startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
        Log.i(TAG, "--- onOptionsItemSelected(MenuItem)");
        return true;
    }
    Log.i(TAG, "--- onOptionsItemSelected(MenuItem)");
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onRestart() {
    Log.i(TAG, "+++ onRestart()");
    super.onRestart();
    Log.i(TAG, "--- onRestart()");
  }

  @Override
  protected void onStart() {
    Log.i(TAG, "+++ onStart()");
    super.onStart();
    Log.i(TAG, "--- onStart()");
  }

  @Override
  protected void onResume() {
    Log.i(TAG, "+++ onResume()");
    super.onResume();
    Log.i(TAG, "--- onResume()");
  }

  @Override
  protected void onPause() {
    Log.i(TAG, "+++ onPause()");
    super.onPause();
    Log.i(TAG, "--- onPause()");
  }

  @Override
  protected void onStop() {
    Log.i(TAG, "+++ onStop()");
    super.onStop();
    Log.i(TAG, "--- onStop()");
  }

  @Override
  protected void onDestroy() {
    Log.i(TAG, "+++ onDestroy()");
    super.onDestroy();
    Log.i(TAG, "--- onDestroy()");
  }
}
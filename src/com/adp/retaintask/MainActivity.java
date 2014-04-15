package com.adp.retaintask;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity displays the screen's UI and starts a TaskFragment which will
 * execute an asynchronous task and will retain itself when configuration
 * changes occur.
 */
public class MainActivity extends FragmentActivity implements TaskFragment.TaskCallbacks {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final boolean DEBUG = true; // Set this to false to disable logs.

  private static final String KEY_CURRENT_PROGRESS = "current_progress";
  private static final String KEY_PERCENT_PROGRESS = "percent_progress";
  private static final String TAG_TASK_FRAGMENT = "task_fragment";

  private TaskFragment mTaskFragment;
  private ProgressBar mProgressBar;
  private TextView mPercent;
  private Button mButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (DEBUG) Log.i(TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Initialize views.
    mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
    mPercent = (TextView) findViewById(R.id.percent_progress);
    mButton = (Button) findViewById(R.id.task_button);
    mButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mTaskFragment.isRunning()) {
          mTaskFragment.cancel();
        } else {
          mTaskFragment.start();
        }
      }
    });

    // Restore saved state.
    if (savedInstanceState != null) {
      mProgressBar.setProgress(savedInstanceState.getInt(KEY_CURRENT_PROGRESS));
      mPercent.setText(savedInstanceState.getString(KEY_PERCENT_PROGRESS));
    }

    FragmentManager fm = getSupportFragmentManager();
    mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

    // If the Fragment is non-null, then it is being retained
    // over a configuration change.
    if (mTaskFragment == null) {
      mTaskFragment = new TaskFragment();
      fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
    }

    if (mTaskFragment.isRunning()) {
      mButton.setText(getString(R.string.cancel));
    } else {
      mButton.setText(getString(R.string.start));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (DEBUG) Log.i(TAG, "onSaveInstanceState(Bundle)");
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_CURRENT_PROGRESS, mProgressBar.getProgress());
    outState.putString(KEY_PERCENT_PROGRESS, mPercent.getText().toString());
  }

  /*********************************/
  /***** TASK CALLBACK METHODS *****/
  /*********************************/

  @Override
  public void onPreExecute() {
    if (DEBUG) Log.i(TAG, "onPreExecute()");
    mButton.setText(getString(R.string.cancel));
    Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onProgressUpdate(int percent) {
    if (DEBUG) Log.i(TAG, "onProgressUpdate(" + percent + "%)");
    mProgressBar.setProgress(percent * mProgressBar.getMax() / 100);
    mPercent.setText(percent + "%");
  }

  @Override
  public void onCancelled() {
    if (DEBUG) Log.i(TAG, "onCancelled()");
    mButton.setText(getString(R.string.start));
    mProgressBar.setProgress(0);
    mPercent.setText(getString(R.string.zero_percent));
    Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onPostExecute() {
    if (DEBUG) Log.i(TAG, "onPostExecute()");
    mButton.setText(getString(R.string.start));
    mProgressBar.setProgress(mProgressBar.getMax());
    mPercent.setText(getString(R.string.one_hundred_percent));
    Toast.makeText(this, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
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

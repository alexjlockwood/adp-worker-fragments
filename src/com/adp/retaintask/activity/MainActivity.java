package com.adp.retaintask.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adp.retaintask.R;

/**
 * This Activity displays the screen's UI and starts a single
 * TaskFragment that will retain itself when configuration
 * changes occur.
 */
public class MainActivity extends FragmentActivity implements TaskFragment.TaskCallbacks {
  private static final String TAG = MainActivity.class.getSimpleName();

  private static final String KEY_CURRENT_PROGRESS = "current_progress";
  private static final String KEY_PERCENT_PROGRESS = "percent_progress";

  private TaskFragment mTaskFragment;
  private ProgressBar mProgressBar;
  private TextView mPercent;
  private Button mButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Initialize views
    mProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
    mPercent = (TextView) findViewById(R.id.percent_progress);
    mButton = (Button) findViewById(R.id.task_button);
    mButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mTaskFragment.isRunning()) {
          mTaskFragment.cancel();
        } else {
          mTaskFragment.start();
        }
      }
    });

    // Restore saved state
    if (savedInstanceState != null) {
      mProgressBar.setProgress(savedInstanceState.getInt(KEY_CURRENT_PROGRESS));
      mPercent.setText(savedInstanceState.getString(KEY_PERCENT_PROGRESS));
    }

    FragmentManager fm = getSupportFragmentManager();
    mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

    // If the Fragment is non-null, then it is currently being
    // retained across a configuration change.
    if (mTaskFragment == null) {
      mTaskFragment = new TaskFragment();
      fm.beginTransaction().add(mTaskFragment, "task").commit();
    }

    if (mTaskFragment.isRunning()) {
      mButton.setText(getString(R.string.cancel));
    } else {
      mButton.setText(getString(R.string.start));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(KEY_CURRENT_PROGRESS, mProgressBar.getProgress());
    outState.putString(KEY_PERCENT_PROGRESS, mPercent.getText().toString());
  }

  /****************************/
  /***** CALLBACK METHODS *****/
  /****************************/

  @Override
  public void onPreExecute() {
    Log.i(TAG, "onPreExecute()");
    mButton.setText(getString(R.string.cancel));
    Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onProgressUpdate(int percent) {
    Log.i(TAG, "onProgressUpdate(" + percent + "%)");
    mPercent.setText(percent + "%");
    mProgressBar.setProgress(percent * mProgressBar.getMax() / 100);
  }

  @Override
  public void onCancelled() {
    Log.i(TAG, "onCancelled()");
    mButton.setText(getString(R.string.start));
    mProgressBar.setProgress(0);
    mPercent.setText(getString(R.string.zero_percent));
    Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onPostExecute() {
    Log.i(TAG, "onPostExecute()");
    Toast.makeText(this, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(getString(R.string.start));
    mProgressBar.setProgress(mProgressBar.getMax());
    mPercent.setText(getString(R.string.one_hundred_percent));
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
      case R.id.menu_recreate_activity:
        // Simulates a configuration change
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
    Log.i(TAG, "onStart()");
    super.onStart();
  }

  @Override
  protected void onResume() {
    Log.i(TAG, "onResume()");
    super.onResume();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, "onPause()");
    super.onPause();
  }

  @Override
  protected void onStop() {
    Log.i(TAG, "onStop()");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    Log.i(TAG, "onDestroy()");
    super.onDestroy();
  }
}
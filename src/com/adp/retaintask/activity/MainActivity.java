package com.adp.retaintask.activity;

import java.text.NumberFormat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adp.retaintask.R;

/**
 * The MainActivity's only responsibility is to instantiate and display the
 * UiFragment to the screen. This activity will be destroyed and re-created on
 * configuration changes.
 */
public class MainActivity extends FragmentActivity implements TaskFragment.TaskCallbacks {
  @SuppressWarnings("unused")
  private static final String TAG = MainActivity.class.getSimpleName();

  private TaskFragment mTaskFragment;
  private ProgressBar mProgressBar;
  private TextView mPercent;
  private Button mButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_main);

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

    if (savedInstanceState != null) {
      mProgressBar.setProgress(savedInstanceState.getInt("current_progress"));
      mPercent.setText(savedInstanceState.getString("percent_progress"));
    }

    FragmentManager fm = getSupportFragmentManager();
    mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

    // If we haven't retained the worker fragment retained, then create it.
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
    outState.putInt("current_progress", mProgressBar.getProgress());
    outState.putString("percent_progress", mPercent.getText().toString());
  }

  /****************************/
  /***** CALLBACK METHODS *****/
  /****************************/

  @Override
  public void onPreExecute() {
    Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(getString(R.string.cancel));
  }

  private static NumberFormat sFormatter = NumberFormat.getPercentInstance();
  static { sFormatter.setMinimumFractionDigits(1); }

  @Override
  public void onProgressUpdate(double percent) {
    int position = (int) (percent * mProgressBar.getMax());
    mProgressBar.setProgress(position);
    mPercent.setText(sFormatter.format(percent));
  }

  @Override
  public void onCancelled() {
    Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(getString(R.string.start));
    mProgressBar.setProgress(0);
    mPercent.setText(getString(R.string.zero_percent));
  }

  @Override
  public void onPostExecute() {
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
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_change_font_size:
        startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
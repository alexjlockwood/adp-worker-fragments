package com.adp.worker.fragments;

import java.text.NumberFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This example shows how you can use a Fragment to easily propagate state (such
 * as threads) across activity instances when an activity needs to be restarted
 * due to, for example, a configuration change. This is a lot easier than using
 * the raw Activity#onRetainNonConfiguratinInstance() API.
 */
public class MainActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
      ft.add(android.R.id.content, new UiFragment()).commit();
    }
  }

  /**
   * This is a fragment showing UI that will be updated from work done in the
   * retained fragment.
   */
  public static class UiFragment extends Fragment implements TaskCallbacks {
    private static final String TAG = UiFragment.class.getSimpleName();

    private TaskFragment mTaskFragment;
    private ProgressBar mProgressBar;
    private Button mButton;
    private TextView mPercent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      Log.i(TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");
      View view = inflater.inflate(R.layout.fragment_main, container, false);

      mProgressBar = (ProgressBar) view.findViewById(R.id.progress_horizontal);

      mButton = (Button) view.findViewById(R.id.task_button);
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

      mPercent = (TextView) view.findViewById(R.id.percent_progress);

      return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      Log.i(TAG, "onActivityCreated(Bundle)");
      super.onActivityCreated(savedInstanceState);

      if (savedInstanceState != null) {
        mProgressBar.setProgress(savedInstanceState.getInt("current_progres"));
        mPercent.setText(savedInstanceState.getString("percent_progress"));
      }

      FragmentManager fm = getFragmentManager();
      mTaskFragment = (TaskFragment) fm.findFragmentByTag("task");

      // If we haven't retained the worker fragment retained, then create it.
      if (mTaskFragment == null) {
        mTaskFragment = new TaskFragment();
        mTaskFragment.setTargetFragment(this, 0);
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
      Log.i(TAG, "onPreExecute()");
      Toast.makeText(getActivity(), R.string.task_started_msg, Toast.LENGTH_SHORT).show();
      mButton.setText(getString(R.string.cancel));
    }

    private static final NumberFormat nf = NumberFormat.getPercentInstance();
    static { nf.setMinimumFractionDigits(1); }

    @Override
    public void onProgressUpdate(double percent) {
      Log.i(TAG, "doInBackground(" + percent + ")");
      int position = (int) (percent * mProgressBar.getMax());
      mProgressBar.setProgress(position);
      mPercent.setText(nf.format(percent));
    }

    @Override
    public void onCancelled() {
      Log.i(TAG, "onCancelled()");
      Toast.makeText(getActivity(), R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
      mProgressBar.setProgress(0);
      mPercent.setText(getString(R.string.zero_percent));
    }

    @Override
    public void onPostExecute() {
      Log.i(TAG, "onPostExecute()");
      Toast.makeText(getActivity(), R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
      mButton.setText(getString(R.string.start));
      mProgressBar.setProgress(mProgressBar.getMax());
      mPercent.setText(getString(R.string.one_hundred_percent));
    }
  }
}

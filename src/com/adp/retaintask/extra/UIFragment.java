package com.adp.retaintask.extra;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adp.retaintask.R;

/**
 * UIFragment displays the screen's UI and starts a single TaskFragment that
 * will retain itself when configuration changes occur.
 */
public class UIFragment extends Fragment implements TaskFragment.TaskCallbacks {
    private static final String TAG = UIFragment.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.

    private static final String KEY_CURRENT_PROGRESS = "current_progress";
    private static final String KEY_PERCENT_PROGRESS = "percent_progress";
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    private TaskFragment mTaskFragment;
    private ProgressBar mProgressBar;
    private TextView mPercent;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      if (DEBUG) Log.i(TAG, "onCreateView(LayoutInflater, ViewGroup, Bundle)");
      View view = inflater.inflate(R.layout.main, container, false);
      mProgressBar = (ProgressBar) view.findViewById(R.id.progress_horizontal);
      mPercent = (TextView) view.findViewById(R.id.percent_progress);
      mButton = (Button) view.findViewById(R.id.task_button);
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
      return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      if (DEBUG) Log.i(TAG, "onActivityCreated(Bundle)");
      super.onActivityCreated(savedInstanceState);

      // Restore saved state.
      if (savedInstanceState != null) {
        mProgressBar.setProgress(savedInstanceState.getInt(KEY_CURRENT_PROGRESS));
        mPercent.setText(savedInstanceState.getString(KEY_PERCENT_PROGRESS));
      }

      FragmentManager fm = getActivity().getSupportFragmentManager();
      mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

      // If we haven't retained the worker fragment, then create it
      // and set this UIFragment as the TaskFragment's target fragment.
      if (mTaskFragment == null) {
        mTaskFragment = new TaskFragment();
        mTaskFragment.setTargetFragment(this, 0);
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

    /****************************/
    /***** CALLBACK METHODS *****/
    /****************************/

    @Override
    public void onPreExecute() {
      if (DEBUG) Log.i(TAG, "onPreExecute()");
      mButton.setText(getString(R.string.cancel));
      Toast.makeText(getActivity(), R.string.task_started_msg, Toast.LENGTH_SHORT).show();
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
      Toast.makeText(getActivity(), R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute() {
      if (DEBUG) Log.i(TAG, "onPostExecute()");
      mButton.setText(getString(R.string.start));
      mProgressBar.setProgress(mProgressBar.getMax());
      mPercent.setText(getString(R.string.one_hundred_percent));
      Toast.makeText(getActivity(), R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    }

}

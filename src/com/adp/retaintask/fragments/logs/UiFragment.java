package com.adp.retaintask.fragments.logs;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adp.retaintask.R;

/**
 * This is a fragment showing UI that will be updated from work done in the
 * retained fragment.
 */
public class UiFragment extends Fragment implements TaskFragment.TaskCallbacks {
  private static final String TAG = UiFragment.class.getSimpleName();

  private Context mContext;
  private Resources mResources;
  private TaskFragment mTaskFragment;
  private ProgressBar mProgressBar;
  private Button mButton;
  private TextView mPercent;

  @Override
  public void onAttach(Activity activity) {
    Log.i(TAG, "   +++ onAttach(Activity)");
    super.onAttach(activity);
    mContext = activity.getApplicationContext();
    mResources = mContext.getResources();
    Log.i(TAG, "   --- onAttach(Activity)");
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "   +++ onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    Log.i(TAG, "   --- onCreate(Bundle)");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.i(TAG, "   +++ onCreateView(LayoutInflater, ViewGroup, Bundle)");
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

    Log.i(TAG, "   --- onCreateView(LayoutInflater, ViewGroup, Bundle)");
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    Log.i(TAG, "   +++ onActivityCreated(Bundle)");
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState != null) {
      mProgressBar.setProgress(savedInstanceState.getInt("current_progress"));
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

    Log.i(TAG, "   --- onActivityCreated(Bundle)");
  }

  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    Log.i(TAG, "   +++ onViewStateRestored(Bundle)");
    super.onViewStateRestored(savedInstanceState);
    Log.i(TAG, "   --- onViewStateRestored(Bundle)");
  }

  @Override
  public void onStart() {
    Log.i(TAG, "   +++ onStart()");
    super.onStart();
    Log.i(TAG, "   --- onStart()");
  }

  @Override
  public void onResume() {
    Log.i(TAG, "   +++ onResume()");
    super.onResume();
    Log.i(TAG, "   --- onResume()");
  }

  @Override
  public void onPause() {
    Log.i(TAG, "   +++ onPause()");
    super.onPause();
    Log.i(TAG, "   --- onPause()");
  }

  @Override
  public void onStop() {
    Log.i(TAG, "   +++ onStop()");
    super.onStop();
    Log.i(TAG, "   --- onStop()");
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    Log.i(TAG, "   +++ onSaveInstanceState(Bundle)");
    super.onSaveInstanceState(outState);
    outState.putInt("current_progress", mProgressBar.getProgress());
    outState.putString("percent_progress", mPercent.getText().toString());
    Log.i(TAG, "   --- onSaveInstanceState(Bundle)");
  }

  @Override
  public void onDestroyView() {
    Log.i(TAG, "   +++ onDestroyView()");
    super.onDestroyView();
    Log.i(TAG, "   --- onDestroyView()");
  }

  @Override
  public void onDestroy() {
    Log.i(TAG, "   +++ onDestroy()");
    super.onDestroy();
    Log.i(TAG, "   --- onDestroy()");
  }

  @Override
  public void onDetach() {
    Log.i(TAG, "   +++ onDetach()");
    super.onDetach();
    Log.i(TAG, "   --- onDetach()");
  }

  /****************************/
  /***** CALLBACK METHODS *****/
  /****************************/

  @Override
  public void onPreExecute() {
    Log.i(TAG, "onPreExecute()");
    Toast.makeText(mContext, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(mResources.getString(R.string.cancel));
  }

  private static NumberFormat sFormatter = NumberFormat.getPercentInstance();
  static { sFormatter.setMinimumFractionDigits(1); }

  @Override
  public void onProgressUpdate(double percent) {
    // Log.i(TAG, "doInBackground(" + percent + ")");
    int position = (int) (percent * mProgressBar.getMax());
    mProgressBar.setProgress(position);
    mPercent.setText(sFormatter.format(percent));
  }

  @Override
  public void onCancelled() {
    Log.i(TAG, "onCancelled()");
    Toast.makeText(mContext, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(mResources.getString(R.string.start));
    mProgressBar.setProgress(0);
    mPercent.setText(mResources.getString(R.string.zero_percent));
  }

  @Override
  public void onPostExecute() {
    Log.i(TAG, "onPostExecute()");
    Toast.makeText(mContext, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    mButton.setText(mResources.getString(R.string.start));
    mProgressBar.setProgress(mProgressBar.getMax());
    mPercent.setText(mResources.getString(R.string.one_hundred_percent));
  }
}

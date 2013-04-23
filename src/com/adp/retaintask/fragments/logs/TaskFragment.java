package com.adp.retaintask.fragments.logs;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is the Fragment implementation that will be retained across activity
 * instances.
 */
public class TaskFragment extends Fragment {
  private static final String TAG = TaskFragment.class.getSimpleName();

  private boolean mRunning;
  private DummyTask mTask;

  @Override
  public void onAttach(Activity activity) {
    Log.i(TAG, "      +++ onAttach(Activity)");
    super.onAttach(activity);
    Log.i(TAG, "      --- onAttach(Activity)");
  }

  /**
   * Called when the fragment is first initialized. Since we are using
   * {@link #setRetainInstance(boolean)}, this method will not be called on
   * configuration changes.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "      +++ onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mRunning = false;
    Log.i(TAG, "      --- onCreate(Bundle)");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.i(TAG, "      +++ onCreateView(LayoutInflater, ViewGroup, Bundle)");
    View view = super.onCreateView(inflater, container, savedInstanceState);
    Log.i(TAG, "      --- onCreateView(LayoutInflater, ViewGroup, Bundle)");
    return view;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    Log.i(TAG, "      +++ onActivityCreated(Bundle)");
    super.onActivityCreated(savedInstanceState);
    Log.i(TAG, "      --- onActivityCreated(Bundle)");
  }

  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    Log.i(TAG, "      +++ onViewStateRestored(Bundle)");
    super.onViewStateRestored(savedInstanceState);
    Log.i(TAG, "      --- onViewStateRestored(Bundle)");
  }

  @Override
  public void onStart() {
    Log.i(TAG, "      +++ onStart()");
    super.onStart();
    Log.i(TAG, "      --- onStart()");
  }

  @Override
  public void onResume() {
    Log.i(TAG, "      +++ onResume()");
    super.onResume();
    Log.i(TAG, "      --- onResume()");
  }

  @Override
  public void onPause() {
    Log.i(TAG, "      +++ onPause()");
    super.onPause();
    Log.i(TAG, "      --- onPause()");
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    Log.i(TAG, "     +++ onSaveInstanceState(Bundle)");
    super.onSaveInstanceState(outState);
    Log.i(TAG, "     --- onSaveInstanceState(Bundle)");
  }

  @Override
  public void onStop() {
    Log.i(TAG, "      +++ onStop()");
    super.onStop();
    Log.i(TAG, "      --- onStop()");
  }

  @Override
  public void onDestroyView() {
    Log.i(TAG, "      +++ onDestroyView()");
    super.onDestroyView();
    Log.i(TAG, "      --- onDestroyView()");
  }

  /**
   * This is called when the fragment is going away. It is NOT called when the
   * fragment is being propagated between activity instances.
   */
  @Override
  public void onDestroy() {
    Log.i(TAG, "      +++ onDestroy()");
    super.onDestroy();
    // Don't need to cancel this if you don't want (the AsyncTask will continue
    // to run and objects will be retained in memory, but presumably the task
    // will be short-lived so this won't matter).
    cancel();
    Log.i(TAG, "      --- onDestroy()");

  }

  @Override
  public void onDetach() {
    Log.i(TAG, "      +++ onDetach()");
    super.onDetach();
    Log.i(TAG, "      --- onDetach()");
  }

  /**
   * Start the background task.
   */
  public void start() {
    Log.i(TAG, "start()");
    if (!mRunning) {
      mTask = new DummyTask();
      mTask.execute();
      mRunning = true;
    }
  }

  /**
   * Cancel the background task.
   */
  public void cancel() {
    Log.i(TAG, "cancel()");
    if (mRunning) {
      mTask.cancel(false);
      mTask = null;
      mRunning = false;
    }
  }

  /**
   * Returns the current state of the background task.
   */
  public boolean isRunning() {
    return mRunning;
  }

  /***************************/
  /***** BACKGROUND TASK *****/
  /***************************/

  /** 
   * Note that this class is nonstatic (because a DummyTask is not supposed to
   * exist without an outer TaskFragment instance). This won't cause any unexpected
   * memory leaks since the DummyTask won't outlive the TaskFragment's lifecycle.
   */
  private class DummyTask extends AsyncTask<Void, Double, Void> {

    @Override
    protected void onPreExecute() {
      // Proxy the call to the UiFragment
      ((TaskCallbacks) getTargetFragment()).onPreExecute();
      mRunning = true;
    }

    @Override
    protected Void doInBackground(Void... ignore) {
      int i = 0;
      while (!isCancelled() && i < 213) {
        publishProgress(i / 213.0);
        SystemClock.sleep(100);
        i++;
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Double... percent) {
      // Proxy the call to the UiFragment
      ((TaskCallbacks) getTargetFragment()).onProgressUpdate(percent[0]);
    }

    @Override
    protected void onCancelled() {
      // Proxy the call to the UiFragment
      ((TaskCallbacks) getTargetFragment()).onCancelled();
      mRunning = false;
    }

    @Override
    protected void onPostExecute(Void ignore) {
      // Proxy the call to the UiFragment
      ((TaskCallbacks) getTargetFragment()).onPostExecute();
      mRunning = false;
    }
  }

  static interface TaskCallbacks {
    public void onPreExecute();
    public void onProgressUpdate(double percent);
    public void onCancelled();
    public void onPostExecute();
  }
}

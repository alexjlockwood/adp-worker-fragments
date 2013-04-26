package com.adp.retaintask.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * This is the Fragment implementation that will be retained across activity
 * instances.
 */
public class TaskFragment extends Fragment {
  private static final String TAG = TaskFragment.class.getSimpleName();

  private TaskCallbacks mCallbacks;
  private DummyTask mTask;
  private boolean mRunning;

  @Override
  public void onAttach(Activity activity) {
    Log.i(TAG, "onAttach(Activity)");
    super.onAttach(activity);
    if (!(activity instanceof TaskCallbacks)) {
      throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
    }
    mCallbacks = (TaskCallbacks) activity;
  }

  /**
   * Called when the fragment is first initialized. Since we are using
   * {@link #setRetainInstance(boolean)}, this method will not be called on
   * configuration changes.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  /**
   * This is called when the fragment is going away. It is NOT called when the
   * fragment is being propagated between activity instances.
   */
  @Override
  public void onDestroy() {
    Log.i(TAG, "onDestroy()");
    super.onDestroy();
    cancel();
  }

  /*****************************/
  /***** TASK FRAGMENT API *****/
  /*****************************/

  /**
   * Start the background task.
   */
  public void start() {
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
   * exist without an outer TaskFragment instance). This won't cause any
   * unexpected memory leaks since the DummyTask won't outlive the
   * TaskFragment's lifecycle.
   */
  private class DummyTask extends AsyncTask<Void, Double, Void> {

    @Override
    protected void onPreExecute() {
      // Proxy the call to the Activity
      mCallbacks.onPreExecute();
      mRunning = true;
    }

    @Override
    protected Void doInBackground(Void... ignore) {
      int i = 0;
      while (!isCancelled() && i < 100) {
        Log.i(TAG, "publishProgress(" + i + "%)");
        publishProgress(i / 100.0);
        SystemClock.sleep(150);
        i++;
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Double... percent) {
      // Proxy the call to the Activity
      mCallbacks.onProgressUpdate(percent[0]);
    }

    @Override
    protected void onCancelled() {
      // Proxy the call to the Activity
      mCallbacks.onCancelled();
      mRunning = false;
    }

    @Override
    protected void onPostExecute(Void ignore) {
      // Proxy the call to the Activity
      mCallbacks.onPostExecute();
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

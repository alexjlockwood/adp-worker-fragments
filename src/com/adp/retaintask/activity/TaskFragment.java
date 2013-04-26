package com.adp.retaintask.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * This Fragment manages a single background task and retains itself across
 * configuration changes.
 */
public class TaskFragment extends Fragment {
  private static final String TAG = TaskFragment.class.getSimpleName();

  /**
   * Callback interface through which the fragment can report the task's
   * progress and results back to the Activity.
   */
  static interface TaskCallbacks {
    public void onPreExecute();
    public void onProgressUpdate(int percent);
    public void onCancelled();
    public void onPostExecute();
  }

  private TaskCallbacks mCallbacks;
  private DummyTask mTask;
  private boolean mRunning;

  /**
   * Android passes us a reference to the newly created Activity by calling this
   * method after each configuration change.
   */
  @Override
  public void onAttach(Activity activity) {
    Log.i(TAG, "onAttach(Activity)");
    super.onAttach(activity);
    if (!(activity instanceof TaskCallbacks)) {
      throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
    }

    // Hold a reference to the parent Activity so we can report back the task's
    // current progress and results.
    mCallbacks = (TaskCallbacks) activity;
  }

  /**
   * This method is called only once when the Fragment is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  /**
   * This method is <em>not</em> called when the Fragment is being retained
   * across Activity instances.
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
   * A dummy task that performs some (dumb) background work and proxies progress
   * updates and results back to the Activity.
   */
  private class DummyTask extends AsyncTask<Void, Integer, Void> {

    @Override
    protected void onPreExecute() {
      // Proxy the call to the Activity
      mCallbacks.onPreExecute();
      mRunning = true;
    }

    @Override
    protected Void doInBackground(Void... ignore) {
      for (int i = 0; !isCancelled() && i < 100; i++) {
        Log.i(TAG, "publishProgress(" + i + "%)");
        SystemClock.sleep(100);
        publishProgress(i);
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
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
}
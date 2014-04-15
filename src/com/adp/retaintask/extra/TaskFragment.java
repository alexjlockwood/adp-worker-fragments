package com.adp.retaintask.extra;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * TaskFragment manages a single background task and retains itself across
 * configuration changes.
 */
public class TaskFragment extends Fragment {
  private static final String TAG = TaskFragment.class.getSimpleName();
  private static final boolean DEBUG = true; // Set this to false to disable logs.

  /**
   * Callback interface through which the fragment can report the task's
   * progress and results back to the Activity.
   */
  static interface TaskCallbacks {
    void onPreExecute();
    void onProgressUpdate(int percent);
    void onCancelled();
    void onPostExecute();
  }

  private TaskCallbacks mCallbacks;
  private DummyTask mTask;
  private boolean mRunning;

  /**
   * Hold a reference to the target fragment so we can report the task's current
   * progress and results. We do so in {@link #onAttach(Activity)} since it is
   * guaranteed to be the first method called after a configuration change
   * occurs (remember, the UIFragment will be recreated after each configuration
   * change, so we will need to obtain a reference to the new instance).
   */
  @Override
  public void onAttach(Activity activity) {
    if (DEBUG) Log.i(TAG, "onAttach(Activity)");
    super.onAttach(activity);
    if (!(getTargetFragment() instanceof TaskCallbacks)) {
      throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
    }

    // Hold a reference to the target fragment so we can report back the task's
    // current progress and results.
    mCallbacks = (TaskCallbacks) getTargetFragment();
  }

  /**
   * This method is called once when the Fragment is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, "onCreate(Bundle)");
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  /**
   * Note that this method is <em>not</em> called when the Fragment is being
   * retained across Activity instances. It will, however, be called when its
   * parent Activity is being destroyed for good (such as when the user clicks
   * the back button, etc.).
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
      // Proxy the call to the Activity.
      mCallbacks.onPreExecute();
      mRunning = true;
    }

    @Override
    protected Void doInBackground(Void... ignore) {
      for (int i = 0; !isCancelled() && i < 100; i++) {
        if (DEBUG) Log.i(TAG, "publishProgress(" + i + "%)");
        SystemClock.sleep(100);
        publishProgress(i);
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... percent) {
      // Proxy the call to the Activity.
      mCallbacks.onProgressUpdate(percent[0]);
    }

    @Override
    protected void onCancelled() {
      // Proxy the call to the Activity.
      mCallbacks.onCancelled();
      mRunning = false;
    }

    @Override
    protected void onPostExecute(Void ignore) {
      // Proxy the call to the Activity.
      mCallbacks.onPostExecute();
      mRunning = false;
    }
  }

  /************************/
  /***** LOGS & STUFF *****/
  /************************/

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    if (DEBUG) Log.i(TAG, "onActivityCreated(Bundle)");
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onStart() {
    if (DEBUG) Log.i(TAG, "onStart()");
    super.onStart();
  }

  @Override
  public void onResume() {
    if (DEBUG) Log.i(TAG, "onResume()");
    super.onResume();
  }

  @Override
  public void onPause() {
    if (DEBUG) Log.i(TAG, "onPause()");
    super.onPause();
  }

  @Override
  public void onStop() {
    if (DEBUG) Log.i(TAG, "onStop()");
    super.onStop();
  }

  @Override
  public void onDetach() {
    if (DEBUG) Log.i(TAG, "onDetach()");
    super.onDetach();
  }

}

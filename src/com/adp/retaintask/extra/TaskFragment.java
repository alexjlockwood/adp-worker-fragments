package com.adp.retaintask.extra;

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
    void onPreExecute();
    void onProgressUpdate(int percent);
    void onCancelled();
    void onPostExecute();
  }

  private TaskCallbacks mCallbacks;
  private DummyTask mTask;
  private boolean mRunning;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (!(getTargetFragment() instanceof TaskCallbacks)) {
      throw new IllegalStateException("Target fragment must implement the TaskCallbacks interface.");
    }

    // Hold a reference to the parent Activity so we can report back the task's
    // current progress and results.
    mCallbacks = (TaskCallbacks) getTargetFragment();
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

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
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
      if (mCallbacks != null) {
        mCallbacks.onPreExecute();
        mRunning = true;
      }
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
      // Proxy the call to the Activity.
      if (mCallbacks != null) {
        mCallbacks.onProgressUpdate(percent[0]);
      }
    }

    @Override
    protected void onCancelled() {
      // Proxy the call to the Activity.
      if (mCallbacks != null) {
        mCallbacks.onCancelled();
        mRunning = false;
      }
    }

    @Override
    protected void onPostExecute(Void ignore) {
      // Proxy the call to the Activity.
      if (mCallbacks != null) {
        mCallbacks.onPostExecute();
        mRunning = false;
      }
    }
  }

  /************************/
  /***** LOGS & STUFF *****/
  /************************/

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    Log.i(TAG, "onActivityCreated(Bundle)");
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onStart() {
    Log.i(TAG, "onStart()");
    super.onStart();
  }

  @Override
  public void onResume() {
    Log.i(TAG, "onResume()");
    super.onResume();
  }

  @Override
  public void onPause() {
    Log.i(TAG, "onPause()");
    super.onPause();
  }

  @Override
  public void onStop() {
    Log.i(TAG, "onStop()");
    super.onStop();
  }
}
package com.adp.worker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * This is the Fragment implementation that will be retained across activity
 * instances. It represents some ongoing work, here a thread we have that sits
 * around incrementing a progress indicator.
 */
public class TaskFragment extends Fragment implements TaskListener {
  private static final String TAG = TaskFragment.class.getSimpleName();
  private boolean mRunning = false;
  private DummyTask mTask;

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
    // Don't need to cancel this if you don't want (the AsyncTask will continue
    // to run and objects will be retained in memory, but presumably the task
    // will be short-lived so this won't matter).
    cancel();
  }

  /**
   * Start the background task.
   */
  public void start() {
    Log.i(TAG, "start()");
    if (!mRunning) {
      mTask = new DummyTask(this);
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
      mRunning = false;
    }
  }

  /**
   * Returns the current state of the background task.
   */
  public boolean isRunning() {
    return mRunning;
  }

  /****************************/
  /***** CALLBACK METHODS *****/
  /****************************/

  @Override
  public void onPreExecute() {
    // Forward the call to the UiFragment
    ((TaskListener) getTargetFragment()).onPreExecute();
    mRunning = true;
  }

  @Override
  public void onProgressUpdate(double percent) {
    // Forward the call to the UiFragment
    ((TaskListener) getTargetFragment()).onProgressUpdate(percent);
  }

  @Override
  public void onCancelled() {
    // Forward the call to the UiFragment
    ((TaskListener) getTargetFragment()).onCancelled();
    mRunning = false;
  }

  @Override
  public void onPostExecute() {
    // Forward the call to the UiFragment
    ((TaskListener) getTargetFragment()).onPostExecute();
    mRunning = false;
  }
}

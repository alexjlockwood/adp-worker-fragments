package com.adp.retaintask.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

/**
 * This is the Fragment implementation that will be retained across activity
 * instances.
 */
public class TaskFragment extends Fragment {
  @SuppressWarnings("unused")
  private static final String TAG = TaskFragment.class.getSimpleName();
  private DummyTask mTask;
  private boolean mRunning;

  /**
   * Called when the fragment is first initialized. Since we are using
   * {@link #setRetainInstance(boolean)}, this method will not be called on
   * configuration changes.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mRunning = false;
  }

  /**
   * This is called when the fragment is going away. It is NOT called when the
   * fragment is being propagated between activity instances.
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    // We don't need to cancel this if you don't want (the AsyncTask will
    // continue to run and objects will be retained in memory, but presumably
    // the task will be short-lived so this won't matter).
    cancel();
  }

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
      // Proxy the call to the MainActivity
      ((TaskCallbacks) getActivity()).onPreExecute();
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
      // Proxy the call to the MainActivity
      ((TaskCallbacks) getActivity()).onProgressUpdate(percent[0]);
    }

    @Override
    protected void onCancelled() {
      // Proxy the call to the MainActivity
      ((TaskCallbacks) getActivity()).onCancelled();
      mRunning = false;
    }

    @Override
    protected void onPostExecute(Void ignore) {
      // Proxy the call to the MainActivity
      ((TaskCallbacks) getActivity()).onPostExecute();
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

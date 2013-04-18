package com.adp.worker.fragments;

import android.os.AsyncTask;
import android.os.SystemClock;

public class DummyTask extends AsyncTask<Void, Double, Void> {
  private TaskCallbacks mCallback;

  public DummyTask(TaskCallbacks callback) {
    mCallback = callback;
  }

  @Override
  protected void onPreExecute() {
    // Forward the call to the TaskFragment
    mCallback.onPreExecute();
  }

  @Override
  protected Void doInBackground(Void... ignore) {
    int i = 0;
    while (!isCancelled() && i < 200) {
      publishProgress(i / 200.0);
      SystemClock.sleep(40);
      i++;
    }
    return null;
  }

  @Override
  protected void onProgressUpdate(Double... percent) {
    // Forward the call to the TaskFragment
    mCallback.onProgressUpdate(percent[0]);
  }

  @Override
  protected void onCancelled() {
    mCallback.onCancelled();
  }

  @Override
  protected void onPostExecute(Void ignore) {
    // Forward the call to the TaskFragment
    mCallback.onPostExecute();
  }
}
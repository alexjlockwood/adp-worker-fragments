package com.adp.retaintask.fragments;

public interface TaskCallbacks {

  public void onPreExecute();

  /**
   * @param percent A real number in the interval [0,1].
   */
  public void onProgressUpdate(double percent);

  public void onCancelled();

  public void onPostExecute();
}
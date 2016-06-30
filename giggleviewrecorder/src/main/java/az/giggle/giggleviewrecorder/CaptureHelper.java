package az.giggle.giggleviewrecorder;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;

import az.giggle.giggleviewrecorder.entity.CropEntity;
import az.giggle.giggleviewrecorder.entity.RecorderSettings;
import az.giggle.giggleviewrecorder.manager.BaseApplication;
import timber.log.Timber;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;

public class CaptureHelper {
  private static final int CREATE_SCREEN_CAPTURE = 4242;

  RecordingSession.Listener listener;

  Activity activity;

  BaseApplication application;

  RecorderSettings recorderSettings;

  public CaptureHelper(Activity activity) {
    this.activity = activity;
  }

  private CaptureHelper() {
    throw new AssertionError("No instances.");
  }

  public void startRecordView(BaseApplication baseApplication, RecorderSettings recorderSettings, RecordingSession.Listener listener) {
    this.application = baseApplication;
    this.recorderSettings = recorderSettings;
    this.listener = listener;

    application.setCaptureHelper(this);

    MediaProjectionManager manager =
            (MediaProjectionManager) activity.getSystemService(MEDIA_PROJECTION_SERVICE);
    Intent intent = manager.createScreenCaptureIntent();
    activity.startActivityForResult(intent, CREATE_SCREEN_CAPTURE);
  }

  public void stopRecordingView(){
    try {
      application.getRecordingSession().stopRecording();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode != CREATE_SCREEN_CAPTURE) {
      return false;
    }

    if (resultCode == Activity.RESULT_OK) {
      Timber.d("Acquired permission to screen capture. Starting service.");
      activity.startService(TelecineService.newIntent(activity, resultCode, data, recorderSettings));
    } else {
      Timber.d("Failed to acquire permission to screen capture.");
    }

    return true;
  }

  public RecordingSession.Listener getListener() {
    return listener;
  }

  public void setListener(RecordingSession.Listener listener) {
    this.listener = listener;
  }

  public boolean isRecording(){
    try {
      return application.getRecordingSession().isRunning();
    }catch (Exception e){
      return false;
    }
  }
}

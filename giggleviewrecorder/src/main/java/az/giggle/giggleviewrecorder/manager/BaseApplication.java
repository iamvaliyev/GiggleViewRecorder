package az.giggle.giggleviewrecorder.manager;

import android.app.Application;

import az.giggle.giggleviewrecorder.CaptureHelper;
import az.giggle.giggleviewrecorder.RecordingSession;
import az.giggle.giggleviewrecorder.entity.CropEntity;

/**
 * Created by Emil Valiyev on 6/28/16.
 */
public class BaseApplication extends Application {

    RecordingSession recordingSession;

    CaptureHelper captureHelper;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public RecordingSession getRecordingSession() {
        return recordingSession;
    }

    public void setRecordingSession(RecordingSession recordingSession) {
        this.recordingSession = recordingSession;
    }

    public CaptureHelper getCaptureHelper() {
        return captureHelper;
    }

    public void setCaptureHelper(CaptureHelper captureHelper) {
        this.captureHelper = captureHelper;
    }
}

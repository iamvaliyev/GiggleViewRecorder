package az.giggle.giggleviewrecorder;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import az.giggle.giggleviewrecorder.entity.RecorderSettings;
import az.giggle.giggleviewrecorder.manager.BaseApplication;
import timber.log.Timber;


public final class TelecineService extends Service {
    private static final String EXTRA_RESULT_CODE = "result-code";
    private static final String EXTRA_DATA = "data";

    RecorderSettings recorderSettings;

    public static Intent newIntent(Context context, int resultCode, Intent data, RecorderSettings recorderSettings) {
        Intent intent = new Intent(context, TelecineService.class);
        intent.putExtra(EXTRA_RESULT_CODE, resultCode);
        intent.putExtra(EXTRA_DATA, data);
        intent.putExtra("recorderSettings", recorderSettings);
        return intent;
    }

    ContentResolver contentResolver;

    private boolean running;
    private RecordingSession recordingSession;

    public RecordingSession.Listener publicListener = null;

    private final RecordingSession.Listener listener = new RecordingSession.Listener() {
        @Override
        public void onStart() {
            Timber.d("Moving service into the foreground with recording notification.");
            if (publicListener != null)
                publicListener.onStart();
        }

        @Override
        public void onStop() {
            if (publicListener != null)
                publicListener.onStop();
        }

        @Override
        public void onError(String error) {
            if (publicListener != null)
                publicListener.onError(error);
        }

        @Override
        public void onEnd(String path) {
            Timber.d("Shutting down.");
            stopSelf();
            if (publicListener != null)
                publicListener.onEnd(path);
        }
    };

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        if (running) {
            Timber.d("Already running! Ignoring...");
            return START_NOT_STICKY;
        }
        Timber.d("Starting up!");
        running = true;

        contentResolver = getContentResolver();

        recorderSettings = (RecorderSettings)intent.getExtras().get("recorderSettings");
        publicListener = ((BaseApplication)getApplication()).getCaptureHelper().getListener();

        int resultCode = intent.getIntExtra(EXTRA_RESULT_CODE, 0);
        Intent data = intent.getParcelableExtra(EXTRA_DATA);
        if (resultCode == 0 || data == null) {
            throw new IllegalStateException("Result code or data missing.");
        }

        recordingSession = new RecordingSession(getApplicationContext(), listener, resultCode, data);
        recordingSession.setRecorderSettings(recorderSettings);
        ((BaseApplication) getApplication()).setRecordingSession(recordingSession);
        recordingSession.showOverlay();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        recordingSession.destroy();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        throw new AssertionError("Not supported.");
    }

    public RecordingSession.Listener getPublicListener() {
        return publicListener;
    }

    public void setPublicListener(RecordingSession.Listener publicListener) {
        this.publicListener = publicListener;
    }
}

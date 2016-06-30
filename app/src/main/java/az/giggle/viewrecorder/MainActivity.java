package az.giggle.viewrecorder;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import az.giggle.giggleviewrecorder.CaptureHelper;
import az.giggle.giggleviewrecorder.RecordingSession;
import az.giggle.giggleviewrecorder.entity.RecorderSettings;
import az.giggle.giggleviewrecorder.manager.BaseApplication;

public class MainActivity extends AppCompatActivity {

    CaptureHelper captureHelper;

    Button btn, record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureHelper = new CaptureHelper(this);

        final LinearLayout view = (LinearLayout) findViewById(R.id.layout);
        btn = (Button) findViewById(R.id.btn);
        record = (Button) findViewById(R.id.record);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!captureHelper.isRecording()) {

                    RecorderSettings recorderSettings = new RecorderSettings(view);
                    recorderSettings.setWithDuration(true);
                    recorderSettings.setDuration(10);

                    captureHelper.startRecordView((BaseApplication) getApplication(), recorderSettings, new RecordingSession.Listener() {
                        @Override
                        public void onStart() {
                            ColorDrawable[] color = {new ColorDrawable(Color.BLUE), new ColorDrawable(Color.RED)};

                            TransitionDrawable trans = new TransitionDrawable(color);
                            view.setBackgroundDrawable(trans);
                            trans.startTransition(10000);
                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onError(String error) {

                        }

                        @Override
                        public void onEnd(String path) {

                        }
                    });

                    record.setText("STOP");

                } else {
                    captureHelper.stopRecordingView();
                    record.setText("RECORD");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        captureHelper.handleActivityResult(requestCode, resultCode, data);
    }
}

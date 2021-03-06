package com.dfa.vinatrip.video_call;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;
import com.skyfishjy.library.RippleBackground;

import java.util.List;

import static com.dfa.vinatrip.utils.Constants.REQUEST_PERMISSION_VIDEO_CALL;

public class IncomingCallScreenActivity extends BaseVideoCallActivity {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private AudioPlayer mAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call_screen);

        LinearLayout answer = (LinearLayout) findViewById(R.id.activity_incoming_call_screen_ll_answer);
        answer.setOnClickListener(mClickListener);
        LinearLayout decline = (LinearLayout) findViewById(R.id.activity_incoming_call_screen_ll_cancel);
        decline.setOnClickListener(mClickListener);
        RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.activity_incoming_call_screen_rb);
        rippleBackground.startRippleAnimation();

        requestPermission();

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }


    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView tvRemoteUser = (TextView) findViewById(R.id.activity_incoming_call_screen_remote_user);
            tvRemoteUser.setText(call.getRemoteUserId());

        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_incoming_call_screen_ll_answer:
                    answerClicked();
                    break;
                case R.id.activity_incoming_call_screen_ll_cancel:
                    declineClicked();
                    break;
            }
        }
    };

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_VIDEO_CALL);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_VIDEO_CALL) {
            if (grantResults.length != 3 || grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bạn đã không cấp quyền, một số chức năng có thể không hoạt động", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
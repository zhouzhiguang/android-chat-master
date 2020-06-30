package cn.wildfire.chat.kit.voip;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.webrtc.StatsReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.GlideApp;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.avenginekit.AVEngineKit;
import cn.wildfirechat.chat.R;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class SingleAudioFragment extends Fragment implements AVEngineKit.CallSessionCallback {
    private AVEngineKit gEngineKit;
    private boolean audioEnable = true;
    private boolean isSpeakerOn = false;

    @BindView(R.id.portraitImageView)
    ImageView portraitImageView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;
    @BindView(R.id.muteImageView)
    ImageView muteImageView;
    @BindView(R.id.speakerImageView)
    ImageView spearImageView;
    @BindView(R.id.incomingActionContainer)
    ViewGroup incomingActionContainer;
    @BindView(R.id.outgoingActionContainer)
    ViewGroup outgoingActionContainer;
    @BindView(R.id.descTextView)
    TextView descTextView;
    @BindView(R.id.durationTextView)
    TextView durationTextView;

    private static final String TAG = "AudioFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.av_p2p_audio_layout, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void didCallEndWithReason(AVEngineKit.CallEndReason reason) {
        // never called
    }

    @Override
    public void didChangeState(AVEngineKit.CallState state) {
        runOnUiThread(() -> {
            if (state == AVEngineKit.CallState.Connected) {
                incomingActionContainer.setVisibility(View.GONE);
                outgoingActionContainer.setVisibility(View.VISIBLE);
                descTextView.setVisibility(View.GONE);
                durationTextView.setVisibility(View.VISIBLE);
            } else if(state == AVEngineKit.CallState.Idle) {
                getActivity().finish();
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void didParticipantJoined(String s) {

    }

    @Override
    public void didParticipantConnected(String userId) {

    }

    @Override
    public void didParticipantLeft(String s, AVEngineKit.CallEndReason callEndReason) {

    }

    @Override
    public void didChangeMode(boolean audioOnly) {
        // never called
    }

    @Override
    public void didCreateLocalVideoTrack() {
        // never called
    }

    @Override
    public void didReceiveRemoteVideoTrack(String s) {

    }

    @Override
    public void didRemoveRemoteVideoTrack(String s) {

    }

    @Override
    public void didError(String error) {

    }

    @Override
    public void didGetStats(StatsReport[] reports) {
        runOnUiThread(() -> {
            //hudFragment.updateEncoderStatistics(reports);
            // TODO
        });
    }

    @Override
    public void didVideoMuted(String s, boolean b) {

    }

    @Override
    public void didReportAudioVolume(String userId, int volume) {
        Log.d(TAG, "voip audio " + userId + " " + volume);

    }

    @OnClick(R.id.muteImageView)
    public void mute() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session != null && session.getState() == AVEngineKit.CallState.Connected) {
            audioEnable = !audioEnable;
            session.muteAudio(!audioEnable);
            muteImageView.setSelected(!audioEnable);
        }
    }

    @OnClick({R.id.incomingHangupImageView, R.id.outgoingHangupImageView})
    public void hangup() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session != null) {
            session.endCall();
        } else {
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @OnClick(R.id.acceptImageView)
    public void onCallConnect() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session == null) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                getActivity().finish();
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            return;
        }
        if (session.getState() == AVEngineKit.CallState.Incoming) {
            session.answerCall(false);
        }
    }

    @OnClick(R.id.minimizeImageView)
    public void minimize() {
        ((SingleCallActivity) getActivity()).showFloatingView(null);
    }

    @OnClick(R.id.speakerImageView)
    public void speakerClick() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session == null || session.getState() != AVEngineKit.CallState.Connected) {
            return;
        }
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (isSpeakerOn) {
            isSpeakerOn = false;
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            isSpeakerOn = true;
            audioManager.setMode(AudioManager.MODE_NORMAL);

        }
        spearImageView.setSelected(isSpeakerOn);
        audioManager.setSpeakerphoneOn(isSpeakerOn);
    }

    private void init() {
        gEngineKit = ((SingleCallActivity) getActivity()).getEngineKit();
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session == null || session.getState() == AVEngineKit.CallState.Idle) {
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return;
        }
        if (session.getState() == AVEngineKit.CallState.Connected) {
            descTextView.setVisibility(View.GONE);
            outgoingActionContainer.setVisibility(View.VISIBLE);
            durationTextView.setVisibility(View.VISIBLE);
        } else {
            if (session.getState() == AVEngineKit.CallState.Outgoing) {
                descTextView.setText(R.string.av_waiting);
                outgoingActionContainer.setVisibility(View.VISIBLE);
                incomingActionContainer.setVisibility(View.GONE);
            } else {
                descTextView.setText(R.string.av_audio_invite);
                outgoingActionContainer.setVisibility(View.GONE);
                incomingActionContainer.setVisibility(View.VISIBLE);
            }
        }
        String targetId = session.getParticipantIds().get(0);
        UserInfo userInfo = ChatManager.Instance().getUserInfo(targetId, false);
        GlideApp.with(this).load(userInfo.portrait).placeholder(R.mipmap.avatar_def).into(portraitImageView);
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        nameTextView.setText(userViewModel.getUserDisplayName(userInfo));
        audioEnable = session.isEnableAudio();
        muteImageView.setSelected(!audioEnable);
        updateCallDuration();

        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        isSpeakerOn = audioManager.getMode() == AudioManager.MODE_NORMAL;
        spearImageView.setSelected(isSpeakerOn);
    }

    private void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    private Handler handler = new Handler();

    private void updateCallDuration() {
        AVEngineKit.CallSession session = gEngineKit.getCurrentSession();
        if (session != null && session.getState() == AVEngineKit.CallState.Connected) {
            long s = System.currentTimeMillis() - session.getConnectedTime();
            s = s / 1000;
            String text;
            if (s > 3600) {
                text = String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
            } else {
                text = String.format("%02d:%02d", s / 60, (s % 60));
            }
            durationTextView.setText(text);
        }
        handler.postDelayed(this::updateCallDuration, 1000);
    }
}

package bombaycinema.cameratimer;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class CurrentProfileFragment extends Fragment {
    private int[] backgroundColours = new int[3];
    private Profile currentProfile;
    private Timer timer = new Timer();
    private TextView currentProfileNameDisplay;
    private TextView currentProfileTimerLengthDisplay;
    private TextView timerProgressDisplay;
    private TextView nextIntervalDisplay;
    private TextView intervalsElapsedDisplay;
    private int intervalsElapsed = 0;
    private boolean timerRunning = false;
    FloatingActionButton startTimerButton;
    FloatingActionButton resetTimerButton;
    FloatingActionButton cancelProfileButton;
    ProgressBar progressBar;
    Ringtone soundEffect;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_profile_fragment, container, false);

        backgroundColours[0] = this.getArguments().getInt("backgroundColour");
        backgroundColours[1] = this.getArguments().getInt("backgroundColourDark");
        backgroundColours[2] = this.getArguments().getInt("backgroundColourLight");

        final RelativeLayout currentProfileLayout = (RelativeLayout) view.findViewById(R.id.currentProfileLayout);
        currentProfileLayout.setBackgroundColor(backgroundColours[2]);

        currentProfileNameDisplay = (TextView) view.findViewById(R.id.currentProfileNameDetails);
        currentProfileTimerLengthDisplay = (TextView) view.findViewById(R.id.currentProfileLengthDetails);
        timerProgressDisplay = (TextView) view.findViewById(R.id.currentProfileTimerProgressDetails);
        nextIntervalDisplay = (TextView) view.findViewById(R.id.currentProfileNextIntervalDetails);
        intervalsElapsedDisplay = (TextView) view.findViewById(R.id.currentProfileIntervalsElapsed);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        soundEffect = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);

        resetTimerButton = (FloatingActionButton) view.findViewById(R.id.resetTimerButton);
        resetTimerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        resetTimerButtonClicked();
                    }
                }
        );

        startTimerButton = (FloatingActionButton) view.findViewById(R.id.startTimerButton);
        startTimerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        startTimerButtonClicked();
                    }
                }
        );

        cancelProfileButton = (FloatingActionButton) view.findViewById(R.id.pauseTimerButton);
        cancelProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        cancelProfileButtonClicked();
                    }
                }
        );

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timerRunning == true) {
                    currentProfile.getCurrentProgress().incrementTime();
                    if (currentProfile.getCurrentProgress().toSeconds() >= currentProfile.getTimerLength().toSeconds()) {
                        timerRunning = false;
                        disableStartTimeButton();
                        changeStopIcon();
                    } else {
                        if (currentProfile.getCurrentProgress().toSeconds() % currentProfile.getIntervalFrequency().toSeconds() == 0) {
                            currentProfile.updateNextInterval();
                        }
                    }
                    if (currentProfile.getCurrentProgress().toSeconds() % currentProfile.getIntervalFrequency().toSeconds() == 0) {
                        playSound();
                        intervalsElapsed++;
                    }

                    updateDisplay();
                }
            }
        }, 0, 1000);

        setButtonsEnabled(false);

        return view;
    }

    public void playSound(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    soundEffect.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void resetTimerButtonClicked() {
        timerRunning = false;
        intervalsElapsed = 0;
        currentProfile.resetCurrentProgress();
        currentProfile.resetNextInterval();
        currentProfile.updateNextInterval();
        updateDisplay();
        changeStopIcon();
        startTimerButton.setEnabled(true);
        progressBar.setProgress(0);
    }

    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerProgressDisplay.setText(currentProfile.getCurrentProgress().toString());
                nextIntervalDisplay.setText(currentProfile.getNextInterval().toString());
                intervalsElapsedDisplay.setText("Intervals Elapsed : " + intervalsElapsed);
                progressBar.setProgress(currentProfile.getCurrentProgress().toSeconds());
            }
        });
    }

    public void setCurrentProfile(Profile profile) {
        currentProfile = profile;
        setupProfile();
        setButtonsEnabled(true);
    }

    public Profile getCurrentProfile(){
        return currentProfile;
    }

    public void resetCurrentProfile(){
        ((MainScreen) getActivity()).stopCurrentProfile(currentProfile);
        currentProfile = null;
        intervalsElapsed = 0;
        currentProfileNameDisplay.setText("");
        currentProfileTimerLengthDisplay.setText("Timer Length : 00h 00m 00s");
        timerProgressDisplay.setText(R.string.emptyTime);
        nextIntervalDisplay.setText(R.string.emptyTime);
        intervalsElapsedDisplay.setText("Intervals Elapsed : " + intervalsElapsed);
        setButtonsEnabled(false);
        progressBar.setProgress(0);
        progressBar.setMax(0);
    }

    public void setupProfile(){
        currentProfile.resetCurrentProgress();
        currentProfile.resetNextInterval();
        intervalsElapsed = 0;
        currentProfile.updateNextInterval();
        currentProfileNameDisplay.setText(currentProfile.getProfileName());
        currentProfileTimerLengthDisplay.setText("Timer Length : " + currentProfile.getTimerLength());
        updateDisplay();
        progressBar.setProgress(0);
        progressBar.setMax(currentProfile.getTimerLength().toSeconds());
    }

    public void setButtonsEnabled(boolean isEnabled){
        startTimerButton.setEnabled(isEnabled);
        resetTimerButton.setEnabled(isEnabled);
        cancelProfileButton.setEnabled(isEnabled);
    }

    public void disableStartTimeButton(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startTimerButton.setEnabled(false);
            }
        });

    }

    public void startTimerButtonClicked() {
        if (timerRunning == false) {
            timerRunning = true;
            changeStartIcon();
        } else {
            timerRunning = false;
            changeStopIcon();
        }
    }

    public void cancelProfileButtonClicked() {
        timerRunning = false;
        resetCurrentProfile();
    }


    public void changeStartIcon(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_media_pause);
                Drawable newIcon = myFabSrc.getConstantState().newDrawable();
                newIcon.mutate().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                ((FloatingActionButton) getActivity().findViewById(R.id.startTimerButton)).setImageDrawable(newIcon);
            }
        });
    }

    public void changeStopIcon(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_media_play);
                Drawable newIcon = myFabSrc.getConstantState().newDrawable();
                newIcon.mutate().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                ((FloatingActionButton) getActivity().findViewById(R.id.startTimerButton)).setImageDrawable(newIcon);
            }
        });
    }
}

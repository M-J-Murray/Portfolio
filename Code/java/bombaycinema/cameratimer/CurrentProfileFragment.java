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

/*
 * Fragment to represent the current timer in use
 * Handles all the functionality of the timer
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
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

    /*
     * Handles the creation of the fragment
     * @param inflater - the layout XML file used for corresponding View objects
     * @param container - the view object used to represent the fragment
     * @param savedInstanceState - bundle of data to be passed into the fragment
     * @return the activity view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //creates view
        View view = inflater.inflate(R.layout.current_profile_fragment, container, false);

        //sets up background colours to be displayed from bundle
        backgroundColours[0] = this.getArguments().getInt("backgroundColour");
        backgroundColours[1] = this.getArguments().getInt("backgroundColourDark");
        backgroundColours[2] = this.getArguments().getInt("backgroundColourLight");

        //creates fragment layout and attaches it to XML file
        final RelativeLayout currentProfileLayout = (RelativeLayout) view.findViewById(R.id.currentProfileLayout);
        //sets layout background colour
        currentProfileLayout.setBackgroundColor(backgroundColours[2]);

        //attaches relevant views to XML file components
        currentProfileNameDisplay = (TextView) view.findViewById(R.id.currentProfileNameDetails);
        currentProfileTimerLengthDisplay = (TextView) view.findViewById(R.id.currentProfileLengthDetails);
        timerProgressDisplay = (TextView) view.findViewById(R.id.currentProfileTimerProgressDetails);
        nextIntervalDisplay = (TextView) view.findViewById(R.id.currentProfileNextIntervalDetails);
        intervalsElapsedDisplay = (TextView) view.findViewById(R.id.currentProfileIntervalsElapsed);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        //sets up sound to be played at each interval
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        soundEffect = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);

        //creates buttons and attaches relevant methods to on click mechanisms
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

        //sets up timer to run methods at set intervals
        timer.scheduleAtFixedRate(new TimerTask() {
            //repeats every 1 second
            @Override
            public void run() {
                //only runs when play button pressed
                if (timerRunning == true) {
                    //increments current timer progress by 1 second
                    currentProfile.getCurrentProgress().incrementTime();
                    //keeps timer running within bounds of time limit
                    if (currentProfile.getCurrentProgress().toSeconds() >= currentProfile.getTimerLength().toSeconds()) {
                        //stops the current timer from running
                        timerRunning = false;
                        disableStartTimeButton();
                        changeStopIcon();
                    } else {
                        //runs if timer is at the next interval
                        if (currentProfile.getCurrentProgress().toSeconds() % currentProfile.getIntervalFrequency().toSeconds() == 0) {
                            //updates the time of next interval
                            currentProfile.updateNextInterval();
                        }
                    }
                    //runs if timer is at the next interval
                    if (currentProfile.getCurrentProgress().toSeconds() % currentProfile.getIntervalFrequency().toSeconds() == 0) {
                        //plays sound effect and increments intervals
                        playSound();
                        intervalsElapsed++;
                    }
                    updateDisplay();
                }
            }
        }, 0, 1000);
        //disables buttons as no profile set
        setButtonsEnabled(false);

        return view;
    }

    /*
     * method to create thread and play sound effect of interval
     */
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

    /*
     * method to reset all the timer settings to default
     */
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

    /*
     * updates the view components to represent the current profiles timer settings
     */
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

    /*
     * sets the current profile
     * @param profile - the new profile being set
     */
    public void setCurrentProfile(Profile profile) {
        currentProfile = profile;
        setupProfile();
        setButtonsEnabled(true);
    }

    /*
     * gets the current profile
     * @return the current profile
     */
    public Profile getCurrentProfile(){
        return currentProfile;
    }

    /*
     * resets all components and variables related to the current profile to the default settings
     */
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

    /*
     * attaches new profile details to the relevant components
     */
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

    /*
     * enables/disables the relevant buttons to play/reset/stop a profile
     * @param isEnables - boolean deciding whether buttons are enables or not
     */
    public void setButtonsEnabled(boolean isEnabled){
        startTimerButton.setEnabled(isEnabled);
        resetTimerButton.setEnabled(isEnabled);
        cancelProfileButton.setEnabled(isEnabled);
    }

    /*
     * creates a thread to disable the start timer button
     */
    public void disableStartTimeButton(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startTimerButton.setEnabled(false);
            }
        });

    }

    /*
     * method runs when start/pause timer button clicked
     */
    public void startTimerButtonClicked() {
        //starts/pauses current timer button and updates icon
        if (timerRunning == false) {
            timerRunning = true;
            changeStartIcon();
        } else {
            timerRunning = false;
            changeStopIcon();
        }
    }

    /*
     * method to cancel and remove all details about current profile timer
     */
    public void cancelProfileButtonClicked() {
        timerRunning = false;
        changeStopIcon();
        resetCurrentProfile();
    }

    /*
     * creates a thread to update the current start icon to the pause icon
     */
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
    /*
     * creates a thread to update the current pause icon to the start icon
     */
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

package bombaycinema.cameratimer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Fragment to represent the current timer in use
 * Handles all the functionality of the timer
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class CurrentProfileFragment extends Fragment{
    private RelativeLayout currentProfileLayout;
    private Profile currentProfile;
    private Timer timer = new Timer();
    private TextView currentProfileNameDisplay;
    private TextView currentProfileTimerLengthDisplay;
    private TextView timerProgressDisplay;
    private TextView nextIntervalDisplay;
    private TextView intervalsElapsedDisplay;
    private int intervalsElapsed = 0;
    private boolean timerRunning = false;
    private FloatingActionButton startTimerButton;
    private FloatingActionButton resetTimerButton;
    private FloatingActionButton cancelProfileButton;
    private ProgressBar progressBar;
    private MediaPlayer tonePlayer;
    private MediaPlayer nextTonePlayer;
    private MediaPlayer beepPlayer;
    private MediaPlayer nextBeepPlayer;
    private TextToSpeech myTTS;
    private int previousInterval;

    /**
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

        tonePlayer = MediaPlayer.create(getActivity(), R.raw.tonenoise);
        beepPlayer = MediaPlayer.create(getActivity(), R.raw.beepnoise);

        //creates a new Text to Speach component with an initialisation method
        myTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result = myTTS.setLanguage(Locale.UK);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });

        //sets up background colours to be displayed from bundle
        int backgroundColour = this.getArguments().getInt("backgroundColour");

        //creates fragment layout and attaches it to XML file
        currentProfileLayout = (RelativeLayout) view.findViewById(R.id.currentProfileLayout);
        applyThemes(backgroundColour);

        //attaches relevant views to XML file components
        currentProfileNameDisplay = (TextView) view.findViewById(R.id.currentProfileNameDetails);
        currentProfileTimerLengthDisplay = (TextView) view.findViewById(R.id.currentProfileLengthDetails);
        timerProgressDisplay = (TextView) view.findViewById(R.id.currentProfileTimerProgressDetails);
        nextIntervalDisplay = (TextView) view.findViewById(R.id.currentProfileNextIntervalDetails);
        intervalsElapsedDisplay = (TextView) view.findViewById(R.id.currentProfileIntervalsElapsed);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //only runs when play button pressed
                            if (timerRunning == true && (currentProfile.getCurrentProgress().toSeconds() <= currentProfile.getTimerLength().toSeconds())) {
                                //increments current timer progress by 1 second
                                currentProfile.getCurrentProgress().incrementTime();

                                //keeps timer running within bounds of time limit
                                if (currentProfile.getCurrentProgress().toSeconds() >= currentProfile.getTimerLength().toSeconds()) {
                                    //stops the current timer from running
                                    startTimerButtonClicked();
                                    disableStartTimeButton();
                                } else {
                                    if (currentProfile.getIntervalFrequency().toSeconds() != 0) {
                                        //runs if timer is at the next interval
                                        if (currentProfile.getCurrentProgress().toSeconds() % currentProfile.getIntervalFrequency().toSeconds() == 0) {
                                            previousInterval = currentProfile.getNextInterval().toSeconds();
                                            //updates the time of next interval
                                            currentProfile.updateNextInterval();
                                        }
                                    }
                                }

                                //runs if timer is at the next interval
                                if (    (currentProfile.getCurrentProgress().toSeconds() >= currentProfile.getNextInterval().toSeconds()-3) &&
                                        (currentProfile.getCurrentProgress().toSeconds() < currentProfile.getNextInterval().toSeconds()-1)) {
                                    //plays beep sound effect
                                    playBeepNoise();
                                }
                                //runs if timer is at the next interval
                                if (    (currentProfile.getCurrentProgress().toSeconds() >= previousInterval) &&
                                        (currentProfile.getCurrentProgress().toSeconds() < previousInterval - 1 + currentProfile.getWhiteNoiseLength().toSeconds())) {
                                    if (currentProfile.getCurrentProgress().toSeconds() == previousInterval){
                                        //plays sound effect and increments intervals
                                        if (currentProfile.isReadOutIntervals() == true) {
                                            playTTS();
                                        }
                                        intervalsElapsed++;
                                    }
                                    if (!myTTS.isSpeaking()){
                                        playToneNoise();
                                    }
                                }
                                updateDisplay();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 0, 1000);
        //disables buttons as no profile set
        setButtonsEnabled(false);

        return view;
    }

    public void setTTSVolume(int volume){
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(am.STREAM_MUSIC, volume, 0);
    }

    /**
     * method to creates thread to read out TTS
     */
    public void playTTS(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    myTTS.speak(currentProfile.getCurrentProgress().toTTS(), TextToSpeech.QUEUE_FLUSH, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setToneVolume(float volume){
        tonePlayer.setVolume(volume, volume);
        nextTonePlayer.setVolume(volume, volume);
    }

    /**
    * method to creates thread to play a tone sound effect
    */
    public void playToneNoise(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    tonePlayer.start();
                    //attaches another tone noise media player for seemless looping
                    nextTonePlayer = MediaPlayer.create(getActivity(), R.raw.tonenoise);
                    tonePlayer.setNextMediaPlayer(nextTonePlayer);
                    tonePlayer.setOnCompletionListener(onToneCompletionListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * method to run on tone noise sound completion
     * attaches the next tone noise media player for seemless looping
     */
    private MediaPlayer.OnCompletionListener onToneCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
            tonePlayer = nextTonePlayer;
        }
    };

    /**
     * sets the volume of the beep noise sound effect
     * @param volume - the volume of the beep
     */
    public void setBeepVolume(float volume){
        beepPlayer.setVolume(volume, volume);
        nextBeepPlayer.setVolume(volume, volume);
    }

    /**
    * method to creates thread to give warning beeps before agitation
    */
    public void playBeepNoise(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    beepPlayer.start();
                    //attaches another tone noise media player for seemless looping
                    nextBeepPlayer = MediaPlayer.create(getActivity(), R.raw.beepnoise);
                    beepPlayer.setNextMediaPlayer(nextBeepPlayer);
                    beepPlayer.setOnCompletionListener(onBeepCompletionListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * method to run on tone noise sound completion
     * attaches the next tone noise media player for seemless looping
     */
    private MediaPlayer.OnCompletionListener onBeepCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
            beepPlayer = nextBeepPlayer;
        }
    };

    /**
     * clears timer schedule
     */
    public void cancelTimer(){
        timer.cancel();
        myTTS.shutdown();
    }

    /**
     * method to reset all the timer settings to default
     */
    public void resetTimerButtonClicked() {
        ((MainScreen) getActivity()).pauseProfileFragment(currentProfile);
        timerRunning = false;
        intervalsElapsed = 0;
        currentProfile.resetCurrentProgress();
        currentProfile.resetNextInterval();
        currentProfile.updateNextInterval();
        previousInterval = currentProfile.getNextInterval().toSeconds();
        updateDisplay();
        changeStopIcon();
        startTimerButton.setEnabled(true);
        progressBar.setProgress(0);
    }

    /**
     * updates the view components to represent the current profiles timer settings
     */
    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerProgressDisplay.setText(currentProfile.getCurrentProgress().toString());
                nextIntervalDisplay.setText(currentProfile.getNextInterval().toString());
                intervalsElapsedDisplay.setText("Agitations Elapsed : " + intervalsElapsed);
                progressBar.setProgress(currentProfile.getCurrentProgress().toSeconds());
            }
        });
    }

    public void applyThemes(int backgroundColour){
        currentProfileLayout.setBackgroundColor(backgroundColour);
    }

    /**
     * sets the current profile
     * @param profile - the new profile being set
     */
    public void setCurrentProfile(Profile profile) {
        cancelProfileButtonClicked();
        currentProfile = profile;
        setupProfile();
        setButtonsEnabled(true);
    }

    /**
     * gets the current profile
     * @return the current profile
     */
    public Profile getCurrentProfile(){
        return currentProfile;
    }

    /**
     * resets all components and variables related to the current profile to the default settings
     */
    public void resetCurrentProfile(){
        if (currentProfile != null){
            ((MainScreen) getActivity()).deactivateCurrentProfileFragment();
        }
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

    /**
     * attaches new profile details to the relevant components
     */
    public void setupProfile(){
        currentProfile.resetCurrentProgress();
        currentProfile.resetNextInterval();
        intervalsElapsed = 0;
        currentProfile.updateNextInterval();
        previousInterval = currentProfile.getNextInterval().toSeconds();
        currentProfileNameDisplay.setText(currentProfile.getProfileName());
        currentProfileTimerLengthDisplay.setText("Timer Length : " + currentProfile.getTimerLength());
        updateDisplay();
        progressBar.setProgress(0);
        progressBar.setMax(currentProfile.getTimerLength().toSeconds());
        externalStartButtonClicked();
    }

    /**
     * enables/disables the relevant buttons to play/reset/stop a profile
     * @param isEnabled - boolean deciding whether buttons are enables or not
     */
    public void setButtonsEnabled(boolean isEnabled){
        startTimerButton.setEnabled(isEnabled);
        resetTimerButton.setEnabled(isEnabled);
        cancelProfileButton.setEnabled(isEnabled);
    }

    /**
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

    /**
     * method runs when start/pause timer button clicked
     */
    public void startTimerButtonClicked() {
        ((MainScreen) getActivity()).playPauseProfileFragment(currentProfile);
        //starts/pauses current timer button and updates icon
        if (timerRunning == false) {
            timerRunning = true;
            changeStartIcon();
        } else {
            timerRunning = false;
            changeStopIcon();
        }
    }

    /**
     * method runs when start/pause timer button clicked from profile fragment
     */
    public void externalStartButtonClicked(){
        if (currentProfile.getCurrentProgress().toSeconds() >= currentProfile.getTimerLength().toSeconds()){
            resetCurrentProfile();
        }
        //starts/pauses current timer button and updates icon
        if (timerRunning == false) {
            timerRunning = true;
            changeStartIcon();
        } else {
            timerRunning = false;
            changeStopIcon();
        }
    }


    /**
     * method to cancel and remove all details about current profile timer
     */
    public void cancelProfileButtonClicked() {
        timerRunning = false;
        changeStopIcon();
        resetCurrentProfile();
    }

    /**
     * creates a thread to update the current start icon to the pause icon
     */
    public void changeStartIcon(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_media_pause);
                Drawable newIcon = myFabSrc.getConstantState().newDrawable();
                newIcon.mutate().setColorFilter(Color.argb(255,85,85,85), PorterDuff.Mode.MULTIPLY);
                ((FloatingActionButton) getActivity().findViewById(R.id.startTimerButton)).setImageDrawable(newIcon);
            }
        });
    }

    /**
     * creates a thread to update the current pause icon to the start icon
     */
    public void changeStopIcon(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_media_play);
                Drawable newIcon = myFabSrc.getConstantState().newDrawable();
                newIcon.mutate().setColorFilter(Color.argb(255,85,85,85), PorterDuff.Mode.MULTIPLY);
                ((FloatingActionButton) getActivity().findViewById(R.id.startTimerButton)).setImageDrawable(newIcon);
            }
        });
    }
}

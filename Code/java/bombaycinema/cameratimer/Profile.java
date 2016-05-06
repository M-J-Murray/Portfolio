package bombaycinema.cameratimer;

import android.util.Log;

public class Profile {
    private String profileName = null;
    private Time timerLength = new Time(0,0,0);
    private Time intervalFrequency = new Time(0,0,0);
    private Time whiteNoiseLength = new Time(0,0,0);
    private boolean readOutIntervals = false;
    private Time currentProgress = new Time(0,0,0);
    private Time nextInterval = new Time(0, 0, 0);

    public Profile(String profileName, Time timerLength, Time intervalFrequency, Time whiteNoiseLength, boolean readOutIntervals){
        this.profileName = profileName;
        this.timerLength = timerLength;
        this.intervalFrequency = intervalFrequency;
        this.whiteNoiseLength = whiteNoiseLength;
        this.readOutIntervals = readOutIntervals;
    }

    public Time getNextInterval() {
        return nextInterval;
    }


    public void resetNextInterval(){
        nextInterval.resetTime();
    }

    public void updateNextInterval(){
        nextInterval.secondsToTime(nextInterval.toSeconds()+intervalFrequency.toSeconds());
    }

    public Time getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Time currentProgress) {
        this.currentProgress = currentProgress;
    }

    public void resetCurrentProgress(){
        currentProgress.resetTime();
    }

    public void incrementCurrentProgress(){
        currentProgress.incrementTime();
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Time getTimerLength() {
        return timerLength;
    }

    public void setTimerLength(Time timerLength) {
        this.timerLength = timerLength;
    }

    public Time getWhiteNoiseLength() {
        return whiteNoiseLength;
    }

    public void setWhiteNoiseLength(Time whiteNoiseLength) {
        this.whiteNoiseLength = whiteNoiseLength;
    }

    public Time getIntervalFrequency() {
        return intervalFrequency;
    }

    public void setIntervalFrequency(Time intervalFrequency) {
        this.intervalFrequency = intervalFrequency;
    }

    public String getReadOutIntervalsAsString(){
        if (readOutIntervals == true){
            return "true";
        } else {
            return "false";
        }
    }

    public boolean isReadOutIntervals() {
        return readOutIntervals;
    }

    public void setReadOutIntervals(boolean readOutIntervals) {
        this.readOutIntervals = readOutIntervals;
    }

}

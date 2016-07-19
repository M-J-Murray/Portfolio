package bombaycinema.cameratimer;


/**
 * Object used to represent the details of a profile
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class Profile {
    private int profileID;
    private String profileName = null;
    private Time timerLength = new Time(0,0,0);
    private Time intervalFrequency = new Time(0,0,0);
    private Time whiteNoiseLength = new Time(0,0,0);
    private boolean readOutIntervals = false;
    private Time currentProgress = new Time(0,0,0);
    private Time nextInterval = new Time(0, 0, 0);

    /**
     * Constructor for the profile object
     * @param profileName - the profile name
     * @param timerLength - the length of the timer
     * @param intervalFrequency - the frequency at which the timer notifies the user
     * @param whiteNoiseLength - the length of time the white noise plays for
     * @param readOutIntervals - boolean stating whether the timer reads out the current interval
     */
    public Profile(String profileName, Time timerLength, Time intervalFrequency, Time whiteNoiseLength, boolean readOutIntervals){
        this.profileName = profileName;
        this.timerLength = timerLength;
        this.intervalFrequency = intervalFrequency;
        this.whiteNoiseLength = whiteNoiseLength;
        this.readOutIntervals = readOutIntervals;
    }

    /**
    * updates the profiles ID
    * @param profileID - the profiles position in the main list
    */
   public void setProfileID(int profileID){
       this.profileID = profileID;
   }

    /**
     * @return the profiles ID
     */
    public int getProfileID(){
        return profileID;
    }

   /**
    * @return the variable for the time of the next interval
    */
    public Time getNextInterval() {
        return nextInterval;
    }

    /**
     * resets the next interval variables time back to 0 everything
     */
    public void resetNextInterval(){
        nextInterval.resetTime();
    }

    /**
     * updates the next interval variables time for the next interval
     */
    public void updateNextInterval(){
        nextInterval.secondsToTime(nextInterval.toSeconds() + intervalFrequency.toSeconds());
    }

    /**
     * @return the variable for the time of the current progress
     */
    public Time getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Time progress){
        currentProgress = progress;
    }

    /**
     * resets the current time variables time back to 0 everything
     */
    public void resetCurrentProgress(){
        currentProgress.resetTime();
    }

    /**
     * @return the name of the profile
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @return the length of the timer
     */
    public Time getTimerLength() {
        return timerLength;
    }

    /**
     * resets the next interval variables time back to 0 everything
     */
    public Time getWhiteNoiseLength() {
        return whiteNoiseLength;
    }

    /**
     * @return the interval frequency of the profile
     */
    public Time getIntervalFrequency() {
        return intervalFrequency;
    }

    /**
     * @return a string stating whether the profile reads out the intervals or not
     */
    public String getReadOutIntervalsAsString(){
        if (readOutIntervals == true){
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * @return a boolean stating whether the profile reads out the intervals or not
     */
    public boolean isReadOutIntervals() {
        return readOutIntervals;
    }

    /**
     * converts the current profile into a string
     * mainly used for inline testing
     */
    public String toString(){
        String out = "";
        out += profileID +"\n";
        out += profileName +"\n";
        out += timerLength.toString() +"\n";
        out += intervalFrequency.toString() +"\n";
        out += whiteNoiseLength.toString() +"\n";
        out += readOutIntervals +"\n";
        return out;
    }
}

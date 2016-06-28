package bombaycinema.cameratimer;


import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * Fragment to represent the profile editor
 * Handles functionality related to adding/editing profile details or even removal
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class NewProfileFragment extends DialogFragment {
    private int profileID;
    private String profileName;
    private Time timeFrame = new Time(0,0,0);
    private Time intervalFrequency = new Time(0,0,0);;
    private Time whiteNoiseLength = new Time(0,0,0);;
    private boolean readOutIntervals;
    private int backgroundColour;
    private TimerInputFragment timerInputFragmentDialog;
    private boolean editProfile;

    /*
     * static method which occurs on instantiation of object
     * @param savedInstanceState - bundle of data to be passed into the fragment
     * @return this fragment
     */
    public static NewProfileFragment newInstance(@Nullable Bundle savedInstanceState) {
        NewProfileFragment f = new NewProfileFragment();
        f.setArguments(savedInstanceState);
        return f;
    }

    /*
     * Handles the creation of the fragment
     * @param inflater - the layout XML file used for corresponding View objects
     * @param container - the view object used to represent the fragment
     * @param savedInstanceState - bundle of data to be passed into the fragment
     * @return the activity view
     */
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        //creates view
        View view = inflater.inflate(R.layout.new_profile_screen, container, false);
        //fetches background colour from bundle
        backgroundColour = this.getArguments().getInt("backgroundColour");
        //attaches Relative layout from XML file and sets background colour
        final RelativeLayout fragmentLayout = ((RelativeLayout) view.findViewById(R.id.newProfileLayout));
        fragmentLayout.setBackgroundColor(backgroundColour);

        //attaches relevant buttons from XML file to setup onClick methods
        final FloatingActionButton saveProfileButton = (FloatingActionButton) view.findViewById(R.id.saveProfileButton);
        saveProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        saveProfileButtonClicked();
                    }
                }
        );

        final FloatingActionButton cancelProfileButton = (FloatingActionButton) view.findViewById(R.id.cancelProfileButton);
        cancelProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        cancelProfileButtonClicked();
                    }
                }
        );

        //fetches whether fragment is being used to edit or add a profile boolean
        editProfile = this.getArguments().getBoolean("editProfile");
        //if fragment is being used to edit a profile fetch the profile details from the bundle and attach them to rhe relevant components
        if (editProfile == true){
            profileID = this.getArguments().getInt("profileID");

            profileName = this.getArguments().getString("profileName");
            ((EditText)view.findViewById(R.id.profileNameEditText)).setText(profileName);

            timeFrame.stringToTime(this.getArguments().getString("profileTimeFrame"));
            ((TextView)view.findViewById(R.id.timeSelectText)).setText(timeFrame.toString());

            intervalFrequency.stringToTime(this.getArguments().getString("profileIntervalFrequency"));
            ((TextView)view.findViewById(R.id.intervalFrequencyDetailsText)).setText(intervalFrequency.toString());

            whiteNoiseLength.stringToTime(this.getArguments().getString("profileWhiteNoiseLength"));
            ((TextView)view.findViewById(R.id.whiteNoiseDetailsText)).setText(whiteNoiseLength.toString());

            readOutIntervals = this.getArguments().getBoolean("readOutIntervals");
            ((CheckBox)view.findViewById(R.id.checkBox)).setChecked(readOutIntervals);

            //creates a deletion button to allow user to delete the current profile
            final FloatingActionButton deleteProfileButton = new FloatingActionButton(getActivity());
            RelativeLayout.LayoutParams lpDelete = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpDelete.addRule(RelativeLayout.BELOW, R.id.readIntervalsCBText);
            lpDelete.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lpDelete.setMargins(0, 20, 0, 0);
            deleteProfileButton.setLayoutParams(lpDelete);
            deleteProfileButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
            deleteProfileButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{new Color().WHITE}));
            deleteProfileButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            deleteProfileButtonClicked();
                        }
                    }
            );

            fragmentLayout.addView(deleteProfileButton);

            //changes the layout of the cancel and save button to fit with the deletion button
            RelativeLayout.LayoutParams lpCancel = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpCancel.addRule(RelativeLayout.BELOW, R.id.readIntervalsCBText);
            lpCancel.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lpCancel.setMargins(120, 20, 0, 0);
            cancelProfileButton.setLayoutParams(lpCancel);

            RelativeLayout.LayoutParams lpSave = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpSave.addRule(RelativeLayout.BELOW, R.id.readIntervalsCBText);
            lpSave.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lpSave.setMargins(0, 20, 120, 0);
            saveProfileButton.setLayoutParams(lpSave);
        }

        //creates the text views to display the details of the relevant timer details with onClick methods
        final TextView timeFrameSelectTextButton = (TextView) view.findViewById(R.id.timeSelectText);
        timeFrameSelectTextButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        showTimerInputFragmentDialog("timeFrameInput", timeFrame.toString());
                    }
                }
        );

        final TextView intervalFrequencySelectTextButton = (TextView) view.findViewById(R.id.intervalFrequencyDetailsText);
        intervalFrequencySelectTextButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        showTimerInputFragmentDialog("intervalFrequencyInput", intervalFrequency.toString());
                    }
                }
        );

        final TextView WhiteNoiseLengthSelectTextButton = (TextView) view.findViewById(R.id.whiteNoiseDetailsText);
        WhiteNoiseLengthSelectTextButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        showTimerInputFragmentDialog("whiteNoiseLengthInput", whiteNoiseLength.toString());
                    }
                }
        );

        return  view;
    }

    /*
     * deletes the current profile being edited
     */
    public void deleteProfileButtonClicked(){
        ((MyProfilesFragment) getTargetFragment()).removeProfile(profileID);
    }

    /*
     * saves all of the current profile details and returns them to the MyProfilesFragment to be saved
     */
    public void saveProfileButtonClicked() {
        profileName = ((EditText) getView().findViewById(R.id.profileNameEditText)).getText().toString();
        timeFrame.stringToTime(((TextView) getView().findViewById(R.id.timeSelectText)).getText().toString());
        intervalFrequency.stringToTime(((TextView) getView().findViewById(R.id.intervalFrequencyDetailsText)).getText().toString());
        whiteNoiseLength.stringToTime(((TextView) getView().findViewById(R.id.whiteNoiseDetailsText)).getText().toString());
        readOutIntervals = ((CheckBox) getView().findViewById(R.id.checkBox)).isChecked();
        if (editProfile == false){
            //adds new profile
            ((MyProfilesFragment) getTargetFragment()).addProfile(new Profile(profileName, timeFrame, intervalFrequency, whiteNoiseLength, readOutIntervals));
        } else {
            //replaces profile being edited
            ((MyProfilesFragment) getTargetFragment()).replaceProfile(profileID, new Profile(profileName, timeFrame, intervalFrequency, whiteNoiseLength, readOutIntervals));

        }
    }

    /*
     * occurs when cancel dialog button pressed
     * closes the fragment
     */
    public void cancelProfileButtonClicked() {
        ((MyProfilesFragment)getTargetFragment()).cancelProfile();
    }

    /*
     * creates a new overlay on top of this fragment to allow the current profile timer details to be edited
     * @param timerInputType - states whether the timer input is being used for the timer length / interval frequency / white noise length
     * @param currentInput - passes the current details of the relevant timer
     */
    public void showTimerInputFragmentDialog(String timerInputType, String currentInput) {
        timerInputFragmentDialog = TimerInputFragment.newInstance(backgroundColour, timerInputType, currentInput);
        timerInputFragmentDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
        timerInputFragmentDialog.setTargetFragment(this, 0);
        timerInputFragmentDialog.show(getActivity().getFragmentManager(), "timerInputFragmentDialog");
    }

    /*
     * saves the current details of the timer input to relevant variable
     * @param displayTime - the current timerInputFragment time input
     * @param timerInputType - states whether the timer input is being used for the timer length / interval frequency / white noise length
     */
    public void saveTimerInputFragmentDialogDetails(Time displayTime, String timerInputType){
        //saves the timerInputFragment time input to the relevant TextView
        switch (timerInputType){
            case "timeFrameInput":
                timeFrame = displayTime;
                ((TextView) getView().findViewById(R.id.timeSelectText)).setText(displayTime.toString());
                timerInputFragmentDialog.dismiss();
                break;
            case "intervalFrequencyInput":
                intervalFrequency = displayTime;
                ((TextView) getView().findViewById(R.id.intervalFrequencyDetailsText)).setText(displayTime.toString());
                timerInputFragmentDialog.dismiss();
                break;
            case "whiteNoiseLengthInput":
                whiteNoiseLength = displayTime;
                ((TextView) getView().findViewById(R.id.whiteNoiseDetailsText)).setText(displayTime.toString());
                timerInputFragmentDialog.dismiss();
                break;
        }
    }

    /*
     * closes the timer input fragment dialog
     */
    public void closeTimerInputFramentDialog(){
        timerInputFragmentDialog.dismiss();
    }
}

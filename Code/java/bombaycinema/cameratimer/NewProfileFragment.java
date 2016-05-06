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


    static NewProfileFragment newInstance(int backgroundColour, boolean editProfile) {
        NewProfileFragment f = new NewProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("backgroundColour", backgroundColour);
        bundle.putBoolean("editProfile", editProfile);
        f.setArguments(bundle);
        return f;
    }

    static NewProfileFragment newInstance(int backgroundColour, boolean editProfile, int profileId, String profileName, String profileTimeFrame, String profileIntervalFrequency, String profileWhiteNoiseLength, Boolean readOutIntervals) {
        NewProfileFragment f = new NewProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("backgroundColour", backgroundColour);
        bundle.putBoolean("editProfile", editProfile);
        bundle.putInt("profileID", profileId);
        bundle.putString("profileName", profileName);
        bundle.putString("profileTimeFrame", profileTimeFrame);
        bundle.putString("profileIntervalFrequency", profileIntervalFrequency);
        bundle.putString("profileWhiteNoiseLength", profileWhiteNoiseLength);
        bundle.putBoolean("readOutIntervals", readOutIntervals);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_profile_screen, container, false);
        backgroundColour = this.getArguments().getInt("backgroundColour");
        final RelativeLayout fragmentLayout = ((RelativeLayout) view.findViewById(R.id.newProfileLayout));
        fragmentLayout.setBackgroundColor(backgroundColour);

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

        editProfile = this.getArguments().getBoolean("editProfile");
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

    public void deleteProfileButtonClicked(){
        ((MyProfilesFragment) getTargetFragment()).removeProfile(profileID);
    }

    public void saveProfileButtonClicked() {
        profileName = ((EditText) getView().findViewById(R.id.profileNameEditText)).getText().toString();
        timeFrame.stringToTime(((TextView) getView().findViewById(R.id.timeSelectText)).getText().toString());
        intervalFrequency.stringToTime(((TextView) getView().findViewById(R.id.intervalFrequencyDetailsText)).getText().toString());
        whiteNoiseLength.stringToTime(((TextView) getView().findViewById(R.id.whiteNoiseDetailsText)).getText().toString());
        readOutIntervals = ((CheckBox) getView().findViewById(R.id.checkBox)).isChecked();
        if (editProfile == false){
            ((MyProfilesFragment) getTargetFragment()).addProfile(new Profile(profileName, timeFrame, intervalFrequency, whiteNoiseLength, readOutIntervals));
        } else {
            ((MyProfilesFragment) getTargetFragment()).replaceProfile(profileID, new Profile(profileName, timeFrame, intervalFrequency, whiteNoiseLength, readOutIntervals));

        }
    }

    public void cancelProfileButtonClicked() {
        ((MyProfilesFragment)getTargetFragment()).cancelProfile();
    }


    public void showTimerInputFragmentDialog(String timerInputType, String currentInput) {
        timerInputFragmentDialog = TimerInputFragment.newInstance(backgroundColour, timerInputType, currentInput);
        timerInputFragmentDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
        timerInputFragmentDialog.setTargetFragment(this, 0);
        timerInputFragmentDialog.show(getActivity().getFragmentManager(), "timerInputFragmentDialog");
    }

    public void saveTimerInputFragmentDialogDetails(Time displayTime, String timerInputType){
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

    public void closeTimerInputFramentDialog(){
        timerInputFragmentDialog.dismiss();
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Time getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(Time timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Time getIntervalFrequency() {
        return intervalFrequency;
    }

    public void setIntervalFrequency(Time intervalFrequency) {
        this.intervalFrequency = intervalFrequency;
    }

    public Time getWhiteNoiseLength() {
        return whiteNoiseLength;
    }

    public void setWhiteNoiseLength(Time whiteNoiseLength) {
        this.whiteNoiseLength = whiteNoiseLength;
    }

    public boolean isReadOutInterval() {
        return readOutIntervals;
    }

    public void setReadsOutInterval(boolean readOutIntervals) {
        this.readOutIntervals = readOutIntervals;
    }
}

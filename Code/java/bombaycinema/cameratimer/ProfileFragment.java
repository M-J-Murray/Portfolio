package bombaycinema.cameratimer;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * Fragment to represent a simplified version of the profile
 * Shows details relating to the profile and enables the deletion or editing of the profile
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class ProfileFragment extends Fragment {
    private int profileID;
    private FloatingActionButton activateButton;
    private FloatingActionButton editButton;
    private TextView profileNameText;
    private TextView profileTimeFrameText;
    private TextView profileIntervalsText;
    private int backgroundColour;
    private boolean profileActivated = false;

    /*
     * Handles the creation of the fragment
     * @param inflater - the layout XML file used for corresponding View objects
     * @param container - the view object used to represent the fragment
     * @param savedInstanceState - bundle of data to be passed into the fragment
     * @return the activity view
     */
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        //creates the view
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        //fetches the background colour
        backgroundColour = this.getArguments().getInt("backgroundColour");
        //attaches the RelativeLayout of the fragment from the XML resources and assigns the background colour
        RelativeLayout profileLayout = (RelativeLayout) view.findViewById(R.id.profileLayout);
        profileLayout.setBackgroundColor(backgroundColour);

        //attaches the profiles ID
        profileID = this.getArguments().getInt("profileID");
        profileLayout.setId(profileID);

        //attaches profile name to the TextView associated with this fragment from the resources
        String profileName = this.getArguments().getString("profileName");
        profileNameText = (TextView) view.findViewById(R.id.profileNameDetails);
        profileNameText.setText(profileName);

        //attaches profile timer length to the TextView associated with this fragment from the resources
        String profileTimeFrame = this.getArguments().getString("profileTimeFrame");
        profileTimeFrameText = (TextView) view.findViewById(R.id.profileTimeFrameDetails);
        profileTimeFrameText.setText(profileTimeFrame);

        //attaches profile interval frequency to the TextView associated with this fragment from the resources
        String profileIntervals = this.getArguments().getString("profileIntervals");
        profileIntervalsText = (TextView) view.findViewById(R.id.profileIntervalDetails);
        profileIntervalsText.setText(profileIntervals);

        //attaches the activate button from the XML resources, sets the icon and onClick methods
        activateButton = (FloatingActionButton) view.findViewById(R.id.activateProfile);
        changeStopIcon();
        activateButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activateButtonClicked();
                    }
                }
        );
        //attaches the edit button from the XML resources, sets the icon and onClick methods
        editButton = (FloatingActionButton) view.findViewById(R.id.editProfile);
        editButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        editButtonClicked();
                    }
                }
        );

        return  view;
    }

    /*
     * method to process edit button clicks
     * displays edit profile fragment overlay
     */
    public void editButtonClicked(){
        ((MyProfilesFragment) getTargetFragment()).showEditProfileFragmentDialog(profileID);
    };

    /*
     * method to process activate button clicks
     * sets the current profile in the currentProfileFragment to this profile
     */
    public void activateButtonClicked(){
        if (profileActivated == false){
            profileActivated = true;
            ((MainScreen) getActivity()).setCurrentProfile(profileID);
            changeStartIcon();
        } else {
            profileActivated = false;
            ((MainScreen) getActivity()).resetCurrentProfile();
            changeStopIcon();
        }
    };

    /*
     * changes the fragments start button icon to a delete icon
     */
    public void changeStartIcon(){
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_delete);
        myFabSrc.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        activateButton.setImageDrawable(myFabSrc);
    }

    /*
     * changes the fragments delete button icon to a start icon
     */
    public void changeStopIcon(){
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_input_add);
        myFabSrc.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        activateButton.setImageDrawable(myFabSrc);
    }

    /*
     * deactivates the profile from being used in the Current Profile Fragment
     */
    public void deactivateProfile(){
        profileActivated = false;
        changeStopIcon();
    }
}

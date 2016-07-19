package bombaycinema.cameratimer;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Fragment to represent the profiles available to the user
 * Handles functionality related to selecting and editing user created profiles
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class MyProfilesFragment extends Fragment {
    //profiles fragment row count
    private int rowCount = 0;
    private int backgroundColour;
    private int backgroundColourDark;
    private NewProfileFragment newProfileFragmentDialog;
    private FloatingActionButton addProfileButton;
    private FragmentTransaction fragmentTransaction;
    private RelativeLayout profilesLayout;
    private ScrollView profilesScroll;

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
        View view = inflater.inflate(R.layout.my_profiles_fragment, container, false);

        //background colours to be used by the fragment
        backgroundColour = this.getArguments().getInt("backgroundColour");
        backgroundColourDark = this.getArguments().getInt("backgroundColourDark");

        //attaches the relative layout for the fragment
        profilesLayout = (RelativeLayout) view.findViewById(R.id.myProfilesFragmentView);

        //scroll view where all profile fragments are placed
        profilesScroll = (ScrollView) view.findViewById(R.id.scrollViewForProfiles);
        profilesScroll.setFadingEdgeLength(500);

        applyThemes(new int[]{backgroundColour, backgroundColourDark});

        //attaches add profile button XML component and sets relevant onClick methdods
        addProfileButton = (FloatingActionButton) view.findViewById(R.id.addProfileButton);
        addProfileButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        addProfileButton.setEnabled(false);
                        addProfileButtonClicked(v);
                    }
                }
        );
        return view;
    }

    /**
     * applies the current themes colours to all views in the fragment
     * and attaches colours to profile fragments
     * @params backgroundColour - the colours used for the theme
     */
    public void applyThemes(int[] backgroundColour){
        profilesLayout.setBackgroundColor(backgroundColour[0]);
        profilesScroll.setBackgroundColor(backgroundColour[0]);

        this.backgroundColour = backgroundColour[0];
        this.backgroundColourDark = backgroundColour[1];

        //attaches colours to profile fragments
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        for (int i = 0; i < rowCount; i++){
            ((ProfileFragment) fragmentManager.findFragmentByTag(i+"")).applyTheme(backgroundColourDark);
        }
    }

    /**
     * Runs on fragment creations to get all profiles loaded
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadProfiles();
    }

    /**
     * gets all profiles from the main activity and sets them up to be displayed
     */
    public void loadProfiles() {
        int profilesSize = ((MainScreen) getActivity()).getProfilesSize();
        if (profilesSize > 0) {
            for (int i = 0; i < profilesSize; i++) {
                addProfileFragment(((MainScreen) getActivity()).getProfile(i));
            }
        }
    }

    /**
     * method to be run on add profile button click
     * @param view - the view associated with this fragment
     */
    public void addProfileButtonClicked(View view) {
        showNewProfileFragmentDiaglog();
    }

    /**
     * creates a fragment overlay on top of activity to allow user to create a new profile
     */
    public void showNewProfileFragmentDiaglog() {
        //attaches bundle to state background colour and that fragment is being used to add a new profile
        Bundle bundle = new Bundle();
        bundle.putInt("backgroundColour", backgroundColourDark);
        bundle.putBoolean("editProfile", false);
        //creates new newProfile instance
        newProfileFragmentDialog = NewProfileFragment.newInstance(bundle);
        newProfileFragmentDialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme);
        newProfileFragmentDialog.setTargetFragment(this, 0);
        newProfileFragmentDialog.show(getActivity().getFragmentManager(), "newProfileFragmentDialog");
    }

    /**
     * creates a fragment overlay on top of activity to allow user to edit a selected profile
     * @param profileFragmentId - the id related to profile being edited
     */
    public void showEditProfileFragmentDialog(int profileFragmentId) {
        ((MainScreen) getActivity()).cancelCurrentProfile();

        //fetches profile being added
        Profile tempProfile = ((MainScreen) getActivity()).getProfile(profileFragmentId);
        //attaches bundle to state background colour and that fragment is being used to edit a profile with the profiles details
        Bundle bundle = new Bundle();
        bundle.putInt("backgroundColour", backgroundColourDark);
        bundle.putBoolean("editProfile", true);
        bundle.putInt("profileID", profileFragmentId);
        bundle.putString("profileName", tempProfile.getProfileName());
        bundle.putString("profileTimeFrame", tempProfile.getTimerLength().toString());
        bundle.putString("profileIntervalFrequency", tempProfile.getIntervalFrequency().toString());
        bundle.putString("profileWhiteNoiseLength", tempProfile.getWhiteNoiseLength().toString());
        bundle.putBoolean("readOutIntervals", tempProfile.isReadOutIntervals());
        //creates new newProfile instance
        newProfileFragmentDialog = NewProfileFragment.newInstance(bundle);
        newProfileFragmentDialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme);
        newProfileFragmentDialog.setTargetFragment(this, 0);
        newProfileFragmentDialog.show(getActivity().getFragmentManager(), "editProfileFragmentDialog");
    }

    /**
     * replaces a profile with a new profile
     * used for editing profiles
     * @param profileToBeReplaced - the id related to the profile being replaced
     * @param profile - the profile object replacing the original
     */
    public void replaceProfile(int profileToBeReplaced, Profile profile) {
        if (checkProfileValid(profile, profileToBeReplaced) == true) {
            ((MainScreen) getActivity()).replaceProfile(profileToBeReplaced, profile);
            editProfileFragment(profileToBeReplaced, profile);
            ((MainScreen) getActivity()).saveProfileData();
        }
    }

    /**
     * checks profile is valid and adds relevant fragment
     * @param profile - profile object to be added
     */
    public void addProfile(Profile profile) {
        if (checkProfileValid(profile, -1) == true) {
            addProfileFragment(profile);
        }
    }

    /**
     * Edits profile fragment details to represent the updated profiles details
     * @param fragmentID - the fragment id which represents the current profile being edited
     * @param profile - the profile which details are being used for fragment
     */
    public void editProfileFragment(int fragmentID, Profile profile) {
        View profileFragment = getView().findViewById(fragmentID);
        ((TextView) profileFragment.findViewById(R.id.profileNameDetails)).setText(profile.getProfileName());
        ((TextView) profileFragment.findViewById(R.id.profileTimeFrameDetails)).setText(profile.getTimerLength().toString());
        ((TextView) profileFragment.findViewById(R.id.profileIntervalDetails)).setText(profile.getIntervalFrequency().toString());
        newProfileFragmentDialog.dismiss();
    }

    /**
     * validation check to see if profile name is already in use
     * no two profiles can have the same name
     * @param profileName - the name of the new profile
     * @return the boolean stating whether the new name is acceptable or not
     */
    public boolean checkProfileNameMatch(String profileName, int orginalID) {
        for (int i = 0; i < ((MainScreen) getActivity()).getProfilesSize(); i++) {
            Profile tempProfile = ((MainScreen) getActivity()).getProfile(i);
            if (profileName.equals(tempProfile.getProfileName()) && orginalID != i){
                return true;
            }
        }
        return false;
    }

    /**
     * checks that the details of the new profile about to be added is valid
     * @param profile - the profile about to be added
     * @return the boolean stating whether the new profile is acceptable or not
     */
    public boolean checkProfileValid(Profile profile, int orginalID) {
        boolean profileValid = true;
        //contains list of errors which occur from the creation
        StringBuilder sb = new StringBuilder();
        //checks profile has a name
        if (profile.getProfileName().length() == 0) {
            sb.append("Please Enter A Profile Name \n \n");
            profileValid = false;
        }
        //checks that the profile name isnt already in use
        if (checkProfileNameMatch(profile.getProfileName(), orginalID) == true) {
            sb.append("Profile Name In Use \n \n");
            profileValid = false;
        }
        //checks that the timer is longer than 0 seconds
        if (profile.getTimerLength().toSeconds() == 0) {
            sb.append("Timer must have a length greater than 0 \n \n");
            profileValid = false;
        }
        //checks that the timer length is longer than the interval frequency
        if (profile.getTimerLength().toSeconds() < profile.getIntervalFrequency().toSeconds()) {
            sb.append("Interval Frequency Is Larger Than Timer Length \n");
            sb.append("Interval Will Never Trigger \n");
            profileValid = false;
        }
        //displays all errors if profile is not valid using an AlertDialod
        if (profileValid == false) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Inavlid Profile")
                    .setMessage(sb.toString())
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return profileValid;
    }

    /**
     * method used to creates a new profile fragment, display it relevantly on screen and add the new profile to the main list
     * @param profile - the new profile being added
     */
    public void addProfileFragment(Profile profile) {
        //attaches XML table layout to the relevant variable
        TableLayout profileTable = (TableLayout) getActivity().findViewById(R.id.profileTable);

        //creates a new row with relevant id and layout then adds it to the table layout
        TableRow row = new TableRow(getActivity());
        TableLayout.LayoutParams row_params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 0.1f);
        row.setId(rowCount + 1000);
        profileTable.addView(row, row_params);

        //creates a new profile fragment
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        //uses bundle to attach the relevant profile details to be assigned on creation
        Bundle tempBundle = new Bundle();
        tempBundle.putInt("profileID", rowCount);
        tempBundle.putString("profileName", profile.getProfileName());
        tempBundle.putString("profileTimeFrame", profile.getTimerLength().toString());
        tempBundle.putString("profileIntervals", profile.getIntervalFrequency().toString());
        tempBundle.putInt("backgroundColour", backgroundColourDark);
        profileFragment.setArguments(tempBundle);
        profileFragment.setTargetFragment(this, 0);
        //adds fragment to fragment list to be created and displayed
        fragmentTransaction.add(row.getId(), profileFragment, rowCount + "");
        fragmentTransaction.commit();

        rowCount++;

        //adds the new profile to the main list and closes the newProfile editor fragment
        if (newProfileFragmentDialog != null) {
            ((MainScreen) getActivity()).addProfile(profile);
            ((MainScreen) getActivity()).saveProfileData();
            newProfileFragmentDialog.dismiss();
        }
        //enables add profile button to allow user to create a new profile
        addProfileButton.setEnabled(true);
    }

    /**
     * closes the addProfile fragment editor
     */
    public void cancelProfile() {
        newProfileFragmentDialog.dismiss();
        addProfileButton.setEnabled(true);
    }

    /**
     * deletes the selected fragment and relevant profile
     * @param fragmentId - the id related to the fragment which is being deleted
     */
    public void removeProfile(int fragmentId) {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //attaches table layout from XML file layout
        TableLayout profileTable = (TableLayout) getActivity().findViewById(R.id.profileTable);
        TableRow row = (TableRow) getActivity().findViewById(rowCount - 1 + 1000);

        //loops through profile fragment list and updates their fragment tags
        //pushes selected fragment to the back of the list for easy removal
        for (int i = fragmentId; i < rowCount - 1; i++) {
            Profile tempProfile = ((MainScreen) getActivity()).getProfile(i + 1);
            ProfileFragment newProfileFragment = new ProfileFragment();
            Bundle tempBundle = new Bundle();
            tempBundle.putInt("profileID", i);
            tempBundle.putString("profileName", tempProfile.getProfileName());
            tempBundle.putString("profileTimeFrame", tempProfile.getTimerLength().toString());
            tempBundle.putString("profileIntervals", tempProfile.getIntervalFrequency().toString());
            tempBundle.putInt("backgroundColour", backgroundColourDark);
            newProfileFragment.setArguments(tempBundle);
            newProfileFragment.setTargetFragment(this, 0);

            fragmentTransaction.remove(fragmentManager.findFragmentByTag(i + ""));
            fragmentTransaction.add(i + 1000, newProfileFragment, i + "");
        }
        //removes selected fragment
        fragmentTransaction.remove(fragmentManager.findFragmentByTag((rowCount - 1) + ""));
        fragmentTransaction.commit();

        //removes relevant fragment row
        profileTable.removeView(row);
        ((MainScreen) getActivity()).removeProfile(fragmentId);

        rowCount--;

        //saves all profiles to file to update for removed profile
        ((MainScreen) getActivity()).saveProfileData();

        newProfileFragmentDialog.dismiss();
        addProfileButton.setEnabled(true);
    }
}

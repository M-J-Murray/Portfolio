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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyProfilesFragment extends Fragment {
    private int rowCount = 0;
    private int[] backgroundColours = new int[4];
    private NewProfileFragment newProfileFragmentDialog;
    private FloatingActionButton addProfileButton;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_profiles_fragment, container, false);

        backgroundColours[0] = this.getArguments().getInt("backgroundColour");
        backgroundColours[1] = this.getArguments().getInt("backgroundColourDark");
        backgroundColours[2] = this.getArguments().getInt("backgroundColourLight");
        backgroundColours[3] = this.getArguments().getInt("backgroundColourLighter");

        ScrollView scroll = (ScrollView) view.findViewById(R.id.scrollViewForProfiles);
        scroll.setFadingEdgeLength(150);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadProfiles();
    }

    public void loadProfiles() {
        int profilesSize = ((MainScreen) getActivity()).getProfilesSize();
        if (profilesSize > 0) {
            for (int i = 0; i < profilesSize; i++) {
                addProfileFragment(((MainScreen) getActivity()).getProfile(i));
            }
        }
    }

    public void addProfileButtonClicked(View view) {
        showNewProfileFragmentDiaglog();
    }

    public void showNewProfileFragmentDiaglog() {
        newProfileFragmentDialog = NewProfileFragment.newInstance(backgroundColours[0], false);
        newProfileFragmentDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
        newProfileFragmentDialog.setTargetFragment(this, 0);
        newProfileFragmentDialog.show(getActivity().getFragmentManager(), "newProfileFragmentDialog");
    }

    public void showEditProfileFragmentDialog(int profileFragmentId) {
        Profile tempProfile = ((MainScreen) getActivity()).getProfile(profileFragmentId);
        newProfileFragmentDialog = NewProfileFragment.newInstance(backgroundColours[0], true, profileFragmentId, tempProfile.getProfileName(), tempProfile.getTimerLength().toString(), tempProfile.getIntervalFrequency().toString(), tempProfile.getWhiteNoiseLength().toString(), tempProfile.isReadOutIntervals());
        newProfileFragmentDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme);
        newProfileFragmentDialog.setTargetFragment(this, 0);
        newProfileFragmentDialog.show(getActivity().getFragmentManager(), "editProfileFragmentDialog");
    }

    public void replaceProfile(int profileToBeReplaced, Profile profile) {
        if (checkProfileValid(profile) == true) {
            ((MainScreen) getActivity()).replaceProfile(profileToBeReplaced, profile);
            editProfileFragment(profileToBeReplaced, profile);
            ((MainScreen) getActivity()).saveProfileData();
        }
    }

    public void addProfile(Profile profile) {
        if (checkProfileValid(profile) == true) {
            addProfileFragment(profile);
        }
    }

    public void editProfileFragment(int fragmentID, Profile profile) {
        View profileFragment = getView().findViewById(fragmentID);
        ((TextView) profileFragment.findViewById(R.id.profileNameDetails)).setText(profile.getProfileName());
        ((TextView) profileFragment.findViewById(R.id.profileTimeFrameDetails)).setText(profile.getTimerLength().toString());
        ((TextView) profileFragment.findViewById(R.id.profileIntervalDetails)).setText(profile.getIntervalFrequency().toString());
        newProfileFragmentDialog.dismiss();
    }


    public boolean checkProfileNameMatch(String profileName) {
        for (int i = 0; i < ((MainScreen) getActivity()).getProfilesSize(); i++) {
            if (profileName == ((MainScreen) getActivity()).getProfile(i).getProfileName()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkProfileValid(Profile profile) {
        boolean profileValid = true;
        StringBuilder sb = new StringBuilder();
        if (profile.getProfileName().length() == 0) {
            sb.append("Please Enter A Profile Name \n \n");
            profileValid = false;
        }
        if (checkProfileNameMatch(profile.getProfileName()) == true) {
            sb.append("Profile Name In Use \n \n");
            profileValid = false;
        }
        if (profile.getTimerLength().toSeconds() < profile.getIntervalFrequency().toSeconds()) {
            sb.append("Interval Frequency Is Larger Than Timer Length \n");
            sb.append("Interval Will Never Trigger \n");
            profileValid = false;
        }
        if (profileValid == false) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Inavlid Profile")
                    .setMessage(sb.toString())
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getExitTransition();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return profileValid;
    }

    public void addProfileFragment(Profile profile) {
        TableLayout profileTable = (TableLayout) getActivity().findViewById(R.id.profileTable);

        TableRow row = new TableRow(getActivity());
        TableLayout.LayoutParams row_params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 0.1f);
        row.setId(rowCount + 1000);
        profileTable.addView(row, row_params);

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle tempBundle = new Bundle();
        tempBundle.putInt("profileID", rowCount);
        tempBundle.putString("profileName", profile.getProfileName());
        tempBundle.putString("profileTimeFrame", profile.getTimerLength().toString());
        tempBundle.putString("profileIntervals", profile.getIntervalFrequency().toString());
        tempBundle.putInt("backgroundColour", backgroundColours[3]);
        profileFragment.setArguments(tempBundle);
        profileFragment.setTargetFragment(this, 0);
        fragmentTransaction.add(row.getId(), profileFragment, rowCount + "");
        fragmentTransaction.commit();

        rowCount++;

        if (newProfileFragmentDialog != null) {
            ((MainScreen) getActivity()).addProfile(profile);
            ((MainScreen) getActivity()).saveProfileData();
            newProfileFragmentDialog.dismiss();
        }
        addProfileButton.setEnabled(true);
    }

    public void cancelProfile() {
        newProfileFragmentDialog.dismiss();
        addProfileButton.setEnabled(true);
    }

    public void removeProfile(int fragmentId) {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        TableLayout profileTable = (TableLayout) getActivity().findViewById(R.id.profileTable);
        TableRow row = (TableRow) getActivity().findViewById(rowCount - 1 + 1000);

        for (int i = fragmentId; i < rowCount - 1; i++) {
            Profile tempProfile = ((MainScreen) getActivity()).getProfile(i + 1);
            ProfileFragment newProfileFragment = new ProfileFragment();
            Bundle tempBundle = new Bundle();
            tempBundle.putInt("profileID", i);
            tempBundle.putString("profileName", tempProfile.getProfileName());
            tempBundle.putString("profileTimeFrame", tempProfile.getTimerLength().toString());
            tempBundle.putString("profileIntervals", tempProfile.getIntervalFrequency().toString());
            newProfileFragment.setArguments(tempBundle);
            newProfileFragment.setTargetFragment(this, 0);

            fragmentTransaction.remove(fragmentManager.findFragmentByTag(i + ""));
            fragmentTransaction.add(i + 1000, newProfileFragment, i + "");
        }
        fragmentTransaction.remove(fragmentManager.findFragmentByTag((rowCount - 1) + ""));
        fragmentTransaction.commit();

        profileTable.removeView(row);
        ((MainScreen) getActivity()).removeProfile(fragmentId);

        rowCount--;

        ((MainScreen) getActivity()).saveProfileData();

        newProfileFragmentDialog.dismiss();
        addProfileButton.setEnabled(true);
    }
}

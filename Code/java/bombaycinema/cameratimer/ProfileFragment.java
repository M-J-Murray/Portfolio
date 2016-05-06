package bombaycinema.cameratimer;


import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
    private int profileID;
    private FloatingActionButton activateButton;
    private FloatingActionButton editButton;
    private TextView profileNameText;
    private TextView profileTimeFrameText;
    private TextView profileIntervalsText;
    private int backgroundColour;
    private boolean profileActivated = false;

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        backgroundColour = this.getArguments().getInt("backgroundColour");

        RelativeLayout profileLayout = (RelativeLayout) view.findViewById(R.id.profileLayout);
        profileLayout.setBackgroundColor(backgroundColour);

        profileID = this.getArguments().getInt("profileID");
        profileLayout.setId(profileID);

        String profileName = this.getArguments().getString("profileName");
        profileNameText = (TextView) view.findViewById(R.id.profileNameDetails);
        profileNameText.setText(profileName);

        String profileTimeFrame = this.getArguments().getString("profileTimeFrame");
        profileTimeFrameText = (TextView) view.findViewById(R.id.profileTimeFrameDetails);
        profileTimeFrameText.setText(profileTimeFrame);

        String profileIntervals = this.getArguments().getString("profileIntervals");
        profileIntervalsText = (TextView) view.findViewById(R.id.profileIntervalDetails);
        profileIntervalsText.setText(profileIntervals);

        activateButton = (FloatingActionButton) view.findViewById(R.id.activateProfile);
        changeStopIcon();
        activateButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        activateButtonClicked();
                    }
                }
        );

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

    private int convertDPToPX(int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    public void editButtonClicked(){
        ((MyProfilesFragment) getTargetFragment()).showEditProfileFragmentDialog(profileID);
    };

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

    public void changeStartIcon(){
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_delete);
        myFabSrc.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        activateButton.setImageDrawable(myFabSrc);
    }

    public void changeStopIcon(){
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_input_add);
        myFabSrc.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        activateButton.setImageDrawable(myFabSrc);
    }

    public void deactivateProfile(){
        profileActivated = false;
        changeStopIcon();
    }
}

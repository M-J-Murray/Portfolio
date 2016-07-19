package bombaycinema.cameratimer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Fragment Dialog to represent the settings
 * Handles all the functionality of changing the settings
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class SettingsFragment extends DialogFragment {
    private int beepVolume;
    private int TTSVolume;
    private int toneVolume;
    private int themeColour;
    private int[] colourOptions = new int[9];
    private SeekBar beepVolumeBar;
    private SeekBar TTSVolumeBar;
    private SeekBar toneVolumeBar;

    /**
     * static method which occurs on instantiation of object
     * @param savedInstanceState - bundle of data to be passed into the fragment
     * @return this fragment
     */
    public static SettingsFragment newInstance(@Nullable Bundle savedInstanceState) {
        SettingsFragment f = new SettingsFragment();
        f.setArguments(savedInstanceState);
        return f;
    }

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
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        //hides notification bar
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //fetches and sets up all volume related settings
        beepVolume = (int) (getArguments().getFloat("beepVolume") * 10);
        beepVolumeBar = (SeekBar) view.findViewById(R.id.beepVolumeBar);
        beepVolumeBar.setMax(10);
        beepVolumeBar.setProgress(beepVolume);

        TTSVolume = getArguments().getInt("TTSVolume");
        TTSVolumeBar = (SeekBar) view.findViewById(R.id.TTSVolumeBar);
        TTSVolumeBar.setMax(15);
        TTSVolumeBar.setProgress(TTSVolume);

        toneVolume = (int) (getArguments().getFloat("toneVolume") * 10);
        toneVolumeBar = (SeekBar) view.findViewById(R.id.toneVolumeBar);
        toneVolumeBar.setMax(10);
        toneVolumeBar.setProgress(toneVolume);

        //selects and attaches themes colours to the relevant views
        themeColour = getArguments().getInt("currentTheme");
        TextView currentThemeView = (TextView) view.findViewById(R.id.currentThemeColour);
        currentThemeView.setBackgroundColor(themeColour);

        RelativeLayout settingsLayout = (RelativeLayout) view.findViewById(R.id.settingsInsideLayout);
        int r = Color.red(themeColour);
        int g = Color.green(themeColour);
        int b = Color.blue(themeColour);
        settingsLayout.setBackgroundColor(new Color().argb(255,r+40,g+40,b+40));

        //scroll view where all theme options are placed
        ScrollView scroll = (ScrollView) view.findViewById(R.id.scrollViewForThemes);
        scroll.setFadingEdgeLength(60);

        //sets up the colours available as themes
        setupThemes();

        //attaches the table layout being used for theme options from the XML resources
        TableLayout themeTable = (TableLayout) view.findViewById(R.id.themesTable);
        //creates a new row with relevant id, layout and colour then adds it to the table layout
        for (int i = 0; i<colourOptions.length; i++){
            TableRow row = new TableRow(getActivity());
            TableLayout.LayoutParams row_params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 0.1f);
            row_params.setMargins(0, 0, 0, 8);
            row.setId(i + 2000);
            row.setBackgroundColor(colourOptions[i]);
            row.setMinimumHeight(100);
            //attaches on click listener to allow user to select themes
            row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    colourThemeClicked(v);
                }
            });
            themeTable.addView(row, row_params);
        }

        //attaches add profile button XML component and sets relevant onClick methdods
        FloatingActionButton saveSettingsButton = (FloatingActionButton) view.findViewById(R.id.saveSettingsButton);
        saveSettingsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        saveSettingsButtonClicked();
                    }
                }
        );

        //attaches add profile button XML component and sets relevant onClick methdods
        FloatingActionButton cancelSettingsButton = (FloatingActionButton) view.findViewById(R.id.cancelSettingsButton);
        cancelSettingsButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        cancelSettingsButtonClicked();
                    }
                }
        );

        return view;
    }

    /**
     * runs upon dialog destruction
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelSettingsButtonClicked();
    }

    /**
     * runs when save settings button is pressed
     * saves all the current settings and applies them to the application
     */
    public void saveSettingsButtonClicked(){
        beepVolume = beepVolumeBar.getProgress();
        ((MainScreen) getActivity()).setBeepVolume((float) beepVolume/10);
        TTSVolume = TTSVolumeBar.getProgress();
        ((MainScreen) getActivity()).setTTSVolume(TTSVolume);
        toneVolume = toneVolumeBar.getProgress();
        ((MainScreen) getActivity()).setToneVolume((float) toneVolume/10);
        ((MainScreen) getActivity()).setupTheme(themeColour);
    };

    /**
     * runs when cancel settings button is clicked
     * closes the dialog without saving
     */
    public void cancelSettingsButtonClicked(){
        ((MainScreen) getActivity()).cancelSettingsScreen();
    };

    /**
     * runs when a row in the table layout is selected
     * identifies which row was selected then applies the correct colour to the current theme
     * @param v - the view which represents the table row
     */
    public void colourThemeClicked(View v){
        TextView currentColour = (TextView) getView().findViewById(R.id.currentThemeColour);
        switch (v.getId()){
            case 2000:
                themeColour = colourOptions[0];
                break;
            case 2001:
                themeColour = colourOptions[1];
                break;
            case 2002:
                themeColour = colourOptions[2];
                break;
            case 2003:
                themeColour = colourOptions[3];
                break;
            case 2004:
                themeColour = colourOptions[4];
                break;
            case 2005:
                themeColour = colourOptions[5];
                break;
            case 2006:
                themeColour = colourOptions[6];
                break;
            case 2007:
                themeColour = colourOptions[7];
                break;
            case 2008:
                themeColour = colourOptions[8];
                break;
        }
        currentColour.setBackgroundColor(themeColour);
    }

    /**
     * creates a list of colours which can be used as a theme
     */
    public void setupThemes(){
        colourOptions[0] = new Color().argb(255, 33, 33, 33);
        colourOptions[1] = new Color().argb(255, 150, 150, 150);
        colourOptions[2] = new Color().argb(255, 13, 102, 214);
        colourOptions[3] = new Color().argb(255, 51, 204, 51);
        colourOptions[4] = new Color().argb(255, 204, 0, 0);
        colourOptions[5] = new Color().argb(255, 0, 214, 214);
        colourOptions[6] = new Color().argb(255, 214, 214, 0);
        colourOptions[7] = new Color().argb(255, 214, 0, 214);
        colourOptions[8] = new Color().argb(255, 102, 0, 51);
    }
}

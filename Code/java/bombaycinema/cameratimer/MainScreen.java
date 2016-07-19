package bombaycinema.cameratimer;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * the main activity of the application
 * stores all the profiles and main fragments in tabs
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class MainScreen extends AppCompatActivity {
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private Window window;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter adapter;
    private MyProfilesFragment myProfilesFragment;
    private CurrentProfileFragment currentProfileFragment;
    private SettingsFragment settingsFragment;
    private int[] backgroundColours = new int[5];
    private int currentTheme = new Color().argb(255, 13, 13, 13);
    private float beepVolume = 0.5f;
    private int TTSVolume = 7;
    private float toneVolume = 0.5f;
    private int theme = R.style.AppTheme;

    /**
    * Handles the creation of the activity
    * @param savedInstanceState - bundle of data to be passed into the fragment
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //sets app full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        //loads saved profiles from a file
        loadProfileData();

        //sets up the background colours
        setupTheme(currentTheme);

        //creates a toolbar accorss the top of the screen and sets its colour
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(new Color().argb(255, 0, 0, 0));
        setSupportActionBar(toolbar);

        //creates a tab layout with a my profiles tab and a current profile tab and sets the background colour
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("My Profiles"));
        tabLayout.addTab(tabLayout.newTab().setText("Current Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //creates a viewPager and page adapter to display the tabs and use tab methods
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount(), backgroundColours);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //applies the themes to all fragments
        applyThemeToFragments();

        UiChangeListener();
    }

    public void UiChangeListener()
    {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentProfileFragment.cancelTimer();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Inflates the menu
     * adds items to the action bar if it is present
     * @return boolean stating successful inflation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    /**
     * Occurs when item on action bar is selected
     * @return boolean stating if settings option was selected or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Bundle bundle = new Bundle();
            bundle.putInt("currentTheme", currentTheme);
            bundle.putFloat("beepVolume", beepVolume);
            bundle.putInt("TTSVolume", TTSVolume);
            bundle.putFloat("toneVolume", toneVolume);
            settingsFragment = SettingsFragment.newInstance(bundle);
            settingsFragment.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme);
            settingsFragment.setTargetFragment(myProfilesFragment, 0);
            settingsFragment.show(getFragmentManager(), "settingsFragmentDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Closes the settings dialog
     */
    public void cancelSettingsScreen(){
        settingsFragment.dismiss();
    }

    /**
     * generates the background colours to be used by the app
     * @param the main colout being used for the theme
     */
    public void setupTheme(int newThemeColour) {
        currentTheme = newThemeColour;
        int r = Color.red(newThemeColour);
        int g = Color.green(newThemeColour);
        int b = Color.blue(newThemeColour);
        backgroundColours[0] = new Color().argb(255, r, g, b);
        backgroundColours[1] = new Color().argb(255, r + 10, g + 10, b + 10);
        backgroundColours[2] = new Color().argb(255, r + 20, g + 20, b + 20);
        backgroundColours[3] = new Color().argb(255, r + 30, g + 30, b + 30);

        if (settingsFragment != null){
            //saves settings to file and upates all fragment colours
            saveProfileData();
            applyThemeToFragments();
            settingsFragment.dismiss();
        }
    }

    /**
     * applies the theme colours to all other fragments on the application
     */
    public void applyThemeToFragments(){
        toolbar.setBackgroundColor(backgroundColours[0]);
        tabLayout.setBackgroundColor(backgroundColours[1]);

        if (myProfilesFragment != null){
            myProfilesFragment.applyThemes(new int[]{backgroundColours[3], backgroundColours[2]});
        }

        if (currentProfileFragment != null){
            currentProfileFragment.applyThemes(backgroundColours[3]);
        }
    }

    /**
     * sets the volume of the beep sound effect
     * @param beepVolume - the volume of the beep
     */
    public void setBeepVolume(float beepVolume) {
        this.beepVolume = beepVolume;
        currentProfileFragment.setBeepVolume(beepVolume);
    }

    /**
     * sets the volume of the TTS
     * @param beepVolume - the volume of the TTS
     */
    public void setTTSVolume(int TTSVolume) {
        this.TTSVolume = TTSVolume;
        currentProfileFragment.setTTSVolume(TTSVolume);
    }

    /**
     * sets the volume of the tone sound effect
     * @param beepVolume - the volume of the tone
     */
    public void setToneVolume(float toneVolume) {
        this.toneVolume = toneVolume;
        currentProfileFragment.setToneVolume(toneVolume);
    }

    /**
     * @return the volume of the beep sound effect
     */
    public float getBeepVolume(){
        return beepVolume;
    }

    /**
     * @return the volume of the TTS
     */
    public int getTTSVolume(){
        return TTSVolume;
    }

    /**
     * @return the volume of the tone sound effect
     */
    public float getToneVolume(){
        return toneVolume;
    }

    /**
     * adds a new profile to the profile list and updates IDs
     * @param profile - the profile object to be added
     */
    public void addProfile(Profile profile) {
        profiles.add(profile);
        updateProfileIDs();
    }

    /**
     * Assigns an ID to each profile which relates to their position within the profiles list
     */
    public void updateProfileIDs(){
        for (int x = 0; x < profiles.size(); x++){
            profiles.get(x).setProfileID(x);
        }
    }

    /**
     * fetches the relevant profile object from the profiles list based on the profiles position in the list
     * @param profileNumber - the profiles position in the profiles array
     */
    public Profile getProfile(int profileNumber) {
        return profiles.get(profileNumber);
    }

    /**
     * replaces a profile object within the profiles list and updates the IDs
     * @param profileID - the position of the profile being replaced in the list
     * @param profile - the profile object being added
     */
    public void replaceProfile(int profileID, Profile profile) {
        profiles.set(profileID, profile);
        updateProfileIDs();
    }

    /**
     * removes a selected profile from the profiles list and updates the IDs
     * @param profileID - the position of the profile in the list
     */
    public void removeProfile(int profileID) {
        profiles.remove(profileID);
        updateProfileIDs();
    }

    /**
     * @return the size of the profiles list
     */
    public int getProfilesSize() {
        return profiles.size();
    }

    /**
     * Sets the current profile being used by the application
     * @param profileID - the position of the profile in the profiles list
     */
    public void setCurrentProfile(int profileID) {
        //sets the current profile in the current Profile fragment
        currentProfileFragment.setCurrentProfile(profiles.get(profileID));
    }

    /**
     * plays/pauses the current profile timer
     */
    public void playPauseCurrentProfile(){
        currentProfileFragment.externalStartButtonClicked();
    }

    /**
     * updates the profile fragment relates to the current profiles icons and booleans
     * @param profile - the profile object which is being stopped
     */
    public void playPauseProfileFragment(Profile profile){
        //finds the fragment based on its tag and deactivates it
        ((ProfileFragment) getFragmentManager().findFragmentByTag(profile.getProfileID()+"")).externalPlayPauseButtonClicked();
    }

    /**
    * updates the current profile fragment icons and booleans
    * @param profile - the profile object which is being stopped
    */
    public void pauseProfileFragment(Profile profile){
        //finds the fragment based on its tag and deactivates it
        ((ProfileFragment) getFragmentManager().findFragmentByTag(profile.getProfileID()+"")).pauseButtonClicked();
    }

    /**
     * removes the current profile from the current timer
     */
    public void cancelCurrentProfile(){
        if (currentProfileFragment.getCurrentProfile() != null){
            currentProfileFragment.cancelProfileButtonClicked();
        }
    }

    /**
     * deactivates the current profile fragment
     * @param profile - the profile object which is being stopped
     */
    public void deactivateCurrentProfileFragment(){
        int profileID = currentProfileFragment.getCurrentProfile().getProfileID();
        //finds the fragment based on its tag and deactivates it
        ((ProfileFragment) getFragmentManager().findFragmentByTag(profileID+"")).deactivateProfile();
    }


    /**
     * saves the profiles list to a file
     */
    public void saveProfileData() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(getFilesDir() + "/profilesData", false);
            //adds the current user settings
            fileWriter.write(currentTheme + "\n");
            fileWriter.write(beepVolume + "\n");
            fileWriter.write(TTSVolume + "\n");
            fileWriter.write(toneVolume + "\n");
            //loops through all the profiles and overwrites all the profiles data to the file
            for (int i = 0; i < profiles.size(); i++) {
                fileWriter.write((profiles.get(i).getProfileName() + "\n"));
                fileWriter.write((profiles.get(i).getTimerLength() + "\n").toString());
                fileWriter.write((profiles.get(i).getIntervalFrequency() + "\n").toString());
                fileWriter.write((profiles.get(i).getWhiteNoiseLength() + "\n").toString());
                fileWriter.write((profiles.get(i).getReadOutIntervalsAsString() + "\n"));
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loads and creates a new profiles list based on a file
     */
    public void loadProfileData() {
        try {
            //sets the file location
            String yourFilePath = getFilesDir() + "/profilesData";

            //creates the file and the means to read from it
            File yourFile = new File(yourFilePath);
            FileInputStream fis = openFileInput(yourFile.getName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            //temporary variables used for parsing from the file
            String line;

            //sets up settings
            line = bufferedReader.readLine();
            currentTheme = Integer.parseInt(line);
            line = bufferedReader.readLine();
            beepVolume  = Float.parseFloat(line);
            line = bufferedReader.readLine();
            TTSVolume  = Integer.parseInt(line);
            line = bufferedReader.readLine();
            toneVolume  = Float.parseFloat(line);

            int lineNumber = 0;
            String profileName = null;
            Time timerLength = new Time(0, 0, 0);
            Time intervalFrequency = new Time(0, 0, 0);
            Time whiteNoiseLength = new Time(0, 0, 0);
            boolean readOutIntervals = false;
            //reads every line from file until it is at the end
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                //sets the relevant variable based on the line number
                //line number stays within bounds of 1-5 as there are 5 variables for each profile
                switch (lineNumber) {
                    case 1:
                        profileName = line;
                        break;
                    case 2:
                        timerLength.stringToTime(line);
                        break;
                    case 3:
                        intervalFrequency.stringToTime(line);
                        break;
                    case 4:
                        whiteNoiseLength.stringToTime(line);
                        break;
                    case 5:
                        if (line.equals("true")){
                            readOutIntervals = true;
                        } else {
                            readOutIntervals = false;
                        }
                        //at last variable creates a new profile and adds it to the list
                        addProfile(new Profile(profileName, timerLength, intervalFrequency, whiteNoiseLength, readOutIntervals));
                        profileName = null;
                        timerLength = new Time(0, 0, 0);
                        intervalFrequency = new Time(0, 0, 0);
                        whiteNoiseLength = new Time(0, 0, 0);
                        readOutIntervals = false;
                        lineNumber = 0;
                        break;
                }
            }
        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
    }

    /**
     * nested Class used to process the functionality of the tabs
     */
    private class PageAdapter extends FragmentStatePagerAdapter {
        private int mNumOfTabs;
        private int[] backgroundColours;

        /**
         * constructor for the page adapter object
         * @param fm - the fragment manager used for processing the tab fragments
         * @param numOfTabs - the total number of tabs in use
         * @param backgroundColours - the colours used for the fragment colour schemes
         */
        public PageAdapter(FragmentManager fm, int numOfTabs, int[] backgroundColours) {
            super(fm);
            this.backgroundColours = backgroundColours;
            this.mNumOfTabs = numOfTabs;
        }

        /**
         * fetches the fragment for the relevant tab
         * @param position - the position of the tab
         * @return the relevant fragment being selected
         */
        @Override
        public Fragment getItem(int position) {
            //creates a bundle for the background colours
            Bundle bundle = new Bundle();
            bundle.putInt("backgroundColour", backgroundColours[3]);
            bundle.putInt("backgroundColourDark", backgroundColours[2]);
            //creates the fragment for the relevant position
            switch (position) {
                case 0:
                    myProfilesFragment = new MyProfilesFragment();
                    myProfilesFragment.setArguments(bundle);
                    return myProfilesFragment;
                case 1:
                    currentProfileFragment = new CurrentProfileFragment();
                    currentProfileFragment.setArguments(bundle);
                    return currentProfileFragment;
                default:
                    return null;
            }
        }

        /**
         * @return the number of tabs in use
         */
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}


package bombaycinema.cameratimer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

/*
 * the main activity of the application
 * stores all the profiles and main fragments in tabs
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class MainScreen extends AppCompatActivity {
    private int r, g, b;
    private int[] backgroundColours = new int[4];
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private MyProfilesFragment myProfilesFragment;
    private CurrentProfileFragment currentProfileFragment;

    /*
    * Handles the creation of the activity
    * @param savedInstanceState - bundle of data to be passed into the fragment
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        //sets up the background colours
        setupColours();

        //loads saved profiles from a file
        loadProfileData();

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(backgroundColours[1]);
        }

        //creates a toolbar accorss the top of the screen and sets its colour
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(backgroundColours[0]);
        setSupportActionBar(toolbar);

        //creates a tab layout with a my profiles tab and a current profile tab and sets the background colour
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("My Profiles"));
        tabLayout.addTab(tabLayout.newTab().setText("Current Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(backgroundColours[0]);

        //creates a viewPager and page adapter to display the tabs and use tab methods
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setBackgroundColor(backgroundColours[2]);
        final PageAdapter adapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount(), backgroundColours);
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
    }

    /*
     * Inflates the menu
     * adds items to the action bar if it is present
     * @return boolean stating successful inflation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    /*
     * Occurs when item on action bar is selected
     * @return boolean stating if settings option was selected or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * generates the background colours to be used by the app
     */
    public void setupColours() {
        r = 50;
        g = 50;
        b = 150;
        backgroundColours[0] = new Color().argb(255, r, g, b);
        backgroundColours[1] = new Color().argb(255, r - 20, g - 20, b - 20);
        backgroundColours[2] = new Color().argb(255, r + 20, g + 20, b + 20);
        backgroundColours[3] = new Color().argb(255, r + 40, g + 40, b + 40);
    }

    /*
     * adds a new profile to the profile list
     * @param profile - the profile object to be added
     */
    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    /*
     * fetches the relevant profile object from the profiles list based on the profiles position in the list
     * @param profileNumber - the profiles position in the profiles array
     */
    public Profile getProfile(int profileNumber) {
        return profiles.get(profileNumber);
    }

    /*
     * replaces a profile object within the profiles list
     * @param profileID - the position of the profile being replaced in the list
     * @param profile - the profile object being added
     */
    public void replaceProfile(int profileID, Profile profile) {
        profiles.set(profileID, profile);
    }

    /*
     * removes a selected profile from the profiles list
     * @param profileID - the position of the profile in the list
     */
    public void removeProfile(int profileID) {
        profiles.remove(profileID);
    }

    /*
     * @return the size of the profiles list
     */
    public int getProfilesSize() {
        return profiles.size();
    }

    /*
     * Sets the current profile being used by the application
     * @param profileID - the position of the profile in the profiles list
     */
    public void setCurrentProfile(int profileID) {
        //if a profile is already in use it searches through the profile fragments list and deactivates it
        if (currentProfileFragment.getCurrentProfile() != null){
            //gets current profile in use
            Profile tempProfile = currentProfileFragment.getCurrentProfile();
            for (int i = 0; i<profiles.size(); i++) {
                if (profiles.get(i) == tempProfile){
                    //finds the fragment based on its fragment ID
                    ((ProfileFragment) getFragmentManager().findFragmentByTag(i+"")).deactivateProfile();
                }
            }
        }
        //sets the current profile in the current Profile fragment
        currentProfileFragment.setCurrentProfile(profiles.get(profileID));

    }

    /*
     * resets the current profiles details back to its default positions
     */
    public void resetCurrentProfile(){
        currentProfileFragment.resetCurrentProfile();
    }

    /*
     * stops the current profiles progress
     * @param profile - the profile object which is being stopped
     */
    public void stopCurrentProfile(Profile profile){
        for (int i = 0; i<profiles.size(); i++) {
            if (profiles.get(i) == profile){
                //finds the fragment based on its tag and deactivates it
                ((ProfileFragment) getFragmentManager().findFragmentByTag(i+"")).deactivateProfile();
            }
        }
    }


    /*
     * saves the profiles list to a file
     */
    public void saveProfileData() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(getFilesDir() + "/profilesData", false);
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

    /*
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
            int lineNumber = 0;
            String profileName = null;
            Time timerLength = new Time(0, 0, 0);
            Time intervalFrequency = new Time(0, 0, 0);
            Time whiteNoiseLength = new Time(0, 0, 0);
            boolean readOutIntervals = false;
            int size = 0;
            //reads every line from file until it is at the end
            while ((line = bufferedReader.readLine()) != null) {
                size++;
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
                        if (line == "true") {
                            readOutIntervals = true;
                        } else {
                            readOutIntervals = false;
                        }
                        //at last variable creates a new profile and adds it to the list
                        profiles.add(new Profile(profileName, timerLength, intervalFrequency, whiteNoiseLength, readOutIntervals));
                        lineNumber = 0;
                        break;
                }
            }
        } catch (FileNotFoundException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
    }

    /*
     * nested Class used to process the functionality of the tabs
     */
    private class PageAdapter extends FragmentStatePagerAdapter {
        private int mNumOfTabs;
        private int[] backgroundColours;

        /*
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

        /*
         * fetches the fragment for the relevant tab
         * @param position - the position of the tab
         * @return the relevant fragment being selected
         */
        @Override
        public Fragment getItem(int position) {
            //creates a bundle for the background colours
            Bundle bundle = new Bundle();
            bundle.putInt("backgroundColour", backgroundColours[0]);
            bundle.putInt("backgroundColourDark", backgroundColours[1]);
            bundle.putInt("backgroundColourLight", backgroundColours[2]);
            bundle.putInt("backgroundColourLighter", backgroundColours[3]);
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

        /*
         * @return the number of tabs in use
         */
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}


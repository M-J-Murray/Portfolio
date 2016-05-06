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


public class MainScreen extends AppCompatActivity {
    private int r, g, b;
    private int[] backgroundColours = new int[4];
    private ArrayList<Profile> profiles = new ArrayList<Profile>();
    private MyProfilesFragment myProfilesFragment;
    private CurrentProfileFragment currentProfileFragment;

    public void setupColours() {
        r = 50;
        g = 50;
        b = 150;
        backgroundColours[0] = new Color().argb(255, r, g, b);
        backgroundColours[1] = new Color().argb(255, r - 20, g - 20, b - 20);
        backgroundColours[2] = new Color().argb(255, r + 20, g + 20, b + 20);
        backgroundColours[3] = new Color().argb(255, r + 40, g + 40, b + 40);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setupColours();

        loadProfileData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(backgroundColours[1]);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(backgroundColours[0]);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("My Profiles"));
        tabLayout.addTab(tabLayout.newTab().setText("Current Profile"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(backgroundColours[0]);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    public Profile getProfile(int profileNumber) {
        return profiles.get(profileNumber);
    }

    public void replaceProfile(int profileID, Profile profile) {
        profiles.set(profileID, profile);
    }

    public void removeProfile(int profile) {
        profiles.remove(profile);
    }

    public int getProfilesSize() {
        return profiles.size();
    }

    public void setCurrentProfile(int profileID) {
        if (currentProfileFragment.getCurrentProfile() != null){
            Profile tempProfile = currentProfileFragment.getCurrentProfile();
            for (int i = 0; i<profiles.size(); i++) {
                if (profiles.get(i) == tempProfile){
                    ((ProfileFragment) getFragmentManager().findFragmentByTag(i+"")).deactivateProfile();
                }
            }
        }
        currentProfileFragment.setCurrentProfile(profiles.get(profileID));

    }

    public void resetCurrentProfile(){
        currentProfileFragment.resetCurrentProfile();
    }

    public void stopCurrentProfile(Profile profile){
        for (int i = 0; i<profiles.size(); i++) {
            if (profiles.get(i) == profile){
                ((ProfileFragment) getFragmentManager().findFragmentByTag(i+"")).deactivateProfile();
            }
        }
    }


    public void saveProfileData() {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(getFilesDir() + "/profilesData", false);
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

    public void loadProfileData() {
        try {
            String yourFilePath = getFilesDir() + "/profilesData";

            File yourFile = new File(yourFilePath);
            FileInputStream fis = openFileInput(yourFile.getName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            int lineNumber = 0;
            String profileName = null;
            Time timerLength = new Time(0, 0, 0);
            Time intervalFrequency = new Time(0, 0, 0);
            Time whiteNoiseLength = new Time(0, 0, 0);
            boolean readOutIntervals = false;
            int size = 0;
            while ((line = bufferedReader.readLine()) != null) {
                size++;
                lineNumber++;
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

    private class PageAdapter extends FragmentStatePagerAdapter {
        private int mNumOfTabs;
        private int[] backgroundColours;

        public PageAdapter(FragmentManager fm, int NumOfTabs, int[] backgroundColours) {
            super(fm);
            this.backgroundColours = backgroundColours;
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("backgroundColour", backgroundColours[0]);
            bundle.putInt("backgroundColourDark", backgroundColours[1]);
            bundle.putInt("backgroundColourLight", backgroundColours[2]);
            bundle.putInt("backgroundColourLighter", backgroundColours[3]);
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

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}


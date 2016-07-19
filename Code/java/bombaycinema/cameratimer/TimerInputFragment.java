package bombaycinema.cameratimer;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Stack;

/**
 * Fragment Dialog to process the input of time values
 * @author Michael Murray
 * @version v2.0, 27/06/2016
 */
public class TimerInputFragment extends DialogFragment {
    private TextView inputTextView;
    private int backgroundColour;
    private Stack<Integer> displayStack = new Stack<Integer>();
    private Time displayTime = new Time(0,0,0);
    private String timerInputType;

    /**
     * static method which occurs on instantiation of object
     * @param backgroundColour - the int value of the colour of the background
     * @param timerInputType - states whether the timer input is being used for the timer length / interval frequency / white noise length
     * @param currentInput - passes the current details of the time
     * @return this fragment
     */
    public static TimerInputFragment newInstance(int backgroundColour, String timerInputType, String currentInput) {
        TimerInputFragment f = new TimerInputFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("backgroundColour", backgroundColour);
        bundle.putString("timerInputType", timerInputType);
        bundle.putString("currentInput", currentInput);
        f.setArguments(bundle);
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
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        //creates view
        View view = inflater.inflate(R.layout.timer_input_fragment, container, false);

        //hides notification bar
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //attaches fragment layout and sets background colour
        backgroundColour = this.getArguments().getInt("backgroundColour");
        ((RelativeLayout) view.findViewById(R.id.timerInputLayout)).setBackgroundColor(backgroundColour);

        //fetches timer input type
        timerInputType = this.getArguments().getString("timerInputType");
        //attaches textView from XML resources
        inputTextView = (TextView)view.findViewById(R.id.timerInputText);
        //fetches current time of input and updates display
        displayTime.stringToTime(this.getArguments().getString("currentInput"));
        displayStack = displayTime.getAsStack();
        displayTime = null;
        updateDisplay();

        //attaches number input button text views and assigns relevant onClick methods
        final TextView key0InputButton = (TextView) view.findViewById(R.id.key0Button);
        key0InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(0);
                    }
                }
        );

        final TextView key1InputButton = (TextView) view.findViewById(R.id.key1Button);
        key1InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(1);
                    }
                }
        );

        final TextView key2InputButton = (TextView) view.findViewById(R.id.key2Button);
        key2InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(2);
                    }
                }
        );

        final TextView key3InputButton = (TextView) view.findViewById(R.id.key3Button);
        key3InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(3);
                    }
                }
        );

        final TextView key4InputButton = (TextView) view.findViewById(R.id.key4Button);
        key4InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(4);
                    }
                }
        );

        final TextView key5InputButton = (TextView) view.findViewById(R.id.key5Button);
        key5InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(5);
                    }
                }
        );

        final TextView key6InputButton = (TextView) view.findViewById(R.id.key6Button);
        key6InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(6);
                    }
                }
        );

        final TextView key7InputButton = (TextView) view.findViewById(R.id.key7Button);
        key7InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(7);
                    }
                }
        );

        final TextView key8InputButton = (TextView) view.findViewById(R.id.key8Button);
        key8InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(8);
                    }
                }
        );

        final TextView key9InputButton = (TextView) view.findViewById(R.id.key9Button);
        key9InputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        updateStack(9);
                    }
                }
        );

        final ImageView cancelInputButton = (ImageView) view.findViewById(R.id.keyCancelButton);
        cancelInputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        ((NewProfileFragment) getTargetFragment()).closeTimerInputFramentDialog();
                    }
                }
        );

        final ImageView saveInputButton = (ImageView) view.findViewById(R.id.keySaveButton);
        saveInputButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        int[] currentTime = displayStackToTime();
                        displayTime = new Time(currentTime[0], currentTime[1], currentTime[2]);
                        ((NewProfileFragment) getTargetFragment()).saveTimerInputFragmentDialogDetails(displayTime, timerInputType);
                    }
                }
        );

        final ImageView keyBackButton = (ImageView) view.findViewById(R.id.keyBackButton);
        keyBackButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (displayStack.size() != 0) {
                            displayStack.pop();
                            updateDisplay();
                        }
                    }
                }
        );

        return  view;
    }

    /**
     * adds a new number to the timer input stack
     * @param keyPressed - the integer related to the key pressed
     */
    public void updateStack(int keyPressed){
        //0 key cannot be pressed first
        if ((keyPressed == 0) && (displayStack.size() == 0)) {
        } else {
            //stack size must stay below 6
            if (displayStack.size() < 6) {
                displayStack.push(keyPressed);
                updateDisplay();
            }
        }
    }

    /**
     * converts the display stack into a list of integers to represent seconds, minutes and hours
     * @return the list of integers
     */
    public int[] displayStackToTime(){
        String temp = "";
        for (int i = 0; i < 6-displayStack.size(); i++) {
            temp += "0";
        }
        for (int i = 0; i < displayStack.size(); i++) {
            temp += displayStack.get(i);
        }
        int hours = Integer.parseInt(temp.substring(0, 2));
        int minutes = Integer.parseInt(temp.substring(2, 4));
        int seconds = Integer.parseInt(temp.substring(4, 6));
        return new int[] {hours, minutes, seconds};
    }

    /**
     * converts the list of integers representing the stack time into a string to be displayed
     */
    public void updateDisplay(){
        int[] currentTime = displayStackToTime();
        inputTextView.setText(Time.getAsDoubleDigit(currentTime[0])+"h "+Time.getAsDoubleDigit(currentTime[1])+"m "+Time.getAsDoubleDigit(currentTime[2])+"s");
    }


}

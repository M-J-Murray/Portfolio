package bombaycinema.cameratimer;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Stack;

public class TimerInputFragment extends DialogFragment {
    private TextView inputTextView;
    private int backgroundColour;
    private Stack<Integer> displayStack = new Stack<Integer>();
    private Time displayTime = new Time(0,0,0);
    private String timerInputType;

    static TimerInputFragment newInstance(int backgroundColour, String timerInputType, String currentInput) {
        TimerInputFragment f = new TimerInputFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("backgroundColour", backgroundColour);
        bundle.putString("timerInputType", timerInputType);
        bundle.putString("currentInput", currentInput);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_input_fragment, container, false);

        backgroundColour = this.getArguments().getInt("backgroundColour");
        ((RelativeLayout) view.findViewById(R.id.timerInputLayout)).setBackgroundColor(backgroundColour);

        timerInputType = this.getArguments().getString("timerInputType");

        inputTextView = (TextView)view.findViewById(R.id.timerInputText);

        displayTime.stringToTime(this.getArguments().getString("currentInput"));
        displayStack = displayTime.getAsStack();
        displayTime = null;
        updateDisplay(false);

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
                        updateDisplay(true);
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
                            updateDisplay(false);
                        }
                    }
                }
        );

        return  view;
    }


    public void updateStack(int keyPressed){
        if ((keyPressed == 0) && (displayStack.size() == 0)) {
        } else {
            if (displayStack.size() < 6) {
                displayStack.push(keyPressed);
                updateDisplay(false);
            }
        }
    }

    public void updateDisplay(boolean updateTime){
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
        if(updateTime == true) {
            displayTime = new Time(hours, minutes, seconds);
        }
        inputTextView.setText(Time.getAsDoubleDigit(hours)+"h "+Time.getAsDoubleDigit(minutes)+"m "+Time.getAsDoubleDigit(seconds)+"s");
    }


}

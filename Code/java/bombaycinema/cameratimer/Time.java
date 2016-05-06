package bombaycinema.cameratimer;

import java.util.Stack;

public class Time {
    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    public Time(int hours, int minutes, int seconds){
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        if (this.seconds >= 60){
            this.seconds -= 60;
            this.minutes++;
        }
        if (this.minutes >= 60){
            this.minutes -= 60;
            if (this.hours <= 98){
                this.hours++;
            }
        }

    }

    public Stack<Integer> getAsStack(){
        boolean addToStack = false;
        Stack<Integer> tempStack = new Stack<Integer>();
        String tempString = getAsDoubleDigit(hours) + getAsDoubleDigit(minutes) + getAsDoubleDigit(seconds);
        for (int i = 0; i < 6; i++) {
            if (tempString.charAt(i) != '0'){
                addToStack = true;
            }
            if (addToStack == true){
                tempStack.add(Integer.parseInt(Character.toString(tempString.charAt(i))));
            }
        }
        return tempStack;
    }

    public void secondsToTime(int seconds){
        resetTime();
        int tempSeconds = seconds;
        while (tempSeconds >= 60){
            if (tempSeconds >= 60){
                minutes++;
                tempSeconds -= 60;
            }
            if (minutes >= 60){
                hours++;
                minutes = 0;
            }
        }
        this.seconds = tempSeconds;
    }

    public void incrementTime(){
        seconds++;
        if (seconds >= 60){
            minutes++;
            seconds = 0;
        }
        if (minutes >= 60){
            hours++;
            minutes = 0;
        }
    }

    public void resetTime(){
        hours = 0;
        minutes = 0;
        seconds = 0;
    }

    public String toString(){
        String tempString = getAsDoubleDigit(hours)+"h "+getAsDoubleDigit(minutes)+"m "+getAsDoubleDigit(seconds)+"s";
        return tempString;
    }

    public int toSeconds(){
        return (60*60*hours)+(minutes*60)+seconds;
    }

    public void stringToTime(String time){
        hours = Integer.parseInt(time.substring(0, 2));
        minutes = Integer.parseInt(time.substring(4, 6));
        seconds = Integer.parseInt(time.substring(8, 10));
    }

    public static String getAsDoubleDigit(int number){
        String tempString = "";
        if ((number+"").length() <= 1){
            tempString += "0"+number;
        } else {
            tempString += number+"";
        }
        return tempString;
    }
}

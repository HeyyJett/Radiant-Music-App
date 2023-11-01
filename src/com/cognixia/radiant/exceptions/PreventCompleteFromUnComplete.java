package com.cognixia.radiant.exceptions;

public class PreventCompleteFromUnComplete extends RuntimeException {

    public PreventCompleteFromUnComplete(String CurrentStatus, String TargetStatus){
        super("You cannot move Music Category from " + CurrentStatus + " to " + TargetStatus + ".");
    }


}

package org.axtin.modules.messaging;

import org.axtin.command.CommandErrors;

/**
 * Created by Matthew on 26/03/2017.
 */
public class NoReplyException extends Exception{

    private String errorMessage;

    public NoReplyException(CommandErrors error){
        this.errorMessage = error.toString();
    }

    public String getError(){
        return this.errorMessage;
    }
}


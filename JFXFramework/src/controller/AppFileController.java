package controller;

import apptemplate.AppTemplate;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.File;


/**
 * This class provides the event programmed responses for the file controls
 * that are provided by this framework.
 *
 * @author Richard McKenna, Ritwik Banerjee
 */
public class AppFileController implements FileController {

    public AppTemplate           appTemplate;     // reference to the application
    public SimpleBooleanProperty saved;           // whether or not changes have been saved
    public File                  currentWorkFile; // the file on which currently work is being done

    /**
     * Constructor to just store the reference to the application.
     *
     * @param appTemplate The application within which this controller will provide file toolbar responses.
     */
    public AppFileController(AppTemplate appTemplate) {
        this.saved = new SimpleBooleanProperty(true);
        this.appTemplate = appTemplate;
    }


    public void handleExitRequest() {
    }


    public boolean handleLoginRequest(){
        // do nothing
        return false;
    }

    public boolean handleCreateProfileRequest(){
        return false;
    }

    public void handleLogoutButton(){};

    public void handleRestartGame(){};

    public void handleNextLevel(){};
}

package controller;


import java.io.IOException;

/**
 * @author BG
 */
public interface FileController {
    void handleExitRequest();
    boolean handleLoginRequest() throws IOException;
    boolean handleCreateProfileRequest() throws IOException;
    void handleLogoutButton();
    void handleRestartGame();
    void handleNextLevel();
}

package ui;

import apptemplate.AppTemplate;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import components.AppStyleArbiter;
import controller.FileController;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import propertymanager.PropertyManager;
import security.SecurityManager;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import static settings.AppPropertyType.*;
import static settings.InitializationParameters.APP_IMAGEDIR_PATH;
import static settings.InitializationParameters.APP_WORKDIR_PATH;

/**
 * This class provides the basic user interface for this application, including all the file controls, but it does not
 * include the workspace, which should be customizable and application dependent.
 *
 * @author Biki Gurung, Richard McKenna, Ritwik Banerjee
 */
public class AppGUI implements AppStyleArbiter {

    protected FileController fileController;   // to react to file-related controls
    protected Stage          primaryStage;     // the application window
    protected Scene          primaryScene;     // the scene graph
    protected Stage          LoginStage;
    protected Stage          CreateProfileStage;
    protected BorderPane     appPane;          // the root node in the scene graph, to organize the containers
    protected FlowPane       toolbarPane;      // the top toolbar
    protected Button         newButton;        // button to create a new instance of the application
    protected Button         saveButton;       // button to save progress on application
    protected Button         loadButton;       // button to load a saved game from (json) file
    protected Button         exitButton;       // button to exit application
    protected String         applicationTitle; // the application title
    protected Button         hintButton;
    protected Label          headingLabel;
    protected BorderPane     titleBar;
    protected BorderPane     startPane;
    protected Button createProfileButton;
    protected Button LoginProfileButton;
    protected String loginUsername;
    protected String loginPassword;
    protected Button userGameMode;
    protected Button homeButton;
    protected FlowPane userProfilePane;
    protected BorderPane userGamePlayPane;
    protected FlowPane levelUserProfile;
    protected BorderPane levelSelectionPane;
    protected String userNameRequest;
    protected String userPasswordRequest;
    protected String gameString;
    Button logoutButton;
    Scene levelSelectionScene;
    Scene gameplayScene;
    protected SecurityManager securityManager;
    Button nextLevelButton;

    final KeyCombination helpComb = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN);
    final KeyCombination logOut = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
    final KeyCombination proSet = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);





    private int appWindowWidth;  // optional parameter for window width that can be set by the application
    private int appWindowHeight; // optional parameter for window height that can be set by the application

    /**
     * This constructor initializes the file toolbar for use.
     *
     * @param initPrimaryStage The window for this application.
     * @param initAppTitle     The title of this application, which
     *                         will appear in the window bar.
     * @param app              The app within this gui is used.
     */
    public AppGUI(Stage initPrimaryStage, String initAppTitle, AppTemplate app) throws IOException, InstantiationException, NoSuchAlgorithmException {
        this(initPrimaryStage, initAppTitle, app, -1, -1);
    }

    public AppGUI(Stage primaryStage, String applicationTitle, AppTemplate appTemplate, int appWindowWidth, int appWindowHeight) throws IOException, InstantiationException, NoSuchAlgorithmException {
        this.appWindowWidth = appWindowWidth;
        this.appWindowHeight = appWindowHeight;
        this.primaryStage = primaryStage;
        this.applicationTitle = applicationTitle;
        securityManager= new SecurityManager();
        initializeHeading();
        initializeToolbar();                    // initialize the top toolbar
        initializeToolbarHandlers(appTemplate); // set the toolbar button handlers
        initializeWindow();                     // start the app window (without the application-specific workspace)

    }

    public FileController getFileController() {
        return this.fileController;
    }

    public FlowPane getToolbarPane() { return toolbarPane; }

    public BorderPane getAppPane() { return appPane; }

    /**
     * Accessor method for getting this application's primary stage's,
     * scene.
     *
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() { return primaryScene; }

    /**
     * Accessor method for getting this application's window,
     * which is the primary stage within which the full GUI will be placed.
     *
     * @return This application's primary stage (i.e. window).
     */
    public Stage getWindow() { return primaryStage; }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initializeToolbar() throws IOException {
        toolbarPane = new FlowPane();
        toolbarPane.setPrefWrapLength(150);
        toolbarPane.setHgap(20);
        toolbarPane.setVgap(20);
        createProfileButton= new Button("CREATE PROFILE");
        final Tooltip createProfileButtonTootip = new Tooltip();
        createProfileButtonTootip.setText(" CREATE A NEW PROFILE ");
        createProfileButton.setTooltip(createProfileButtonTootip);

        LoginProfileButton= new Button("LOGIN  PROFILE");
        final Tooltip LoginProfileButtonTool = new Tooltip();
        LoginProfileButtonTool.setText(" LOGIN TO AN EXISTING PROFILE");
        LoginProfileButton.setTooltip(LoginProfileButtonTool);


        //<------- HelpScreen Button
        Button HelpButton= new Button("HELP BUTTON");
        final Tooltip helpButtonTooltip = new Tooltip();
        helpButtonTooltip.setText("Help Instructions provided");
        HelpButton.setTooltip(helpButtonTooltip);
        HelpButton.setOnAction(e -> {
            helpProfile();
        });
        //HelpButton.setStyle("-fx-font-size: 15px; -fx-padding: 5px; -fx-border-insets: 10px");


        toolbarPane.getChildren().addAll(createProfileButton, LoginProfileButton,HelpButton);

        newButton= new Button();
        loadButton= new Button();
        hintButton= new Button();
        saveButton= new Button();

        //newButton = initializeChildButton(toolbarPane, NEW_ICON.toString(), NEW_TOOLTIP.toString(), true);
//        loadButton = initializeChildButton(toolbarPane, LOAD_ICON.toString(), LOAD_TOOLTIP.toString(), true);
//        saveButton = initializeChildButton(toolbarPane, SAVE_ICON.toString(), SAVE_TOOLTIP.toString(), true);
//        hintButton = initializeChildButton(toolbarPane, HINT_ICON.toString(), HINT_TOOLTIP.toString(), true);
    }

    /**
     * Initialize the Heading for the Buzzword Game where you initialize the title and the exit button
     * @throws IOException
     * @Author BG
     */


    public void initializeHeading() throws IOException {
        PropertyManager propertyManager = PropertyManager.getManager();

        titleBar = new BorderPane();
        titleBar.setStyle(" -fx-background-color: linear-gradient(to bottom, midnightblue 2%, royalblue 98%)");

        headingLabel= new Label("Buzzword!!");
        headingLabel.setStyle("-fx-font-size: 40 ; -fx-font-family:Papyrus; -fx-text-fill: white;");



        startPane = new BorderPane();

        URL imgDirURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGEDIR_PATH.getParameter());
        if (imgDirURL == null)
            throw new FileNotFoundException("Image resources folder does not exist.");

        exitButton = new Button("EXIT");

        try (InputStream imgInputStream = Files.newInputStream(Paths.get(imgDirURL.toURI()).resolve(propertyManager.getPropertyValue(EXIT_ICON.toString())))) {
            Image buttonImage = new Image(imgInputStream);
            exitButton.setDisable(false);
            exitButton.setGraphic(new ImageView(buttonImage));
            Tooltip buttonTooltip = new Tooltip(propertyManager.getPropertyValue(EXIT_TOOLTIP.toString()));
            exitButton.setTooltip(buttonTooltip);
            titleBar.setRight(exitButton);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try (InputStream imgInputStream = Files.newInputStream(Paths.get(imgDirURL.toURI()).resolve(propertyManager.getPropertyValue(BUZZWORD.toString())))) {
            Image buzzwordImage = new Image(imgInputStream);
            ImageView imageLabel= new ImageView(buzzwordImage);
            titleBar.setLeft(imageLabel);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }


        titleBar.setCenter(headingLabel);
        generateStartGraphs();
    }


    /**
     * This method generate the starting graphics Circles in the Home Screen.
     * The startPane BorderPane has the controller of the display.
     * @Author BG
     */
    public void generateStartGraphs(){

        Circle circle= new Circle(80);

        GridPane displayLetterGrid= new GridPane();
        displayLetterGrid.setVgap(40);
        displayLetterGrid.setHgap(40);
        Button[] gridButtons= new Button[16];
        int c=0;
        final String buzzString="BUZZWORD";
        for(int i=0;i<16;i++){
            if(i % 2 == 0){
                char putStr= buzzString.charAt(c);
                gridButtons[i]= new Button(putStr+"");
                c++;
            }else{
                gridButtons[i]= new Button("");
            }

            gridButtons[i].setShape(circle);
            gridButtons[i].setMinSize(80,80);
            gridButtons[i].setStyle("-fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: white");

            if(i < 4) {
                displayLetterGrid.add(gridButtons[i], i , 1);
            }
            else if(i >= 4 && i < 8){
                displayLetterGrid.add(gridButtons[i], i-4 , 2);

            }
            else if(i >= 8 && i <12 ){
                displayLetterGrid.add(gridButtons[i], i-8, 3);
            }
            else{
                displayLetterGrid.add(gridButtons[i], i-12 , 4);
            }
        }
        startPane.setLeft(displayLetterGrid);
        startPane.setStyle("-fx-background-color: cornflowerblue; -fx-padding: 30px");
    }


    /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     *
     * @param toolbarPane Toolbar pane into which to place this button.
     * @param icon        Icon image file name for the button.
     * @param tooltip     Tooltip to appear when the user mouses over the button.
     * @param disabled    true if the button is to start off disabled, false otherwise.
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    public Button initializeChildButton(Pane toolbarPane, String icon, String tooltip, boolean disabled) throws IOException {
        PropertyManager propertyManager = PropertyManager.getManager();

        URL imgDirURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGEDIR_PATH.getParameter());
        if (imgDirURL == null)
            throw new FileNotFoundException("Image resources folder does not exist.");

        Button button = new Button();
        try (InputStream imgInputStream = Files.newInputStream(Paths.get(imgDirURL.toURI()).resolve(propertyManager.getPropertyValue(icon)))) {
            Image buttonImage = new Image(imgInputStream);
            button.setDisable(disabled);
            button.setGraphic(new ImageView(buttonImage));
            Tooltip buttonTooltip = new Tooltip(propertyManager.getPropertyValue(tooltip));
            button.setTooltip(buttonTooltip);
            toolbarPane.getChildren().add(button);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return button;
    }

    /**
     * This function specifies the CSS style classes for the controls managed
     * by this framework.
     */
    @Override
    public void initStyle() {
        // currently, we do not provide any stylization at the framework-level
    }

    private void initializeToolbarHandlers(AppTemplate app) throws InstantiationException {
        try {
            Method         getFileControllerClassMethod = app.getClass().getMethod("getFileControllerClass");
            String         fileControllerClassName      = (String) getFileControllerClassMethod.invoke(app);
            Class<?>       klass                        = Class.forName("controller." + fileControllerClassName);
            Constructor<?> constructor                  = klass.getConstructor(AppTemplate.class);
            fileController = (FileController) constructor.newInstance(app);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }


        exitButton.setOnAction(e -> fileController.handleExitRequest());

        createProfileButton.setOnAction(e -> {
            createProfileDialog();
            createProfileButton.setDisable(true);
        });

        LoginProfileButton.setOnAction(e -> LoginProfileDialog());

    }

    /**
     * This method helps to create the Profile popup for the Buzzword game
     * @Author BG
     */


    public void createProfileDialog(){

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: black");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene LoginScene = new Scene(grid, 300, 275);

        CreateProfileStage = new Stage();

        CreateProfileStage.setOpacity(0.5);
        CreateProfileStage.setScene(LoginScene);


        Text scenetitle = new Text("CREATE PROFILE");
        scenetitle.setStyle("-fx-font-size: 20px; -fx-font-family: Papyrus ");
        scenetitle.setFill(Color.WHITE);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("USERNAME:");
        userName.setStyle("-fx-text-fill: white");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label password = new Label("PASSWORD:");
        password.setStyle("-fx-text-fill: white");
        grid.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        CreateProfileStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {

                if(t.getCode()== KeyCode.ESCAPE)
                {
                    CreateProfileStage.close();
                }

                if(t.getCode() == KeyCode.ENTER){
                    loginUsername = userTextField.getText();
                    //loginPassword = passwordField.getText();

                    try {
                        loginPassword = securityManager.securityEncrypt(passwordField.getText());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    try {
                        boolean checkingProfile = fileController.handleCreateProfileRequest();
                        if(checkingProfile){
                            CreateProfileStage.close();
                            PropertyManager           manager    = PropertyManager.getManager();
                            AppMessageDialogSingleton dialog     = AppMessageDialogSingleton.getSingleton();
                            dialog.show(manager.getPropertyValue("Error New Username"), "USERNAME ALREADY EXISTS!!");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    CreateProfileStage.close();
                }
            }
        });
        CreateProfileStage.show();
    }

    /**
     * This method helps to create the Login popup for the Buzzword
     * @Author BG
     */

    public void LoginProfileDialog(){
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: black");

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene LoginScene = new Scene(grid, 300, 275);
        LoginScene.setFill(null);
        LoginStage = new Stage();
        LoginStage.setOpacity(0.5);
        LoginStage.setScene(LoginScene);

        Text scenetitle = new Text("LOG IN ");
        scenetitle.setStyle("-fx-font-size: 20px; -fx-font-family: Papyrus ");
        scenetitle.setFill(Color.WHITE);
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("USERNAME: ");
        userName.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label password = new Label("PASSWORD: ");

        password.setStyle("-fx-text-fill: white; -fx-font-size: 15px");
        grid.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);


        try {
            LoginStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ESCAPE) {
                        LoginStage.close();
                    }

                    if (t.getCode() == KeyCode.ENTER) {
                        userNameRequest = userTextField.getText();
                        userPasswordRequest = passwordField.getText();

                        boolean handle = false;
                        try {
                            handle = fileController.handleLoginRequest();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (handle){
                            newButton.setVisible(false);
                            saveButton.setVisible(false);
                            loadButton.setVisible(false);
                            createProfileButton.setVisible(false);
                            LoginProfileButton.setVisible(false);
                            LoginStage.close();
                            PropertyManager           manager    = PropertyManager.getManager();
                            AppMessageDialogSingleton dialog     = AppMessageDialogSingleton.getSingleton();
                            dialog.show(manager.getPropertyValue("Login Sucessful"), "Welcome to Buzzword!!");

                        }


                    }
                }
            });
        }catch (Exception e){
            // do nothing wait till Login
        }
        LoginStage.show();
    }


    /**
     *
     * This method is used to check if the username is matched in some other dependencies from other classes or
     * parts of the frameworks.
     * @return String
     */

    public String getLoginUsername(){
        return loginUsername;
    }

    public String getUsernameRequest(){
        return userNameRequest;
    }

    public String getPasswordRequest(){
        return userPasswordRequest;
    }


    /**
     * This method is initializes the window that is the base of getting the layout for the Buzzword game.
     * The initialze window has the primary stage that is used to initialize the layout of the game.
     * @throws IOException
     */

    private void initializeWindow() throws IOException {
        PropertyManager propertyManager = PropertyManager.getManager();

        // SET THE WINDOW TITLE
        primaryStage.setTitle(applicationTitle);
        primaryStage.setResizable(false);               //resize of the primarystage disabled

        // add the toolbar to the constructed workspace
        appPane = new BorderPane();
        appPane.setTop(titleBar);
        appPane.setLeft(toolbarPane);
        appPane.setRight(startPane);

        primaryScene = appWindowWidth < 1 || appWindowHeight < 1 ? new Scene(appPane)
                : new Scene(appPane,
                appWindowWidth,
                appWindowHeight);

        URL imgDirURL = AppTemplate.class.getClassLoader().getResource(APP_IMAGEDIR_PATH.getParameter());
        if (imgDirURL == null)
            throw new FileNotFoundException("Image resrouces folder does not exist.");
        try (InputStream appLogoStream = Files.newInputStream(Paths.get(imgDirURL.toURI()).resolve(propertyManager.getPropertyValue(APP_LOGO)))) {
            primaryStage.getIcons().add(new Image(appLogoStream));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        hintButton.setVisible(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(primaryScene);

        primaryScene.setOnKeyPressed((KeyEvent event) -> {
            if (helpComb.match(event)) {
                helpProfile();
            }

        });
        primaryStage.show();
    }



    /**
     * Method That returns the Level Selection Pane so that the Workspace can edit the components
     * @return BorderPane
     * @Author BG
     */


    public BorderPane getLevelSelectionPane(){
        return levelSelectionPane;
    }

    public void changeLevelSelectionScreen(String gameString) throws IOException{
        levelUserProfile= new FlowPane();
        this.gameString = gameString;
        levelSelectionPane= new BorderPane();
        levelSelectionPane.setTop(titleBar);
        levelSelectionPane.setLeft(levelUserProfile);
        initializeGameLevel();
        levelSelectionScene = appWindowWidth < 1 || appWindowHeight < 1 ? new Scene(levelSelectionPane)
                : new Scene(levelSelectionPane,
                appWindowWidth,
                appWindowHeight);
        primaryStage.setScene(levelSelectionScene);

        levelSelectionScene.setOnKeyPressed((KeyEvent event) -> {
            if (helpComb.match(event)) {
                helpProfile();
            }

            if(logOut.match(event)){
                try {
                    LogoutButtonController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(proSet.match(event)){
                editProfile();
            }

        });

    }

    private void initializeGameLevel() throws IOException {
        levelUserProfile.setStyle(" -fx-background-color: linear-gradient(to bottom, midnightblue 2%, royalblue 98%)");
        levelUserProfile.setPrefWrapLength(150);
        Label emptyLabel= new Label(" ");
        levelUserProfile.setHgap(30);
        levelUserProfile.setVgap(30);


        userGameMode= new Button("WELCOME");
        userGameMode.setStyle("-fx-font-size: 20px; -fx-padding:10px; -fx-border-insets: 10px");

        //<-------- Home Button Setup
        homeButton= new Button("HOME BUTTON");
        final Tooltip homeButtonTooltip = new Tooltip();
        homeButtonTooltip.setText(" RETURN BACK TO HOME ");
        homeButton.setTooltip(homeButtonTooltip);
        homeButton.setStyle("-fx-font-size: 15px; -fx-padding: 5px; -fx-border-insets: 10px");

        //<------- Profile Setup Button
        Button profileSetupButton= new Button("PROFILE SETUP");
        final Tooltip profileSetupButtonTooltip = new Tooltip();
        profileSetupButtonTooltip.setText("EDIT PROFILE");
        profileSetupButton.setTooltip(profileSetupButtonTooltip);
        profileSetupButton.setOnAction(e -> {
            editProfile();
        });
        profileSetupButton.setStyle("-fx-font-size: 15px; -fx-padding: 5px; -fx-border-insets: 10px");

        //<------- HelpScreen Button
        Button HelpButton= new Button("HELP BUTTON");
        final Tooltip helpButtonTooltip = new Tooltip();
        helpButtonTooltip.setText("Help Instructions provided");
        HelpButton.setTooltip(helpButtonTooltip);
        HelpButton.setOnAction(e -> {
            helpProfile();
        });
        HelpButton.setStyle("-fx-font-size: 15px; -fx-padding: 5px; -fx-border-insets: 10px");

        //<------- Logout Button
        logoutButton= new Button("LOGOUT BUTTON");
        logoutButton.setStyle("-fx-font-size: 15px; -fx-padding: 5px; -fx-border-insets: 10px");
        logoutButton.setOnAction(e -> {
            try {
                LogoutButtonController();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        levelUserProfile.getChildren().addAll(emptyLabel,userGameMode,homeButton, profileSetupButton, HelpButton , logoutButton);
        homeButton.setOnAction(e -> homeButtonActionHandler());
    }


    public Button getLogoutButton(){
        return logoutButton;
    }

    public void LogoutButtonController() throws IOException{
        primaryStage.setScene(primaryScene);
        appPane.setTop(titleBar);
        appPane.setLeft(toolbarPane);
        createProfileButton.setVisible(true);
        LoginProfileButton.setVisible(true);
        fileController.handleLogoutButton();
        //appPane.setRight(startPane);

    }

    public void editProfile(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene editProfileScene = new Scene(grid, 600, 400);


        CreateProfileStage = new Stage();

        //CreateProfileStage.setOpacity(0.5);
        CreateProfileStage.setScene(editProfileScene);

        Text scenetitle = new Text("EDIT PROFILE");
        grid.add(scenetitle, 0, 1, 2, 1);

        Label curUser = new Label("CURRENT USERNAME:");
        //curUser.setStyle("-fx-text-fill: white");
        grid.add(curUser, 0, 2);

        Label userNameTextArea = new Label(userNameRequest+"");
        grid.add(userNameTextArea, 1, 2);

        Label userPass = new Label("CURRENT PASSWORD:");
        //userPass.setStyle("-fx-text-fill: white");
        grid.add(userPass, 0, 3);

        Label userPassWare = new Label(userPasswordRequest+"");
        grid.add(userPassWare, 1, 3);

        Text changeScenetitle = new Text("CHANGE USERNAME AND PASSWORD??");
        changeScenetitle.setStyle("-fx-font-size: 12px; ");
        //changeScenetitle.setFill(Color.WHITE);
        grid.add(changeScenetitle, 0, 4, 2, 1);

        Label userName = new Label("USERNAME:");
        //userName.setStyle("-fx-text-fill: white");
        grid.add(userName, 0, 5);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 5);

        Label password = new Label("PASSWORD:");
        grid.add(password, 0, 6);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 6);

        CreateProfileStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent t) {
                        if (t.getCode() == KeyCode.ESCAPE) {
                            CreateProfileStage.close();
                        }

                        if (t.getCode() == KeyCode.ENTER) {
                            loginUsername = userTextField.getText();
                            loginPassword = passwordField.getText();
                            File file = declareClasspath().toFile();

                            InputStream in = null;
                            try {
                                in = new FileInputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode rootArray = mapper.readTree(file);
                                ((ObjectNode) rootArray).put("USERNAME", loginUsername + "");
                                ((ObjectNode) rootArray).put("PASSWORD", securityManager.securityEncrypt(loginPassword) + "");
                                mapper.writeValue(file, rootArray);
                            } catch (JsonGenerationException e) {
                                e.printStackTrace();
                            } catch (JsonMappingException e) {
                                e.printStackTrace();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            CreateProfileStage.close();
                        }
                    }
        });
        CreateProfileStage.show();
    }

    public Path declareClasspath() {
        PropertyManager propertyManager = PropertyManager.getManager();
        Path appDirPath = Paths.get(propertyManager.getPropertyValue(APP_TITLE)).toAbsolutePath();
        Path targetPath = appDirPath.resolve(APP_WORKDIR_PATH.getParameter());
        return targetPath;
    }

    public void helpProfile() {

        GridPane gridHelp = new GridPane();

        gridHelp.setAlignment(Pos.CENTER_LEFT);
        gridHelp.setHgap(15);
        gridHelp.setVgap(15);
        gridHelp.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Welcome, To Boggle Help!!");
        gridHelp.add(scenetitle, 0, 1, 2, 1);
        Label userNameTextArea = new Label("Hello " + userNameRequest + ",");
        gridHelp.add(userNameTextArea, 0, 2);

        Text curUser = new Text("This is the boggle Game. If you want to play this game. Then Please follow the instructions:");
        gridHelp.add(curUser, 0, 3);

        Text inst1 = new Text("1) Choose the intended GAMEMODE:");
        gridHelp.add(inst1, 0, 4);
        Text inst2 = new Text("2) Then Press the Start Game Button:");
        gridHelp.add(inst2, 0, 5);
        Text inst3 = new Text("3) Choose the Level You want to play:");
        gridHelp.add(inst3, 0, 6);
        Text inst4 = new Text("4) Press the Start Button:");
        gridHelp.add(inst4, 0, 7);
        Text inst5 = new Text(" 5) You are now in the game. When you click the play button your time is decreasing!!");
        gridHelp.add(inst5, 0, 8);
        Text inst6 = new Text("6) Find the appropriate words that match the target points");
        gridHelp.add(inst6, 0, 9);
        Text inst7 = new Text("7) If you finish the game click on next Level !! Hurray!!");
        gridHelp.add(inst7, 0, 10);
        Text inst8 = new Text("8) Press on Exit Button if you want to quit the application.");
        gridHelp.add(inst8, 0, 11);
        Text inst9 = new Text("9) Thank you!!");
        gridHelp.add(inst9, 0, 12);


        ScrollPane scrollPane= new ScrollPane(gridHelp);
        scrollPane.setFitToWidth(true);


        Scene helpProfileScene = new Scene(scrollPane, 800, 300);
        Stage HelpProfielStage = new Stage();

        //CreateProfileStage.setOpacity(0.5);
        HelpProfielStage.setScene(helpProfileScene);

        HelpProfielStage.show();

    }

    public void changetoGameplayScreen() throws IOException {
        userProfilePane = new FlowPane();
        userGamePlayPane = new BorderPane();
        userGamePlayPane.setTop(titleBar);
        userGamePlayPane.setLeft(userProfilePane);
        initializeUserGamePlay();
        gameplayScene = appWindowWidth < 1 || appWindowHeight < 1 ? new Scene(userGamePlayPane)
                : new Scene(userGamePlayPane,
                appWindowWidth,
                appWindowHeight);
        primaryStage.setScene(gameplayScene);




        gameplayScene.setOnKeyPressed((KeyEvent event) -> {

            if(logOut.match(event)){
                try {
                    LogoutButtonController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (helpComb.match(event)) {
                helpProfile();
            }
        });
    }



    public Scene getGameplayScene() {
        return gameplayScene;
    }


    public BorderPane getUserGamePlayPane(){
        return userGamePlayPane;
    }


    private void initializeUserGamePlay() throws IOException {
        userProfilePane.setStyle(" -fx-background-color: linear-gradient(to bottom, midnightblue 2%, royalblue 98%)");
        userProfilePane.setPrefWrapLength(150);
        userProfilePane.setHgap(30);
        userProfilePane.setVgap(30);

        //<------ welcome Button

        userGameMode= new Button("WELCOME" );
        userGameMode.setStyle("-fx-font-size: 20px; -fx-padding:10px; -fx-border-insets: 10px");

        //<------ Home Button
        homeButton= new Button("HOME BUTTON");
        homeButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-border-insets: 10px");
        homeButton.setOnAction(e -> homeButtonActionHandler());


        //<------ Logout Button
        logoutButton= new Button("LOGOUT BUTTON");
        logoutButton.setStyle("-fx-font-size: 20px; -fx-padding: 5px; -fx-border-insets: 10px");
        logoutButton.setOnAction(e -> {
            try {
                LogoutButtonController();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        //<------- HelpScreen Button
        Button HelpButton= new Button("HELP BUTTON");
        final Tooltip helpButtonTooltip = new Tooltip();
        helpButtonTooltip.setText("Help Instructions provided");
        HelpButton.setTooltip(helpButtonTooltip);
        HelpButton.setOnAction(e -> {
            helpProfile();
        });
        HelpButton.setStyle("-fx-font-size: 20px; -fx-padding: 5px; -fx-border-insets: 10px");

        //<------- Restart Button
        Button restartButton= new Button("RESTART GAME");
        final Tooltip restartToolTip = new Tooltip();
        restartToolTip.setText("Restart the Game");
        restartButton.setTooltip(restartToolTip);
        restartButton.setOnAction(e -> {
            restartGame();
        });
        restartButton.setStyle("-fx-font-size: 20px; -fx-padding: 5px; -fx-border-insets: 10px");

        //<------- NextLevel Button
        nextLevelButton= new Button("NEXT LEVEL");
        nextLevelButton.setDisable(true);
        final Tooltip nextLevelTooltip = new Tooltip();
        nextLevelTooltip.setText("Go to Next Level");
        nextLevelButton.setTooltip(nextLevelTooltip);
        nextLevelButton.setOnAction(e -> {
            fileController.handleNextLevel();
        });

        userProfilePane.getChildren().addAll(userGameMode, homeButton, HelpButton,restartButton, nextLevelButton, logoutButton);
    }

    public Button getNextLevelButton(){
        return nextLevelButton;
    }

    public void restartGame(){
        fileController.handleRestartGame();
    }



    public void homeButtonActionHandler(){
        appPane.setTop(titleBar);
        primaryStage.setScene(primaryScene);
    }


    public String getLoginPassword() {
        return loginPassword;
    }

    public Button getExitButton(){
        return exitButton;
    }
}

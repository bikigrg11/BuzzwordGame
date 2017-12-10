package gui;

import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import controller.BuzzwordController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import propertymanager.PropertyManager;
import ui.AppGUI;
import ui.YesNoCancelDialogSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import data.pointsCalculator;

import static buzzword.BuzzwordProperties.*;


/**
 * This class serves as the GUI component for the Buzzword game.
 *
 * @author BG
 */
public class Workspace extends AppWorkspaceComponent {

    AppTemplate app; // the actual application
    AppGUI      gui; // the GUI inside which the application sits

    Label             guiHeadingLabel;   // workspace (GUI) heading label
    VBox              headPane;          // conatainer to display the heading
    VBox              bodyPane;          // container for the main game displays
    ToolBar           footToolbar;       // toolbar for game buttons
    FlowPane          toolBar;
    BorderPane        figurePane;
    VBox              gameTextsPane;     // container to display the text-related parts of the game
    VBox              guessedLetters;    // text area displaying all the letters guessed so far
    HBox              remainingGuessBox; // container to display the number of remaining guesses
    HBox              buzzwordGraphsPane;
    Button            startGame;         // the button to start playing a game of Buzzword
    BuzzwordController controller;
    ToolBar           contollerPane;
    Button levelSelect;
    char levelSelected;
    Canvas canvas;
    GraphicsContext gc;
    Button[] gridButtons;
    Button[] gridButtons2;
    Button[] gridButtons3;
    Button[] gridButtons4;
    Button[] gridButtons5;
    VBox playpause;
    String gameString="";
    GridPane gridPaneLevel;
    final Button[] play = new Button[2];
    Integer STARTTIME=50;
    Timeline timeline;
    private Label timeRemaining = new Label();
    IntegerProperty timeSeconds;
    GridPane guessedLettersBox;
    String gameModeString;
    int levelPoints = 0;
    ArrayList<String> wordsGenerate;
    GridPane lettersPointPane;
    GridPane displayLetterGrid;
    GridPane totalPane;
    pointsCalculator calculator;
    Button[] newReturnButton= new Button[16];
    StringBuilder strBuilder;;


    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     * @throws IOException Thrown should there be an error loading application
     *                     data for setting up the user interface.
     */

    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
        controller = (BuzzwordController) gui.getFileController();
        layoutGUI();     // initialize all the workspace (GUI) components including the containers and their layout
    }



    private void layoutGUI() {

        guiHeadingLabel = new Label();
        headPane = new VBox();
        headPane.getChildren().add(guiHeadingLabel);
        headPane.setAlignment(Pos.CENTER);
        toolBar= app.getGUI().getToolbarPane();
        figurePane = new BorderPane();
        guessedLetters = new VBox();
        guessedLetters.setStyle("-fx-background-color: transparent;");
        remainingGuessBox = new HBox();
        gameTextsPane = new VBox();
        gameTextsPane.getChildren().setAll(guessedLetters,remainingGuessBox );

        bodyPane = new VBox();

        buzzwordGraphsPane = new HBox();
        double h=400;
        double w=500;
        canvas = new Canvas(h,w);
        gc = canvas.getGraphicsContext2D();

        bodyPane.getChildren().addAll(gameTextsPane);
        startGame = new Button("Start Playing");
        final Tooltip starGameTool = new Tooltip();
        starGameTool.setText(" PRESS TO START GAME ");
        startGame.setTooltip(starGameTool);

        levelSelect= new Button("Select Level");
        levelSelect.setDisable(true);
        final Tooltip levelSelectTool = new Tooltip();
        levelSelectTool.setText(" PRESS TO SELECT A LEVEL");
        levelSelect.setTooltip(levelSelectTool);


        HBox blankBoxLeft  = new HBox();
        HBox blankBoxRight = new HBox();
        HBox.setHgrow(blankBoxLeft, Priority.ALWAYS);
        HBox.setHgrow(blankBoxRight, Priority.ALWAYS);
        footToolbar = new ToolBar(blankBoxLeft,levelSelect,blankBoxRight);
        workspace = new VBox();
        workspace.getChildren().addAll(headPane);
    }

    /**
     * This function specifies the CSS for all the UI components known at the time the workspace is initially
     * constructed. Components added and/or removed dynamically as the application runs need to be set up separately.
     */
    @Override
    public void initStyle() {
        PropertyManager propertyManager = PropertyManager.getManager();

        gui.getAppPane().setId(propertyManager.getPropertyValue(ROOT_BORDERPANE_ID));
        gui.getToolbarPane().getStyleClass().setAll(propertyManager.getPropertyValue(SEGMENTED_BUTTON_BAR));
        gui.getToolbarPane().setId(propertyManager.getPropertyValue(TOP_TOOLBAR_ID));

        ObservableList<Node> toolbarChildren = gui.getToolbarPane().getChildren();
        toolbarChildren.get(0).getStyleClass().add(propertyManager.getPropertyValue(FIRST_TOOLBAR_BUTTON));
        toolbarChildren.get(toolbarChildren.size() - 1).getStyleClass().add(propertyManager.getPropertyValue(LAST_TOOLBAR_BUTTON));

        workspace.getStyleClass().add(CLASS_BORDERED_PANE);
        guiHeadingLabel.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));
    }



    public GraphicsContext getCanvas(){
        return gc;
    }

    public void changeToLevelSelectionScene(String gamemodeTitle, String gameString, int curr ){
        try {
            app.getGUI().changeLevelSelectionScreen(gameString);
            this.gameString = gameString;
            this.gameModeString = gamemodeTitle;
            guiHeadingLabel= new Label("GAME MODE : "+ gamemodeTitle);
            guiHeadingLabel.setStyle(" -fx-font-size: 25px; -fx-alignment: center; -fx-font-style: oblique ");
            levelSelectionScreen(curr);

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void levelSelectionScreen(int curr) {
        BorderPane levelSelectionPane=app.getGUI().getLevelSelectionPane();
        reinitializeGridPane();
        createLevelforMode(curr);
        enableSelectLevelButton(curr);
        guiHeadingLabel.setStyle("-fx-font-size: 50px; -fx-padding: 20px; -fx-font-family:Papyrus; -fx-border-color: darkred");
        BorderPane insideLevelScreen= new BorderPane();
        insideLevelScreen.setTop(guiHeadingLabel);
        insideLevelScreen.setCenter(headPane);
        insideLevelScreen.setBottom(footToolbar);
        levelSelectionPane.setCenter(insideLevelScreen);
    }

    public void reinitializeGridPane(){
        headPane.getChildren().clear();
    }

    public void createLevelforMode(int cc){
        gridPaneLevel= new GridPane();
        Circle circle= new Circle(40);
        gridPaneLevel.setHgap(20);
        gridPaneLevel.setVgap(20);
        gridPaneLevel.setAlignment(Pos.CENTER);
        Button[] button= new Button[10];
        Label selectLevelIns= new Label("Please Choose a Level , and Click the Select Level button.");
        selectLevelIns.setStyle(" -fx-font-size: 20px");


        for(int i=0;i<10;i++){
            button[i]= new Button((i+1)+"");
            button[i].setShape(circle);
            button[i].setMinSize(80,80);
            final int num = i;
            button[i].setOnAction(e -> {
                LevelSelectorData(button[num].getText());
                levelSelect.setDisable(false);
            });

            int curr=cc-1;

            if(i < 5) {
                gridPaneLevel.add(button[i], i , 1);
                button[i].setStyle("-fx-background-color: #6f7069; -fx-font-size: 20; -fx-text-fill:black");
                button[i].setDisable(true);
                if(i <= curr){
                    button[i].setStyle("-fx-font-size: 20; -fx-text-fill: black");
                    button[i].setDisable(false);
                }
            }
            else{
                gridPaneLevel.add(button[i], i-5 , 2);
                button[i].setStyle("-fx-background-color: #6f7069; -fx-font-size: 20; -fx-text-fill:black");
                button[i].setDisable(true);
                if(i <= curr){
                    button[i].setStyle("-fx-font-size: 20; -fx-text-fill: black");
                    button[i].setDisable(false);
                }
            }
        }
        headPane.getChildren().addAll(selectLevelIns, gridPaneLevel);
    }

    public void LevelSelectorData(String num) {
        levelSelected= num.charAt(0);
    }

    public int getLevel(){
        return levelSelected-48;
    }


    public void manageTimeLabel(){
        STARTTIME = 50;
        timeSeconds = new SimpleIntegerProperty(STARTTIME);
        timeRemaining.textProperty().bind(timeSeconds.asString());
        timeline = new Timeline();
    }

    public GridPane getGuessedLettersBox() {
        return guessedLettersBox;
    }

    public GridPane getLettersPointPane() {
        return lettersPointPane;
    }

    public GridPane getTotalGridPane(){
        return totalPane;
    }

    /**
     * This method creates the Gameplay Screen which is the screen where the user plays the game.
     * @Author BG
     */
    public void gameplayScreen(int curLevel){
        BorderPane gameplayScreenPane= app.getGUI().getUserGamePlayPane(); // get the GamePlayPane from AppGui

        BorderPane headingGamePlay= new BorderPane();
        createGenerateWordsList(curLevel);


        manageTimeLabel();
        Label timerLabel= new Label("Timer: ");
        Label timerLabelSeconds= new Label(" Second");
        HBox timerBox= new HBox();
        timerBox.getChildren().addAll(timerLabel,timeRemaining,timerLabelSeconds);
        timerBox.setStyle("-fx-background-color: powderblue ; -fx-font-size: 30px; -fx-text-fill: white; -fx-padding: 20px ");

        guiHeadingLabel.setStyle("-fx-font-size: 35px; -fx-font-family: cursive; -fx-text-fill: darkred; -fx-padding: 10px; -fx-underline: true");
        headingGamePlay.setLeft(guiHeadingLabel);       // Label for Selected GameMode
        headingGamePlay.setRight(timerBox);        // Label for Time Remaining
        VBox boxShiftGap= new VBox(20);
        headingGamePlay.setBottom(boxShiftGap);         // some space for the display

// <---- Box that Displays the Words that has been guessed on the TOP

        guessedLettersBox= new GridPane();
        guessedLettersBox.setMinSize(25,30);
        guessedLettersBox.setStyle("-fx-background-color: black");
        guessedLettersBox.setGridLinesVisible(true);

// <-- Display all the letters that has been sucessfullly guessed. with the points are displayed.

        lettersPointPane = new GridPane();
        totalPane= new GridPane();

        controller.makePointsPane();
        lettersPointPane.setStyle("-fx-border-color: black");

        ScrollPane sc= new ScrollPane();
        sc.setContent(lettersPointPane);
        sc.setMaxSize(320,300);
// <--- The main Point box that includes all the guessed Letters and Points with total


        VBox pointBox= new VBox();
        pointBox.getChildren().setAll(sc, totalPane);



       // int numTarget= ((int) levelSelected)- 48;
//<------ targerBox for the gameplay
        Label targerLabel= new Label("Target");
        targerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-underline: true");
        Label targetPointsLabel= new Label(levelPoints +" POINTS");
        targetPointsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 30px");
        VBox targerBox= new VBox();
        targerBox.setStyle("-fx-background-color: dimgrey");
        targerBox.getChildren().addAll(targerLabel, targetPointsLabel);

// <-- The Sidemost BOX that Contains the three part of the Label
        VBox guessBox= new VBox();
        guessBox.setSpacing(20);
        guessBox.getChildren().addAll(guessedLettersBox,pointBox,targerBox);
        guessBox.setStyle("-fx-padding: 20px");

        // The main Controlle of the Borderpane in Gameplay is insideLevelScreen

        BorderPane insideLevelScreen= new BorderPane(); // The Screen to put the graphics, points table
        insideLevelScreen.setTop(headingGamePlay);
        figurePane.setStyle("-fx-padding: 30px");

        for(int i=0;i<10;i++){
            makeLineGraphics(i);
        }

        insideLevelScreen.setLeft(figurePane);              // set Changes here
        //insideLevelScreen.setCenter(canvas);
        insideLevelScreen.setRight(guessBox);               // Set Changes here for the Words Point BOx
        getBottomLabel();                                   // Create the PLAY/PAUSE Button and the Level
        insideLevelScreen.setBottom(contollerPane);         // Add the ControlPane for the PLAY/PAUSE

        gameplayScreenPane.setCenter(insideLevelScreen);    // Set the Gameplay Screen add all components
    }

    public boolean checkIfWordsIsGenereated(String generatedWord){
        if(wordsGenerate.contains(generatedWord)){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList getArrayWords(){
        return wordsGenerate;
    }


    public void createGenerateWordsList(int curLevel){
        levelPoints =  pointCalculationMethod(curLevel);
    }

    public int getLevelPoints(){
        return levelPoints;
    }


    public int pointCalculationMethod(int currentLevel){

        int totalPoints = 0;
        int totallength= 0;
        int numberOfWords= wordsGenerate.size();

       for(int i=0;i<numberOfWords;i++){
           totallength = totallength+ wordsGenerate.get(i).length();
       }
        System.out.println("TOTAL LENGTH OF WORD: "+ totallength);
        System.out.println("NUMBER OF WORDS: "+numberOfWords);

       int percentileLength = (currentLevel *  10 * totallength)/100;     // taking the percent according to the currentlevel
        totalPoints = percentileLength * 5;
        System.out.println("TOTAL POINTS: "+totalPoints);
        return totalPoints;
    }



    public void getBottomLabel(){
        StackPane stackPlayPause= new StackPane();
        playpause = new VBox();
        playpause.setSpacing(10);
        play[0] = new Button("PLAY");
        play[0].setStyle(" -fx-font-size: 20px");
        play[1] = new Button("PAUSE");
        play[1].setStyle(" -fx-font-size: 20px");


        // PLAY BUTTON SETUP :::
        final Tooltip playTool = new Tooltip();
        playTool.setText(" PLAY YOUR GAME ");
        play[0].setTooltip(playTool);
        play[0].setOnAction(e -> {
            onClickPlay();
            STARTTIME = timeSeconds.getValue();
            playMethodTimer();
            play[0].setVisible(false);
            play[1].setVisible(true);
        });


        // PAUSE BUTTON SETUP :::
        final Tooltip pauseTool = new Tooltip();
        pauseTool.setText(" PAUSE YOUR GAME ");
        play[1].setTooltip(pauseTool);
        play[1].setVisible(false);
        play[1].setOnAction(e -> {
            timeline.stop();
            onClickPause();
            play[0].setVisible(true);
            play[1].setVisible(false);
        });

        stackPlayPause.getChildren().addAll(play[0], play[1]);
        Label levelBottomLabel= new Label("Level "+levelSelected);
        levelBottomLabel.setStyle("-fx-font-size: 20px; -fx-underline: true ");

        playpause.getChildren().addAll(levelBottomLabel, stackPlayPause);

        HBox blankBoxLeft  = new HBox();
        HBox blankBoxRight = new HBox();
        HBox.setHgrow(blankBoxLeft, Priority.ALWAYS);
        HBox.setHgrow(blankBoxRight, Priority.ALWAYS);
        contollerPane = new ToolBar( blankBoxLeft, playpause,blankBoxRight);
    }


    public void playMethodTimer(){
        timeSeconds.set(STARTTIME);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        if(timeSeconds.getValue() <= 0){
            play[1].setDisable(true);
            play[0].setDisable(true);
            STARTTIME = 50;
            onClickPause();
            timeline = new Timeline();
            timeline.playFromStart();
        }
    }

    public IntegerProperty getTimeSeconds(){
        return timeSeconds;
    }

    public void checkPlayExit(){
        Button exitButton=app.getGUI().getExitButton();
        exitButton.setOnAction(e -> {
            timeline.stop();
            STARTTIME = timeSeconds.getValue();
            play[0].setVisible(true);
            play[1].setVisible(false);
            onClickPause();
            YesNoCancelDialogSingleton yesNoCancelDialog = YesNoCancelDialogSingleton.getSingleton();
            yesNoCancelDialog.show("Exit Application"," Are you sure you want to Exit ?");

            try{
                if (yesNoCancelDialog.getSelection().equals(YesNoCancelDialogSingleton.YES)){
                    System.exit(0);
                }
                else if (yesNoCancelDialog.getSelection().equals(YesNoCancelDialogSingleton.NO)){
                    //do nothing
                }
                else{
                    // do nothing
                }
            }catch (Exception ev){
                // do nothing
            }

        });
    }


    public void onClickPlay(){
        try{
            checkPlayExit();                    // Setup the Exit Button Actions for pausing the game
        }
        catch (Exception e){
            // do nothing
        }
        controller.buildGuessLabel();       // Setup for the play Method that controls the Events in gameplay
        controller.play();                  // Runs the play method from the BuzzwordController

        int counter = 0;
        for(int i = 0 ;i <16; i ++){
            gridButtons[i].setDisable(false);
            gridButtons2[i].setDisable(false);
            gridButtons3[i].setDisable(false);
            gridButtons4[i].setDisable(false);
            gridButtons5[i].setDisable(false);

            if(counter == 0){
                gridButtons[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons2[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons3[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons4[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons5[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");

                counter ++;
            }
            else if(counter == 1){
                gridButtons[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons2[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons3[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons4[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons5[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");

                counter ++;
            }
            else{
                gridButtons[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons2[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons3[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons4[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons5[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");

                counter ++;
            }
            if(counter == 3){
                counter = 0;
            }
        }
    }

    public void onClickPause(){
        int counter = 0;
        for(int i = 0 ;i <16; i ++){
            gridButtons[i].setDisable(true);
            gridButtons2[i].setDisable(true);
            gridButtons3[i].setDisable(true);
            gridButtons4[i].setDisable(true);
            gridButtons5[i].setDisable(true);
            if(counter == 0){
                gridButtons[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons2[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons3[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons4[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons5[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                counter ++;
            }
            else if(counter == 1){
                gridButtons[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons2[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons3[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons4[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons5[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                counter ++;
            }
            else{
                gridButtons[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons2[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons3[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons4[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                gridButtons5[i].setStyle("-fx-background-color: black; -fx-font-size: 20; -fx-text-fill: black");
                counter ++;
            }
            if(counter == 3){
                counter = 0;
            }
        }
    }





    public void enableSelectLevelButton(int curr){
        levelSelect.setOnAction(e -> {
            try {
                app.getGUI().changetoGameplayScreen();

                displayLetters();
                gameplayScreen(curr);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }


    public Button[] getButtonPressed(){
        return newReturnButton;
    }

    public GridPane getDisplayLetterGrid(){
        return displayLetterGrid;
    }

    public void displayLetters(){

        StackPane floPaneGraphics= new StackPane();
        makeLineGraphics(8);
        displayLetterGrid= new GridPane();

        //displayLetterGrid.set

        Circle circle= new Circle(80);
        circle.setFill(Color.RED);

        displayLetterGrid.setVgap(40);
        gridButtons= new Button[16];
        gridButtons2 = new Button[16];
        gridButtons3 = new Button[16];
        gridButtons4 = new Button[16];
        gridButtons5 = new Button[16];

        final String buzzString=gameString.toUpperCase();

        for(int i=0;i<16;i++) {
            if (i < buzzString.length()) {
                 char putStr = buzzString.charAt(i);
//                Random r = new Random();
//                char Str = (char) (r.nextInt(26) + 'a');
//                String putStr = (Str + "").toUpperCase();

                gridButtons[i] = new Button(putStr + "");
                gridButtons2[i] = new Button(putStr + "");
                gridButtons3[i] = new Button(putStr + "");
                gridButtons4[i] = new Button(putStr + "");
                gridButtons5[i] = new Button(putStr + "");

            } else {
                Random r = new Random();
                char ch = (char) (r.nextInt(26) + 'a');
                String charRand = (ch + "").toUpperCase();
                gridButtons[i] = new Button(charRand);
                gridButtons2[i] = new Button(charRand);
                gridButtons3[i] = new Button(charRand);
                gridButtons4[i] = new Button(charRand);
                gridButtons5[i] = new Button(charRand);
            }

            gridButtons[i].setShape(circle);
            gridButtons[i].setMinSize(80, 80);
            gridButtons2[i].setShape(circle);
            gridButtons2[i].setMinSize(80, 80);
            gridButtons3[i].setShape(circle);
            gridButtons3[i].setMinSize(80, 80);
            gridButtons4[i].setShape(circle);
            gridButtons4[i].setMinSize(80, 80);
            gridButtons5[i].setShape(circle);
            gridButtons5[i].setMinSize(80, 80);

            Line line1 = new Line(800, 480, 900, 480);
            line1.setStyle("-fx-border-insets: 20px");
            line1.setStroke(Color.WHITESMOKE);

            gridButtons[i].setStyle("-fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: black");
            gridButtons2[i].setStyle("-fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: black");
            gridButtons3[i].setStyle("-fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: black");
            gridButtons4[i].setStyle("-fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: black");
            gridButtons5[i].setStyle("-fx-background-color: black; -fx-font-size: 20px; -fx-text-fill: black");

            if (i < 4) {
                displayLetterGrid.add(line1, i, 1);
            } else if (i >= 4 && i < 8) {
                displayLetterGrid.add(line1, i - 4, 2);
            } else if (i >= 8 && i < 12) {
                displayLetterGrid.add(line1, i - 8, 3);
            } else {
                displayLetterGrid.add(line1, i - 12, 4);
            }
        }

        Random rand= new Random();

        int numRand= rand.nextInt(4);
        strBuilder= new StringBuilder(16);
        switch (numRand) {
            case 0: {
                newReturnButton = gridButtons;
                displayLetterGrid.add(gridButtons[0], 0, 1);
                strBuilder.append(gridButtons[0].getText());
                displayLetterGrid.add(gridButtons[1], 1, 1);
                strBuilder.append(gridButtons[1].getText());
                displayLetterGrid.add(gridButtons[2], 2, 1);
                strBuilder.append(gridButtons[2].getText());
                displayLetterGrid.add(gridButtons[3], 3, 1);
                strBuilder.append(gridButtons[3].getText());
                displayLetterGrid.add(gridButtons[7], 0, 2);
                strBuilder.append(gridButtons[7].getText());
                displayLetterGrid.add(gridButtons[6], 1, 2);
                strBuilder.append(gridButtons[6].getText());
                displayLetterGrid.add(gridButtons[5], 2, 2);
                strBuilder.append(gridButtons[5].getText());
                displayLetterGrid.add(gridButtons[4], 3, 2);
                strBuilder.append(gridButtons[4].getText());
                displayLetterGrid.add(gridButtons[8], 0, 3);
                strBuilder.append(gridButtons[8].getText());
                displayLetterGrid.add(gridButtons[9], 1, 3);
                strBuilder.append(gridButtons[9].getText());
                displayLetterGrid.add(gridButtons[10], 2, 3);
                strBuilder.append(gridButtons[10].getText());
                displayLetterGrid.add(gridButtons[11], 3, 3);
                strBuilder.append(gridButtons[11].getText());
                displayLetterGrid.add(gridButtons[15], 0, 4);
                strBuilder.append(gridButtons[15].getText());
                displayLetterGrid.add(gridButtons[14], 1, 4);
                strBuilder.append(gridButtons[14].getText());
                displayLetterGrid.add(gridButtons[13], 2, 4);
                strBuilder.append(gridButtons[13].getText());
                displayLetterGrid.add(gridButtons[12], 3, 4);
                strBuilder.append(gridButtons[12].getText());
                break;
            }
            case 1: {
                newReturnButton = gridButtons2;
                displayLetterGrid.add(gridButtons2[9], 0, 0);
                strBuilder.append(gridButtons2[9].getText());
                displayLetterGrid.add(gridButtons2[8], 1, 0);
                strBuilder.append(gridButtons2[8].getText());
                displayLetterGrid.add(gridButtons2[7], 2, 0);
                strBuilder.append(gridButtons2[7].getText());
                displayLetterGrid.add(gridButtons2[4], 3, 0);
                strBuilder.append(gridButtons2[4].getText());
                displayLetterGrid.add(gridButtons2[10], 0, 1);
                strBuilder.append(gridButtons2[10].getText());
                displayLetterGrid.add(gridButtons2[6], 1, 1);
                strBuilder.append(gridButtons2[6].getText());
                displayLetterGrid.add(gridButtons2[5], 2, 1);
                strBuilder.append(gridButtons2[5].getText());
                displayLetterGrid.add(gridButtons2[3], 3, 1);
                strBuilder.append(gridButtons2[3].getText());
                displayLetterGrid.add(gridButtons2[13], 0, 2);
                strBuilder.append(gridButtons2[13].getText());
                displayLetterGrid.add(gridButtons2[11], 1, 2);
                strBuilder.append(gridButtons2[11].getText());
                displayLetterGrid.add(gridButtons2[2], 2, 2);
                strBuilder.append(gridButtons2[2].getText());
                displayLetterGrid.add(gridButtons2[0], 3, 2);
                strBuilder.append(gridButtons2[0].getText());
                displayLetterGrid.add(gridButtons2[12], 0, 3);
                strBuilder.append(gridButtons2[12].getText());
                displayLetterGrid.add(gridButtons2[14], 1, 3);
                strBuilder.append(gridButtons2[14].getText());
                displayLetterGrid.add(gridButtons2[15], 2, 3);
                strBuilder.append(gridButtons2[15].getText());
                displayLetterGrid.add(gridButtons2[1], 3, 3);
                strBuilder.append(gridButtons2[1].getText());
                break;
            }
            case 2: {
                // for Gridbuttons 3
                newReturnButton = gridButtons3;
                displayLetterGrid.add(gridButtons3[3], 0, 0);
                strBuilder.append(gridButtons3[3].getText());
                displayLetterGrid.add(gridButtons3[4], 1, 0);
                strBuilder.append(gridButtons3[4].getText());
                displayLetterGrid.add(gridButtons3[1], 2, 0);
                strBuilder.append(gridButtons3[1].getText());
                displayLetterGrid.add(gridButtons3[15], 3, 0);
                strBuilder.append(gridButtons3[15].getText());
                displayLetterGrid.add(gridButtons3[5], 0, 1);
                strBuilder.append(gridButtons3[5].getText());
                displayLetterGrid.add(gridButtons3[2], 1, 1);
                strBuilder.append(gridButtons3[2].getText());
                displayLetterGrid.add(gridButtons3[0], 2, 1);
                strBuilder.append(gridButtons3[0].getText());
                displayLetterGrid.add(gridButtons3[14], 3, 1);
                strBuilder.append(gridButtons3[14].getText());
                displayLetterGrid.add(gridButtons3[8], 0, 2);
                strBuilder.append(gridButtons3[8].getText());
                displayLetterGrid.add(gridButtons3[6], 1, 2);
                strBuilder.append(gridButtons3[6].getText());
                displayLetterGrid.add(gridButtons3[10], 2, 2);
                strBuilder.append(gridButtons3[10].getText());
                displayLetterGrid.add(gridButtons3[13], 3, 2);
                strBuilder.append(gridButtons3[13].getText());
                displayLetterGrid.add(gridButtons3[7], 0, 3);
                strBuilder.append(gridButtons3[7].getText());
                displayLetterGrid.add(gridButtons3[9], 1, 3);
                strBuilder.append(gridButtons3[9].getText());
                displayLetterGrid.add(gridButtons3[12], 2, 3);
                strBuilder.append(gridButtons3[12].getText());
                displayLetterGrid.add(gridButtons3[11], 3, 3);
                strBuilder.append(gridButtons3[11].getText());

                break;
            }

            case 3: {
                // for Gridbuttons 3
                newReturnButton = gridButtons4;
                displayLetterGrid.add(gridButtons4[0], 0, 0);
                strBuilder.append(gridButtons4[0].getText());
                displayLetterGrid.add(gridButtons4[7], 1, 0);
                strBuilder.append(gridButtons4[7].getText());
                displayLetterGrid.add(gridButtons4[8], 2, 0);
                strBuilder.append(gridButtons4[8].getText());
                displayLetterGrid.add(gridButtons4[15], 3, 0);
                strBuilder.append(gridButtons4[15].getText());
                displayLetterGrid.add(gridButtons4[1], 0, 1);
                strBuilder.append(gridButtons4[1].getText());
                displayLetterGrid.add(gridButtons4[6], 1, 1);
                strBuilder.append(gridButtons4[6].getText());
                displayLetterGrid.add(gridButtons4[9], 2, 1);
                strBuilder.append(gridButtons4[9].getText());
                displayLetterGrid.add(gridButtons4[14], 3, 1);
                strBuilder.append(gridButtons4[14].getText());
                displayLetterGrid.add(gridButtons4[2], 0, 2);
                strBuilder.append(gridButtons4[2].getText());
                displayLetterGrid.add(gridButtons4[5], 1, 2);
                strBuilder.append(gridButtons4[5].getText());
                displayLetterGrid.add(gridButtons4[10], 2, 2);
                strBuilder.append(gridButtons4[10].getText());
                displayLetterGrid.add(gridButtons4[13], 3, 2);
                strBuilder.append(gridButtons4[13].getText());
                displayLetterGrid.add(gridButtons4[3], 0, 3);
                strBuilder.append(gridButtons4[3].getText());
                displayLetterGrid.add(gridButtons4[4], 1, 3);
                strBuilder.append(gridButtons4[4].getText());
                displayLetterGrid.add(gridButtons4[11], 2, 3);
                strBuilder.append(gridButtons4[11].getText());
                displayLetterGrid.add(gridButtons4[12], 3, 3);
                strBuilder.append(gridButtons4[12].getText());
                break;
            }
            case 4: {
                // for Gridbuttons 5
                newReturnButton = gridButtons5;
                displayLetterGrid.add(gridButtons5[15], 0, 0);
                strBuilder.append(gridButtons5[15].getText());
                displayLetterGrid.add(gridButtons5[14], 1, 0);
                strBuilder.append(gridButtons5[14].getText());
                displayLetterGrid.add(gridButtons5[13], 2, 0);
                strBuilder.append(gridButtons5[13].getText());
                displayLetterGrid.add(gridButtons5[12], 3, 0);
                strBuilder.append(gridButtons5[12].getText());
                displayLetterGrid.add(gridButtons5[8], 0, 1);
                strBuilder.append(gridButtons5[8].getText());
                displayLetterGrid.add(gridButtons5[9], 1, 1);
                strBuilder.append(gridButtons5[9].getText());
                displayLetterGrid.add(gridButtons5[10], 2, 1);
                strBuilder.append(gridButtons5[10].getText());
                displayLetterGrid.add(gridButtons5[11], 3, 1);
                strBuilder.append(gridButtons5[11].getText());
                displayLetterGrid.add(gridButtons5[7], 0, 2);
                strBuilder.append(gridButtons5[7].getText());
                displayLetterGrid.add(gridButtons5[6], 1, 2);
                strBuilder.append(gridButtons5[6].getText());
                displayLetterGrid.add(gridButtons5[5], 2, 2);
                strBuilder.append(gridButtons5[5].getText());
                displayLetterGrid.add(gridButtons5[4], 3, 2);
                strBuilder.append(gridButtons5[4].getText());
                displayLetterGrid.add(gridButtons5[0], 0, 3);
                strBuilder.append(gridButtons5[0].getText());
                displayLetterGrid.add(gridButtons5[1], 1, 3);
                strBuilder.append(gridButtons5[1].getText());
                displayLetterGrid.add(gridButtons5[2], 2, 3);
                strBuilder.append(gridButtons5[2].getText());
                displayLetterGrid.add(gridButtons5[3], 3, 3);
                strBuilder.append(gridButtons5[3].getText());
                break;
            }
            default: {
                break;
            }
        }
        System.out.println(strBuilder.toString());


        try {
            calculator= new pointsCalculator(strBuilder.toString());
            wordsGenerate= new ArrayList<>();
            wordsGenerate = calculator.readData(gameModeString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        floPaneGraphics.getChildren().addAll(canvas,displayLetterGrid);
        figurePane.setCenter(floPaneGraphics);
    }

    public String getStringAssemble(){
        return strBuilder.toString();
    }


    public void makeLineGraphics(int numBadGuess)  {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        int numberRefGraph = 0;
        for(int i =0;i<(numBadGuess-1);i++) {
            numberRefGraph = i+1;
            switch (numberRefGraph){
                case 1:
                    gc.strokeLine(35, 65, 35, 425);
                    break;
                case 2:
                    gc.strokeLine(35, 65, 335, 65);
                    break;
                case 3:
                    gc.strokeLine(35,425, 335, 425);
                    break;
                case 4:
                    gc.strokeLine(340,65,340,425);
                    break;
                case 5:
                    gc.strokeLine(35,185, 340, 185);
                    break;
                case 6:
                    gc.strokeLine(35,305,340,305);
                    break;
                case 7:
                    gc.strokeLine(140,65,140,425);
                    break;
                case 8:
                    gc.strokeLine(240,65, 240,425);
                    break;

                default:
                    System.out.println(" Restart the Game. ");
                    break;
            }
        }
    }

    @Override
    public void reloadWorkspace() {
    }

    @Override
    public void changeToLevelSelectionScene() {

    }

    public void restartGame(int curr) throws IOException {
        app.getGUI().changetoGameplayScreen();
        displayLetters();
        gameplayScreen(curr);
    }

}


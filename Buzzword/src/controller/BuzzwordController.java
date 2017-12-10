    package controller;

    import apptemplate.AppTemplate;
    import data.GameData;
    import data.User;
    import gui.Workspace;
    import javafx.animation.AnimationTimer;
    import javafx.event.EventHandler;
    import javafx.scene.canvas.GraphicsContext;
    import javafx.scene.control.*;
    import javafx.scene.input.*;
    import javafx.scene.layout.*;
    import javafx.scene.shape.Rectangle;
    import ui.AppMessageDialogSingleton;
    import ui.YesNoCancelDialogSingleton;
    import java.io.*;
    import java.security.NoSuchAlgorithmException;
    import java.util.ArrayList;
    import static java.lang.System.*;

    /**
     * @author Biki Gurung
     */

    public class BuzzwordController implements FileController {

        private AppTemplate appTemplate; // shared reference to the application
        public GraphicsContext gc;
        ProfileController profileController;
        FlowPane toolbarPane;
        String currentUsername;
        String currentPassword;
        int currentLevel;
        GameData gameData;
        Button userNameButton;
        Button startGame;
        ComboBox<String> selectGameMode;
        String correctWord;
        StringBuilder strBuilder;
        int counterGuessLetters=0;
        GridPane guessLetterBox;
        StackPane[] guessStackPane;
        GridPane letterPointPane;
        StringBuilder strTypedWord= new StringBuilder(20);
        //StringBuilder mouseTypesWords;
        Button[] gridButtons;
        GridPane gameGridPane;
        int levelPoints;
        ArrayList<String> inputArrayList= new ArrayList<>();
        String currentGameMode;
        Button nextLevel;
        GridPane totalPane;
        ArrayList<String> strArray;
        int restartLevelholder=1;
        int tempDictLevel=1;
        int tempPokeLevel=1;
        int tempPeopleLevel=1;
        int tempAnimalLevel=1;

        public BuzzwordController(AppTemplate appTemplate) {
            this.appTemplate = appTemplate;
            gameData = (GameData) appTemplate.getDataComponent();
            try {
                profileController = new ProfileController(appTemplate);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        public void start() {
            Workspace gameWorkspace = (Workspace) appTemplate.getWorkspaceComponent();
            gc = gameWorkspace.getCanvas();
        }


        @Override
        public void handleExitRequest() {
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
            }catch (Exception e){
                // do nothing
            }
        }

        public boolean handleLoginRequest() throws IOException {

            String inputUsername=  appTemplate.getGUI().getUsernameRequest();
            String inputPassword= appTemplate.getGUI().getPasswordRequest();

            if(profileController.checkLoginRequest(inputUsername, inputPassword)){
                getUserDetails();
                setUpGameModeHandlers();
                return true;
            }
            return false;
        }

        public void getUserDetails(){
            User user= profileController.getUser();
            currentUsername=user.getUsername();
            currentPassword=user.getPassword();
           // currentLevel=user.getLevelCompeleted();
        }

        public void handleLogoutButton(){
            toolbarPane.getChildren().removeAll(userNameButton, selectGameMode, startGame);
        }


        public void setUpGameModeHandlers(){
            toolbarPane = appTemplate.getGUI().getToolbarPane();
            userNameButton = new Button("Welcome, " + currentUsername.toUpperCase());
            startGame = new Button("START GAME");
            startGame.setDisable(true);

            selectGameMode = new ComboBox();
            final Tooltip selctGameModeTool = new Tooltip();
            selctGameModeTool.setText(" CHOOSE A GAME MODE TO PLAY");
            selectGameMode.setTooltip(selctGameModeTool);

            selectGameMode.setValue("SELECT GAME MODE");
            selectGameMode.getItems().addAll(
                    "PEOPLE",
                    "DICTIONAIRY",
                    "ANIMALS",
                    "POKEMON"
            );

            selectGameMode.setOnAction(e -> startGame.setDisable(false));


            final Tooltip startGameTool = new Tooltip();
            startGameTool.setText(" CHOOSE A GAME MODE AND THEN PRESS START");
            startGame.setTooltip(startGameTool);


            toolbarPane.getChildren().addAll(userNameButton, selectGameMode, startGame);
            startGame.setOnAction(e -> {
                checkGameLevel(selectGameMode.getValue().toString());
            });
        }


        public void checkGameLevel(String gamemode) {
            Workspace gameworkspace = (Workspace) appTemplate.getWorkspaceComponent();
            boolean flag=true;
            currentGameMode = gamemode;
            switch (gamemode){
                case "ANIMALS":
                    String animalStr = gameData.getAnimalsTargetWord().toUpperCase();
                    animalStr =  checkString(animalStr);
                    //restartLevelholder= tempAnimalLevel;

                    while(flag) {
                        if(animalStr.length() > 3) {
                            if (animalStr.length() < 15) {
                                flag= false;
                                if(tempAnimalLevel > gameData.getAnimalsLevel()){
                                    gameworkspace.changeToLevelSelectionScene(gamemode, animalStr,tempAnimalLevel);
                                }else{
                                    gameworkspace.changeToLevelSelectionScene(gamemode, animalStr,gameData.getAnimalsLevel());
                                }
                                out.println(animalStr);

                            }
                            else {
                                animalStr =  checkString(animalStr);
                            }
                        } else {
                            animalStr =  checkString(animalStr);
                        }
                    }
                    flag= true;
                    break;

                case "DICTIONAIRY":
                    String dictionairyStr = gameData.getDictionairyWord().toUpperCase();
                    dictionairyStr =  checkString(dictionairyStr);
                    //restartLevelholder= tempDictLevel;
                    while(flag) {

                        if(dictionairyStr.length() > 3) {
                            if (dictionairyStr.length() < 15) {
                                flag= false;
                                if(tempDictLevel > gameData.getDicitionairyLevel()){
                                    gameworkspace.changeToLevelSelectionScene(gamemode, dictionairyStr,tempDictLevel);
                                    out.println(dictionairyStr);
                                }else{
                                    gameworkspace.changeToLevelSelectionScene(gamemode, dictionairyStr,gameData.getDicitionairyLevel());
                                }
                            }
                            else {
                                dictionairyStr =  checkString(dictionairyStr);
                                }
                        } else {
                            dictionairyStr =  checkString(dictionairyStr);
                        }
                        out.println(dictionairyStr);

                    }
                    flag= true;
                    break;
                case "PEOPLE":
                    String peopeleStr = gameData.getPeopleTargetWord().toUpperCase();
                    peopeleStr =  checkString(peopeleStr);

                    while(flag) {
                        if(peopeleStr.length() > 3) {
                            if (peopeleStr.length() < 15) {
                                flag= false;
                                if(tempPeopleLevel > gameData.getFamousPeoplevel()){
                                    gameworkspace.changeToLevelSelectionScene(gamemode, peopeleStr,tempPeopleLevel);
                                }else{
                                    gameworkspace.changeToLevelSelectionScene(gamemode, peopeleStr,gameData.getFamousPeoplevel());
                                }
                                out.println(peopeleStr);
                            }
                            else {
                                peopeleStr =  checkString(peopeleStr);
                            }
                        }
                        else {
                            peopeleStr =  checkString(peopeleStr);
                        }
                    }
                    flag= true;
                    break;
                case "POKEMON":
                    String pokemonStr= gameData.getPokmonTargetWord().toUpperCase();
                    pokemonStr =  checkString(pokemonStr);

                    while(flag) {
                        if(pokemonStr.length() > 3) {
                            if (pokemonStr.length() < 15) {
                                pokemonStr = gameData.getPokmonTargetWord().toUpperCase();
                                flag= false;
                                if(tempPokeLevel > gameData.getPokemonLevel()){
                                    gameworkspace.changeToLevelSelectionScene(gamemode, pokemonStr,tempPokeLevel);
                                }else{
                                    gameworkspace.changeToLevelSelectionScene(gamemode, pokemonStr,gameData.getPokemonLevel());
                                }
                                out.println(pokemonStr);
                            }
                            else {
                                pokemonStr =  checkString(pokemonStr);
                            }
                        }
                        else {
                            pokemonStr =  checkString(pokemonStr);
                        }
                    }
                    flag= true;
                    break;
                default:
                    break;
            }
        }


        public String checkString( String wordStringChoice) {
            char[] newStringWord= wordStringChoice.toCharArray();
            correctWord=null;
            boolean flag= true;
            for (char checkTargetWord : newStringWord) {
                if (checkTargetWord >= 'A' && checkTargetWord <= 'Z') {
                    //do nothing
                } else {
                    flag = false;
                    break;
                }
            }

            if(flag){
                correctWord = wordStringChoice;
                return correctWord;
            }
            else{
                correctWord = checkString(gameData.getAnimalsTargetWord());    // Using recursion to load the incorrect string again
                return correctWord;
            }

        }

        @Override
        public boolean handleCreateProfileRequest() throws IOException {
            String username = appTemplate.getGUI().getLoginUsername();
            String password= appTemplate.getGUI().getLoginPassword();
            boolean profileExist = profileController.checkUserExist(username, password);
            return profileExist;
        }

        public void buildGuessLabel(){
            Workspace gameworkspace = (Workspace) appTemplate.getWorkspaceComponent();
            guessLetterBox= gameworkspace.getGuessedLettersBox();
            guessStackPane=new StackPane[16];
            levelPoints= gameworkspace.getLevelPoints();
            getButtonsFromWorkspace();
            nextLevel= appTemplate.getGUI().getNextLevelButton();
        }

        public void inititalizeGuessPlay(){
            counterGuessLetters = 0;
            StringBuilder mouseStringBuilder= new StringBuilder(20);
            for(int i=0;i<16;i++){
                gridButtons[i].setStyle(" -fx-background-color:lightblue; -fx-font-size: 20; -fx-text-fill: black");
            }
        }


        public void makePointsPane() {
            Workspace gameworkspace = (Workspace) appTemplate.getWorkspaceComponent();
            letterPointPane = gameworkspace.getLettersPointPane();
            StackPane[] pointsStackPane = new StackPane[6];
            totalPane= gameworkspace.getTotalGridPane();


            Label TotalLabel= new Label("TOTAL:   ");
            TotalLabel.setMinWidth(25);
            TotalLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 30px");
            totalPane.add(TotalLabel,0,1);

            Label TotalPoint= new Label("00  ");
            TotalPoint.setMinWidth(5);
            TotalPoint.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 30px");
            totalPane.add(TotalPoint,1,1);


            Label guessedWordsLabel;
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    guessedWordsLabel = new Label("      ");
                } else if (i == 2) {
                    guessedWordsLabel = new Label("      ");
                } else if (i == 3) {
                    guessedWordsLabel = new Label("     ");
                } else if (i == 4) {
                    guessedWordsLabel = new Label("   ");
                } else if (i == 5) {
                    guessedWordsLabel = new Label("      ");
                } else {
                    guessedWordsLabel = new Label("      ");
                }

                guessedWordsLabel.setMinWidth(30);
                guessedWordsLabel.setStyle(" -fx-text-fill: black; -fx-font-size: 30px");
                letterPointPane.add(guessedWordsLabel, 1, i);

                Label guessPoints = new Label("   ");
                guessPoints.setStyle("-fx-text-fill: black; -fx-font-size: 30px");
                pointsStackPane[i] = new StackPane();
                pointsStackPane[i].setStyle("-fx-border-color: black");
                pointsStackPane[i].getChildren().addAll(guessPoints);
                letterPointPane.add(pointsStackPane[i], 2, i);
            }

                Label totalLabel= new Label("                 ");
                totalLabel.setMinWidth(30);
                totalLabel.setStyle("-fx-background-color: white; -fx-text-fill: white; -fx-font-size: 30px");
                letterPointPane.add(totalLabel,1,6);
                Label totalPoint= new Label("     ");
                totalPoint.setStyle("-fx-background-color: white; -fx-text-fill: white; -fx-font-size: 30px");
                letterPointPane.add(totalPoint,2,6);
        }

        public void getButtonsFromWorkspace(){
            Workspace gameworkspace = (Workspace) appTemplate.getWorkspaceComponent();
            gridButtons = gameworkspace.getButtonPressed();
            gameGridPane = gameworkspace.getDisplayLetterGrid();
        }

        public void play() {

            final int[] num = {0};
            final int[] sum = {0};

            inputArrayList= new ArrayList<>();
            StringBuilder mouseStringBuilder= new StringBuilder(20);

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    Workspace gameWorkspace = (Workspace) appTemplate.getWorkspaceComponent();

                    gameGridPane.setOnMouseDragExited(e -> {
                        checkWordEnter();
                    });

                    gameGridPane.setOnDragDetected(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            gameGridPane.startFullDrag();       // Set the gridpane to full drag for dragable

                            for(int i=0;i<16;i++){
                                int finalI = i;
                                gridButtons[i].setOnMousePressed(e -> {         // when the mouse is dragged along
                                    gridButtons[finalI].setOnDragDetected(event1 -> {   //Detect the mouse drag

                                        gridButtons[finalI].startFullDrag();        // After than enable the mouse drag
                                        gridButtons[finalI].setOnMouseReleased(e2 -> {  // Check The string when  drag is released
                                            checkWordEnter();
                                        });
                                        gridButtons[finalI].setOnMouseDragEntered(evt -> {      // Check when the mouse drag in entered
                                            displayMouseWords(finalI);
                                            checkInputString();
                                        });
                                    });
                                });
                                gameGridPane.setOnMouseReleased(e -> {
                                    checkWordEnter();
                                    gridButtons[finalI].setOnDragDetected(event1 -> {         // check the release when on gridpane
                                        gameGridPane.startFullDrag();
                                    });
                                });

                                gridButtons[i].setOnMouseDragEntered(e -> {     // Check the entered on the buttons
                                    displayMouseWords(finalI);
                                    checkInputString();
                                });
                            }
                            event.consume();
                        }
                    });


                    for(int i=0;i<16;i++){
                        int finalI = i;
                        gridButtons[i].setOnMouseClicked(e -> {
                            gridButtons[finalI].setOnDragDetected(event1 -> {
                                System.out.println("Mouse Dragged when clicked");
                            });
                            displayMouseWords(finalI);
                            checkInputString();
                        });
                    }


                    appTemplate.getGUI().getGameplayScene().setOnKeyTyped((KeyEvent event) -> {
                        char guess=0;
                        int countGuess=0;

                        try{
                            guess=Character.toLowerCase(event.getCharacter().charAt(0));

        //<------------- Trying for the KeyBoard!!!

    //                        String wordListing = gameWorkspace.getStringAssemble();
    //                        strArray= new ArrayList<String>();
    //                        strArray.add(guess+"");
    //                        String ok= strArray.get(strArray.size()-1);
    //                        char okk= ok.charAt(0);
    //
    //                        char[] chars=new char[16];
    //                        for(int i=0;i<16;i++){
    //                            chars[i] = wordListing.toLowerCase().charAt(i);
    //                        }
    //                        for(int i=0;i<16;i++){
    //                            if( guess == chars[i]){
    //
    //                                System.out.print(i+" ");
    //
    //                                switch (i){
    //                                    case 0:
    //                                        System.out.println(okk);
    //                                        System.out.println(wordListing.toLowerCase().charAt(0));
    //
    //                                        if( okk == wordListing.toLowerCase().charAt(1)){
    //                                            System.out.println("Hello 1");
    //                                            gridButtons[0].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(4)){
    //                                            System.out.println("Hello 2");
    //                                            gridButtons[0].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            System.out.println("Hello 3");
    //                                            gridButtons[0].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 1:
    //                                        if(okk == wordListing.toLowerCase().charAt(0)){
    //                                            gridButtons[1].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(2)){
    //                                            gridButtons[1].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(4)){
    //                                            gridButtons[1].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[1].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[1].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 2:
    //                                        if(okk == wordListing.toLowerCase().charAt(1)){
    //                                            gridButtons[2].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(3)){
    //                                            gridButtons[2].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[2].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[2].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(7)){
    //                                            gridButtons[2].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 3:
    //                                        if(okk == wordListing.toLowerCase().charAt(2)){
    //                                            gridButtons[3].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[3].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(7)){
    //                                            gridButtons[3].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 4:
    //                                        if(okk == wordListing.toLowerCase().charAt(0)){
    //                                            gridButtons[4].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(1)){
    //                                            gridButtons[4].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[4].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(8)){
    //                                            gridButtons[4].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[4].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 5:
    //                                        if(okk == wordListing.toLowerCase().charAt(0)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(1)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(2)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(4)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(8)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 6:
    //                                        if(okk == wordListing.toLowerCase().charAt(1)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(2)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(3)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(7)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(11)){
    //                                            gridButtons[5].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 7:
    //                                        if(okk == wordListing.toLowerCase().charAt(2)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(3)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(11)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 8:
    //                                        if(okk == wordListing.toLowerCase().charAt(4)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(12)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(13)){
    //                                            gridButtons[7].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 9:
    //                                        if(okk == wordListing.toLowerCase().charAt(4)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(8)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(12)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(13)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(14)){
    //                                            gridButtons[9].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 10:
    //                                        if(okk == wordListing.toLowerCase().charAt(5)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(7)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(11)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(13)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(14)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(15)){
    //                                            gridButtons[10].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 11:
    //                                        if(okk == wordListing.toLowerCase().charAt(6)){
    //                                            gridButtons[11].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(7)){
    //                                            gridButtons[11].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[11].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(14)){
    //                                            gridButtons[11].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(15)){
    //                                            gridButtons[11].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 12:
    //                                        if(okk == wordListing.toLowerCase().charAt(8)){
    //                                            gridButtons[12].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[12].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(13)){
    //                                            gridButtons[12].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 13:
    //                                        if(okk == wordListing.toLowerCase().charAt(8)){
    //                                            gridButtons[13].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[13].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[13].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(12)){
    //                                            gridButtons[13].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(14)){
    //                                            gridButtons[13].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 14:
    //                                        if(okk == wordListing.toLowerCase().charAt(9)){
    //                                            gridButtons[14].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[14].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(11)){
    //                                            gridButtons[14].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(13)){
    //                                            gridButtons[14].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(15)){
    //                                            gridButtons[14].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    case 15:
    //                                        if(okk == wordListing.toLowerCase().charAt(10)){
    //                                            gridButtons[15].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(14)){
    //                                            gridButtons[15].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        if(okk == wordListing.toLowerCase().charAt(11)){
    //                                            gridButtons[15].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
    //                                        }
    //                                        break;
    //                                    default:
    //                                        System.out.println("error");
    //
    //                                }
    //
    //                            }
    //                        }
                        }catch(Exception e){
                            // do nothing because there is some exceptions thrown
                        }
                        for(int i=0;i<16;i++){
                            if(gridButtons[i].getText().toLowerCase().charAt(0) == guess){
                                gridButtons[i].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
                            }
                        }

                        if(guess >='a' && guess <='z'){        // validate user input to only alphabets
                            guessStackPane[counterGuessLetters]=new StackPane();

                            Rectangle rectLetters= new Rectangle(13,25);
                            strTypedWord.append(guess);
                            Label guessLabel = new Label(guess +"");
                            guessLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20px");

                            guessStackPane[counterGuessLetters].getChildren().addAll(rectLetters, guessLabel);
                            guessLetterBox.add(guessStackPane[counterGuessLetters], counterGuessLetters , 0);
                            counterGuessLetters++;

                            checkInputString();
                        }
                        else{

                        }
                    });


                    appTemplate.getGUI().getGameplayScene().setOnKeyPressed((KeyEvent event) -> {

                        if(event.getCode() == KeyCode.ENTER){
                            checkWordEnter();
                        }else{

                        }
                    });
                    if (gameWorkspace.getTimeSeconds().getValue() <=0 ){
                        stop();
                    }
                }

                public void checkWordEnter(){
                    Workspace gameWorkspace = (Workspace) appTemplate.getWorkspaceComponent();
                    AppMessageDialogSingleton checkWordValidation= AppMessageDialogSingleton.getSingleton();

                    boolean checkBoolean=false;
                    boolean checkInputExists=false;
                    strArray= new ArrayList<String>();
                    if(strTypedWord.toString().length() > 3){
                        checkBoolean= gameWorkspace.checkIfWordsIsGenereated(strTypedWord.toString());
                        checkInputExists = checkAddedString(strTypedWord.toString());
                    }

                    if (checkBoolean){
                        if(checkInputExists){

                            //checkWordValidation.show("Checking Your Word", "Your Word "+strTypedWord.toString()+ " is Correct.!");
                            Label guessedWordsLabel = new Label(strTypedWord.toString());
                            guessedWordsLabel.setMinWidth(30);
                            guessedWordsLabel.setStyle(" -fx-text-fill: black; -fx-font-size: 30px");

                            letterPointPane.add(guessedWordsLabel, 1, num[0]);
                            sum[0] = sum[0] +strTypedWord.toString().length()*5;

                            if(sum[0]>=levelPoints){
                                try {
                                    AppMessageDialogSingleton stageCleared= AppMessageDialogSingleton.getSingleton();
                                    stageCleared.show("Stage Cleared", "You have cleared this level please try new stage!!");
                                    nextLevel.setDisable(false);
                                    currentLevel= gameWorkspace.getLevel();
                                    levelClearedIncrease(currentUsername, currentGameMode, (gameWorkspace.getLevel()) +""); // current level not updated !

                                    // For the temporary update of the Level in the game:
                                    switch (currentGameMode){
                                        case "DICTIONAIRY":
                                            tempDictLevel=gameWorkspace.getLevel()+1;
                                            System.out.println(tempDictLevel+"tempDictLevel");
                                            break;
                                        case "ANIMALS":
                                            //tempAnimalLevel=tempAnimalLevel+1;
                                            tempAnimalLevel=gameWorkspace.getLevel()+1;

                                            break;
                                        case "PEOPLE":
                                            // tempPeopleLevel=tempPeopleLevel+1;
                                            tempPeopleLevel=gameWorkspace.getLevel()+1;


                                            break;
                                        case "POKEMON":
                                            //tempPokeLevel=tempPokeLevel+1;
                                            tempPokeLevel=gameWorkspace.getLevel()+1;

                                            break;
                                        default:
                                            //do nothing
                                            break;
                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                            Label TotalPoint= new Label(sum[0]+"");
                            TotalPoint.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 30px");
                            totalPane.add(TotalPoint,1,1);

                            Label guessPoints = new Label(strTypedWord.toString().length()*5+"");
                            guessPoints.setStyle("-fx-text-fill: black; -fx-font-size: 30px");

                            StackPane pointsStackPane = new StackPane();
                            pointsStackPane.setStyle("-fx-border-color: black");
                            pointsStackPane.getChildren().addAll(guessPoints);

                            letterPointPane.add(pointsStackPane, 2, num[0]);

                            num[0]++;
                            strTypedWord= new StringBuilder(20);
                        }else{
                            //checkWordValidation.show("Checking Your Word", "Your Word "+strTypedWord.toString()+ " is already Guessed.!");
                        }
                    }
                    else{
                        //checkWordValidation.show("Checking Your Word", "Your Word "+strTypedWord.toString()+ " is Not Correct.!");
                    }
                    strTypedWord.delete(0,20);
                    guessLetterBox.getChildren().removeAll(guessStackPane);
                    inititalizeGuessPlay();
                }

                public void displayMouseWords(int finalI){
                    gridButtons[finalI].setStyle("-fx-border-color: darkcyan; -fx-border-insets: 10px");
                    mouseStringBuilder.append(gridButtons[finalI].getText().toLowerCase().charAt(0));
                    strTypedWord.append(gridButtons[finalI].getText().toLowerCase().charAt(0));

                    guessStackPane[counterGuessLetters]=new StackPane();
                    Rectangle rectLetters= new Rectangle(13,25);

                    Label guessLabel = new Label(gridButtons[finalI].getText().charAt(0) +"");
                    guessLabel.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 20px");

                    guessStackPane[counterGuessLetters].getChildren().addAll(rectLetters, guessLabel);
                    guessLetterBox.add(guessStackPane[counterGuessLetters], counterGuessLetters , 0);
                    counterGuessLetters++;
                }

                public void checkInputString(){
                    AppMessageDialogSingleton wrongInput= AppMessageDialogSingleton.getSingleton();

                    if(counterGuessLetters >= 15){
                        try{
                            //wrongInput.show("Maximum Word Invalidation  ", "Your Word is more than the required letter");
                            checkWordEnter();
                        }catch (Exception e){
                            // do nothing
                        }
                        //guessLetterBox.getChildren().removeAll(guessStackPane);
                        strTypedWord.delete(0,20);
                        //inititalizeGuessPlay();
                    }
                    else{
                        // do nothing
                    }
                }

                @Override
                public void stop() {
                    super.stop();
                    end();
                }


                // Dispaly the solution to all the work to the user!
                public void displayAll(){
                    Workspace gameWorkspace = (Workspace) appTemplate.getWorkspaceComponent();

                    ArrayList<String> aList= gameWorkspace.getArrayWords();
                    sum[0]=0;

                    letterPointPane.getChildren().clear();

                    for(int i=0 ; i < aList.size();i++){
                        Label guessedWordsLabel = new Label(aList.get(i).toString());
                        guessedWordsLabel.setMinWidth(30);
                        guessedWordsLabel.setStyle(" -fx-text-fill: black; -fx-font-size: 30px");

                        letterPointPane.add(guessedWordsLabel, 1, i);
                        sum[0] = sum[0] + aList.get(i).toString().length()*5;

                        Label guessPoints = new Label(aList.get(i).toString().length()*5+"");
                        guessPoints.setStyle("-fx-text-fill: black; -fx-font-size: 30px");

                        StackPane pointsStackPane = new StackPane();
                        pointsStackPane.setStyle("-fx-border-color: black");
                        pointsStackPane.getChildren().addAll(guessPoints);

                        letterPointPane.add(pointsStackPane, 2, i);

                        Label TotalPoint= new Label(sum[0]+"");
                        TotalPoint.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 30px");
                        totalPane.add(TotalPoint,1,1);
                    }

                }

                public void end(){
                    for(int i=0;i<16;i++){
                        gridButtons[i].setDisable(true);
                    }

                    displayAll();

                    appTemplate.getGUI().getGameplayScene().setOnKeyPressed((KeyEvent event) -> {
                        if (event.getCode() == KeyCode.ENTER) {
                            AppMessageDialogSingleton checkWordValidation= AppMessageDialogSingleton.getSingleton();
                            checkWordValidation.show("Time is Finished", "Sorry !! Times UP!!!! All the words are displayed on ->");

                        }
                    });
                    appTemplate.getGUI().getGameplayScene().setOnKeyTyped((KeyEvent event) -> {
                        char guess = 0;
                        try {
                            guess = Character.toLowerCase(event.getCharacter().charAt(0));

                        } catch (Exception e) {
                            // do nothing because there is some exceptions thrown
                        }
                        if (guess >= 'a' && guess <= 'z') {
                            AppMessageDialogSingleton checkWordValidation= AppMessageDialogSingleton.getSingleton();
                            checkWordValidation.show("Time is Finished", "Sorry !! Times UP!!!! All the words are displayed on ->");
                        }// validate user input to on
                    });

                }
            };
            timer.start();
        }

        public void levelClearedIncrease(String Username, String gamemode, String level ) throws IOException{
            profileController.updateLevelClearence(Username,gamemode, level);

        }

        public int getTempDictLevel(){
            return  tempDictLevel;
        }

        public int getTempAnimalLevel(){
            return tempAnimalLevel;
        }

        public int getTempPokeLevel(){
            return  tempAnimalLevel;
        }

        public int getTempPeopleLevel(){
            return tempPokeLevel;
        }

        public boolean checkAddedString(String inputString){
            if(!inputArrayList.contains(inputString)){
                inputArrayList.add(inputString);
                return true;
            }
            return false;
        }

        @Override
        public void handleRestartGame(){
            Workspace gameWorkspace = (Workspace) appTemplate.getWorkspaceComponent();
            restartLevelholder= gameWorkspace.getLevel();
            try {
                System.out.println(restartLevelholder);
                gameWorkspace.restartGame(restartLevelholder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleNextLevel(){
            Workspace gameWorkspace = (Workspace) appTemplate.getWorkspaceComponent();

            int nextLevelholder= gameWorkspace.getLevel()+1;
            gameWorkspace.LevelSelectorData(nextLevelholder+"");
            try {
                System.out.println(restartLevelholder);
                gameWorkspace.restartGame(nextLevelholder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

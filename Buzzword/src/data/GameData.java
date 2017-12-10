package data;

import apptemplate.AppTemplate;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import components.AppDataComponent;
import controller.BuzzwordController;
import controller.GameError;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author Biki Gurung
 */
public class GameData implements AppDataComponent {

    static int peopleLevel=1;
    static int  animalsLevel=1;
    static int  dicitionairyLevel=1;
    static int pokemonLevel = 1;
    private static String username;
    private static String password;
    AppTemplate    appTemplate;
    BuzzwordController controller;

    public GameData(AppTemplate appTemplate) {
        this(appTemplate, false);
        controller= new BuzzwordController(appTemplate);
        animalsLevel        = 1;
        dicitionairyLevel   = 1;
        pokemonLevel        = 1;
        peopleLevel         = 1;
    }

    public GameData(AppTemplate appTemplate, boolean initiateGame) {
        if (initiateGame) {
            this.appTemplate = appTemplate;
        } else {
            this.appTemplate = appTemplate;
        }
    }


    public String getAnimalsTargetWord() {
        URL wordsResource = getClass().getClassLoader().getResource("words/dictionary.txt");
        assert wordsResource != null;

        int toSkip = new Random().nextInt(330622);

        try (Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
            return lines.skip(toSkip).findFirst().get();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        throw new GameError("Unable to load initial target word.");
    }

    public String getPeopleTargetWord(){
        URL wordsResource = getClass().getClassLoader().getResource("words/dictionary.txt");
        assert wordsResource != null;

        int toSkip = new Random().nextInt(330622);

        try (Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
            return lines.skip(toSkip).findFirst().get();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        throw new GameError("Unable to load initial target word.");
    }

    public String getDictionairyWord(){
        //getTempDictLevel();
        URL wordsResource = getClass().getClassLoader().getResource("words/dictionary.txt");
        assert wordsResource != null;

        int toSkip = new Random().nextInt(330622);

        try (Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
            return lines.skip(toSkip).findFirst().get();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        throw new GameError("Unable to load initial target word.");
    }

    public String getPokmonTargetWord(){
        URL wordsResource = getClass().getClassLoader().getResource("words/pokemon.txt");
        assert wordsResource != null;

        int toSkip = new Random().nextInt(719);

        try (Stream<String> lines = Files.lines(Paths.get(wordsResource.toURI()))) {
            return lines.skip(toSkip).findFirst().get();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }
        throw new GameError("Unable to load initial target word.");
    }

    public static void loadGameLevel(File file, String Username) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(file);
        boolean flag=true;
        String username="";
        while (!jsonParser.isClosed()) {
            JsonToken token = jsonParser.nextToken();
            if (JsonToken.FIELD_NAME.equals(token)) {
                String fieldname = jsonParser.getCurrentName();
                switch (fieldname) {
                    case "USERNAME":
                        jsonParser.nextToken();
                        username = jsonParser.getValueAsString();
                        break;
                    case "PASSWORD":
                        jsonParser.nextToken();
                        password = jsonParser.getValueAsString();
                        break;
                    case "DICTIONAIRY":
                        jsonParser.nextToken();
                        dicitionairyLevel = jsonParser.getValueAsInt();
                        break;
                    case "ANIMALS":
                        jsonParser.nextToken();
                        animalsLevel = jsonParser.getValueAsInt();
                        break;
                    case "PEOPLE":
                        jsonParser.nextToken();
                        peopleLevel = jsonParser.getValueAsInt();
                        break;
                    case "POKEMON":
                        jsonParser.nextToken();
                        pokemonLevel = jsonParser.getValueAsInt();
                        if(username.equals(Username)){
                            flag =false;
                        }
                        break;
                    default:
                        throw new JsonParseException(jsonParser, "Unable to load JSON data");
                }
                if(!flag){
                    break;
                }
            }

        }
    }

    public static int getFamousPeoplevel() { return peopleLevel; }

    public static int getAnimalsLevel() {
        return animalsLevel;
    }

    public static int getDicitionairyLevel() {
        return dicitionairyLevel;
    }

    public static int getPokemonLevel(){ return pokemonLevel; }


    public void getTempDictLevel(){
        int tempLevel=controller.getTempDictLevel();
        if(tempLevel > dicitionairyLevel){
            dicitionairyLevel = tempLevel;
        }
    }


    @Override
    public void reset(){

    }

}

package controller;

import apptemplate.AppTemplate;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data.GameData;
import data.User;
import propertymanager.PropertyManager;
import security.SecurityManager;
import static settings.AppPropertyType.APP_TITLE;
import static settings.InitializationParameters.APP_WORKDIR_PATH;

/**
 * Created by BG on 11/19/16.
 */
public class ProfileController {

    AppTemplate appTemplate;
    File file;
    User user;
    SecurityManager securityManager;
    final String PASSWORD = "PASSWORD";
    final String USERNAME = "USERNAME";
    int initialLevel = 1;
    HashMap<String, Object> arraylist;


    public ProfileController(AppTemplate appTemplate) throws NoSuchAlgorithmException {
        this.appTemplate = appTemplate;
        file = declareClasspath().toFile();
        user = new User("", "");
        arraylist = new HashMap();
        securityManager = new SecurityManager();

    }

    public Path declareClasspath() {
        PropertyManager propertyManager = PropertyManager.getManager();
        Path appDirPath = Paths.get(propertyManager.getPropertyValue(APP_TITLE)).toAbsolutePath();
        Path targetPath = appDirPath.resolve(APP_WORKDIR_PATH.getParameter());
        return targetPath;
    }


    public void saveUserInfo(String username, String password) throws IOException {

        JsonFactory jsonFactory = new JsonFactory();

        try (OutputStream out = new FileOutputStream(file, true)) {
            JsonGenerator generator = jsonFactory.createGenerator(out, JsonEncoding.UTF8);

            generator.writeStartObject();
            generator.writeStringField(USERNAME, username);
            generator.writeStringField(PASSWORD, password);
            generator.writeStringField("DICTIONAIRY", String.valueOf(initialLevel));
            generator.writeStringField("ANIMALS", String.valueOf(initialLevel));
            generator.writeStringField("PEOPLE", String.valueOf(initialLevel));
            generator.writeStringField("POKEMON", String.valueOf(initialLevel));
            generator.writeEndObject();
            generator.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public boolean checkUserExist(String Username, String Password) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(String.valueOf(file.toPath())));
        if (br.readLine() == null) {
            saveUserInfo(Username, Password);
            return false;
        }
        final InputStream in = new FileInputStream(file);
        try {
            LinkedHashMap field = null;
            for (Iterator it = new ObjectMapper().readValues(
                    new JsonFactory().createJsonParser(in), Map.class); it.hasNext(); ) {
                field = (LinkedHashMap) it.next();
                if (field.containsKey(USERNAME) && field.containsValue(Username)) {
                    return true;
                }
            }
        } finally {
            in.close();
        }
        saveUserInfo(Username, Password);
        return false;
    }


    public boolean checkLoginRequest(String Username, String Password) throws IOException {
        final InputStream in = new FileInputStream(file);
        try {
            LinkedHashMap field = null;
            for (Iterator it = new ObjectMapper().readValues(
                    new JsonFactory().createJsonParser(in), Map.class); it.hasNext(); ) {
                field = (LinkedHashMap) it.next();
                if (field.containsKey(USERNAME) && field.containsValue(Username) && field.containsKey(PASSWORD)) {
                    List<List<String>> l = new ArrayList<List<String>>(field.values());
                    String s = String.valueOf(l.get(1));
                    if (securityManager.securityDecryption(Password).equals(s)) {
                        user.setUsername(Username);
                        user.setPassword(Password);
                        GameData.loadGameLevel(file, Username);
                        return true;
                    }
                }
            }

        } finally {
            in.close();
        }
        return false;
    }

    public User getUser() {
        return user;
    }


    /**
     * Neccessary Changes here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * @param username
     * @param gamemode
     * @param playingLevel
     * @throws IOException
     */


    public void updateLevelClearence(String username, String gamemode, String playingLevel) throws IOException {

        final InputStream in = new FileInputStream(file);
        try{
        LinkedHashMap field = null;
        for (Iterator it = new ObjectMapper().readValues(new JsonFactory().createJsonParser(in), Map.class); it.hasNext(); ) {
            field = (LinkedHashMap) it.next();

            if (field.containsKey(USERNAME) && field.containsValue(username)) {
                List<List<String>> listValue = new ArrayList<List<String>>(field.values());
                String s = "";

                switch (gamemode) {
                    case "DICTIONAIRY":
                        s = String.valueOf(listValue.get(2));
                        if (s.charAt(0) == playingLevel.charAt(0)) {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode rootArray = mapper.readTree(file);
                            int val= s.charAt(0)-48;
                            int  newValue= val+1;
                            ((ObjectNode) rootArray).put(gamemode, newValue+"");
                            mapper.writeValue(file, rootArray);
                        }
                        break;
                    case "ANIMALS":
                        s = String.valueOf(listValue.get(3));
                        if (s.charAt(0) >= playingLevel.charAt(0)) {

                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode rootArray = mapper.readTree(file);
                            int val= s.charAt(0)-48;
                            int  newValue= val+1;
                            ((ObjectNode) rootArray).put(gamemode, newValue+"");
                            mapper.writeValue(file, rootArray);
                        }

                        break;
                    case "PEOPLE":
                        s = String.valueOf(listValue.get(4));
                        if (s.charAt(0) >= playingLevel.charAt(0)) {

                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode rootArray = mapper.readTree(file);
                            int val= s.charAt(0)-48;
                            int  newValue= val+1;
                            ((ObjectNode) rootArray).put(gamemode, newValue+"");
                            mapper.writeValue(file, rootArray);
                        }

                        break;
                    case "POKEMON":
                        s = String.valueOf(listValue.get(5));
                        if (s.charAt(0) >= playingLevel.charAt(0)) {
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode rootArray = mapper.readTree(file);
                            int val= s.charAt(0)-48;
                            int  newValue= val+1;
                            ((ObjectNode) rootArray).put(gamemode, newValue+"");
                            mapper.writeValue(file, rootArray);
                        }

                        break;
                    default:
                        break;
                }
                break;
            }
        }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
        in.close();
        }
    }
}





//<------- Need to make some changes up in the update later so please see the code below for corrections!!!


//        final InputStream in = new FileInputStream(file);
//        try {
//            LinkedHashMap field = null;
//
//            for (Iterator it = new ObjectMapper().readValues(new JsonFactory().createJsonParser(in), Map.class); it.hasNext(); ) {
//                field = (LinkedHashMap) it.next();
//                System.out.println("Step 3");
//
//
//                if (field.containsKey(USERNAME) && field.containsValue(username)) {
//                    List<List<String>> listValue = new ArrayList<List<String>>(field.values());
//                    String s = "";
//
//
//                    ObjectMapper mapper= new ObjectMapper();
//                    JsonNode rootNode= mapper.readTree(file);
//                    System.out.println(rootNode.toString());
//
//                    ((ObjectNode) rootNode).put(gamemode, "10");
//                    System.out.println(rootNode.toString());
//                    mapper.writeValue(file, rootNode);
//                    //Files.write(file, rootNode, StandardOpenOption.APPEND)
//
//                    switch (gamemode) {
//                        case "DICTIONAIRY":
//                            s = String.valueOf(listValue.get(2));
//                            if (s.charAt(0) >= playingLevel.charAt(0)) {
//                                field.replace("DICTIONAIRY", s, "3");
//
//                                try (OutputStream out = new FileOutputStream(file, true)) {
//                                    JsonFactory jsonFactory = new JsonFactory();
//                                    JsonGenerator generator = jsonFactory.createGenerator(out, JsonEncoding.UTF8);
//                                    generator.writeStartObject();
//
//                                    generator.writeStringField(USERNAME, username);
//                                    generator.writeStringField(PASSWORD, String.valueOf(listValue.get(1)));
//                                    generator.writeStringField("DICTIONAIRY", "3");
//                                    generator.writeStringField("ANIMALS", String.valueOf(listValue.get(3)));
//                                    generator.writeStringField("PEOPLE", String.valueOf(listValue.get(4)));
//                                    generator.writeStringField("POKEMON", String.valueOf(listValue.get(5)));
//                                    generator.writeEndObject();
//                                    generator.close();
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                    System.exit(1);
//                                } finally {
//                                    out.close();
//                                }
//                            }
//
//                            break;
//                        case "ANIMALS":
//                            s = String.valueOf(listValue.get(3));
//                            if (s.charAt(0) >= playingLevel.charAt(0)) {
//                                field.replace(gamemode.toUpperCase(), s, s.charAt(0) + 1);
//                            }
//
//                            break;
//                        case "PEOPLE":
//                            s = String.valueOf(listValue.get(4));
//                            if (s.charAt(0) >= playingLevel.charAt(0)) {
//                                field.replace(gamemode.toUpperCase(), s, s.charAt(0) + 1);
//                            }
//
//                            break;
//                        case "POKEMON":
//                            s = String.valueOf(listValue.get(5));
//                            if (s.charAt(0) >= playingLevel.charAt(0)) {
//                                field.replace(gamemode.toUpperCase(), s, s.charAt(0) + 1);
//                            }
//
//                            break;
//                        default:
//                            break;
//                    }
//                }
//                break;
//            }
//        } finally {
//            in.close();
//        }
//    }
//
//}

//JSONPObject jsonObject = (JSONPObject) json.parse(new FileReader(file.getAbsolutePath()));

//
//    }
//}



//        for(JsonNode root : rootArray){
//            ro
//            System.out.println(root);
//        }


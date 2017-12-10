package data;

/**
 * Created by BG on 11/20/16.
 */

public class User {

    String username;
    String password;
    int levelCompeleted;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevelCompeleted() {
        return levelCompeleted;
    }

    public void setLevelCompeleted(int levelCompeleted) {
        this.levelCompeleted = levelCompeleted;
    }



}

package dao.obj;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by luongduy on 2/25/16.
 */
public class UserObj implements Serializable {
    private int userId;
    private String email;
    private String password;
    private Date registeredDate;
    private String status;
    private int entryHighScore;
    private int kanjiHighScore;

    public UserObj() {

    }

    public UserObj(String email, String password) {
        this.userId = 0;
        this.email = email;
        this.password = password;
        this.registeredDate = new Date(new java.util.Date().getTime());
        this.status = "active";
        this.entryHighScore = 0;
        this.kanjiHighScore = 0;
    }

    public enum UserStatus {
        active,
        inactive
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registedDate) {
        this.registeredDate = registedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status.name();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getKanjiHighScore() {
        return kanjiHighScore;
    }

    public void setKanjiHighScore(int kanjiHighScore) {
        this.kanjiHighScore = kanjiHighScore;
    }

    public int getEntryHighScore() {
        return entryHighScore;
    }

    public void setEntryHighScore(int entryHighScore) {
        this.entryHighScore = entryHighScore;
    }
}

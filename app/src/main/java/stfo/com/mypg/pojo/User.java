package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 22/02/17.
 */
public class User {
    private String name, email, company, currentPG;

    public User() {
    }

    public User(String name, String email, String company, String currentPG) {
        this.name = name;
        this.email = email;
        this.company = company;
        this.currentPG = currentPG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCurrentPG() {
        return currentPG;
    }

    public void setCurrentPG(String currentPG) {
        this.currentPG = currentPG;
    }
}

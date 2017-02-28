package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class ChatMessage {
    private String Message;
    private Boolean User;

    public ChatMessage() {
    }

    public ChatMessage(String message, Boolean user) {
        Message = message;
        User = user;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Boolean getUser() {
        return User;
    }

    public void setUser(Boolean user) {
        User = user;
    }
}

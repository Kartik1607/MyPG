package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 27/02/17.
 */
public class ComplaintAdmin extends Complaint {

    String userEmail;

    public ComplaintAdmin(String complaintID, String date, String message, String userEmail) {
        super(complaintID,  date, message);
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

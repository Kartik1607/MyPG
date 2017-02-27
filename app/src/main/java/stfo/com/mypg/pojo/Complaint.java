package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 25/02/17.
 */
public class Complaint {
    private Long ComplaintID,Status;
    private String Date,Message;

    public Complaint() {
    }

    public Complaint(Long complaintID, Long status, String date, String message) {
        ComplaintID = complaintID;
        Status = status;
        Date = date;
        Message = message;
    }

    public Long getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(Long complaintID) {
        ComplaintID = complaintID;
    }

    public Long getStatus() {
        return Status;
    }

    public void setStatus(Long status) {
        Status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setLastMessage(String Message) {
        this.Message = Message;
    }
}

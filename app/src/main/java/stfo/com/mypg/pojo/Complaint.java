package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 25/02/17.
 */
public class Complaint {
    private String ComplaintID;
    private Long Status;
    private String Date,Message;

    public Complaint() {
    }

    public Complaint(String complaintID, Long status, String date, String message) {
        ComplaintID = complaintID;
        Status = status;
        Date = date;
        Message = message;
    }

    public String getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(String complaintID) {
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

    public void setMessage(String Message) {
        this.Message = Message;
    }
}

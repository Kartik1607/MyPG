package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 25/02/17.
 */
public class Complaint {
    private Long ComplaintID,Status;
    private String Date,LastMessage;

    public Complaint() {
    }

    public Complaint(Long complaintID, Long status, String date, String lastMessage) {
        ComplaintID = complaintID;
        Status = status;
        Date = date;
        LastMessage = lastMessage;
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

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }
}

package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 23/02/17.
 */
public class Payment {
    private String Date;
    private Long Status,Amount;

    public Payment(){}

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Long getStatus() {
        return Status;
    }

    public void setStatus(Long status) {
        Status = status;
    }

    public Long getAmount() {
        return Amount;
    }

    public void setAmount(Long amount) {
        Amount = amount;
    }
}

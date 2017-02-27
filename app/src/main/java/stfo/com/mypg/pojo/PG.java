package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 20/02/17.
 */
public class PG {
    private String Image, Location, Facilities ,Phone, Address;
    private Double Latitude,Longitude;
    private long Price;

    public PG() {
    }

    public String getAddress() {
        return Address;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getImage() {
        return Image;
    }

    public String getLocation() {
        return Location;
    }

    public String getFacilities() {
        return Facilities;
    }

    public long getPrice() {
        return Price;
    }

    public String getPhone() {
        return Phone;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setFacilities(String facilities) {
        Facilities = facilities;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setPrice(long price) {
        Price = price;
    }
}

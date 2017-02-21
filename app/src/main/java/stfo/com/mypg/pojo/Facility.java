package stfo.com.mypg.pojo;

/**
 * Created by Kartik Sharma on 21/02/17.
 */
public class Facility {
    private String name;
    private int image;

    public Facility(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}

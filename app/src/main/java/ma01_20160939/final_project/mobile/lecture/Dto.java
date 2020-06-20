package ma01_20160939.final_project.mobile.lecture;

public class Dto {
    private long id;
    private String name;        //상호명
    private String hour;        //영업시간
    private String phone;       //전화번호
    private String address;     //주소
    private double latitude;     //위도
    private double longitude;    //경도

    public Dto(String name, String phone, String address)   {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHour() { return hour; }
    public void setHour(String hour) { this.hour = hour; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return id + "," + name +", "+ hour +", " + phone + ", " +
                address + "(" + latitude + ", " + longitude + ")";
    }
}

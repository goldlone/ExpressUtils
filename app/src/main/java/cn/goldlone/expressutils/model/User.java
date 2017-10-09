package cn.goldlone.expressutils.model;

/**
 * Created by CN on 2017/8/26.
 * 权限：1-员工  2-管理员
 */

public class User {
    private String phone;
    private String password;
    private String name;
    private int level;
    private String stationNum;
    private String stationName;
    private boolean isSuccess;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", stationNum='" + stationNum + '\'' +
                ", stationName='" + stationName + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}

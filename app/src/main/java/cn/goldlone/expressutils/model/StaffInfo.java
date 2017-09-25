package cn.goldlone.expressutils.model;

import java.util.ArrayList;

/**
 * Created by CN on 2017/9/4.
 */

public class StaffInfo {
    private boolean success;
    private String info;
    private ArrayList<User> users;
    private ArrayList<User> admins;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<User> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<User> admins) {
        this.admins = admins;
    }
}

package cn.goldlone.expressutils.server;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cn.goldlone.expressutils.Datas;
import cn.goldlone.expressutils.model.ExpressInfo;
import cn.goldlone.expressutils.model.RegistInfo;
import cn.goldlone.expressutils.model.SecondSecret;
import cn.goldlone.expressutils.model.StaffInfo;
import cn.goldlone.expressutils.model.StationExpress;
import cn.goldlone.expressutils.model.UpdateFirstSecret;
import cn.goldlone.expressutils.model.User;
import cn.goldlone.expressutils.utils.HttpUtils;

/**
 * Created by CN on 2017/8/21.
 */

public class ServerApi {

    /**
     * 用户登录
     * @param phone
     * @param passwd
     * @return
     */
    public User login(String phone, String passwd) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(passwd);
        JSONObject json = new JSONObject();
        try {
            json.put("phone", phone);
            json.put("passwd", passwd);
            String result = HttpUtils.post(Datas.serverIp+"userLogin", json.toString());
            if(result==null){
                user.setSuccess(false);
                return user;
            }
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);
            user.setSuccess(res.getBoolean("success"));


            if(user.isSuccess()) {
                if(res.has("level"))
                    user.setLevel(res.getInt("level"));
                if(res.has("expStationNum"))
                    user.setStationNum(res.getString("expStationNum"));
                if(res.has("expStationName"))
                    user.setStationName(res.getString("expStationName"));
                if(res.has("name"))
                    user.setName(res.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 添加员工
     * @param user
     * @return
     */
    public RegistInfo addUser(User user) {
        RegistInfo info = new RegistInfo();
        try {
            JSONObject json = new JSONObject();
            json.put("phone", user.getPhone());
            json.put("passwd", user.getPassword());
            json.put("rePassword", user.getPassword());
            json.put("name", user.getName());
            json.put("level", user.getLevel());
            json.put("expStationNum", user.getStationNum());
            String result = HttpUtils.post(Datas.serverIp+"addUsers", json.toString());

            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);
            if(res.has("status"))
                info.setStatus(res.getBoolean("status"));
            if(res.has("info"))
                info.setInfo(res.getString("info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * 修改密码
     * @param user
     * @param oldPassword
     * @return
     */
    public RegistInfo updatePassword(User user, String oldPassword){
        RegistInfo info = new RegistInfo();

        try {
            JSONObject json = new JSONObject();
            json.put("phone", user.getPhone());
            json.put("oldPassword", oldPassword);
            json.put("newPassword", user.getPassword());
            json.put("rePassword", user.getPassword());

            String result = HttpUtils.post(Datas.serverIp+"updatePassword", json.toString());
            Log.i("修改密码", result);
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);

            info.setStatus(res.getBoolean("success"));
            info.setInfo(res.getString("info"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }


    /**
     * 获取并更新第一类解密密钥
     * @return
     */
    public UpdateFirstSecret getFirstSecret(String stationNum) {
        UpdateFirstSecret key = null;
        // 请求服务器
        String result = HttpUtils.post(Datas.serverIp+"getFirstSk", stationNum);

        if(result!=null) {
            try {
                key = new UpdateFirstSecret();
                JSONTokener jt = new JSONTokener(result);
                JSONObject json = new JSONObject(jt);

                key.setUpdate(json.getBoolean("isUpdated"));
                key.setPrivateKey(json.getString("privateKey"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return key;
    }


    /**
     * 获取第二类解密密钥
     * @return
     */
    public SecondSecret getSecondSecret(String expressNum, int encryptNum) {
        SecondSecret keys = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("expressNum", expressNum);
            obj.put("encryptNum", encryptNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 请求服务器
        String result = HttpUtils.post(Datas.serverIp+"getSk", obj.toString());
        if(result!=null) {
            try {
                JSONTokener jt = new JSONTokener(result);
                JSONObject json = new JSONObject(jt);
                if(json.has("secondDesKey") || json.has("secondRsaSK")) {
                    keys = new SecondSecret();
                    if (json.has("secondDesKey"))
                        keys.setSecondDesKey(json.getString("secondDesKey"));
                    if (json.has("secondRsaSK"))
                        keys.setSecondRsaKey(json.getString("secondRsaSK"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return keys;
    }


    // 获取本站点所有员工信息
    public StaffInfo getStationStaff(String stationNum, String phone) {
        StaffInfo info = new StaffInfo();
        try {
            JSONObject json = new JSONObject();
            json.put("expStationNum", stationNum);
            json.put("phone", phone);

            String result = HttpUtils.post(Datas.serverIp+"getUserInfoByPhone", json.toString());
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);

            info.setSuccess(res.getBoolean("success"));
            info.setInfo(res.getString("info"));
            ArrayList<User> users = new ArrayList<>();
            ArrayList<User> admins = new ArrayList<>();
            JSONArray arr = res.getJSONArray("users");
            for(int i=0; i<arr.length(); i++) {
                User user = new User();
                user.setPhone(arr.getJSONObject(i).getString("phone"));
                user.setName(arr.getJSONObject(i).getString("name"));
                user.setLevel(1);
                users.add(user);
            }
            info.setUsers(users);
            arr = res.getJSONArray("admins");
            for(int i=0; i<arr.length(); i++) {
                User user = new User();
                user.setPhone(arr.getJSONObject(i).getString("phone"));
                user.setName(arr.getJSONObject(i).getString("name"));
                user.setLevel(2);
                admins.add(user);
            }
            info.setAdmins(admins);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return info;
    }


    /**
     * 获取站点快递信息
     * @param expStationNum
     * @param date
     * @param pageNum
     * @param type
     * @return
     */
    // 接受json数据
    // 具体传入(所接收)参数:
    // 1.date:所查快递列表的日期
    // 2.expStationNum:快递站点
    // 3.pageNum:(实际当前页数)页面编号,目前每页为三条记录
    // 4.type:类型(0为以该站点为初始站点<startingNum>,1为以该站点为终点站<terminalNum>)
    // {
    //         "date":"2017-8-22",
    //         "expStationNum":"sxty11110",
    //         "pageNum":1,
    //         "type":0
    // }
    // 传回(发送到客户端)参数为1. pageNum:当前查询页  2. pageSize:分页大小 3.totalPages:总页数 4.totalRecords: 总记录条数
    // 5.list: Express对象数组 6.success: 布尔型判断
    //
    public StationExpress getStationExpress(String expStationNum, String date, int pageNum, int type) {
        StationExpress station = new StationExpress();
        try {
            JSONObject reqJson = new JSONObject();
            reqJson.put("expStationNum", expStationNum);
            reqJson.put("date", date);
            reqJson.put("pageNum", pageNum);
            reqJson.put("type", type);

            String result = HttpUtils.post(Datas.serverIp+"getExpInfo", reqJson.toString());
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);

            station.setSuccess(res.getBoolean("success"));
            station.setTotalRecords(res.getInt("totalRecords"));
            station.setPageSize(res.getInt("pageSize"));
            station.setTotalPages(res.getInt("totalPages"));
            station.setPageNum(res.getInt("pageNum"));
            JSONArray arr = res.getJSONArray("list");

            ArrayList<ExpressInfo>  list = new ArrayList<>();
            for(int i=0; i<arr.length(); i++) {
                ExpressInfo info = new ExpressInfo();
                info.setExpressNum(arr.getJSONObject(i).getString("expressNum"));
                info.setEncryptNum(arr.getJSONObject(i).getInt("encryptNum"));
                info.setGenTime(arr.getJSONObject(i).getString("genTime"));
                info.setGoodsNums(arr.getJSONObject(i).getInt("goodsNums"));
                info.setGoodsWeight((float) arr.getJSONObject(i).getDouble("goodsWeight"));
                info.setIsInsured(arr.getJSONObject(i).getInt("isInsured"));
                info.setInsuredFee(arr.getJSONObject(i).getInt("insuredFee"));
                info.setInsuredValue((float) arr.getJSONObject(i).getDouble("insuredValue"));
                info.setIsFile(arr.getJSONObject(i).getInt("isFile"));
                info.setIsGoods(arr.getJSONObject(i).getInt("isGoods"));
                info.setPayMode(arr.getJSONObject(i).getInt("payMode"));
                info.setStatus(arr.getJSONObject(i).getInt("status"));
                info.setTotalFee((float) arr.getJSONObject(i).getDouble("totalFee"));
                info.setReceiver(arr.getJSONObject(i).getString("receiver"));
                info.setReceiverNum(arr.getJSONObject(i).getString("receiverNum"));
                info.setReceiverProvince(arr.getJSONObject(i).getString("receiver_province"));
                info.setReceiverCity(arr.getJSONObject(i).getString("receiver_city"));
                info.setReceiverAreas(arr.getJSONObject(i).getString("receiver_areas"));
                info.setReceiver_details(arr.getJSONObject(i).getString("receiver_details"));
                info.setReceiverAdr(arr.getJSONObject(i).getString("receiverAdr"));
                info.setSender(arr.getJSONObject(i).getString("sender"));
                info.setSenderNum(arr.getJSONObject(i).getString("senderNum"));
                info.setSenderProvince(arr.getJSONObject(i).getString("sender_province"));
                info.setSenderCity(arr.getJSONObject(i).getString("sender_city"));
                info.setSenderAreas(arr.getJSONObject(i).getString("sender_areas"));
                info.setSenderDetails(arr.getJSONObject(i).getString("sender_details"));
                info.setSenderAdr(arr.getJSONObject(i).getString("senderAdr"));
                info.setStartingNum(arr.getJSONObject(i).getString("startingNum"));
                info.setTerminalNum(arr.getJSONObject(i).getString("terminalNum"));
                list.add(info);
            }
            station.setList(list);
        } catch (JSONException e){
            e.printStackTrace();
        }

        return station;
    }

    /**
     * 给快递添加派件员信息
     * @param phone
     * @param expressNum
     */
    public void updateExpressman(String phone, String expressNum) {
        JSONObject req = new JSONObject();
        try {
            req.put("phone", phone);
            req.put("expressNum", expressNum);

            String result = HttpUtils.post(Datas.serverIp+"updateExpressman", req.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询某个员工的具体派件情况
     * @param phone
     * @param date
     * @return
     */
    public StationExpress getStaffExpress(String phone, String date) {
        StationExpress express = new StationExpress();
        try {
            JSONObject reqJson = new JSONObject();
            reqJson.put("date", date);
            reqJson.put("phone", phone);

            String result = HttpUtils.post(Datas.serverIp+"getDeliverInfo", reqJson.toString());
            System.out.println(result);
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);

            express.setSuccess(res.getBoolean("success"));
            if(!express.isSuccess()) {
                express.setTotalPages(0);
                return express;
            }
            JSONArray arr = res.getJSONArray("expressList");

            ArrayList<ExpressInfo>  list = new ArrayList<>();
            for(int i=0; i<arr.length(); i++) {
                ExpressInfo info = new ExpressInfo();
                info.setExpressNum(arr.getJSONObject(i).getString("expressNum"));
                info.setEncryptNum(arr.getJSONObject(i).getInt("encryptNum"));
                info.setGenTime(arr.getJSONObject(i).getString("genTime"));
                info.setGoodsNums(arr.getJSONObject(i).getInt("goodsNums"));
                info.setGoodsWeight((float) arr.getJSONObject(i).getDouble("goodsWeight"));
                info.setIsInsured(arr.getJSONObject(i).getInt("isInsured"));
                info.setInsuredFee(arr.getJSONObject(i).getInt("insuredFee"));
                info.setInsuredValue((float) arr.getJSONObject(i).getDouble("insuredValue"));
                info.setIsFile(arr.getJSONObject(i).getInt("isFile"));
                info.setIsGoods(arr.getJSONObject(i).getInt("isGoods"));
                info.setPayMode(arr.getJSONObject(i).getInt("payMode"));
                info.setStatus(arr.getJSONObject(i).getInt("status"));
                info.setTotalFee((float) arr.getJSONObject(i).getDouble("totalFee"));
                info.setReceiver(arr.getJSONObject(i).getString("receiver"));
                info.setReceiverNum(arr.getJSONObject(i).getString("receiverNum"));
                info.setReceiverProvince(arr.getJSONObject(i).getString("receiver_province"));
                info.setReceiverCity(arr.getJSONObject(i).getString("receiver_city"));
                info.setReceiverAreas(arr.getJSONObject(i).getString("receiver_areas"));
                info.setReceiver_details(arr.getJSONObject(i).getString("receiver_details"));
                info.setReceiverAdr(arr.getJSONObject(i).getString("receiverAdr"));
                info.setSender(arr.getJSONObject(i).getString("sender"));
                info.setSenderNum(arr.getJSONObject(i).getString("senderNum"));
                info.setSenderProvince(arr.getJSONObject(i).getString("sender_province"));
                info.setSenderCity(arr.getJSONObject(i).getString("sender_city"));
                info.setSenderAreas(arr.getJSONObject(i).getString("sender_areas"));
                info.setSenderDetails(arr.getJSONObject(i).getString("sender_details"));
                info.setSenderAdr(arr.getJSONObject(i).getString("senderAdr"));
                info.setStartingNum(arr.getJSONObject(i).getString("startingNum"));
                info.setTerminalNum(arr.getJSONObject(i).getString("terminalNum"));
                list.add(info);
            }
            express.setList(list);
        } catch (JSONException e){
            e.printStackTrace();
        }

        return express;
    }


    /**
     * 更新物流轨迹
     * @param expressNum
     * @param expStationNum
     * @param expStationName
     * @return
     */
    public String updateTrack(String expressNum, String expStationNum, String expStationName) {
        JSONObject req = new JSONObject();
        try {
            req.put("expressNum", expressNum);
            req.put("expStationNum", expStationNum);
            req.put("expStationName", expStationName);

            System.out.println(req.toString());
            String result = HttpUtils.post(Datas.serverIp+"updateTrack", req.toString());
            System.out.println(result);
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);

            System.out.println("更新轨迹返回结果："+result);
            return res.getString("info");
        } catch (JSONException e) {
            e.printStackTrace();
            return "更新轨迹出现问题";
        }
    }

    /**
     * 获取物流轨迹
     * @param expressNum
     */
    public void getTrack(String expressNum) {
        JSONObject req = new JSONObject();
        try {
            req.put("expressNum", expressNum);
            String result = HttpUtils.post(Datas.serverIp+"getTrack", req.toString());
            JSONTokener jt = new JSONTokener(result);
            JSONObject res = new JSONObject(jt);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

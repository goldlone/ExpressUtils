package cn.goldlone.expressutils.model;

import java.io.Serializable;

/**
 * Created by CN on 2017/8/27.
 */

public class ExpressInfo implements Serializable{
    // 快递单号
    private String expressNum;
    // 加密编号
    private int encryptNum;
    // 创建时间
    private String genTime;
    // 物品数量
    private int goodsNums;
    // 物品重量
    private float goodsWeight;
    // 是否保值
    private int isInsured;
    // 投保价格
    private int insuredFee;
    // 保值额度
    private float insuredValue;
    // 是否是文件
    private int isFile;
    // 是否是物品
    private int isGoods;
    // 付款方式（货到付款还是寄件人付）
    private int payMode;
    // 收件人
    private String receiver;
    // 收件人联系方式
    private String receiverNum;

    private String receiverProvince;
    private String receiverCity;
    private String receiverAreas;
    // 收件人详细地址
    private String receiver_details;
    // 全部绝对地址
    private String receiverAdr;
    private String sender;
    private String senderNum;
    private String senderProvince;
    private String senderCity;
    private String senderAreas;
    private String senderDetails;
    private String senderAdr;
    // 起始站点编号
    private String startingNum;
    private String terminalNum;
    // 快递状态
    private int status;
    // 总计运费
    private float totalFee;

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public int getEncryptNum() {
        return encryptNum;
    }

    public void setEncryptNum(int encryptNum) {
        this.encryptNum = encryptNum;
    }

    public int getGoodsNums() {
        return goodsNums;
    }

    public void setGoodsNums(int goodsNums) {
        this.goodsNums = goodsNums;
    }

    public float getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(float goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public int getInsuredFee() {
        return insuredFee;
    }

    public void setInsuredFee(int insuredFee) {
        this.insuredFee = insuredFee;
    }

    public float getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(float insuredValue) {
        this.insuredValue = insuredValue;
    }

    public int getIsFile() {
        return isFile;
    }

    public void setIsFile(int isFile) {
        this.isFile = isFile;
    }

    public int getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(int isGoods) {
        this.isGoods = isGoods;
    }

    public int getIsInsured() {
        return isInsured;
    }

    public void setIsInsured(int isInsured) {
        this.isInsured = isInsured;
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverNum() {
        return receiverNum;
    }

    public void setReceiverNum(String receiverNum) {
        this.receiverNum = receiverNum;
    }

    public String getReceiver_details() {
        return receiver_details;
    }

    public void setReceiver_details(String receiver_details) {
        this.receiver_details = receiver_details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(float totalFee) {
        this.totalFee = totalFee;
    }

    public String getGenTime() {
        return genTime;
    }

    public void setGenTime(String genTime) {
        this.genTime = genTime;
    }

    public String getReceiverProvince() {
        return receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverAreas() {
        return receiverAreas;
    }

    public void setReceiverAreas(String receiverAreas) {
        this.receiverAreas = receiverAreas;
    }

    public String getReceiverAdr() {
        return receiverAdr;
    }

    public void setReceiverAdr(String receiverAdr) {
        this.receiverAdr = receiverAdr;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderNum() {
        return senderNum;
    }

    public void setSenderNum(String senderNum) {
        this.senderNum = senderNum;
    }

    public String getSenderProvince() {
        return senderProvince;
    }

    public void setSenderProvince(String senderProvince) {
        this.senderProvince = senderProvince;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderAreas() {
        return senderAreas;
    }

    public void setSenderAreas(String senderAreas) {
        this.senderAreas = senderAreas;
    }

    public String getSenderDetails() {
        return senderDetails;
    }

    public void setSenderDetails(String senderDetails) {
        this.senderDetails = senderDetails;
    }

    public String getSenderAdr() {
        return senderAdr;
    }

    public void setSenderAdr(String senderAdr) {
        this.senderAdr = senderAdr;
    }

    public String getStartingNum() {
        return startingNum;
    }

    public void setStartingNum(String startingNum) {
        this.startingNum = startingNum;
    }

    public String getTerminalNum() {
        return terminalNum;
    }

    public void setTerminalNum(String terminalNum) {
        this.terminalNum = terminalNum;
    }



}

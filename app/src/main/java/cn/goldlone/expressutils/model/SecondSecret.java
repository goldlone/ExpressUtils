package cn.goldlone.expressutils.model;

/**
 * 服务器返回的第二类解密密钥
 * Created by CN on 2017/8/21.
 */

public class SecondSecret {

    private String secondDesKey;
    private String secondRsaKey;

    public String getSecondDesKey() {
        return secondDesKey;
    }

    public void setSecondDesKey(String secondDesKey) {
        this.secondDesKey = secondDesKey;
    }

    public String getSecondRsaKey() {
        return secondRsaKey;
    }

    public void setSecondRsaKey(String secondRsaKey) {
        this.secondRsaKey = secondRsaKey;
    }

    @Override
    public String toString() {
        return "SecondSecret{" +
                "secondDesKey='" + secondDesKey + '\'' +
                ", secondRsaKey='" + secondRsaKey + '\'' +
                '}';
    }
}

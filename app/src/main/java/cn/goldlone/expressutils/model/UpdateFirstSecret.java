package cn.goldlone.expressutils.model;

/**
 * 获取第一类解密密钥，返回的信息
 * Created by CN on 2017/8/21.
 */

public class UpdateFirstSecret {
    // 是否更新
    private boolean isUpdate;
    // 站点私钥
    private String privateKey;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "UpdateFirstSecret{" +
                "isUpdate=" + isUpdate +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}

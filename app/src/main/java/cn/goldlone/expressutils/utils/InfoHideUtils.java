package cn.goldlone.expressutils.utils;


/**
 * Created by CN on 2017/8/27.
 */

public class InfoHideUtils {
    /**
     * 隐藏姓名
     * @param name
     * @return
     */
    public static String hideName(String name){
        StringBuilder sb = new StringBuilder();
        sb.append(name.charAt(0));
        for(int i=1; i<name.length(); i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    /**
     * 隐藏电话号码
     * @param phone
     * @return
     */
    public static String hidePhone(String phone) {
        StringBuilder sb = new StringBuilder();
        if(AccountValidatorUtil.isMobile(phone)) {
            sb.append(phone.substring(0, 3));
            sb.append("****");
            sb.append(phone.substring(7));
        } else if(phone.contains("-")) {
            sb.append(phone.substring(0, phone.indexOf("-")+1));
            for(int i=sb.length(); i<phone.length()-3; i++) {
                sb.append("*");
            }
            sb.append(phone.substring(phone.length()-3));
        } else {
            sb.append(phone.substring(0, 3));
            for(int i=0; i<phone.length()-7; i++) {
                sb.append("*");
            }
            sb.append(phone.substring(phone.length()-4));
        }

        return sb.toString();
    }

}

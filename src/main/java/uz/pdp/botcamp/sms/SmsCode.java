package uz.pdp.botcamp.sms;

import java.util.HashMap;

public class SmsCode {
    public static HashMap<String,Integer>smscode=new HashMap<>();

    public static int getCode(String phone) {
        int code= (int) (Math.random()*8999+10000);
        smscode.put(phone,code);
      return code;
    }
}

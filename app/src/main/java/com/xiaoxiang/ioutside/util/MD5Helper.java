package com.xiaoxiang.ioutside.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by oubin6666 on 2016/4/25.
 */
public class MD5Helper {

    public static String getMd5(String plainText) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            byte i;
            int k=0;
            int j=b.length;

            char str[] = new char[j * 2];
            for (int offset = 0; offset < j; offset++) {
                i = b[offset];
                str[k++] = hexDigits[i>> 4 & 0xf];
                str[k++] = hexDigits[i & 0xf];
            }
            Log.d("PhoneRegiActivity",new String(str));
            return new String(str);
            //return Base64.encodeToString(buf.toString().getBytes(),Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }


}

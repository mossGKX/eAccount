package com.ucsmy.core.utils;

import java.util.regex.Pattern;


public class CheckoutUtils {
    // 中文字符区间
    public static final String REGEX_CHINESE = "[\\u4e00-\\u9fa5]";
    // 密码校验规则
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9`~!@#$%^&*()\\-_=+{}\\[\\]:\"|;'\\\\<>?,./]{6,32}$";
    // 手机号规则
    public static final String REGEX_PHONE = "^1[3|4|5|7|8|9]\\d{9}$";
    //联系电话规则(包括手机号，固话)
    public static final String REGEX_TEL = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}|[0]{1}[0-9]{2,3}-[0-9]{7,8}$";
    // 电子邮件规则
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
    // 公共校验规则
    public static final String REGEX_COMMON = "^[0-9|a-z|A-Z]{1,50}$";

    // ip规则
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
    // 日期规则
    public static final String REGEX_DATE = "^\\d{4}\\-\\d{2}\\-\\d{2}$";
    // 邮编规则
    public static final String REGEX_ZIPCODE = "^\\d{6}$";
    // 营业执照号、税务登记证号规则
    public static final String REGEX_BUSICARDNO = "^[0-9]{15}$";
    
    /**
     * 英文与字符串
     */
    public static final String REGEX_ALPHANUMBER = "^[a-zA-Z0-9]+$";
    /**
     * 匹配数字，一位或多位
     */
    public static final String REGEX_DIGIT = "^\\d{1,}$";



    public static boolean isPhone(String phone) {
        return Pattern.matches(REGEX_PHONE, phone);
    }

    public static boolean isTel(String tel) {
        return Pattern.matches(REGEX_TEL, tel);
    }

    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }


    public static boolean isCard(String card) {
        return Pattern.matches(REGEX_COMMON, card);
    }

    public static boolean isCommonRegex(String card) {
        return Pattern.matches(REGEX_COMMON, card);
    }

    public static boolean isDate(String date) {
        return Pattern.matches(REGEX_DATE, date);
    }

    public static boolean isZipcode(String zipcode) {
        return Pattern.matches(REGEX_ZIPCODE, zipcode);
    }

    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean isTaxNo(String taxNo) {
        return Pattern.matches(REGEX_BUSICARDNO, taxNo);
    }
    
    /**
     * 校验银行卡号
     * @param bankCardStr
     * @return
     */
    public static boolean isBankCard(String bankCardStr){
    	try{
    		Long.parseLong(bankCardStr);
    	}catch(Exception e){
    		return false;
    	}
    	return LuhmCheck(bankCardStr, true);
    }
    
    /**
     * Luhm算法校验（主要用于校验卡号）
     * @param bankCardStr
     * @param bCheck true-校验；false-不校验（默认为true）
     * @return
     */
	public static boolean LuhmCheck(String bankCardStr, boolean bCheck){
		int nLength = bankCardStr.length();
		if (nLength != 16 && nLength != 19)
			return false;
		if (!bCheck)
			return true;
		int nCheck = (bankCardStr.charAt(nLength - 1) - '0');
		int sum = 0;
		nLength -= 2;
		for (int i = 0; i <= nLength; ++i){
			int j = nLength - i;
			int w = bankCardStr.charAt(j) - '0';
			if (i % 2 == 0){
				int n = w * 2;
				if (n > 9){
					sum += (n % 10);
					sum += (n / 10);
				} else
					sum += n;
			} else {
				sum += w;
			}
		}
		if ((sum + nCheck) % 10 == 0)
			return true;
		else
			return false;
	}



}

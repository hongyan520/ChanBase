package com.demo.base.util;

import android.util.Log;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 *<p>Title:PingYinUtil.java</p>
 *<p>Description:</p>
 *<p>Copyright:Copyright (c) 2012</p>
 *<p>Company:湖南科创</p>
 *@author 王博
 *@version 1.0
 *2012-2-13
 */
public class PingYinUtil{
	
	private static final String TAG ="PingYinUtil";

	/**
	*<b>Summary: </b>
	* getPingYin(将字符串中的中文转换拼音)
	* @param inputString
	* @return
	 */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
						if(temp!=null)
						{
							output += temp[0];
						}
					} else
							output += java.lang.Character.toString(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			Log.e(TAG, StringUtil.deNull(e==null?"":e.getMessage()));
		}
		return output;
	}
	
	/**
	*<b>Summary: </b>
	* converterToFirstSpell(获取字符串第一个汉字拼音的首字母)
	* @param chines
	* @return
	 */
    public static String converterToFirstSpell(String chines){
    	chines = chines.replaceAll("（", "");
    	chines = chines.replaceAll("）", "");
    	chines = chines.replaceAll("、", "");
        String pinyinName = "";      
        char[] nameChar = chines.toCharArray();      
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();      
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);      
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);      
        for (int i = 0; i < nameChar.length; i++) {      
            if (nameChar[i] > 128) {      
                try {      
                     pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);      
                 } catch (BadHanyuPinyinOutputFormatCombination e) {      
                	 Log.e(TAG, StringUtil.deNull(e==null?"":e.getMessage()));
                 }      
             }else{      
                 pinyinName += nameChar[i];      
             }      
         }      
        return pinyinName;      
     }
}

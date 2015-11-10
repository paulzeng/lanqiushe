package cn.lanqiushe.manager;
 
import java.io.File;

import cn.lanqiushe.R;
 
 
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 字符串操作工具包
 */
public class StringManager {
	/**
	 * 密码框中，控制密码的显示和隐藏的开关
	 */
	public static void togglePwd(EditText pwdEt,ImageView lock) {
		if(pwdEt.getInputType()!=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
			lock.setImageResource(R.drawable.lock);//显示密码
			pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			lock.setImageResource(R.drawable.lock);//隐藏密码
			pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);    
		}
		pwdEt.setSelection(pwdEt.getText().toString().trim().length());
	}
	

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格\s、制表符\t 、回车符\n、换行符\r 组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		return input == null || "".equals(input);
	}
	/**
	 * 判断是否手机号码
	 * @param cellphone
	 * @return
	 */
    public static boolean isBadCellphone(String cellphone){
    	return cellphone.length()!=11;
    }
    /**
     * 判断密码的格式
     * @param pwd
     * @return
     */
    public static boolean isBadPwd(String pwd){
    	return false;//密码没有做最少位数限制
    }
    public static boolean isBadVerificationCode(String code){
    	return code.length()!=6;
    }
	/**
	 * 将EditText中中文本获取到
	 */
	public static String getStringByET(EditText et) {
		return et.getText().toString().trim();
	}
 
	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	
	 
	  

	
	
}

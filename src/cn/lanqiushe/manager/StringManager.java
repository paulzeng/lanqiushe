package cn.lanqiushe.manager;
 
import java.io.File;

import cn.lanqiushe.R;
 
 
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * �ַ����������߰�
 */
public class StringManager {
	/**
	 * ������У������������ʾ�����صĿ���
	 */
	public static void togglePwd(EditText pwdEt,ImageView lock) {
		if(pwdEt.getInputType()!=InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
			lock.setImageResource(R.drawable.lock);//��ʾ����
			pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}else{
			lock.setImageResource(R.drawable.lock);//��������
			pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);    
		}
		pwdEt.setSelection(pwdEt.getText().toString().trim().length());
	}
	

	/**
	 * �жϸ����ַ����Ƿ�հ״��� �հ״���ָ�ɿո�\s���Ʊ��\t ���س���\n�����з�\r ��ɵ��ַ���
	 * �������ַ���Ϊnull����ַ���������true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		return input == null || "".equals(input);
	}
	/**
	 * �ж��Ƿ��ֻ�����
	 * @param cellphone
	 * @return
	 */
    public static boolean isBadCellphone(String cellphone){
    	return cellphone.length()!=11;
    }
    /**
     * �ж�����ĸ�ʽ
     * @param pwd
     * @return
     */
    public static boolean isBadPwd(String pwd){
    	return false;//����û��������λ������
    }
    public static boolean isBadVerificationCode(String code){
    	return code.length()!=6;
    }
	/**
	 * ��EditText�����ı���ȡ��
	 */
	public static String getStringByET(EditText et) {
		return et.getText().toString().trim();
	}
 
	/**
	 * �����ļ�����·����ȡ�ļ���
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

package cn.lanqiushe.manager;

 

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * ���ڳ���������ʵĺ�ʱ�����������ļ���д
 * @author lee
 *
 */
public class ThreadPool {
	private ExecutorService service;
	
	private ThreadPool(){
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num*2);
	}
	
	private static final ThreadPool manager= new ThreadPool();
	
	public static ThreadPool getInstance(){
		return manager;
	}
	
	public void addTask(Runnable runnable){
		
		service.execute(runnable);
	}
}

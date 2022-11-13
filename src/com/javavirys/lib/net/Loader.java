package com.javavirys.lib.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.net.HttpURLConnection;

import android.os.AsyncTask;

public class Loader extends AsyncTask<URL,Integer,StringBuffer[]>{

	public static final int IOEXCEPTION = 5010;
	
	LoaderCallback listener;
	
	public int level = 0; // нужен для внешней информации
	
	public Vector<String> paths;
	
	public Loader(LoaderCallback listener)
	{
		this.listener = listener;
		paths = new Vector<String>();
	}

	protected void onPreExecute()
	{
		if(listener != null)
			listener.onPreExecute(this);
	}
	
	@Override
	protected StringBuffer[] doInBackground(URL... params) {
		// TODO Auto-generated method stub
		
		StringBuffer[] result = new StringBuffer[params.length];
		for(int i = 0; i < params.length; i++)
		{
			paths.add(params[i].toString());
			result[i] = new StringBuffer();
			BufferedInputStream bis = null;
			try {
				URLConnection conn = params[i].openConnection();
				conn.setConnectTimeout(16000);
				int http_code = ((HttpURLConnection)conn).getResponseCode();
				if (http_code != HttpURLConnection.HTTP_OK)
				{
					LoaderException ex = new LoaderException("Loader.doInBackground().Code: " + http_code);
					ex.errorCode = http_code;
					throw ex;
				}
				bis = new BufferedInputStream(conn.getInputStream());
				int ch = 0;
				while((ch = bis.read()) != -1)
				{
					result[i].append((char)ch);
				}
			} catch (LoaderException e) {
				// TODO Auto-generated catch block
				//System.out.println("Loader.doInBackground().loaderexception: " + e.toString());
				e.printStackTrace();
				if(listener != null)
				{	
					listener.onErrorFoundExecute(this, i, e);
				}
				
				result[i] = null;
			} catch (IOException e)
			{
				//System.out.println("Loader.doInBackground().ioexception: " + e.toString());
				e.printStackTrace();
				if(listener != null)
				{	
					LoaderException ex = new LoaderException(e.toString());
					ex.errorCode = IOEXCEPTION;
					listener.onErrorFoundExecute(this, i, ex);
				}
				
				result[i] = null;
			}finally{
				try {
					if(bis != null)
						bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return result;
	}
	
	protected void onPostExecute(StringBuffer[] result)
	{
		if(listener != null)
		{
			listener.onSuccessfulExecute(this,result);
		}
	}
	
	public void	onProgressUpdate(Integer... values)
	{
		if(listener != null)
			listener.onProgressUpdate(this,values[0]);
	}
	
	public interface LoaderCallback
	{
		public void onPreExecute(Loader sender);
		
		public void onSuccessfulExecute(Loader sender,StringBuffer[] result);
		
		public void onErrorFoundExecute(Loader sender,int index,LoaderException ex);
		
		public void	onProgressUpdate(Loader sender,int progress);
	}
	
	public class LoaderException extends Exception{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public int errorCode = 0;
		
		public LoaderException(String msg)
		{
			super(msg);
		}
		
	}
	
}

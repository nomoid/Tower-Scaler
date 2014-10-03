package com.github.assisstion.towerScaler.data;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public final class DataHelper{

	private DataHelper(){
		// Not to be instantiated
	}

	public static boolean writeObjects(File output, OutputStream os,
			List<? extends Serializable> object){
		ObjectOutputStream oos = null;
		try{
			oos = new ObjectOutputStream(os);
			for(Serializable s : object){
				oos.writeObject(s);
			}
			return true;
		}
		catch(SecurityException se){
			se.printStackTrace();
			return false;
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			return false;
		}
		finally{
			if(oos != null){
				try{
					oos.close();
				}
				catch(IOException e){
					e.printStackTrace();
					return false;
				}
			}
		}
	}

	public static List<? extends Object> readObjects(InputStream is){
		ObjectInputStream ois = null;
		try{
			LinkedList<Object> list = new LinkedList<Object>();
			ois = new ObjectInputStream(is);
			try{
				Object o = ois.readObject();
				while(o != null){
					list.add(o);
					o = ois.readObject();
				}
			}
			catch(EOFException e){
			}
			return list;
		}
		catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
			return null;
		}
		catch(SecurityException se){
			se.printStackTrace();
			return null;
		}
		catch(IOException ioe){
			ioe.printStackTrace();
			return null;
		}
		finally{
			if(ois != null){
				try{
					ois.close();
				}
				catch(IOException e){
					e.printStackTrace();
					return null;
				}
			}
		}
	}
}

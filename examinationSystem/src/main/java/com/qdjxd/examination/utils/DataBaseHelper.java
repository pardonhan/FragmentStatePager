package com.qdjxd.examination.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
 
	//数据库文件目标存放路径为系统默认位置
	private static String DB_PATH;//  = "/data/data/examinationSystem/ESDataBase/";
    private String DB_NAME = null;
	private String ASSETS_NAME = null;
	
    public static String DB_NAME_BASE = "ANDROID.db";
    
    public static String DB_NAME_LOCAL = "result.db";
	//第一个文件名后缀
	private static final int ASSETS_SUFFIX_BEGIN = 1;
	//最后一个文件名后缀
	private static final int ASSETS_SUFFIX_END = 25;
    
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
    private boolean dbExist;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME_BASE, null, 1);
    	DB_NAME = DB_NAME_BASE;
    	ASSETS_NAME = DB_NAME_BASE;
        this.myContext = context;
        try {
			DB_PATH = android.os.Environment  
					.getExternalStorageDirectory().getAbsolutePath() +
					"/data/examinationSystem/ESDataBase/";//+context.getString(R.string.app_name)+"/HSEDataBase/";
			DebugLog.i("1try-->"+DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
			DB_PATH  = "/data/examinationSystem/ESDataBase/";//+context.getString(R.string.app_name)+"/HSEDataBase/";
			DebugLog.i("1catch-->"+DB_PATH);
		}
    }
    public DataBaseHelper(Context context,String vocational){
    	super(context, vocational, null, 1);
    	DB_NAME = vocational;
    	ASSETS_NAME = vocational;
    	this.myContext = context;
        try {
			DB_PATH = android.os.Environment  
					.getExternalStorageDirectory().getAbsolutePath() +
					"/data/examinationSystem/ESDataBase/";//+context.getString(R.string.app_name)+"/HSEDataBase/";
			DebugLog.i("2try-->"+DB_PATH);
		} catch (Exception e) {
			e.printStackTrace();
			DB_PATH  = "/data/examinationSystem/ESDataBase/";//+context.getString(R.string.app_name)+"/HSEDataBase/";
			DebugLog.i("2catch-->"+DB_PATH);
		}
    }
    
    /**
     * 打开数据库，首先检查是否已经打开
     */
    public void openDataBase(){
    	//检查是否已经打开数据库
    	dbExist = checkDataBase();
    	DebugLog.v(dbExist+"<--检查结果");
    	if(!dbExist){
    		createDataBase();
    	}
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        DebugLog.i(myPath);
		if (myDataBase != null) {
			myDataBase.close();
			myDataBase = null;
		}
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
    /*** Creates a empty database on the system and rewrites it with your own database. * */
    public void createDataBase(){
    	DebugLog.v("beginning  createDataBase");
    	//boolean dbExist = checkDataBase();
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are going to be able to overwrite that database with our database.
    		//this.getReadableDatabase();
        	try {
        		//打开路径，如果没有，创建
        		File dir = new File(DB_PATH);
        		if(!dir.exists()){
        			dir.mkdirs();
        		}
        		
        		File dbf = new File(DB_PATH + DB_NAME);
        		//dbf.createNewFile();
        		//判断是否创建成功
        		if(dbf.exists()){
        			dbf.delete();
        			//  copyBigDataBase();
        		}
        		copyDataBase();
    			//	PublicTools.createTableMB(this.myContext);//跟业务相关
    		} catch (IOException e) {
    			e.printStackTrace();
    			//  throw new Error("Error copying database");
        	}
    	}
    }
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	}catch(SQLiteException e){
    		DebugLog.i(e.toString());
    		e.printStackTrace();
    		//database does't exist yet.
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
 
	//复制assets下的大数据库文件时用这个
	@SuppressWarnings("unused")
	private void copyBigDataBase() throws IOException{
		InputStream myInput;
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END+1; i++) {
			myInput = myContext.getAssets().open(ASSETS_NAME + i+".db");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
		}
		myOutput.close();
	}
 
    @Override
	public synchronized void close() {
		if(myDataBase != null){
		    myDataBase.close();
		    DB_NAME = null;
		}
		super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 
	/**执行查询*/
	public Cursor queryData(String sql) {
		Cursor cur = null;
		try {
			if (myDataBase != null)
				cur = myDataBase.rawQuery(sql, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return cur;
	}
	/**执行删除、插入*/
	public boolean execNonQuery(String sql) {
		try {
			if (myDataBase != null)
				myDataBase.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveBlob(String sqlstr, Object[] args) {
		try {
			if (myDataBase != null)
				myDataBase.execSQL(sqlstr, args);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean execList(List<String> list) {
		try {
			if (myDataBase != null) {
				myDataBase.beginTransaction(); // 手动设置开始事务
				for (int i = 0; i < list.size(); i++) {
					myDataBase.execSQL(list.get(i));
				}
				myDataBase.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
				myDataBase.endTransaction(); // 处理完成
			}
		} catch (Exception e) {
			e.printStackTrace();
			myDataBase.endTransaction();
			return false;
		} finally {
		}
		return true;
	}
	
	public static final String GenerateGUID(){
        return UUID.randomUUID().toString().toUpperCase();       
    } 
	 /*** 用于测试 */
    public void del(){
    	try {
			File dbf = new File(DB_PATH + DB_NAME);
			if(dbf.exists()){
				dbf.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}

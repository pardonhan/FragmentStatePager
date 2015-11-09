package com.qdjxd.examination.examacitvity.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;

import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.DataBaseHelper;
import com.qdjxd.examination.utils.DebugLog;

/**
 * operation the database
 * @author asus
 *
 */
public class DataBaseUtils {
	/**
	 * 获取到所有题目，用于模拟考试
	 * @param context
	 * @param typeid
	 * @return
	 */
	public static ArrayList<QuestionInfo> getAllQuestions(Context context,String typeid){
		ArrayList<QuestionInfo> qList = new ArrayList<QuestionInfo>();
		String sql ="";
		if(typeid.equals("0")){
			sql = "SELECT * FROM JXD7_EX_QUESTION ORDER BY QUESTIONID";
		}else{
			sql = "SELECT * FROM JXD7_EX_QUESTION WHERE TYPEID="+typeid+" ORDER BY QUESTIONID";
		}
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		Cursor cs = db.queryData(sql);
		if(cs!=null){
			while(cs.moveToNext()){
				QuestionInfo qf= new QuestionInfo(); 
				qf.questionid = cs.getString(cs.getColumnIndex("QUESTIONID"));
				qf.typeid = cs.getString(cs.getColumnIndex("TYPEID"));
				qf.qcontent = cs.getString(cs.getColumnIndex("QCONTENT"));
				String[] as = cs.getString(cs.getColumnIndex("ANSWER")).split(",");
				for(String a:as){
					qf.answer.add(a);
				}
			
				String sql2 = "SELECT * FROM JXD7_EX_ANSWERITEM WHERE QUESTIONID='" + qf.questionid + "'";
				Cursor css = db.queryData(sql2);
				qf.answerItem = new ArrayList<AnswerInfo>();
				if(css!=null){
					while(css.moveToNext()){
						AnswerInfo af = new AnswerInfo();
						af.questionid = css.getString(css.getColumnIndex("QUESTIONID"));
						af.itemvalue = css.getString(css.getColumnIndex("ITEMVALUE"));
						af.itemcontent = css.getString(css.getColumnIndex("ITEMCONTENT"));
						qf.answerItem.add(af);
					}
					css.close();
				}
				qList.add(qf);
			}
			cs.close();
			db.close();
		}
		return qList;
	}
	
	
	/**
	 * 获取随机问题列表
	 * @param context
	 * @return
	 */
	public static ArrayList<QuestionInfo> getRandomQuestionInfo(Context context,String typeid){
		ArrayList<QuestionInfo> qList = new ArrayList<QuestionInfo>();
		String sql ="";
		if(typeid!=null) {
			if (typeid.equals("0")) {
				sql = "SELECT * FROM JXD7_EX_QUESTION ORDER BY QUESTIONID";
			} else {
				sql = "SELECT * FROM JXD7_EX_QUESTION WHERE TYPEID=" + typeid + " ORDER BY QUESTIONID";
			}
		}else if (typeid==null){
			sql = "SELECT * FROM JXD7_EX_QUESTION ORDER BY QUESTIONID";
		}
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		Cursor cs = db.queryData(sql);
		if(cs!=null){
			while(cs.moveToNext()){
				QuestionInfo qf= new QuestionInfo(); 
				qf.questionid = cs.getString(cs.getColumnIndex("QUESTIONID"));
				qf.typeid = cs.getString(cs.getColumnIndex("TYPEID"));
				qf.qcontent = cs.getString(cs.getColumnIndex("QCONTENT"));
				String[] as = cs.getString(cs.getColumnIndex("ANSWER")).split(",");
				for(String a:as){
					qf.answer.add(a);
				}
			
				String sql2 = "SELECT * FROM JXD7_EX_ANSWERITEM WHERE QUESTIONID='" + qf.questionid + "'";
				Cursor css = db.queryData(sql2);
				qf.answerItem = new ArrayList<AnswerInfo>();
				if(css!=null){
					while(css.moveToNext()){
						AnswerInfo af = new AnswerInfo();
						af.questionid = css.getString(css.getColumnIndex("QUESTIONID"));
						af.itemvalue = css.getString(css.getColumnIndex("ITEMVALUE"));
						af.itemcontent = css.getString(css.getColumnIndex("ITEMCONTENT"));
						qf.answerItem.add(af);
					}
					css.close();
				}
				qList.add(qf);
			}
			cs.close();
			db.close();
		}
		
		DataBaseHelper dbLocal = new DataBaseHelper(context,DataBaseHelper.DB_NAME_LOCAL);
		dbLocal.openDataBase();
		for(QuestionInfo que:qList){
			String sqlResult = "SELECT * FROM JXD7_EX_RANDOMRESULT WHERE QUESTIONID='"+que.questionid+"'";
			Cursor cr = dbLocal.queryData(sqlResult);
			if(cr.getCount()>0){
				cr.moveToFirst();
				//
				String[] as = cr.getString(cr.getColumnIndex("SELECTANSWER")).split(",");
				for(String a:as){
					que.selectAnswer.add(a);
				}
				que.wrongModel = cr.getInt(cr.getColumnIndex("WRONGMODEL"));
			}
			cr.close();
		}
		dbLocal.close();
		return qList;
	}
	/**
	 * 按照题目类型获取题目
	 * @param context
	 * @return
	 */
	public static ArrayList<QuestionInfo> getOrdinalQuestionInfo(Context context){
		ArrayList<QuestionInfo> qList = new ArrayList<QuestionInfo>();
		String sql = "SELECT * FROM JXD7_EX_QUESTION ORDER BY TYPEID";
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		Cursor cs = db.queryData(sql);
		if(cs!=null){
			while(cs.moveToNext()){
				QuestionInfo qf= new QuestionInfo(); 
				qf.questionid = cs.getString(cs.getColumnIndex("QUESTIONID"));
				qf.typeid = cs.getString(cs.getColumnIndex("TYPEID"));
				qf.qcontent = cs.getString(cs.getColumnIndex("QCONTENT"));
				String[] as = cs.getString(cs.getColumnIndex("ANSWER")).split(",");
				for(String a:as){
					qf.answer.add(a);
				}
				String sql2 = "SELECT * FROM JXD7_EX_ANSWERITEM WHERE QUESTIONID='"+qf.questionid+"'";
				Cursor css = db.queryData(sql2);
				qf.answerItem = new ArrayList<AnswerInfo>();
				if(css!=null){
					while(css.moveToNext()){
						AnswerInfo af = new AnswerInfo();
						af.questionid = css.getString(css.getColumnIndex("QUESTIONID"));
						af.itemvalue = css.getString(css.getColumnIndex("ITEMVALUE"));
						af.itemcontent = css.getString(css.getColumnIndex("ITEMCONTENT"));
						qf.answerItem.add(af);
					}
					css.close();
				}
				qList.add(qf);
			}
			cs.close();
			db.close();
		}
		
		DataBaseHelper dbLocal = new DataBaseHelper(context,DataBaseHelper.DB_NAME_LOCAL);
		dbLocal.openDataBase();
		for(QuestionInfo que:qList){
			String sqlResult = "SELECT * FROM JXD7_EX_ORDINALRESULT WHERE QUESTIONID='"+que.questionid+"'";
			Cursor cr = dbLocal.queryData(sqlResult);
			if(cr.getCount()>0){
				cr.moveToFirst();
				String[] as = cr.getString(cr.getColumnIndex("SELECTANSWER")).split(",");
				for(String a:as){
					que.selectAnswer.add(a);
				}
				que.wrongModel = cr.getInt(cr.getColumnIndex("WRONGMODEL"));
			}
			cr.close();
		}
		dbLocal.close();
		
		return qList;
	}
	/**
	 * 向数据库中插入新的数据
	 * @param context
	 * @return
	 */
	public static boolean insertIntoNewInfo(Context context){
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		//读取文件，依次执行
		String FILEPATH = android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath() +
				"/data/examinationSystem/ESDataBase/";
		File file  = new File(FILEPATH+"examination.txt");
		if(!file.exists()||file.length()<=0){
			return false;
		}
		BufferedReader bReader = null;
		try{
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
			String sql="";
			while((sql = bReader.readLine())!=null){
				sql = sql.trim();
				DebugLog.v(sql);
				db.execNonQuery(sql);
			}
			bReader.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * 
	 */
	public static ArrayList<QuestionInfo> getPracticeQuestionInfo(Context context){
		ArrayList<QuestionInfo> qList = new ArrayList<QuestionInfo>();
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		//String sql = "";
		return qList;
	}
	/**
	 * 问题查询方法
	 * 	根据输入的关键字查询问题结果
	 * @param context
	 * @param selectText
	 * @return
	 */
	public static ArrayList<QuestionInfo> getSelectQuestion(Context context,String selectText){
		DebugLog.v(selectText);
		ArrayList<QuestionInfo> mList = new ArrayList<QuestionInfo>();
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		String seaerchSql = "SELECT * FROM JXD7_EX_QUESTION WHERE QCONTENT LIKE '%"+selectText+"%' ORDER BY TYPEID";
		Cursor cs = db.queryData(seaerchSql);
		if(cs!=null){
			while(cs.moveToNext()){
				QuestionInfo qf= new QuestionInfo(); 
				qf.questionid = cs.getString(cs.getColumnIndex("QUESTIONID"));
				qf.typeid = cs.getString(cs.getColumnIndex("TYPEID"));
				qf.qcontent = cs.getString(cs.getColumnIndex("QCONTENT"));
				String[] as = cs.getString(cs.getColumnIndex("ANSWER")).split(",");
				for(String a:as){
					qf.answer.add(a);
				}
				String sql2 = "SELECT * FROM JXD7_EX_ANSWERITEM WHERE QUESTIONID='"+qf.questionid+"'";
				Cursor css = db.queryData(sql2);
				qf.answerItem = new ArrayList<AnswerInfo>();
				if(css!=null){
					while(css.moveToNext()){
						AnswerInfo af = new AnswerInfo();
						af.questionid = css.getString(css.getColumnIndex("QUESTIONID"));
						af.itemvalue = css.getString(css.getColumnIndex("ITEMVALUE"));
						af.itemcontent = css.getString(css.getColumnIndex("ITEMCONTENT"));
						qf.answerItem.add(af);
					}
					css.close();
				}
				mList.add(qf);
			}
			cs.close();
			db.close();
		}
		return mList;
	}
	/**
	 * 将答题结果插入到数据库中
	 * @param context
	 * @param mList
	 */
	public static void insertResult(Context context,ArrayList<QuestionInfo> mList,String flag){
		DataBaseHelper db = new DataBaseHelper(context, DataBaseHelper.DB_NAME_LOCAL);
		db.openDataBase();
		StringBuffer sb = new StringBuffer("INSERT INTO JXD7_EX_RESULT(ID,EXAMNAME) VALUES(");
		sb.append("'"+UUID.randomUUID().toString().toUpperCase()+"',");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sb.append("'"+sdf.format(new Date())+"')");
		DebugLog.v(sb);
		db.execNonQuery(sb.toString());
		db.close();
	}
	/**
	 * 保存问题答案
	 * @param context
	 * @param questioninfo
	 * @param tableName 表名
	 * @param flag
	 */
	public static void saveExamResult(Context context,QuestionInfo questioninfo ,
			String tableName,String flag){
		DataBaseHelper db = new DataBaseHelper(context, DataBaseHelper.DB_NAME_LOCAL);
		db.openDataBase();
		//如果对这个题目做出了答案
		if(questioninfo.wrongModel!=3){
			String deleteSql = "DELETE FROM "+tableName+" WHERE QUESTIONID = '"+questioninfo.questionid+"'";
			db.execNonQuery(deleteSql);
			StringBuffer sb = new StringBuffer("INSERT INTO "+tableName+"(QUESTIONID,SELECTANSWER,WRONGMODEL) VALUES");
			sb.append("('"+questioninfo.questionid+"','");
			
			if(questioninfo.selectAnswer.size()==1){
				Iterator<String> ite2 = questioninfo.selectAnswer.iterator();
				sb.append(ite2.next()+"'");
			}else{
				Iterator<String> ite2 = questioninfo.selectAnswer.iterator();
				while(ite2.hasNext()){
					sb.append(ite2.next()+",");
				}
				sb.append("'");
			}
			sb.append(",'"+questioninfo.wrongModel);
			sb.append("')");
			db.execNonQuery(sb.toString());
		}
		db.close();
	}
	
}

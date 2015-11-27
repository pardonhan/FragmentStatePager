package com.qdjxd.examination.examacitvity.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		String[] ans = {"正确","错误"};
		String[] iValue= {"1","0"};
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
				}else{
					for(int i=0;i<2;i++){
						AnswerInfo af = new AnswerInfo();
						af.questionid = qf.questionid;
						af.itemvalue = iValue[i];
						af.itemcontent = ans[i];
						qf.answerItem.add(af);
					}
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
		sb.append("'" + UUID.randomUUID().toString().toUpperCase() + "',");
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

	/**
	 *
	 * @param context
	 * @param _List
	 * @param tableName
	 */
	public static void deleteExamResult(Context context,List<QuestionInfo> _List,String tableName){
		DataBaseHelper db = new DataBaseHelper(context, DataBaseHelper.DB_NAME_LOCAL);
		db.openDataBase();
		for(QuestionInfo questionInfo:_List){
			DebugLog.i("questionInfo.wrongModel-->"+questionInfo.wrongModel);
			if(questionInfo.wrongModel!=3){
				DebugLog.i(questionInfo.questionid);
				String deleteSql = "DELETE FROM "+tableName+" WHERE QUESTIONID = '"+questionInfo.questionid+"'";
				DebugLog.i(deleteSql);
				db.execNonQuery(deleteSql);
			}
		}
		//db.execNonQuery("DELETE FROM "+tableName);
		db.close();
	}

	public static Map<String,String> selectNumbers(Context context){
		int allNum = 0;
		Map<String,String> map = new HashMap<String, String>();
		//获取题目总数 begin
		DataBaseHelper db = new DataBaseHelper(context,DataBaseHelper.DB_NAME_BASE);
		db.openDataBase();
		String sql = "SELECT COUNT(*) NUMBER FROM JXD7_EX_QUESTION";
		Cursor cs = db.queryData(sql);
		if(cs.getCount()>0){
			cs.moveToFirst();
			allNum= Integer.parseInt(cs.getString(cs.getColumnIndex("NUMBER")));
			//map.put("allNum",allNum);
			cs.close();
		}
		db.close();
		//获取题目总数 end

		//获取已经答题数目 begin
		DataBaseHelper dbh = new DataBaseHelper(context,DataBaseHelper.DB_NAME_LOCAL);
		dbh.openDataBase();
		Cursor cs2;
		//正确的数量
		String sqlRight = "SELECT COUNT(*) NUMBER FROM JXD7_EX_RANDOMRESULT WHERE WRONGMODEL = 1";
		cs2 = dbh.queryData(sqlRight);
		if(cs2.getCount()>0){
			cs2.moveToFirst();
			String right_num = cs2.getString(cs2.getColumnIndex("NUMBER"));
			map.put("right_num",right_num);
			cs2.close();
		}else{
			map.put("right_num","0");
		}
		//答错的数量
		String sqlWrong = "SELECT COUNT(*) NUMBER FROM JXD7_EX_RANDOMRESULT WHERE WRONGMODEL = 0";
		cs2 = dbh.queryData(sqlWrong);
		if(cs2.getCount()>0){
			cs2.moveToFirst();
			String wrong_num = cs2.getString(cs2.getColumnIndex("NUMBER"));
			map.put("wrong_num",wrong_num);
			cs2.close();
		}else{
			map.put("wrong_num","0");
		}
		//收藏的数量
		String sqlCollect = "SELECT COUNT(*) NUMBER FROM JXD7_EX_COLLECTQ";
		cs2 = dbh.queryData(sqlCollect);
		if(cs2.getCount()>0){
			cs2.moveToFirst();
			String wrong_num = cs2.getString(cs2.getColumnIndex("NUMBER"));
			map.put("collect_num",wrong_num);
			cs2.close();
		}else{
			map.put("collect_num","0");
		}
		dbh.close();
		int ansNum = Integer.parseInt(map.get("right_num"))+Integer.parseInt(map.get("wrong_num"));
		map.put("undone_num",(allNum - ansNum)+"");
		//获取已经答题数目 end
		return map;
	}

}

package com.qdjxd.examination.examacitvity.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 471919831441477952L;
	/**问题id*/
	public String questionid;
	/**题目类型id*/
	public String typeid;
	/**正确答案*/
	public Set<String> answer = new HashSet<String>();
	/**题目内容*/
	public String qcontent;
	/**选项信息*/
	public List<AnswerInfo> answerItem;
	/**所选答案*/
	public Set<String> selectAnswer = new HashSet<String>();
	/**是否正确 1：正确； 0： 错误  3：未作答*/
	public int wrongModel = 3;
}

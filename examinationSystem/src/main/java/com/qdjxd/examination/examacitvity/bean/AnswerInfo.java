package com.qdjxd.examination.examacitvity.bean;

import java.io.Serializable;

public class AnswerInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5148142526577941415L;
	/**问题Id*/
	public String questionid;
	/**所属选项  A B C D */
	public String itemvalue;
	/**选项内容*/
	public String itemcontent;

}

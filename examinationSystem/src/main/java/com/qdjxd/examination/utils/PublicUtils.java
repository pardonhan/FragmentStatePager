package com.qdjxd.examination.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PublicUtils {
	/**
	 *  生成指定范围的指定个数的不重复的随机数
	 *  返回一个List保存已经生成的随机数
	 * @param size 随机数生成范围
	 * @param count 随机数生成个数
	 * @return
	 */
	public static List<Integer> getRandomList(int size,int count){
		List<Integer> ranList = new ArrayList<Integer>();
		int num = 0;
		Random rand = new Random();  
		boolean[] bool = new boolean[size];
		//
		for(int i= 0;i<count;i++){
			//do{}while();循环判断是否产生了重复的随机数，如果重复则重新生成
			do{
				num = rand.nextInt(size);	
			}while(bool[num]);
			bool[num] = true;
			ranList.add(num);
		}
		return ranList;
	}
	/**
	 * 比较set中的数据是否相同
	 * @param set1
	 * @param set2
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isSetEqual(Set set1, Set set2) {

		if (set1 == null && set2 == null) {
			return true; // Both are null
		}

		if (set1 == null || set2 == null || set1.size() != set2.size()
				|| set1.size() == 0 || set2.size() == 0) {
			return false;
		}

		@SuppressWarnings("unused")
		Iterator ite1 = set1.iterator();
		Iterator ite2 = set2.iterator();

		boolean isFullEqual = true;

		while (ite2.hasNext()) {
			if (!set1.contains(ite2.next())) {
				isFullEqual = false;
			}
		}

		return isFullEqual;
	}
}

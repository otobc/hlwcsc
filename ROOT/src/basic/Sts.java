package basic;

import java.util.*;

public abstract class Sts {	
	public Object max;
	public Object min;
	public Object sum;
	public long count;
	public double avg;
	public double var;
	public double tp; // 若值为布尔型或者字符串型，则tp为该例在样本库中的比例
	
	public abstract void setSts(String id) throws Exception;
	
	public abstract void setSts(List<Integer> range) throws Exception;
}
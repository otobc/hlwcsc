package meth;

import java.lang.Math;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import basic.Sts;
import basic.Definition;

public class Meth {
	private static Map<String, Method> nmmtd = null;
	private static Map<String, Method> gemtd = null;
	private static Map<String, Method> wtmtd = null;
	public static final InnerMeth meth = new InnerMeth();
	
	private static class InnerMeth {
		@SuppressWarnings("unused")
		public double threshold1(String indexid , Object value) throws Exception {
			double ret = 0.0;
			Sts sts = new Sts(indexid);
			if (value instanceof Number) {
				ret = (double)sts.max / (double)value;
			}
			else
			{
				throw new Exception(String.format("threshold1，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double threshold2(String indexid , Object value) throws Exception {
			double ret = 0.0;
			Sts sts = new Sts(indexid);
			if (value instanceof Number) {	
				double max = (double)sts.max;
				double min = (double)sts.min;
				ret = (max + min - (double)value) / max;
			}
			else
			{
				throw new Exception(String.format("threshold2，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double threshold3(String indexid , Object value) throws Exception {
			double ret = 0.0;
			Sts sts = new Sts(indexid);
			if (value instanceof Number) {	
				double max = (double)sts.max;
				double min = (double)sts.min;
				if (Math.abs(max - min) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = (max-(double)value) / (max-min);
				}
			}
			else
			{
				throw new Exception(String.format("threshold3，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double threshold4(String indexid , Object value) throws Exception {
			double ret = 0.0;
			Sts sts = new Sts(indexid);
			if (value instanceof Number) {	
				double max = (double)sts.max;
				double min = (double)sts.min;
				if (Math.abs(max - min) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = ((double)value-min) / (max-min);
				}
			}
			else
			{
				throw new Exception(String.format("threshold4，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double normalize(String indexid , Object value) throws Exception {
			double ret = 0.0;
			Sts sts = new Sts(indexid);
			if (value instanceof Number) {	
				double avg = (double)sts.avg;
				double var = (double)sts.var;
				if (Math.abs(var) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = ((double)value-avg) / (var);
				}
			}
			else
			{
				throw new Exception(String.format("normalize，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double bool1(String indexid , Object value) throws Exception {
			double ret = 0.0;
			if (value instanceof Boolean) {
				ret = (boolean)value ? 1.0 : 0.0;
			}
			else
			{
				throw new Exception(String.format("bool1，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double bool2(String indexid , Object value) throws Exception {
			double ret = 0.0;
			Sts sts = new Sts(indexid);
			if (value instanceof Boolean) {
				double tp = (double)sts.tp; 
				ret = (boolean)value ? tp : 1.0-tp;
			}
			else
			{
				throw new Exception(String.format("bool2，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public int maxmbsp(List<Double> vector, List<Integer> level) {
			double maxmbsp = 0.0;
			int ret = 0;
			int idx = 0;
			for (Iterator<Double> i = vector.iterator();i.hasNext();) {
				double mbsp = (double)i.next();
				if (idx == 0) {
					maxmbsp = mbsp;
					ret = (int)level.get(idx);
				}
				else {
					if (mbsp > maxmbsp) {
						maxmbsp = mbsp;
						ret = (int)level.get(idx);
					}
				}
				idx += 1;
			}
			return ret;
		}
		
		private int avgmbsp(List<Double> vector, List<Integer> level, int k) {
			int ret = 0;
			double sum1 = 0.0;
			double sum2 = 0.0;
			int idx = 0;
			for (Iterator<Double> i = vector.iterator();i.hasNext();) {
				double mbsp = (double)i.next();
				sum1 += (int)level.get(idx) * Math.pow(mbsp, k);
				sum2 +=  Math.pow(mbsp, k);
				idx += 1;
			}
			
			ret = (int)Math.round(sum1/sum2);
			return ret;
		}
		
		@SuppressWarnings("unused")
		public int avgmbsp1(List<Double> vector, List<Integer> level) {
			return avgmbsp(vector, level, 1);
		}
		
		@SuppressWarnings("unused")
		public int avgmbsp2(List<Double> vector, List<Integer> level) {
			return avgmbsp(vector, level, 2);
		}
		
		@SuppressWarnings("unused")
		public double cweighting(double weight, double score){
			return weight * score;
		}
		
		@SuppressWarnings("unused")
		public double gweighting(double weight, double score){
			return Math.pow(score, weight);
		}
	}
	
	public Meth() throws NoSuchMethodException, SecurityException {
		nmmtd = new HashMap<String, Method>();
		nmmtd.put("01", InnerMeth.class.getMethod("threshold1", String.class, Object.class));
		nmmtd.put("02", InnerMeth.class.getMethod("threshold2", String.class, Object.class));
		nmmtd.put("03", InnerMeth.class.getMethod("threshold3", String.class, Object.class));
		nmmtd.put("04", InnerMeth.class.getMethod("threshold4", String.class, Object.class));
		nmmtd.put("05", InnerMeth.class.getMethod("normalize", String.class, Object.class));
		nmmtd.put("06", InnerMeth.class.getMethod("bool1", String.class, Object.class));
		nmmtd.put("07", InnerMeth.class.getMethod("bool2", String.class, Object.class));
		
		gemtd = new HashMap<String, Method>();
		gemtd.put("01", InnerMeth.class.getMethod("maxmbsp", List.class, List.class));
		gemtd.put("02", InnerMeth.class.getMethod("avgmbsp1", List.class, List.class));
		gemtd.put("03", InnerMeth.class.getMethod("avgmbsp2", List.class, List.class));
		
		wtmtd = new HashMap<String, Method>();
		wtmtd.put("01", InnerMeth.class.getMethod("cweighting", double.class, double.class));
		wtmtd.put("02", InnerMeth.class.getMethod("gweighting", double.class, double.class));
	}
	
	// 公共无量纲化函数入口
	public static double exenm(String id, String indexid, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		double ret = 0.0;
		Method method = (Method)nmmtd.get(id);
		ret = (double)method.invoke(meth, indexid, value);
		return ret;
	}
	
	// 公共综合函数入口
	public static int exege(String id, List<Double> vector, List<Integer> level) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int ret = 0;
		Method method = (Method)gemtd.get(id);
		ret = (int)method.invoke(meth, vector, level);
		return ret;
	}

	// 公共加权计算函数入口
	public static int exewt(String id, double weight, double score) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int ret = 0;
		Method method = (Method)wtmtd.get(id);
		ret = (int)method.invoke(meth, weight, score);
		return ret;
	}
	
}

package meth;

import java.lang.Math;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import db.DBConnect;
import basic.IdxSts;
import basic.LvlSts;
import basic.Sts;
import basic.Definition;

public class Meth {
	private static Map<String, Method> nmmtd = null;
	private static Map<String, Method> gemtd = null;
	private static Map<String, Method> wtmtd = null;
	public static final InnerMeth meth = new InnerMeth();
	
	private static class InnerMeth {
		@SuppressWarnings("unused")
		public double threshold1(Sts sts, Object value) throws Exception {
			double ret = 0.0;
			if (value instanceof Long) {
				ret = (long)value * 1.0 / sts.lmax * 1.0;
			}
			else if(value instanceof Double) {
				ret = (double)value / sts.dmax;
			}
			else
			{
				throw new Exception(String.format("threshold1，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double threshold2(Sts sts, Object value) throws Exception {
			double ret = 0.0;
			if (value instanceof Long) {	
				long max = sts.lmax;
				long min = sts.lmin;
				ret = (max + min - (long)value) * 1.0 / max;
			}
			else if (value instanceof Double) {	
				double max = sts.dmax;
				double min = sts.dmin;
				ret = (max + min - (double)value) / max;
			}
			else
			{
				throw new Exception(String.format("threshold2，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double threshold3(Sts sts, Object value) throws Exception {
			double ret = 0.0;
			if (value instanceof Long) {	
				long max = sts.lmax;
				long min = sts.lmin;
				if (Math.abs(max - min) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = (max-(long)value) * 1.0 / (max-min);
				}
			}
			else if (value instanceof Double) {	
				double max = sts.dmax;
				double min = sts.dmin;
				if (Math.abs(max - min) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = (max-(long)value) / (max-min);
				}
			}
			else
			{
				throw new Exception(String.format("threshold3，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double threshold4(Sts sts, Object value) throws Exception {
			double ret = 0.0;
			if (value instanceof Long) {	
				long max = sts.lmax;
				long min = sts.lmin;
				if (Math.abs(max - min) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = ((long)value-min) * 1.0 / (max-min);
				}
			}
			else if (value instanceof Double) {	
				double max = sts.dmax;
				double min = sts.dmin;
				if (Math.abs(max - min) < Definition.threhold) {
					ret = 0.5;
				}
				else {
					ret = ((long)value-min) / (max-min);
				}
			}
			else
			{
				throw new Exception(String.format("threshold4，错误的数据类型"));
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double normalize(Sts sts, Object value) throws Exception {
			double ret = 0.0;
			if (value instanceof Number) {
				double avg = 0.0;
				double var = 0.0;
				if (value instanceof Long) {	
					avg = (double)sts.lavg;
					var = (double)sts.lvar;
				}
				else if (value instanceof Double) {	
					avg = (double)sts.davg;
					var = (double)sts.dvar;
				}
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
		public double bool1(Sts sts, Object value) throws Exception {
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
		public double bool2(Sts sts, Object value) throws Exception {
			double ret = 0.0;
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
			double wsum = 0.0;
			double sum = 0.0;
			int idx = 0;
			for (Iterator<Double> i = vector.iterator();i.hasNext();) {
				double mbsp = (double)i.next();
				wsum += (int)level.get(idx) * Math.pow(mbsp, k);
				sum +=  Math.pow(mbsp, k);
				idx += 1;
			}
			
			ret = (int)Math.round(wsum/sum);
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
		public double cweighting(List<Double> weight, List<Double> score){
			double ret = 0.0;
			int idx = 0;
			for (Iterator<Double> i = weight.iterator();i.hasNext();) {
				ret += i.next() * score.get(idx);
				idx += 1;
			}
			return ret;
		}
		
		@SuppressWarnings("unused")
		public double gweighting(List<Double> weight, List<Double> score){
			double ret = 1.0;
			int idx = 0;
			for (Iterator<Double> i = weight.iterator();i.hasNext();) {
				ret *= Math.pow(i.next(), score.get(idx));
				idx += 1;
			}
			return ret;
		}
	}
	
	public Meth() throws NoSuchMethodException, SecurityException {
		nmmtd = new HashMap<String, Method>();
		nmmtd.put("01", InnerMeth.class.getMethod("threshold1", Sts.class, Object.class));
		nmmtd.put("02", InnerMeth.class.getMethod("threshold2", Sts.class, Object.class));
		nmmtd.put("03", InnerMeth.class.getMethod("threshold3", Sts.class, Object.class));
		nmmtd.put("04", InnerMeth.class.getMethod("threshold4", Sts.class, Object.class));
		nmmtd.put("05", InnerMeth.class.getMethod("normalize", Sts.class, Object.class));
		nmmtd.put("06", InnerMeth.class.getMethod("bool1", Sts.class, Object.class));
		nmmtd.put("07", InnerMeth.class.getMethod("bool2", Sts.class, Object.class));
		
		gemtd = new HashMap<String, Method>();
		gemtd.put("01", InnerMeth.class.getMethod("maxmbsp", List.class, List.class));
		gemtd.put("02", InnerMeth.class.getMethod("avgmbsp1", List.class, List.class));
		gemtd.put("03", InnerMeth.class.getMethod("avgmbsp2", List.class, List.class));
		
		wtmtd = new HashMap<String, Method>();
		wtmtd.put("01", InnerMeth.class.getMethod("cweighting", List.class, List.class));
		wtmtd.put("02", InnerMeth.class.getMethod("gweighting", List.class, List.class));
	}
	
	// 公共无量纲化函数入口
	public static double exenm(String id, Sts sts, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		double ret = 0.0;
		Method method = (Method)nmmtd.get(id);
		ret = (double)method.invoke(meth, sts, value);
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
	public static double exewt(String id, List<Double> vct, List<Double> lvl) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		double ret = 0;
		Method method = (Method)wtmtd.get(id);
		ret = (double)method.invoke(meth, vct, lvl);
		return ret;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		Meth mth = new Meth();
		DBConnect.setDbInfo();
		Object value = Long.valueOf("2");
		
		Sts sts = new IdxSts();
		sts.setSts("1");
		System.out.println(sts);
		double nmv = exenm("04", sts, value);
		System.out.println("nmv:" + nmv);
		
		sts = new LvlSts();
		List<Integer> lvl = new ArrayList<Integer>();
		lvl.add(1);lvl.add(2);lvl.add(3);
		sts.setSts(lvl);
		System.out.println(sts);
		nmv = exenm("01", sts, value);
		System.out.println("nmv:" + nmv);
		
		List<Double> vct = new ArrayList<Double>();
		vct.add(Double.valueOf("0.5"));vct.add(Double.valueOf("0.3"));vct.add(Double.valueOf("0.2"));
		int gev = exege("03", vct, lvl);
		System.out.println("gev:" + gev);
		
		List<Double> sc = new ArrayList<Double>();
		sc.add(Double.valueOf("0.5"));sc.add(Double.valueOf("0.3"));sc.add(Double.valueOf("0.2"));
		double wtv = exewt("02", vct, sc);
		System.out.println("wtv:" + wtv);
	}	
}

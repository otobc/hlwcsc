package extexp;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.wltea.expression.*;
import org.wltea.expression.datameta.Variable;

import basic.Definition;

public class Extexp {
	private static SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");	
	private static Random random = new Random(100);
	
	// 日期转换
	public static Date todate(String date) throws ParseException {
		Date ret = dateFm.parse(date);
		return ret;
	}
	
	// 计算日期
	public static Date cpdate(Date date, long offset) throws ParseException {
		Date ret = new Date();
		ret.setTime(date.getTime()+offset);
		return ret;
	}
	
	// 原始数据提取
	@SuppressWarnings("rawtypes")
	public static List<List<List<Comparable>>> select(String id, String environmentid, Date begintime, Date endtime) throws InterruptedException {
		List<List<List<Comparable>>> ret = new ArrayList<List<List<Comparable>>>(); 

		// TODO
		id = String.format("%08d", random.nextInt(100));
		for (int i = 0; i < 5; i++) {
			List<List<Comparable>> group = new ArrayList<List<Comparable>>();
			Date date = new java.util.Date();
			for (int j = 0; j < 10; j++) {
				List<Comparable> nodedata = new ArrayList<Comparable>();
				nodedata.add(id);
				nodedata.add(String.format("第%s组的原始数据", id));
				nodedata.add("00000000");
				nodedata.add(String.format("%08d", i*10+j));
				nodedata.add(random.nextLong());
				nodedata.add(date);
				group.add(nodedata);
			}
			ret.add(group);
			Thread.sleep(1000);
		}
		return ret;
	}
	
	// 基础融合函数
	@SuppressWarnings("rawtypes")
	public static List<List<Comparable>> merge_basic(List<List<List<Comparable>>> data, int flag) throws Exception {
		if (data.size() == 0) {
			throw new Exception(String.format("merge_basic，输入数据为空"));
		}
		List<List<Comparable>> ret = new ArrayList<List<Comparable>>();
		String dataid = null;
		String dataname = null;
		Object datavalue = null;
		long datavalue_l = 0;
		double datavalue_d = 0.0;
		boolean datavalue_b = false;
		long datasum_l = 0;
		double datasum_d = 0.0;
		double dataavg = 0.0;
		long datamax_l = 0;
		double datamax_d = 0.0;
		long datamin_l = 0;
		double datamin_d = 0.0;
		boolean dataall = false;
		boolean dataany = false;
		boolean datanone = true;
		Date datatime = null;
		
		for(Iterator<List<List<Comparable>>> i = data.iterator();i.hasNext();) {
			List<List<Comparable>> group = i.next();
			datasum_l = 0;
			datasum_d = 0.0;
			long count = 0;
			for(Iterator<List<Comparable>> j = group.iterator();j.hasNext();) {
				List<Comparable> nodedata = j.next();
				dataid = (String)nodedata.get(0);
				dataname = (String)nodedata.get(1);
				datavalue = nodedata.get(4);
				datatime = (Date)nodedata.get(5);
				if (datavalue instanceof Long) {
					datavalue_l = (long)datavalue;
					if (0 == count) {
						datamax_l = datamin_l = datavalue_l;
					}
					else {
						datamax_l = datavalue_l > datamax_l ? datavalue_l : datamax_l;
						datamin_l = datavalue_l < datamin_l ? datavalue_l : datamin_l;
					}
					datasum_l += datavalue_l;
				}
				else if (datavalue instanceof Double) {
					datavalue_d = (double)datavalue;
					if (0 == count) {
						datamax_d = datamin_d = datavalue_d;
					}
					else {
						datamax_d = datavalue_d > datamax_d ? datavalue_d : datamax_d;					
						datamin_d = datavalue_d < datamin_d ? datavalue_d : datamin_d;
					}
					datasum_d += datavalue_d;
				}
				else if (datavalue instanceof Boolean) {
					datavalue_b = (Boolean)datavalue;
					dataall &= datavalue_b;
					dataany |= datavalue_b;
					datanone &= !datavalue_b;
				}
				else {
					throw new Exception(String.format("merge_basic，错误的数据类型"));
				}
				count += 1;
			}
			
			List<Comparable> merge = new ArrayList<Comparable>();
			merge.add(dataid);
			merge.add(dataname);
			switch (flag) {
			case Definition.MergeBasicArgs.AVG:
				if (datavalue instanceof Long) {
					dataavg = datasum_l / 1.0 * group.size();
				}
				else {
					dataavg = datasum_d / group.size();
				}
				merge.add(dataavg);
				break;
			case Definition.MergeBasicArgs.SUM:
				if (datavalue instanceof Long) {
					merge.add(datasum_l);
				}
				else if (datavalue instanceof Double) {
					merge.add(datasum_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.MAX:
				if (datavalue instanceof Long) {
					merge.add(datamax_l);
				}
				else if (datavalue instanceof Double) {
					merge.add(datamax_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.MIN:
				if (datavalue instanceof Long) {
					merge.add(datamin_l);
				}
				else if (datavalue instanceof Double) {
					merge.add(datamin_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.ALL:
				if (datavalue instanceof Boolean) {
					merge.add(dataall);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.ANY:
				if (datavalue instanceof Boolean) {
					merge.add(dataany);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.NONE:
				if (datavalue instanceof Boolean) {
					merge.add(datanone);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			default:
				throw new Exception(String.format("merge_basic，错误的参数[%d]", flag));
			}
			
			merge.add(datatime);
			ret.add(merge);
		}
		return ret;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object estimate_basic(List<List<Comparable>> data, Date t, int flag) throws Exception {
		if (data.size() == 0) {
			throw new Exception(String.format("estimate_basic，输入数据为空"));
		}
		Object ret = null;
		Object datavalue = null;
		long datavalue_l = 0;
		double datavalue_d = 0.0;
		double dataavg = 0.0;
		long datasum_l = 0;
		double datasum_d = 0.0;
		long datamax_l = 0;
		double datamax_d = 0.0;
		long datamin_l = 0;
		double datamin_d = 0.0;
		Object datanearest = null;
		Object dataearlier = null;
		Date earlier = null;
		Object datalater = null;
		Date later = null;
		Date datatime = null;
		
		long count = 0;
		for(Iterator<List<Comparable>> i = data.iterator();i.hasNext();) {
			List<Comparable> nodedata = i.next();
			datavalue = nodedata.get(2);
			datatime = (Date)nodedata.get(3);
			if (datavalue instanceof Long) {
				datavalue_l = (long)datavalue;
				if (0 == count) {
					datamax_l = datamin_l = datavalue_l;
				}
				else {
					datamax_l = datavalue_l > datamax_l ? datavalue_l : datamax_l;
					datamin_l = datavalue_l < datamin_l ? datavalue_l : datamin_l;
				}
				datasum_l += datavalue_l;
			}
			else if (datavalue instanceof Double) {
				datavalue_d = (double)datavalue;
				if (0 == count) {
					datamax_d = datamin_d = datavalue_d;
				}
				else {
					datamax_d = datavalue_d > datamax_d ? datavalue_d : datamax_d;					
					datamin_d = datavalue_d < datamin_d ? datavalue_d : datamin_d;
				}
				datasum_d += datavalue_d;
			}
			
			if (datatime.before(t) || datatime.equals(t)) {
				if (earlier == null || earlier.before(datatime)) {
					earlier = datatime;
					dataearlier = datavalue;
				}
			}
			if (datatime.after(t) || datatime.equals(t)) {
				if (later == null || later.after(datatime)) {
					later = datatime;
					datalater = datavalue;
				}
			}
			count += 1;
		}
		
		if (earlier == null) {
			datanearest = datalater;
		}
		else if (later == null) {
			datanearest = dataearlier;
		}
		else {
			long diff_earlier = t.getTime() - earlier.getTime();
			long diff_later = later.getTime() - t.getTime();
			if (diff_earlier < diff_later) {
				datanearest = dataearlier;
			}
			else {
				datanearest = datalater;
			}
		}
		
		switch (flag) {
		case Definition.EstimateBasicArgs.AVG:
			if (datavalue instanceof Long) {
				dataavg = datasum_l / 1.0 * data.size();
			}
			else {
				dataavg = datasum_d / data.size();
			}
			ret = dataavg;
			break;
		case Definition.EstimateBasicArgs.MAX:
			if (datavalue instanceof Long) {
				ret = datamax_l;
			}
			else if (datavalue instanceof Double) {
				ret = datamax_d;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型不匹配", flag));
			}
			break;
		case Definition.EstimateBasicArgs.MIN:
			if (datavalue instanceof Long) {
				ret = datamin_l;
			}
			else if (datavalue instanceof Double) {
				ret = datamin_d;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型不匹配", flag));
			}
			break;
		case Definition.EstimateBasicArgs.NEAREST:
			ret = datanearest;
			break;
		case Definition.EstimateBasicArgs.EARLIER:
			if (earlier != null) {
				ret = dataearlier;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]，时间[%s]超出范围", flag, dateFm.format(t)));
			}
			break;
		case Definition.EstimateBasicArgs.LATER:
			if (later != null) {
				ret = datalater;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]，时间[%s]超出范围", flag, dateFm.format(t)));
			}
			break;
		default:
			throw new Exception(String.format("estimate_basic，错误的参数[%d]", flag));
		}
		
		return ret;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {

		Date t = new Date();
		String expression = null;
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable. createVariable ("t", t));
		
		expression = "$SELECT(\"A\", \"B\", $TODATE(\"2016-01-06 10:00:00.00\"), $TODATE(\"2016-01-06 10:03:00.00\"))";
		List<List<List<Comparable>>> rawdata = (List<List<List<Comparable>>>)ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(rawdata);
		
		expression = "$MERGE_BASIC($SELECT(\"A\", \"B\", $TODATE(\"2016-01-06 10:00:00.00\"), $TODATE(\"2016-01-06 10:03:00.00\")), 1)";
		List<List<Comparable>> mergedata = (List<List<Comparable>>)ExpressionEvaluator. evaluate (expression, variables);		
		System.out.println(mergedata);
		
		expression = "$ESTIMATE_BASIC($MERGE_BASIC($SELECT(\"A\", \"B\", $TODATE(\"2016-01-06 10:00:00.00\"), $TODATE(\"2016-01-06 10:03:00.00\")), 1), t, 0)";
		Object estimatedata = ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(estimatedata);
	}
}

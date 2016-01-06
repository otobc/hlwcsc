package extexp;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.wltea.expression.*;
import org.wltea.expression.datameta.Variable;
import basic.Definition;

public class Extexp {
	private static SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");

	private static Thread thread = Thread.currentThread(); 	
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
	public static List select(String id, String environmentid, Date begintime, Date endtime) {
		List ret = new ArrayList(); 

		// DEBUG
		id = String.format("%08d", random.nextInt(100));
		for (int i = 0; i < 5; i++) {
			List group = new ArrayList();
			Date date = new java.util.Date();
			for (int j = 0; j < 10; j++) {
				List nodedata = new ArrayList();
				nodedata.add(id);
				nodedata.add(String.format("第%s组的原始数据", id));
				nodedata.add("00000000");
				nodedata.add(String.format("%08d", i*10+j));
				nodedata.add(random.nextDouble());
				nodedata.add(date);
				group.add(nodedata);
			}
			ret.add(group);
			//thread.sleep(1000);
		}
		return ret;
	}
	
	// 基础融合函数
	public static List merge_basic(List data, int flag) throws Exception {
		if (data.size() == 0) {
			throw new Exception(String.format("merge_basic，输入数据为空"));
		}
		List ret = new ArrayList();
		String dataid = null;
		String dataname = null;
		Object datavalue = null;
		int datavalue_i = 0;
		double datavalue_d = 0.0;
		boolean datavalue_b = false;
		int datasum_i = 0;
		double datasum_d = 0.0;
		double dataavg = 0.0;
		int datamax_i = 0;
		double datamax_d = 0.0;
		int datamin_i = 0;
		double datamin_d = 0.0;
		boolean dataall = false;
		boolean dataany = false;
		boolean datanone = true;
		Date datatime = null;
		
		for(Iterator i = data.iterator();i.hasNext();) {
			List group = (List)i.next();
			datasum_i = 0;
			datasum_d = 0.0;
			int count = 0;
			for(Iterator j = group.iterator();j.hasNext();) {
				List nodedata = (List)j.next();
				dataid = (String)nodedata.get(0);
				dataname = (String)nodedata.get(1);
				datavalue = nodedata.get(4);
				datatime = (Date)nodedata.get(5);
				if (datavalue instanceof Integer) {
					datavalue_i = (int)datavalue;
					if (0 == count) {
						datamax_i = datamin_i = datavalue_i;
					}
					else {
						datamax_i = datavalue_i > datamax_i ? datavalue_i : datamax_i;
						datamin_i = datavalue_i < datamin_i ? datavalue_i : datamin_i;
					}
					datasum_i += datavalue_i;
				}
				if (datavalue instanceof Double) {
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
			
			List merge = new ArrayList();
			merge.add(dataid);
			merge.add(dataname);
			switch (flag) {
			case Definition.MergeBasicArgs.AVG:
				dataavg = datasum_d / group.size();
				merge.add(dataavg);
				break;
			case Definition.MergeBasicArgs.SUM:
				if (datavalue instanceof Integer) {
					merge.add(datasum_i);
				}
				else if (datavalue instanceof Double) {
					merge.add(datasum_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.MAX:
				if (datavalue instanceof Integer) {
					merge.add(datamax_i);
				}
				else if (datavalue instanceof Double) {
					merge.add(datamax_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.MIN:
				if (datavalue instanceof Integer) {
					merge.add(datamin_i);
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
	
	public static Object estimate_basic(List data, Date t, int flag) throws Exception {
		if (data.size() == 0) {
			throw new Exception(String.format("estimate_basic，输入数据为空"));
		}
		Object ret = null;
		
		String dataid = null;
		String dataname = null;
		Object datavalue = null;
		int datavalue_i = 0;
		double datavalue_d = 0.0;
		double dataavg = 0.0;
		int datasum_i = 0;
		double datasum_d = 0.0;
		int datamax_i = 0;
		double datamax_d = 0.0;
		int datamin_i = 0;
		double datamin_d = 0.0;
		Object datanearest = null;
		Date nearest = null;
		Object dataearlier = null;
		Date earlier = null;
		Object datalater = null;
		Date later = null;
		Date datatime = null;
		
		int count = 0;
		for(Iterator i = data.iterator();i.hasNext();) {
			List nodedata = (List)i.next();
			dataid = (String)nodedata.get(0);
			dataname = (String)nodedata.get(1);
			datavalue = nodedata.get(2);
			datatime = (Date)nodedata.get(3);
			if (datavalue instanceof Integer) {
				datavalue_i = (int)datavalue;
				if (0 == count) {
					datamax_i = datamin_i = datavalue_i;
				}
				else {
					datamax_i = datavalue_i > datamax_i ? datavalue_i : datamax_i;
					datamin_i = datavalue_i < datamin_i ? datavalue_i : datamin_i;
				}
				datasum_i += datavalue_i;
			}
			if (datavalue instanceof Double) {
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
			nearest = later;
			datanearest = datalater;
		}
		else if (later == null) {
			nearest = earlier;
			datanearest = dataearlier;
		}
		else {
			long diff_earlier = t.getTime() - earlier.getTime();
			long diff_later = later.getTime() - t.getTime();
			if (diff_earlier < diff_later) {
				nearest = earlier;
				datanearest = dataearlier;
			}
			else {
				nearest = later;
				datanearest = datalater;
			}
		}
		
		
		switch (flag) {
		case Definition.EstimateBasicArgs.AVG:
			dataavg = datasum_d / data.size();
			ret = dataavg;
			break;
		case Definition.EstimateBasicArgs.MAX:
			if (datavalue instanceof Integer) {
				ret = datamax_i;
			}
			else if (datavalue instanceof Double) {
				ret = datamax_d;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型不匹配", flag));
			}
			break;
		case Definition.EstimateBasicArgs.MIN:
			if (datavalue instanceof Integer) {
				ret = datamin_i;
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
	
	public static void main(String[] args) throws Exception {

		Date t = new Date();
		String expression = null;
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable. createVariable ("t", t));
		
		expression = "$SELECT(\"A\", \"B\", $TODATE(\"2016-01-06 10:00:00.00\"), $TODATE(\"2016-01-06 10:03:00.00\"))";
		List rawdata = (List)ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(rawdata);
		
		expression = "$MERGE_BASIC($SELECT(\"A\", \"B\", $TODATE(\"2016-01-06 10:00:00.00\"), $TODATE(\"2016-01-06 10:03:00.00\")), 1)";
		List mergedata = (List)ExpressionEvaluator. evaluate (expression, variables);		
		System.out.println(mergedata);
		
		expression = "$ESTIMATE_BASIC($MERGE_BASIC($SELECT(\"A\", \"B\", $TODATE(\"2016-01-06 10:00:00.00\"), $TODATE(\"2016-01-06 10:03:00.00\")), 1), t, 0)";
		Object estimatedata = ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(estimatedata);

	}
}

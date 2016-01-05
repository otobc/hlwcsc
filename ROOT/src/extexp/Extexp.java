package extexp;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.wltea.expression.*;
import org.wltea.expression.datameta.Variable;
import basic.Definition;

public class Extexp {
	private static SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ms");
	
	// 日期转换
	public static Date todate(String date) throws ParseException {
		return dateFm.parse(date);
	}
	
	// 原始数据提取
	public static List select(String id, String environmentid, Date begintime, Date endtime) {
		List ret = new ArrayList();
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
		String datatype = null;
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
				datatype = (String)nodedata.get(4);
				datatime = (Date)nodedata.get(6);
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					datavalue_i = (int)nodedata.get(5);
					if (0 == count) {
						datamax_i = datamin_i = datavalue_i;
					}
					else {
						datamax_i = datavalue_i > datamax_i ? datavalue_i : datamax_i;
						datamin_i = datavalue_i < datamin_i ? datavalue_i : datamin_i;
					}
					datasum_i += datavalue_i;
				}
				if (datatype.equals(Definition.Datatype.DOUBLE)) {
					datavalue_d = (double)nodedata.get(5);
					if (0 == count) {
						datamax_d = datamin_d = datavalue_d;
					}
					else {
						datamax_d = datavalue_d > datamax_d ? datavalue_d : datamax_d;					
						datamin_d = datavalue_d < datamin_d ? datavalue_d : datamin_d;
					}
					datasum_d += datavalue_d;
				}
				else if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					datavalue_b = Boolean.valueOf((String)nodedata.get(5));
					dataall &= datavalue_b;
					dataany |= datavalue_b;
					datanone &= !datavalue_b;
				}
				else {
					throw new Exception(String.format("merge_basic，错误的数据类型[%s]", datatype));
				}
				count += 1;
			}
			
			List merge = new ArrayList();
			merge.add(dataid);
			merge.add(dataname);
			merge.add(datatype);
			switch (flag) {
			case Definition.MergeBasicArgs.AVG:
				dataavg = datasum_d / group.size();
				merge.add(dataavg);
				break;
			case Definition.MergeBasicArgs.SUM:
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					merge.add(datasum_i);
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					merge.add(datasum_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.MAX:
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					merge.add(datamax_i);
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					merge.add(datamax_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.MIN:
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					merge.add(datamin_i);
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					merge.add(datamin_d);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.ALL:
				if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					merge.add(dataall);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.ANY:
				if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					merge.add(dataany);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.NONE:
				if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					merge.add(datanone);
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
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
	
	public static List estimate_basic(List data, Date t, int flag) throws Exception {
		if (data.size() == 0) {
			throw new Exception(String.format("estimate_basic，输入数据为空"));
		}
		List ret = new ArrayList();
		
		String dataid = null;
		String dataname = null;
		String datatype = null;
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
			datatype = (String)nodedata.get(2);
			datatime = (Date)nodedata.get(4);
			if (datatype.equals(Definition.Datatype.INTEGER)) {
				datavalue_i = (int)nodedata.get(3);
				if (0 == count) {
					datamax_i = datamin_i = datavalue_i;
				}
				else {
					datamax_i = datavalue_i > datamax_i ? datavalue_i : datamax_i;
					datamin_i = datavalue_i < datamin_i ? datavalue_i : datamin_i;
				}
				datasum_i += datavalue_i;
			}
			if (datatype.equals(Definition.Datatype.DOUBLE)) {
				datavalue_d = (double)nodedata.get(3);
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
					dataearlier = nodedata.get(3);
				}
			}
			if (datatime.after(t) || datatime.equals(t)) {
				if (later == null || later.after(datatime)) {
					later = datatime;
					datalater = nodedata.get(3);
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
		
		
		ret.add(dataid);
		ret.add(dataname);
		ret.add(datatype);
		switch (flag) {
		case Definition.EstimateBasicArgs.AVG:
			dataavg = datasum_d / data.size();
			ret.add(dataavg);
			break;
		case Definition.EstimateBasicArgs.MAX:
			if (datatype.equals(Definition.Datatype.INTEGER)) {
				ret.add(datamax_i);
			}
			else if (datatype.equals(Definition.Datatype.DOUBLE)) {
				ret.add(datamax_d);
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
			}
			break;
		case Definition.EstimateBasicArgs.MIN:
			if (datatype.equals(Definition.Datatype.INTEGER)) {
				ret.add(datamin_i);
			}
			else if (datatype.equals(Definition.Datatype.DOUBLE)) {
				ret.add(datamin_d);
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型[%s]不匹配", flag, datatype));
			}
			break;
		case Definition.EstimateBasicArgs.NEAREST:
			ret.add(datanearest);
			break;
		case Definition.EstimateBasicArgs.EARLIER:
			if (earlier != null) {
				ret.add(dataearlier);
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]，时间[%s]超出范围", flag, dateFm.format(t)));
			}
			break;
		case Definition.EstimateBasicArgs.LATER:
			if (later != null) {
				ret.add(datalater);
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]，时间[%s]超出范围", flag, dateFm.format(t)));
			}
			break;
		default:
			throw new Exception(String.format("estimate_basic，错误的参数[%d]", flag));
		}
		ret.add(t);
		
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
		
		Random random = new Random(100);
		Date t = null;
		Thread thread = Thread.currentThread();  

		int id = random.nextInt(100);
		List rawdata = new ArrayList();
		for (int i = 0; i < 5; i++) {
			List group = new ArrayList();
			Date date = new java.util.Date();
			if (i == 2) {
				thread.sleep(100);
				t = new java.util.Date();
				thread.sleep(100);
			}
			for (int j = 0; j < 10; j++) {
				List nodedata = new ArrayList();
				nodedata.add(String.format("%08d", id));
				nodedata.add(String.format("第%d组的原始数据", id));
				nodedata.add("00000000");
				nodedata.add(String.format("%08d", i*10+j));
				nodedata.add("2");
				nodedata.add(random.nextDouble());
				nodedata.add(date);
				group.add(nodedata);
			}
			rawdata.add(group);
			//thread.sleep(1000);
		}
		System.out.println(rawdata);
		
		String expression = "$MERGE_BASIC(rawdata, 1)";
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable. createVariable ("rawdata", rawdata));
		List mergedata = (List)ExpressionEvaluator. evaluate (expression, variables);		
		System.out.println(mergedata);
		
		expression = "$ESTIMATE_BASIC($MERGE_BASIC(rawdata, 1), t, 0)";
		variables.add(Variable. createVariable ("t", t));
		List estimatedata = (List)ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(estimatedata);
	}
}

package extexp;

import java.util.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.wltea.expression.*;
import org.wltea.expression.datameta.Variable;

import basic.Definition;
import db.DBConnect;

public class Extexp {
	@SuppressWarnings("unused")
	private static Random random = new Random(100);
	
	private static SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	
	static String dateToString(Date date) {
		return dateFm.format(date);
	}
	
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
	public static List<List<RawData>> select(String id, String environmentid, Date begintime, Date endtime) throws Exception {
		List<List<RawData>> ret = new ArrayList<List<RawData>>(); 


//		id = String.format("%08d", random.nextInt(100));
//		for (int i = 0; i < 5; i++) {
//			List<RawData> group = new ArrayList<RawData>();
//			Date date = new java.util.Date();
//			for (int j = 0; j < 10; j++) {
//				RawData rawData = Data.genData(id, String.format("第%s组的原始数据", id), "00000000", String.format("%08d", i*10+j), date, (Object)random.nextLong());
//				group.add(rawData);
//			}
//			ret.add(group);
//			Thread.sleep(1000);
//		}
		
		
		Connection connection = DBConnect.getConnection();
		Statement stat = connection.createStatement();
		String sql = "select * from rawdata where id = \"" + id + "\" and environmentid = \"" + environmentid + "\"  and time between \""+ dateToString(begintime) +"\" and \"" + dateToString(endtime) + "\" order by time;";
		ResultSet rs = stat.executeQuery(sql);
		String lastTime = null;
		String firstDataType = null;
		List<RawData> group = null;
		while (rs.next()) {
			String time = rs.getString("time");
			String dataType = rs.getString("datatype");
			
			if (lastTime != null && !lastTime.equals(time)) {
				ret.add(group);
				group = new ArrayList<RawData>();
			}
			else {
				group = new ArrayList<RawData>();
			}
			
			if (firstDataType != null) {
				if (!firstDataType.equals(dataType)) {
					throw new Exception(String.format("select，数据类型不统一"));
				}
			}
			else {
				firstDataType = dataType;
			}
			
			
			RawData rawData = Data.genData(rs);
			group.add(rawData);
			
			lastTime = time;
		}
		ret.add(group);
		return ret;
	}
	
	// 基础融合函数
	public static List<MergeData> merge_basic(List<List<RawData>> data, int flag) throws Exception {
		if (data.size() == 1 && data.get(0) == null) {
			throw new Exception(String.format("merge_basic，输入数据为空"));
		}
		List<MergeData> ret = new ArrayList<MergeData>();
		String dataId = null;
		String dataName = null;
		String dataEnvironmentId = null;
		Object dataValue = null;
		Object mergeValue = null;
		long dataValue_l = 0;
		double dataValue_d = 0.0;
		boolean dataValue_b = false;
		long dataSum_l = 0;
		double dataSum_d = 0.0;
		double dataAvg = 0.0;
		long dataMax_l = 0;
		double dataMax_d = 0.0;
		long dataMin_l = 0;
		double dataMin_d = 0.0;
		boolean dataAll = false;
		boolean dataAny = false;
		boolean dataNone = true;
		Date dataTime = null;
		
		for(Iterator<List<RawData>> i = data.iterator();i.hasNext();) {
			List<RawData> group = i.next();
			dataSum_l = 0;
			dataSum_d = 0.0;
			long count = 0;
			for(Iterator<RawData> j = group.iterator();j.hasNext();) {
				RawData rawData = j.next();
				dataId = rawData.id;
				dataName = rawData.name;
				dataEnvironmentId = rawData.environmentId;
				dataValue = rawData.value;
				dataTime = rawData.time;
				if (dataValue instanceof Long) {
					dataValue_l = (long)dataValue;
					if (0 == count) {
						dataMax_l = dataMin_l = dataValue_l;
					}
					else {
						dataMax_l = dataValue_l > dataMax_l ? dataValue_l : dataMax_l;
						dataMin_l = dataValue_l < dataMin_l ? dataValue_l : dataMin_l;
					}
					
					dataSum_l += dataValue_l;
				}
				else if (dataValue instanceof Double) {
					dataValue_d = (double)dataValue;
					if (0 == count) {
						dataMax_d = dataMin_d = dataValue_d;
					}
					else {
						dataMax_d = dataValue_d > dataMax_d ? dataValue_d : dataMax_d;					
						dataMin_d = dataValue_d < dataMin_d ? dataValue_d : dataMin_d;
					}
					dataSum_d += dataValue_d;
				}
				else if (dataValue instanceof Boolean) {
					dataValue_b = (Boolean)dataValue;
					dataAll &= dataValue_b;
					dataAny |= dataValue_b;
					dataNone &= !dataValue_b;
				}
				else {
					throw new Exception(String.format("merge_basic，错误的数据类型"));
				}
				count += 1;
			}

			switch (flag) {
			case Definition.MergeBasicArgs.AVG:
				if (dataValue instanceof Long) {
					dataAvg = dataSum_l / (1.0 * group.size());
				}
				else {
					dataAvg = dataSum_d / group.size();
				}
				mergeValue = dataAvg;
				break;
			case Definition.MergeBasicArgs.SUM:
				if (dataValue instanceof Long) {
					mergeValue = dataSum_l;
				}
				else if (dataValue instanceof Double) {
					mergeValue = dataSum_d;
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.MAX:
				if (dataValue instanceof Long) {
					mergeValue = dataMax_l;
				}
				else if (dataValue instanceof Double) {
					mergeValue = dataMax_d;
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.MIN:
				if (dataValue instanceof Long) {
					mergeValue = dataMin_l;
				}
				else if (dataValue instanceof Double) {
					mergeValue = dataMin_d;
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.ALL:
				if (dataValue instanceof Boolean) {
					mergeValue = dataAll;
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.ANY:
				if (dataValue instanceof Boolean) {
					mergeValue = dataAny;
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			case Definition.MergeBasicArgs.NONE:
				if (dataValue instanceof Boolean) {
					mergeValue = dataNone;
				}
				else {
					throw new Exception(String.format("merge_basic，参数[%d]与类型不匹配", flag));
				}
				break;
			default:
				throw new Exception(String.format("merge_basic，错误的参数[%d]", flag));
			}
			
			MergeData mergeData = Data.genData(dataId, dataName, dataEnvironmentId, dataTime, mergeValue);
			ret.add(mergeData);
		}
		return ret;
	}
	
	public static Object estimate_basic(List<MergeData> data, Date t, int flag) throws Exception {
		if (data.size() == 0) {
			throw new Exception(String.format("estimate_basic，输入数据为空"));
		}
		Object ret = null;
		Object dataValue = null;
		long dataValue_l = 0;
		double dataValue_d = 0.0;
		double dataAvg = 0.0;
		long dataSum_l = 0;
		double dataSum_d = 0.0;
		long dataMax_l = 0;
		double dataMax_d = 0.0;
		long dataMin_l = 0;
		double dataMin_d = 0.0;
		Object dataNearest = null;
		Object dataEarlier = null;
		Date earlier = null;
		Object dataLater = null;
		Date later = null;
		Date dataTime = null;
		
		long count = 0;
		for(Iterator<MergeData> i = data.iterator();i.hasNext();) {
			MergeData mergeData = i.next();
			dataValue = mergeData.value;
			dataTime = mergeData.time;
			if (dataValue instanceof Long) {
				dataValue_l = (long)dataValue;
				if (0 == count) {
					dataMax_l = dataMin_l = dataValue_l;
				}
				else {
					dataMax_l = dataValue_l > dataMax_l ? dataValue_l : dataMax_l;
					dataMin_l = dataValue_l < dataMin_l ? dataValue_l : dataMin_l;
				}
				dataSum_l += dataValue_l;
			}
			else if (dataValue instanceof Double) {
				dataValue_d = (double)dataValue;
				if (0 == count) {
					dataMax_d = dataMin_d = dataValue_d;
				}
				else {
					dataMax_d = dataValue_d > dataMax_d ? dataValue_d : dataMax_d;					
					dataMin_d = dataValue_d < dataMin_d ? dataValue_d : dataMin_d;
				}
				dataSum_d += dataValue_d;
			}
			
			if (dataTime.before(t) || dataTime.equals(t)) {
				if (earlier == null || earlier.before(dataTime)) {
					earlier = dataTime;
					dataEarlier = dataValue;
				}
			}
			if (dataTime.after(t) || dataTime.equals(t)) {
				if (later == null || later.after(dataTime)) {
					later = dataTime;
					dataLater = dataValue;
				}
			}
			count += 1;
		}
		
		if (earlier == null) {
			dataNearest = dataLater;
		}
		else if (later == null) {
			dataNearest = dataEarlier;
		}
		else {
			long diff_earlier = t.getTime() - earlier.getTime();
			long diff_later = later.getTime() - t.getTime();
			if (diff_earlier < diff_later) {
				dataNearest = dataEarlier;
			}
			else {
				dataNearest = dataLater;
			}
		}
		
		switch (flag) {
		case Definition.EstimateBasicArgs.AVG:
			if (dataValue instanceof Long) {
				dataAvg = dataSum_l / 1.0 * data.size();
			}
			else {
				dataAvg = dataSum_d / data.size();
			}
			ret = dataAvg;
			break;
		case Definition.EstimateBasicArgs.MAX:
			if (dataValue instanceof Long) {
				ret = dataMax_l;
			}
			else if (dataValue instanceof Double) {
				ret = dataMax_d;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型不匹配", flag));
			}
			break;
		case Definition.EstimateBasicArgs.MIN:
			if (dataValue instanceof Long) {
				ret = dataMin_l;
			}
			else if (dataValue instanceof Double) {
				ret = dataMin_d;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]与类型不匹配", flag));
			}
			break;
		case Definition.EstimateBasicArgs.NEAREST:
			ret = dataNearest;
			break;
		case Definition.EstimateBasicArgs.EARLIER:
			if (earlier != null) {
				ret = dataEarlier;
			}
			else {
				throw new Exception(String.format("estimate_basic，参数[%d]，时间[%s]超出范围", flag, dateFm.format(t)));
			}
			break;
		case Definition.EstimateBasicArgs.LATER:
			if (later != null) {
				ret = dataLater;
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

		Date t = todate("2016-01-15 13:44:22.00");
		String expression = null;
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable. createVariable ("t", t));
		
		expression = "$SELECT(\"00000015\", \"00000000\", $TODATE(\"2016-01-27 15:26:36.902\"), $TODATE(\"2016-01-27 15:26:36.906\"))";
		List<List<List<Comparable>>> rawdata = (List<List<List<Comparable>>>)ExpressionEvaluator. evaluate (expression, variables);
		System.out.println("rawdata:" +rawdata);
		
		expression = "$MERGE_BASIC($SELECT(\"00000015\", \"00000000\", $TODATE(\"2016-01-27 15:26:36.902\"), $TODATE(\"2016-01-27 15:26:36.906\")), 0)";
		List<List<Comparable>> mergedata = (List<List<Comparable>>)ExpressionEvaluator. evaluate (expression, variables);		
		System.out.println("mergedata:" + mergedata);
		
		expression = "$ESTIMATE_BASIC($MERGE_BASIC($SELECT(\"00000015\", \"00000000\", $TODATE(\"2016-01-27 15:26:36.902\"), $TODATE(\"2016-01-27 15:26:36.906\")), 0), t, 3)";
		Object estimatedata = ExpressionEvaluator. evaluate (expression, variables);
		System.out.println("estimatedata:" + estimatedata);
	}
}

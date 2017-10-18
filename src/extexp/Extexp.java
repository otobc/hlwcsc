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
import basic.HomoValue;
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
//				RawData rawData = Data.genData(id, String.format("第%s组的原始数据", id), "00000000", String.format("%08d", i*10+j), date, new HomoValue(Definition.Datatype.LONG, String.valueOf(random.nextLong())));
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
		
		

		Date dataTime = null;
		
		for(Iterator<List<RawData>> i = data.iterator();i.hasNext();) {
			List<RawData> group = i.next();
			long count = 0;
			HomoValue mergeValue = new HomoValue();
			for(Iterator<RawData> j = group.iterator();j.hasNext();) {
				RawData rawData = j.next();
				dataId = rawData.id;
				dataName = rawData.name;
				dataEnvironmentId = rawData.environmentId;
				dataTime = rawData.time;
				
				switch (flag) {
				case Definition.MergeBasicArgs.AVG:
					mergeValue.add(rawData.homoValue);
					break;
				case Definition.MergeBasicArgs.SUM:
					mergeValue.add(rawData.homoValue);
					break;
				case Definition.MergeBasicArgs.MAX:
					mergeValue.max(rawData.homoValue);
					break;
				case Definition.MergeBasicArgs.MIN:
					mergeValue.min(rawData.homoValue);
					break;
				case Definition.MergeBasicArgs.ALL:
					mergeValue.all(rawData.homoValue);
					break;
				case Definition.MergeBasicArgs.ANY:
					mergeValue.any(rawData.homoValue);
					break;
				case Definition.MergeBasicArgs.NONE:
					mergeValue.none(rawData.homoValue);
					break;
				default:
					throw new Exception(String.format("merge_basic，错误的参数[%d]", flag));
				}
				count += 1;
			}

			if (flag == Definition.MergeBasicArgs.AVG) {
				mergeValue.avg(count);
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
		
		HomoValue dataValue = new HomoValue();

		HomoValue dataNearest = null;
		HomoValue dataEarlier = null;
		HomoValue dataLater = null;
		
		Date earlier = null;
		Date later = null;
		Date dataTime = null;
		
		long count = 0;
		for(Iterator<MergeData> i = data.iterator();i.hasNext();) {
			MergeData mergeData = i.next();
			dataTime = mergeData.time;
			
			switch (flag) {
			case Definition.EstimateBasicArgs.AVG:
				dataValue.add(mergeData.homoValue);
				break;
			case Definition.EstimateBasicArgs.MAX:
				dataValue.max(mergeData.homoValue);
				break;
			case Definition.EstimateBasicArgs.MIN:
				dataValue.min(mergeData.homoValue);
				break;
			case Definition.EstimateBasicArgs.NEAREST:
				break;
			case Definition.EstimateBasicArgs.EARLIER:
				break;
			case Definition.EstimateBasicArgs.LATER:
				break;
			default:
				throw new Exception(String.format("estimate_basic，错误的参数[%d]", flag));
			}
			if (dataTime.before(t) || dataTime.equals(t)) {
				if (earlier == null || earlier.before(dataTime)) {
					earlier = dataTime;
					dataEarlier = mergeData.homoValue;
				}
			}
			if (dataTime.after(t) || dataTime.equals(t)) {
				if (later == null || later.after(dataTime)) {
					later = dataTime;
					dataLater = mergeData.homoValue;
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
			dataValue.avg(count);
			break;
		case Definition.EstimateBasicArgs.NEAREST:
			dataValue = dataNearest;
		case Definition.EstimateBasicArgs.EARLIER:
			if (dataEarlier == null) {
				throw new Exception(String.format("estimate_basic，时间范围不准确"));
			}
			dataValue = dataEarlier;
			break;
		case Definition.EstimateBasicArgs.LATER:
			if (dataLater == null) {
				throw new Exception(String.format("estimate_basic，时间范围不准确"));
			}
			dataValue = dataLater;
			break;
		default:
			throw new Exception(String.format("estimate_basic，错误的参数[%d]", flag));
		}
		
		return dataValue.getValue();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {

		Date t = todate("2016-01-27 15:26:36.903");
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

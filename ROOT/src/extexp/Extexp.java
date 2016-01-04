package extexp;

import java.util.*;
import java.text.SimpleDateFormat;
import org.wltea.expression.*;
import org.wltea.expression.datameta.Variable;
import basic.Definition;

public class Extexp {
	// 原始数据提取
	public static List select(String id, String environmentid, Date begintime, Date endtime) {
		List ret = new ArrayList();
		return ret;
	}
	
	// 基础融合函数，args：0-求和融合，1-平均融合，2-最大值融合，3-最小值融合
	public static List merge_basic(List data, int flag) throws Exception {
		List ret = new ArrayList();
		String dataid = null;
		String dataname = null;
		String datatype = null;
		double datavalue = 0.0;
		double datasum = 0.0;
		double dataavg = 0.0;
		double datamax = 0.0;
		double datamin = 0.0;
		String datatime = null;
		for(Iterator i = data.iterator();i.hasNext();) {
			List group = (List)i.next();
			datasum = 0.0;
			int count = 0;
			for(Iterator j = group.iterator();j.hasNext();) {
				List nodedata = (List)j.next();
				dataid = (String)nodedata.get(0);
				dataname = (String)nodedata.get(1);
				datatype = (String)nodedata.get(4);
				datatime = (String)nodedata.get(6);
				if (datatype.equals(Definition.Datatype.INTEGER) ||
						datatype.equals(Definition.Datatype.DOUBLE)) {
					datavalue = Double.valueOf((String)nodedata.get(5));
					if (0 == count) {
						datamax = datamin = datavalue;
					}
					else {
						if (datavalue > datamax) {
							datamax = datavalue;
						}
						if (datavalue < datamin) {
							datamin = datavalue;
						}
					}
					datasum += datavalue;
				}
				else {
					throw new Exception(String.format("merge_avg，错误的数据类型:[%s]", datatype));
				}
				count += 1;
			}
			dataavg = (double)datasum / group.size();
			List merge = new ArrayList();
			merge.add(dataid);
			merge.add(dataname);
			merge.add(datatype);
			switch (flag) {
			case Definition.MergeBasicArgs.AVG:
				merge.add(String.valueOf(dataavg));
				break;
			case Definition.MergeBasicArgs.SUM:
				merge.add(String.valueOf(datasum));
				break;
			case Definition.MergeBasicArgs.MAX:
				merge.add(String.valueOf(datamax));
				break;
			case Definition.MergeBasicArgs.MIN:
				merge.add(String.valueOf(datamin));
				break;
			default:
				throw new Exception(String.format("merge_avg，错误的参数:[%s]", flag));
			}
			
			merge.add(datatime);
			ret.add(merge);
		}
		return ret;
	}
	
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd-HH-mm:ss:ms");
		Random random = new Random(100);
		// 测试merge_avg
		List rawdata = new ArrayList();
		for (int i = 0; i < 10; i++) {
			List group = new ArrayList();
			for (int j = 0; j < 10; j++) {
				List nodedata = new ArrayList();
				nodedata.add(String.format("%08d", i, j));
				nodedata.add(String.format("第%d组的原始数据", i, j));
				nodedata.add("00000000");
				nodedata.add(String.format("%08d", i*10+j));
				nodedata.add("2");
				nodedata.add(String.valueOf(random.nextInt(10)));
				nodedata.add(dateFm.format(new java.util.Date()));
				group.add(nodedata);
			}
			rawdata.add(group);
		}
		
		String expression = "$MERGE_BASIC(rawdata, 0)";
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable. createVariable ("rawdata", rawdata));
		Object mergedata = ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(rawdata);
		System.out.println(mergedata);
	}
}

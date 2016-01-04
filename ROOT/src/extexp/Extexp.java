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
	
	// 基础融合函数
	public static List merge_basic(List data, int flag) throws Exception {
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
		String datatime = null;
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
				datatime = (String)nodedata.get(6);
				if (datatype.equals(Definition.Datatype.INTEGER) || datatype.equals(Definition.Datatype.DOUBLE)) {
					datavalue_i = Integer.valueOf((String)nodedata.get(5));
					datavalue_d = Double.valueOf((String)nodedata.get(5));
					
					if (0 == count) {
						datamax_i = datamin_i = datavalue_i;
						datamax_d = datamin_d = datavalue_d;
					}
					else {
						datamax_i = datavalue_i > datamax_i ? datavalue_i : datamax_i;
						datamax_d = datavalue_d > datamax_d ? datavalue_d : datamax_d;
						datamin_i = datavalue_i < datamin_i ? datavalue_i : datamin_i;
						datamin_d = datavalue_d < datamin_d ? datavalue_d : datamin_d;
					}
					datasum_i += datavalue_i;
					datasum_d += datavalue_d;
				}
				else if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					datavalue_b = Boolean.valueOf((String)nodedata.get(5));
					dataall &= datavalue_b;
					dataany |= datavalue_b;
					datanone &= !datavalue_b;
				}
				else {
					throw new Exception(String.format("merge_avg，错误的数据类型[%s]", datatype));
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
				merge.add(String.valueOf(dataavg));
				break;
			case Definition.MergeBasicArgs.SUM:
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					merge.add(String.valueOf(datasum_i));
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					merge.add(String.valueOf(datasum_d));
				}
				else {
					throw new Exception(String.format("merge_avg，参数[%s]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.MAX:
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					merge.add(String.valueOf(datamax_i));
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					merge.add(String.valueOf(datamax_d));
				}
				else {
					throw new Exception(String.format("merge_avg，参数[%s]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.MIN:
				if (datatype.equals(Definition.Datatype.INTEGER)) {
					merge.add(String.valueOf(datamin_i));
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					merge.add(String.valueOf(datamin_d));
				}
				else {
					throw new Exception(String.format("merge_avg，参数[%s]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.ALL:
				if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					merge.add(String.valueOf(dataall));
				}
				else {
					throw new Exception(String.format("merge_avg，参数[%s]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.ANY:
				if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					merge.add(String.valueOf(dataany));
				}
				else {
					throw new Exception(String.format("merge_avg，参数[%s]与类型[%s]不匹配", flag, datatype));
				}
				break;
			case Definition.MergeBasicArgs.NONE:
				if (datatype.equals(Definition.Datatype.BOOLEAN)) {
					merge.add(String.valueOf(datanone));
				}
				else {
					throw new Exception(String.format("merge_avg，参数[%s]与类型[%s]不匹配", flag, datatype));
				}
				break;
			default:
				throw new Exception(String.format("merge_avg，错误的参数[%s]", flag));
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
				nodedata.add("3");
				nodedata.add(String.valueOf(false));
				nodedata.add(dateFm.format(new java.util.Date()));
				group.add(nodedata);
			}
			rawdata.add(group);
		}
		
		String expression = "$MERGE_BASIC(rawdata, 6)";
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(Variable. createVariable ("rawdata", rawdata));
		Object mergedata = ExpressionEvaluator. evaluate (expression, variables);
		System.out.println(rawdata);
		System.out.println(mergedata);
	}
}

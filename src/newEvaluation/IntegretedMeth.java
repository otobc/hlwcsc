package newEvaluation;


import java.util.ArrayList;
import java.util.List;


import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.*;
import org.wltea.expression.datameta.BaseDataMeta.DataType;



public class IntegretedMeth {
	
	
	
	
	/**加权几何平均*/
	public static Double wGeometricMean(List<Double> weights, List<Double> vals){
		Double result = new Double(0);
		for(int i = 0; i < weights.size(); i++){
			result += Math.pow(vals.get(i), weights.get(i));
		}
		return result;
	}
	
	
	/**加权算数平均*/
	public static Double wArithmeticMean(List<Double> weights, List<Double> vals){
		Double result = new Double(0);
		for(int i = 0; i < weights.size(); i++){
			result += weights.get(i) * vals.get(i);
		}
		return result;
	}
	
	
	/**模糊综合评估
	输入值分别表示权重向量、因素集（指标）、评语集（级别）、与评语集对应的隶属函数集、综合函数*/
	public static Double fuzzyComprehensiveEvaluation
	(List<Double> weights, List<Double> vals,Integer[] level, String[] mbsp,String conclude){
		Double result = new Double(0);
		
//		计算判决矩阵R
		Double[][] R = new Double[vals.size()][mbsp.length];
		for(int i = 0; i < vals.size(); i++){
			for(int j = 0; j < mbsp.length; j++){
				String expression = mbsp[j];
				List<Variable> var = new ArrayList<Variable>();
				var.add(new Variable("x", DataType.DATATYPE_DOUBLE, vals.get(i)));
				Double r = new Double(ExpressionEvaluator.evaluate(expression, var).toString());
				R[i][j] = r;
			}
		}
		
//		计算结果集E
		Double[] E = new Double[mbsp.length];
		for(int j = 0; j < E.length; j++){
			Double e = new Double(0.0d);
			for(int i = 0; i < vals.size(); i++){
				e += weights.get(i) * R[i][j];
			}
			E[j] = min(1.0d, e);
		}
		
//		模糊综合结果处理
		switch(conclude){
		case "01":
			result = maxmbsp(E);
			break;
		case "02":
			result = avgmbsp(E, 1.0d);
			break;
		case "03":
			result = avgmbsp(E, 2.0d);
			break;
		default:
			System.out.println("综合函数错误！");
		}
		
		return result;
	}
	
	/**取最小值*/
	public static Double min(Double num1, Double num2){
		if(num1 < num2)
			return num1;
		else return num2;
	}
	
	/**综合函数-最大隶属度函数*/
	public static Double maxmbsp(Double[] e){
		Double r = new Double(0);
		if(e.length> 0)
			r = e[0];
		else
			System.out.println("结果集错误！");
		
		for(int i = 0; i < e.length; i++){
			if(e[i] < r)
				r = e[i];
		}
		return r;
	}
	
	/**综合函数-加权平均法*/
	public static Double avgmbsp(Double[] e, Double k){
		Double r = new Double(0);
		Double numerator = new Double(0);
		Double denominator = new Double(0);
		for(int i = 0; i < e.length; i++){
			numerator += (i+1) * Math.pow(e[i], k);
			denominator += Math.pow(e[i], k);
		}
		r = numerator / denominator;
		return r;
	}
	
	public static void main(String[] args) {
		
		Double[] e = {1.0d, 1.0d, 2.0d, 0.0d, 0.9d, 0.3d};
		System.out.println(avgmbsp(e, 1.0d));
		
	}
}

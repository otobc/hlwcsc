package basic;

import java.util.Iterator;
import java.util.List;

public class LvlSts extends Sts {

	@Override
	public void setSts(String id) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception(String.format("不支持该功能"));
	}

	@Override
	public void setSts(List<Integer> level) {
		long max = 0;
		long min = 0;
		long sum = 0;
		double dsum = 0.0;
		long count = 0;
		double avg;
		double var;
		double tp = 0.0;
		// TODO Auto-generated method stub
		for(Iterator<Integer> i = level.iterator();i.hasNext();) {
			int value = i.next();
			if (0 == count) {
				max = min = value;
			}
			else {
				max = (value > max) ? value : max;
				min = (value < min) ? value : min;
				sum += value;
			}
			count += 1;
		}
		tp = 1.0 / count;
		avg = sum * 1.0 / count;
		if (count > 1) {
			for(Iterator<Integer> i = level.iterator();i.hasNext();) {
				int value = i.next();
				dsum += Math.pow((value-avg), 2);
			}
			var = Math.sqrt(dsum/count-1);
		}
		else {
			var = 0.0;
		}
		this.max = max;
		this.min = min;
		this.sum = sum;
		this.count = count;
		this.avg = avg;
		this.var = var;
		this.tp = tp;		
	}
}

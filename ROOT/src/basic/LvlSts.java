package basic;

import java.util.Iterator;
import java.util.List;

public class LvlSts extends Sts {

	@Override
	public void setSts(String id) throws Exception {
		throw new Exception(String.format("不支持该功能"));
	}

	@Override
	public void setSts(List<Integer> level) {
		long lmax = 0;
		double dmax = 0.0;
		long lmin = 0;
		double dmin = 0.0;
		long lsum = 0;
		double dsum = 0.0;
		double varsum = 0.0;
		long count = 0;
		double lavg = 0.0;
		double davg = 0.0;
		double lvar = 0.0;
		double dvar = 0.0;
		double tp = 0.0;
		for(Iterator<Integer> i = level.iterator();i.hasNext();) {
			int value = i.next();
			if (0 == count) {
				lmax = lmin = value;
			}
			else {
				lmax = (value > lmax) ? value : lmax;
				lmin = (value < lmin) ? value : lmin;
				lsum += value;
			}
			count += 1;
		}
		tp = 1.0 / count;
		lavg = lsum * 1.0 / count;
		if (count > 1) {
			for(Iterator<Integer> i = level.iterator();i.hasNext();) {
				int value = i.next();
				varsum += Math.pow((value-lavg), 2);
			}
			lvar = Math.sqrt(varsum/(count-1));
		}
		
		this.lmax = lmax;
		this.dmax = dmax;
		this.lmin = lmin;
		this.dmin = dmin;
		this.lsum = lsum;
		this.dsum = dsum;
		this.count = count;
		this.lavg = lavg;
		this.davg = davg;
		this.lvar = lvar;
		this.dvar = dvar;
		this.tp = tp;		
	}
}

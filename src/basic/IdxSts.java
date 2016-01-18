package basic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import url_db.DBConnect;

public class IdxSts extends Sts {
	private String id;
	
	public IdxSts(String id) {
		this.id = id;
	}
	
	@Override
	public void setSts() throws Exception {
		
		long lmax = 0;
		double dmax = 0.0;
		long lmin = 0;
		double dmin = 0.0;
		long lsum = 0;
		double dsum = 0.0;
		double lvarsum = 0.0;
		double dvarsum = 0.0;
		long count = 0;
		long tcount = 0;
		double lavg = 0.0;
		double davg = 0.0;
		double lvar = 0.0;
		double dvar = 0.0;
		double tp = 0.0;
		
		String datatype = null;
		Connection connection = DBConnect.getConnection();
		Statement stat = connection.createStatement();
		String sql = "select * from wpidxcache where wpindexid = \"" + id + "\";";

		ResultSet rs = stat.executeQuery(sql);
		while (rs.next()) {
			datatype = rs.getString("datatype");
			if (datatype.equals(Definition.Datatype.STRING)) {
				// NOTHING TODO
			}
			else if (datatype.equals(Definition.Datatype.LONG)) {
				long value = Long.valueOf(rs.getString("value"));
				if (0 == count) {
					lmax = lmin = value;
				}
				else {
					lmax = (value > lmax) ? value : lmax;
					lmin = (value < lmin) ? value : lmin;
					lsum += value;
				}
			}
			else if (datatype.equals(Definition.Datatype.DOUBLE)) {
				double value = Double.valueOf(rs.getString("value"));
				if (0 == count) {
					dmax = dmin = value;
				}
				else {
					dmax = (value > dmax) ? value : dmax;
					dmin = (value < dmin) ? value : dmin;
					dsum += value;
				}
			}
			else if (datatype.equals(Definition.Datatype.BOOLEAN)) {
				boolean value = Boolean.valueOf(rs.getString("value"));
				if (value) {
					tcount += 1;
				}
			}
			else {
				throw new Exception(String.format("setSts，错误的数据类型"));
			}
			count += 1;
		}
		lavg = (count == 0) ? 0.0 : lsum * 1.0 / count;
		davg = (count == 0) ? 0.0 : dsum / count;		
		tp = (count == 0) ? 0.0 : tcount * 1.0 / count;
		
		if (count > 1) {
			rs = stat.executeQuery(sql);
			while (rs.next()) {
				datatype = rs.getString("datatype");
				if (datatype.equals(Definition.Datatype.LONG)) {
					long value = Long.valueOf(rs.getString("value"));
					lvarsum += Math.pow((value-lavg), 2);
				}
				else if (datatype.equals(Definition.Datatype.DOUBLE)) {
					double value = Double.valueOf(rs.getString("value"));
					dvarsum += Math.pow((value-davg), 2);
				}
			}
			lvar = Math.sqrt(lvarsum/(count-1));
			dvar = Math.sqrt(dvarsum/(count-1));
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
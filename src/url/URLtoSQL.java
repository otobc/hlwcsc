package url;

import java.util.ArrayList;

import bean_json.JsonToBean;
import bean_json.TableBean;

public class URLtoSQL
{
	/*
	 * 使用statement,首先读取配置文件拿到列属性信息,拼接时根据列属性判断每个值，处理char型加引号
	 * 判断是否使用prepareStatement：此系统为了保持通用性（少依赖JDBC），并且SQL语句结构不固定，
	 * 所以prepareStatement不适用
	 */
	public String getRangeSQLFromURL(String table, String key, String value,
			String kvquery)
	{
		String rangesql = "select " + key + ", " + value + " from " + table;
		String condition = getCondition(kvquery, table);
		rangesql = rangesql + condition;// 组成的完整SQL语句
		return rangesql;
	}

	private String getCondition(String kvquery, String table)
	{
		String condition = "";
		if (!kvquery.equals(""))
		{
			ArrayList<ArrayList<String>> queryTypeList = judgekTypeToSetv(
					kvquery, table);// 处理使char值加双引号
			ArrayList<String> klist = queryTypeList.get(0);
			ArrayList<String> vlist = queryTypeList.get(1);
			condition = " where " + klist.get(0) + "=" + vlist.get(0);
			String temp = "";
			for (int i = 1; i < klist.size(); i++)
			{
				temp += " and " + klist.get(i) + "=" + vlist.get(i);
			}
			condition = condition + temp;
		}
		return condition;
	}

	public String getInsertSQLFromURL(String table, String kvdata)
	{
		String insertsql = null;
		insertsql = "insert into " + table;

		String values = "";
		ArrayList<ArrayList<String>> dataTypeList = judgekTypeToSetv(kvdata,
				table);// 处理使char值加双引号
		ArrayList<String> vlist = dataTypeList.get(1);//klist not used
		int i;
		for (i = 0; i < vlist.size() - 1; i++)
		{
			values += vlist.get(i) + ", ";
		}
		values = values + vlist.get(i);

		insertsql = insertsql + " values (" + values + ")";// 组成的完整SQL语句
		return insertsql;
	}

	public String getSearchSQLFromURL(String table, String kdata,
			String kvquery)
	{
		String searchsql = null;
		String cols = "";
		ArrayList<ArrayList<String>> kvdataArrayList = Base64
				.decodeBase64Sentence(kdata);
		ArrayList<String> kdataList = kvdataArrayList.get(0);
		int i = 0;
		for (i = 0; i < kdataList.size() - 1; i++)
		{
			cols += kdataList.get(i) + ", ";
		}
		cols = cols + kdataList.get(i);
		searchsql = "select " + cols + " from " + table;
		String condition = "";
		if (!kvquery.equals(""))
		{
			ArrayList<ArrayList<String>> queryselectTypeList = judgekselectTypeToSetv(
					kvquery, table); // 处理使char值加引号
			ArrayList<String> klist = queryselectTypeList.get(0);
			ArrayList<String> vlist = queryselectTypeList.get(1);
			if (vlist.size() > 0)
			{
				condition = " where " + klist.get(0) + vlist.get(0);
			}
			String temp = "";
			for (int j = 1; j < vlist.size(); j++)// change
													// vlist.size->klist.size to
													// look query para except
			{
				temp += " and " + klist.get(j) + vlist.get(j);
			}
			condition = condition + temp;
		}
		searchsql = searchsql + condition;// 组成的完整SQL语句
		return searchsql;
	}

	private ArrayList<ArrayList<String>> judgekTypeToSetv(String kvquery,
			String table)
	{// 此函数处理使char值加引号
		ArrayList<ArrayList<String>> queryTypeList = new ArrayList<ArrayList<String>>();

		ArrayList<ArrayList<String>> queryDecodedList = Base64
				.decodeBase64Sentence(kvquery);
		ArrayList<String> klist = queryDecodedList.get(0);
		ArrayList<String> vlist = queryDecodedList.get(1);
		JsonToBean jsonToBean = new JsonToBean();
		TableBean tableBean = jsonToBean.getTableBean(table);
		for (int i = 0; i < klist.size(); i++)
		{
			for (int j = 0; j < tableBean.columns.size(); j++)
			{
				if (tableBean.columns.get(j).id.equals(klist.get(i))
						&& tableBean.columns.get(j).type == 0)
				{
					vlist.set(i, "'" + vlist.get(i) + "'");
				}
			}
		}
		queryTypeList.add(klist);
		queryTypeList.add(vlist);
		return queryTypeList;

	}

	private ArrayList<ArrayList<String>> judgekselectTypeToSetv(String kvquery,
			String table)
	{// 此函数处理使char值加引号，并且根据配置文件中的列查询类型选择比较符号
		ArrayList<ArrayList<String>> queryTypeList = new ArrayList<ArrayList<String>>();

		ArrayList<ArrayList<String>> queryDecodedList = Base64
				.decodeBase64Sentence(kvquery);
		ArrayList<String> klist = queryDecodedList.get(0);
		ArrayList<String> vlist = queryDecodedList.get(1);
		TableBean tableBean = JsonToBean.getTableBean(table);
		for (int i = 0; i < vlist.size(); i++)// change vlist.size->klist.size
												// to look query para except
		{
			for (int j = 0; j < tableBean.columns.size(); j++)
			{
				if (tableBean.columns.get(j).id.equals(klist.get(i)))
				{
					if (tableBean.columns.get(j).type == 0
							&& tableBean.columns.get(j).selectType != 1)
						vlist.set(i, "'" + vlist.get(i) + "'");

					switch (tableBean.columns.get(j).selectType)
					{
					case 0:
						vlist.set(i, " = " + vlist.get(i));
						break;
					case 1:
						vlist.set(i, " like " + "'%" + vlist.get(i) + "%'");
						break;
					case 2:
						vlist.set(i, " < " + vlist.get(i));
						break;
					case 3:
						vlist.set(i, " > " + vlist.get(i));
						break;
					case 4:
						vlist.set(i, " <= " + vlist.get(i));
						break;
					case 5:
						vlist.set(i, " >= " + vlist.get(i));
						break;
					default:
						vlist.set(i, " = " + vlist.get(i));
					}
				}
			}
		}
		queryTypeList.add(klist);
		queryTypeList.add(vlist);
		return queryTypeList;

	}

	public String getDeleteSQLFromURL(String table, String query)
	{
		String deletesql = "delete from " + table;
		String condition = getCondition(query, table);
		deletesql = deletesql + condition;
		return deletesql;
	}

	public String getUpdateSQLFromURL(String table, String kvdata,
			String kvquery)
	{
		String updatesql = null;
		updatesql = "update " + table + " set ";

		ArrayList<ArrayList<String>> dataTypeList = judgekTypeToSetv(kvdata,
				table);// 处理使char值加双引号
		ArrayList<String> klist = dataTypeList.get(0);
		ArrayList<String> vlist = dataTypeList.get(1);
		String setVal = klist.get(0) + "=" + vlist.get(0);
		int i;
		for (i = 1; i < vlist.size(); i++)
		{
			setVal =setVal+ ", " + klist.get(i) + "=" + vlist.get(i);
		}
		String condition = getCondition(kvquery, table);
		updatesql = updatesql + setVal + condition;// 组成的完整SQL语句
		return updatesql;
	}
}

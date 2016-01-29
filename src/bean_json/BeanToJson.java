package bean_json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import url.Base64;
import db.ExecuteSQL;

public class BeanToJson
{
	public String getInitJson(String table)
	{
		String initJson = "";
		String path = "";
		try
		{
			path = JsonToBean.class.getClassLoader()
					.getResource("../../configfile/init" + table + ".json")
					.toURI().getPath();
		} catch (URISyntaxException e1)
		{
			e1.printStackTrace();
		}
		File initFile = new File(path);
		Scanner input = new Scanner("");
		try
		{
			input = new Scanner(initFile);
			while (input.hasNextLine())
			{
				initJson = initJson + input.nextLine();
			}
		} catch (FileNotFoundException e)
		{
			// œÏ”¶ ß∞‹json
			e.printStackTrace();
		} finally
		{
			input.close();
		}
		if(initJson.equals(""))
		{
			initJson="{\"result\":\"01\",\"message\":\"failed\"}";
		}
		return initJson;
	}

	public String getRangeJson(ResultSet resultSet) throws IOException,
			JsonGenerationException, JsonMappingException
	{
		String rangeJson = "";
		ArrayList<KVBean> kvBeanList = new ArrayList<KVBean>();
		try
		{
			while (resultSet.next())
			{
				KVBean kvBean = new KVBean();
				kvBean.key = resultSet.getString(1);
				kvBean.value = resultSet.getString(2);
				kvBeanList.add(kvBean);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		RangeBean bean = new RangeBean();
		bean.result = "00";
		bean.message = "OK";
		bean.data = kvBeanList;
		ObjectMapper mapper = new ObjectMapper();
		rangeJson = mapper.writeValueAsString(bean);
		
		return rangeJson;
	}

	public String getInsertJson(int result)
	{
		String insertJson = "";
		if (result == 1)
		{
			insertJson = "{\"result\":\"00\",\"message\":\"OK\"}";
			System.out.println("insert successfully,result=" + result);
		}
		else
		{
			insertJson = "{\"result\":\"01\",\"message\":\"OK\"}";
			System.out.println("insert failed,result=" + result);
		}
		return insertJson;
	}

	public String getSearchJson(ResultSet resultSet, String table,
			String kdata, String begin, String count)
	{
		String searchJson = "";

		ArrayList<ArrayList<String>> kvdataArrayList = Base64
				.decodeBase64Sentence(kdata);
		ArrayList<String> klist = kvdataArrayList.get(0);
		ArrayList<ArrayList<String>> Listklist = new ArrayList<ArrayList<String>>();
		TableBean tableBean = JsonToBean.getTableBean(table);
		try
		{
			resultSet.absolute(Integer.parseInt(begin));
			for (int p = 0; p < Integer.parseInt(count) && resultSet.next(); p++)
			{
				ArrayList<String> vlist = new ArrayList<String>();// a new one
				for (int i = 0; i < klist.size(); i++)
				{
					String value = resultSet.getString(klist.get(i));
					for (int j = 0; j < tableBean.columns.size(); j++)
					{
						if (tableBean.columns.get(j).id.equals(klist.get(i))
								&& tableBean.columns.get(j).candidate == 1)
						{
							String name = getName(resultSet, tableBean, value,
									j);
							value = Base64.encodeBase64String(value) + "|"
									+ Base64.encodeBase64String(name);
							break;
						}
					}
					System.out.println("#value=" + value);
					vlist.add(value);
				}
				Listklist.add(vlist);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		ObjectMapper map = new ObjectMapper();
		SearchBean searchBean = new SearchBean();
		searchBean.result = "00";
		searchBean.message = "OK";
		searchBean.data = Listklist;
		try
		{
			searchJson = "{\"result\":\"00\",\"message\":\"OK\",\"data\":"
					+ map.writeValueAsString(Listklist) + "}";
			// searchJson=map.writeValueAsString(searchBean);
			// two list embeded can not work
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return searchJson;
	}

	private String getName(ResultSet resultSet, TableBean tableBean,
			String value, int j) throws SQLException
	{
		// when isPrimary=true,then must have isShow=true
		String refvalue = tableBean.columns.get(j).flexible.value;
		String reftable = tableBean.columns.get(j).flexible.table;
		String refkey = tableBean.columns.get(j).flexible.key;
		ArrayList<String> refwhere = tableBean.columns.get(j).flexible.where;
		String sql = "select " + refvalue + " from " + reftable;
		String condition = " where " + refkey + "='" + value + "'";
		for (int k = 0; k < refwhere.size(); k++)
		{
			condition += " and " + refwhere.get(k) + "='"
					+ resultSet.getString(refwhere.get(k)) + "'";
		}
		sql = sql + condition;
		System.out.println("*|Vsql=" + sql);

		ResultSet reSet = ExecuteSQL.getExecuteQueryResult(sql);
		String name = "";
		while (reSet.next())
		{
			name = reSet.getString(refvalue);
		}
		System.out.println("*|name=" + name);
		return name;
	}

	public String getDeleteJson(int result)
	{
		String deleteJson = "";
		if (result == 1)
		{
			deleteJson = "{\"result\":\"00\",\"message\":\"OK\"}";
			System.out.println("delete successfully,result=" + result);
		}
		else
		{
			deleteJson = "{\"result\":\"01\",\"message\":\"OK\"}";
			System.out.println("delete failed,result=" + result);
		}
		return deleteJson;
	}

	public String getUpdateJson(int result)
	{
		String updateJson = "";
		if (result == 1)
		{
			updateJson = "{\"result\":\"00\",\"message\":\"OK\"}";
			System.out.println("update successfully,result=" + result);
		}
		else
		{
			updateJson = "{\"result\":\"01\",\"message\":\"failed\"}";
			System.out.println("update failed,result=" + result);
		}
		return updateJson;
	}
}

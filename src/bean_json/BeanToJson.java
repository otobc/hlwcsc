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

public class BeanToJson
{
	public String getInitJson(String table)
	{
		String path = null;
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
		String initJson = "";
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
		JsonToBean jsonToBean = new JsonToBean();
		TableBean tableBean = jsonToBean.getTableBean(table);
		try
		{
			while (resultSet.next())
			{
				ArrayList<String> vlist = new ArrayList<String>();// a new one
																	// in memry

				for (int i = 0; i < klist.size(); i++)
				{
					String value = resultSet.getString(klist.get(i));

					for (int j = 0; j < tableBean.columns.size(); j++)
					{
						if (tableBean.columns.get(j).id.equals(klist.get(i))
								&& tableBean.columns.get(j).candidate == 1)
						{
							String name = tableBean.columns.get(j).name;
							value = Base64.encodeBase64String(value) + "|"
									+ Base64.encodeBase64String(name);
							System.out.println("*|name="+name);
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
			//two list embeded can not work
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return searchJson;
	}
}

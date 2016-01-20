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
			// 响应失败json
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
		// 对数据库执行结果处理，为bean赋值,转化为json串
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

	public String getSearchJson(ResultSet resultSet, String begin, String count)
	{
		String searchJson = "";
		try
		{
			while (resultSet.next())
			{
				// x,y,post/get
				// 处理执行结果赋值给bean，bean to json
				// 重构URLtoSQLtoResult类，sql产生和执行分开
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return searchJson;
	}
}

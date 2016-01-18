package bean_json;

import java.io.IOException;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonToBean
{
	public TableBean getTableBean(String table)
	{
		TableBean tableBean = new TableBean();
		ObjectMapper map = new ObjectMapper();
		map.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		try
		{
			tableBean = map.readValue(new BeanToJson().getInitJson(table),
					TableBean.class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return tableBean;
	}

}

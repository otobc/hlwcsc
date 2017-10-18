package url;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64
{
	public static ArrayList<ArrayList<String>> decodeBase64Sentence(
			String base64Sentence)
	{
		ArrayList<ArrayList<String>> colAndVal = new ArrayList<ArrayList<String>>();
		ArrayList<String> col = new ArrayList<String>();
		ArrayList<String> val = new ArrayList<String>();
		colAndVal.add(col);
		colAndVal.add(val);
		// 需要判断query是否为空，空字符串""分割后仍会产生一个""
		if (!base64Sentence.equals(""))
		{
			String[] kv = base64Sentence.split(",");
			for (int i = 0; i < kv.length; i++)
			{
				String[] temp = kv[i].split(":");
				// 将列属性名和起对应的选择值保存到colAndVal这个列表里
				colAndVal.get(0).add(decodeBase64String(temp[0]));
				if (temp.length > 1)// 处理{k:v,k:v},{k,k,k}情况不执行,avoid temp
									// outofbound
				{
					colAndVal.get(1).add(decodeBase64String(temp[1]));
				}
			}
		}
		return colAndVal;
	}

	public static String decodeBase64String(String base64String)
	{
		String result = null;
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = null;
		try
		{
			b = decoder
					.decodeBuffer(new String(base64String.getBytes("utf-8")));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			result = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public static String encodeBase64String(String str)
	{
		String base64String = "";
		byte[] b = null;
		try
		{
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		base64String = encoder.encode(b);
		return base64String;

	}

}

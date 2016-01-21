package bean_json;

public class ColumnBean
{
	public String	id;			// 数据库中列名（英文）
	public String	name;			// 中文说明
	public boolean	isShow;
	public boolean	isPrimaryKey;	// 此列是否为主键
	public int		type;			// 列的数据类型0-string,1-long,2-double,3-boolean,4-Date
	public boolean	isSelect;		// 是否作为可被查询的列
	public int		selectType;	// 查询类型，0-=,1-like,2-<,3->,4-<=,5->=
	public int		candidate;		// 此处为1时需要处理为K|V
}

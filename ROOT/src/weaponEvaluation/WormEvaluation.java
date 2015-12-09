package weaponEvaluation;

public class WormEvaluation {
	//请在下面添加数据（包括运算所需数据以及运算结果数据），添加的数据请定义为public
	public int id;
	public String name = new String();
	public String weaponType = new String();
	public String weaponName = new String();
	public String tester = new String();
	public String time = new String();
	
	
	//待输入的数据
	public long timeOfSaturation;	//达到饱和所需时间
	public long timeOfPeak;		//达到峰值所需时间
	public long sumOfNumber;	//网络中结点总数
	public long infectedNumberOfSaturation;		//达到饱和时受感染的结点数
	public long infectedNumberOfPeak;		//受感染结点数的峰值
	public long timeStep;	//时间间隔
	public long maxOfNewInfectedNumber;		//在时间间隔内新增被感染结点的最大值
	public long sumOfTraffic;		//总通信量
	public long infectedTraffic;	//蠕虫产生的通信量
	
	
	//需要计算得到的数据
	public double _scoreOfSaturationTime;		//根据蠕虫达到饱和时间的评分
	public double _prevalenceOfInfectionAtSaturation;		//达到饱和时受感染结点在所有结点中的比例
	public double _prevalenceOfInfectionAtPeak;	//达到峰值时受感染结点在所有结点中的比例
	public double meanSpeedOfInfection;		//蠕虫传染的平均速率
	public double maxSpeedOfInfection;	//蠕虫传染的最大速率
	public double _scoreOfMeanSpeed;		//平均传染速率评分
	public double _scoreOfMaxSpeed;		//最大传染速率评分
	public double _prevalenceOfInfectedTraffic;	//蠕虫通信量占总通信量的比例
	
	
	
	/**此处为评估函数，返回值为空，请直接将运算结果赋值给结果变量*/
	public void wormEvaluation(){
		_scoreOfSaturationTime = 
				1 - Math.atan((double)timeOfSaturation/30) * 2 / Math.PI;
		
		_prevalenceOfInfectionAtSaturation = 
				(double)infectedNumberOfSaturation / (double)sumOfNumber;
		
		_prevalenceOfInfectionAtPeak = 
				(double)infectedNumberOfPeak / (double)sumOfNumber;
		
		meanSpeedOfInfection = 
				(double)infectedNumberOfSaturation / (double)timeOfSaturation;
		
		_scoreOfMeanSpeed = 
				Math.atan((double)meanSpeedOfInfection) * 2 / Math.PI;
		
		maxSpeedOfInfection = 
				(double)maxOfNewInfectedNumber / (double)timeStep;
		
		_scoreOfMaxSpeed = 
				Math.atan((double)maxSpeedOfInfection) * 2 / Math.PI;
		
		_prevalenceOfInfectedTraffic = 
				(double)infectedTraffic / (double)sumOfTraffic;
		
	}

}

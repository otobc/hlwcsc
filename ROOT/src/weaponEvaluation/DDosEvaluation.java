package weaponEvaluation;

public class DDosEvaluation {
	// 请在下面添加数据（包括运算所需数据以及运算结果数据），添加的数据请定义为public
	/*
	 * 示例： public int totalHostNum; public int infectedHostNum; public float
	 * infectedRate;
	 */
	public double id=789;
	public String name="Test3";
	public String weaponType="DDos";
	public String weaponName="SDbot";
	public String tester="mike";
	public String time="2015-11-30 11:16:20";
	
	public double availableMachine;
	public double totalMachine;
	public double availableMachineIncreaseRate;
	public double _botnetAvaliable;

	public boolean isMachineInDifferenceNet;
	public boolean isMachineIpFake;
	public double masterServerLevel;
	public double _botnetDeffence;

	public double botnetMachineAverageCpu;
	public double botnetMachineAverageMemery;
	public double _botnetConsume;
	
	public double ddosTime;
	public double ddosPacket;
	public double ddosSendPacketSpace;
	public double _ddosAttackAbility;
	
	public double serverToDeadNum;
	public double attackServerNum;
	public double _serverDeadRate;
	
	
	/** 此处为评估函数，返回值为空，请直接将运算结果赋值给结果变量 */
	public void ddosEvaluation() {
		//
		_botnetAvaliable = (availableMachine / totalMachine) * 0.5 + availableMachineIncreaseRate * 0.5;
		
		//
		if (masterServerLevel >= 2) {
			if (isMachineInDifferenceNet && isMachineIpFake) {
				_botnetDeffence = 0.9;
			} else if (isMachineInDifferenceNet || isMachineIpFake) {
				_botnetDeffence = 0.6;
			} else {
				_botnetDeffence = 0.3;
			}
		} else {
			_botnetDeffence=0.1;
		}
		
		//
		_botnetConsume=botnetMachineAverageCpu*0.5+botnetMachineAverageMemery*0.5;
		
		//
		if(ddosTime>1000 && ddosPacket>8192 && ddosSendPacketSpace<0.01){
			_ddosAttackAbility=0.7;
		}else{
			_ddosAttackAbility=0.4;
		}
		
		//
		_serverDeadRate=serverToDeadNum/attackServerNum;

	}
}

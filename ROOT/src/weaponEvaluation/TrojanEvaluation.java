package WeaponEvaluation;

import java.lang.Math;

public class TrojanEvaluation {
	//请在下面添加数据（包括运算所需数据以及运算结果数据），添加的数据请定义为public
	public int 实验编号;
	public String 试验名称 = new String();
	public String 武器类型 = new String();
	public String 武器名称 = new String();
	public String 试验者 = new String();
	public String 试验时间 = new String();
	
	public int 节点数目;
	public int 可拉取文件的节点数目;
    public int 可推送文件的节点数目;
    public int 可修改文件的节点数目;
    public int 可接收数据的节点数目;
    public int 可发送数据的节点数目;
    public boolean 是否有异常进程启动行为; 
    public boolean 是否有异常执行文件存在;
    public boolean 是否有异常DLL注入行为;
    public boolean 是否有异常DLL文件存在;
    public boolean 是否有在用户模式下的异常新建启动项行为;
    public boolean 是否有在用户模式下的异常启动项存在;
    public boolean 是否有在内核模式下的异常新建启动项行为;
    public boolean 是否有在内核模式下的异常启动项存在;
    public boolean 是否有异常新建服务行为;
    public boolean 是否有异常服务存在;
    public boolean 是否有授权行为发生;
    public boolean 是否有越权行为发生;
    public int 行为发生总数目;
    public double 行为发生总耗时;
    public double 最大平均行为发生总耗时;
    public double 最小平均行为发生总耗时;
    
    public double _感染力得分;
    public double _隐藏力得分;
    public double _行为广度得分;
    public double _行为效率得分;
	public double 总分;
	
	/**此处为评估函数，返回值为空，请直接将运算结果赋值给结果变量*/
	public void trojanEvaluation(){
        double rtOfFilePullable = (double)可拉取文件的节点数目 / 节点数目;
        double rtOfFilePushable = (double)可推送文件的节点数目 / 节点数目;
        double rtOfFileModifiable = (double)可修改文件的节点数目 / 节点数目;
        double rtOfDataReceivable = (double)可接收数据的节点数目 / 节点数目;
        double rtOfDataSendable = (double)可发送数据的节点数目 / 节点数目;
        _感染力得分 = 0.5 * (0.25 * rtOfFilePullable + 0.25 * rtOfFilePushable + 0.5 * rtOfFileModifiable) + 0.5 * (0.5 * rtOfDataReceivable + 0.5 * rtOfDataSendable);
        double scOfExecutiveHiddenness;
        if (是否有异常执行文件存在){
            scOfExecutiveHiddenness = 0.0;
            }
        else if (是否有异常进程启动行为){
            scOfExecutiveHiddenness = 0.25;
            }
        else if (是否有异常DLL文件存在){
            scOfExecutiveHiddenness = 0.5;
            }
        else if (是否有异常DLL注入行为){
            scOfExecutiveHiddenness = 0.75;
            }
        else{
            scOfExecutiveHiddenness = 1.0;
            }
        double scOfStartupHiddenness;
        if (是否有在用户模式下的异常启动项存在){
            scOfStartupHiddenness = 0.0;
            }
        else if (是否有在用户模式下的异常新建启动项行为){
            scOfStartupHiddenness = 0.25;
            }
        else if (是否有在内核模式下的异常启动项存在){
            scOfStartupHiddenness = 0.5;
            }
        else if (是否有在内核模式下的异常新建启动项行为){
            scOfStartupHiddenness = 0.75;
            }
        else{
            scOfStartupHiddenness = 1.0;
            }
        double scOfServiceHiddenness;
        if (是否有异常服务存在){
            scOfServiceHiddenness = 0.0;
            }
        else if (是否有异常新建服务行为){
            scOfServiceHiddenness = 0.5;
            }
        else{
            scOfServiceHiddenness = 1.0;
            }
        _隐藏力得分 = 0.75 * scOfExecutiveHiddenness + 0.25 * Math.min(scOfStartupHiddenness, scOfServiceHiddenness);
        _行为广度得分 = 0.0;
        if (是否有授权行为发生){
            _行为广度得分 += 0.5;
            }
        if (是否有越权行为发生){
            _行为广度得分 += 0.5;
            }
        double avgTmOfBehaviorAccomplished = 行为发生总耗时 / 行为发生总数目;
        double nmlAvgTmOfBehaviorAccomplished = (avgTmOfBehaviorAccomplished - 最小平均行为发生总耗时) / (最大平均行为发生总耗时 - 最小平均行为发生总耗时);
        _行为效率得分 = 1 - nmlAvgTmOfBehaviorAccomplished;
        总分 = 0.3 * _感染力得分 + 0.6 * _隐藏力得分 + 0.05 * _行为广度得分 + 0.05 * _行为效率得分;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}

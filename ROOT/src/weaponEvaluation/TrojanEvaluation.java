package weaponEvaluation;

import java.lang.Math;

public class TrojanEvaluation {
	//请在下面添加数据（包括运算所需数据以及运算结果数据），添加的数据请定义为public
	public int id;
	public String name = new String();
	public String weaponType = new String();
	public String weaponName = new String();
	public String tester = new String();
	public String time = new String();
	
	public int cntOfNode;
	public int cntOfFilePullable;
    public int cntOfFilePushable;
    public int cntOfFileModifiable;
    public int cntOfDataReceivable;
    public int cntOfDataSendable;
    public boolean isAbnormalProcessStarting; 
    public boolean isAbnormalExecutableFileExistent;
    public boolean isAbnormalDLLBeingInjected;
    public boolean isAbnormalDLLFileExistent;
    public boolean isAbnormalStartupBeingAddedInUsermode;
    public boolean isAbnormalStartupExistentInUsermode;
    public boolean isAbnormalStartupBeingAddedInKernelmode;
    public boolean isAbnormalStartupExistentInKernelmode;
    public boolean isAbnormalServiceBeingAdded;
    public boolean isAbnormalServiceExistent;
    public boolean isAunthorizedBehaviorAccomplished;
    public boolean isUnauthorizedBehaviorAccomplished;
    public int cntOfBehaviorAccomplished;
    public double tmOfBehaviorAccomplished;
    public double maxAvgTmOfBehaviorAccomplished;
    public double minAvgTmOfBehaviorAccomplished;
    
    public double _scOfInfection;
    public double _scOfHiddenness;
    public double _scOfBehaviorScope;
    public double _scOfBehaviorAccomplished;
	public double score;
	
	/**此处为评估函数，返回值为空，请直接将运算结果赋值给结果变量*/
	public void trojanEvaluation(){
        double rtOfFilePullable = (double)cntOfFilePullable / cntOfNode;
        double rtOfFilePushable = (double)cntOfFilePushable / cntOfNode;
        double rtOfFileModifiable = (double)cntOfFileModifiable / cntOfNode;
        double rtOfDataReceivable = (double)cntOfDataReceivable / cntOfNode;
        double rtOfDataSendable = (double)cntOfDataSendable / cntOfNode;
        _scOfInfection = 0.5 * (0.25 * rtOfFilePullable + 0.25 * rtOfFilePushable + 0.5 * rtOfFileModifiable) + 0.5 * (0.5 * rtOfDataReceivable + 0.5 * rtOfDataSendable);
        double scOfExecutiveHiddenness;
        if (isAbnormalExecutableFileExistent){
            scOfExecutiveHiddenness = 0.0;
            }
        else if (isAbnormalProcessStarting){
            scOfExecutiveHiddenness = 0.25;
            }
        else if (isAbnormalDLLFileExistent){
            scOfExecutiveHiddenness = 0.5;
            }
        else if (isAbnormalDLLBeingInjected){
            scOfExecutiveHiddenness = 0.75;
            }
        else{
            scOfExecutiveHiddenness = 1.0;
            }
        double scOfStartupHiddenness;
        if (isAbnormalStartupExistentInUsermode){
            scOfStartupHiddenness = 0.0;
            }
        else if (isAbnormalStartupBeingAddedInUsermode){
            scOfStartupHiddenness = 0.25;
            }
        else if (isAbnormalStartupExistentInKernelmode){
            scOfStartupHiddenness = 0.5;
            }
        else if (isAbnormalStartupBeingAddedInKernelmode){
            scOfStartupHiddenness = 0.75;
            }
        else{
            scOfStartupHiddenness = 1.0;
            }
        double scOfServiceHiddenness;
        if (isAbnormalServiceExistent){
            scOfServiceHiddenness = 0.0;
            }
        else if (isAbnormalServiceBeingAdded){
            scOfServiceHiddenness = 0.5;
            }
        else{
            scOfServiceHiddenness = 1.0;
            }
        _scOfHiddenness = 0.75 * scOfExecutiveHiddenness + 0.25 * Math.min(scOfStartupHiddenness, scOfServiceHiddenness);
        _scOfBehaviorScope = 0.0;
        if (isAunthorizedBehaviorAccomplished){
            _scOfBehaviorScope += 0.5;
            }
        if (isUnauthorizedBehaviorAccomplished){
            _scOfBehaviorScope += 0.5;
            }
        double avgTmOfBehaviorAccomplished = tmOfBehaviorAccomplished / cntOfBehaviorAccomplished;
        double nmlAvgTmOfBehaviorAccomplished = (avgTmOfBehaviorAccomplished - minAvgTmOfBehaviorAccomplished) / (maxAvgTmOfBehaviorAccomplished - minAvgTmOfBehaviorAccomplished);
        _scOfBehaviorAccomplished = 1 - nmlAvgTmOfBehaviorAccomplished;
        score = 0.3 * _scOfInfection + 0.6 * _scOfHiddenness + 0.05 * _scOfBehaviorScope + 0.05 * _scOfBehaviorAccomplished;
	}
	
}

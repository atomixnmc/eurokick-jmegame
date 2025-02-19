package sg.games.football.gameplay.ai.states.goalkeeper;

import sg.games.football.entities.GoalKeeper;
import sg.games.football.gameplay.ai.fsm.State;
import sg.games.football.gameplay.ai.event.Telegram;
//----------------- InterceptBall ----------------------------------------
//
// In this state the GP will attempt to intercept the ball using the
// pursuit steering behavior, but he only does so so long as he remains
// within his home region.
//------------------------------------------------------------------------
public class InterceptBall extends State<GoalKeeper>{
    static InterceptBall instance;
    public static InterceptBall Instance(){
        if (instance==null){
            instance = new InterceptBall();
        }
        return instance;
    }

    public void enter(GoalKeeper keeper){
        keeper.getSteering().pursuitOn(); 
    }

    public void execute(GoalKeeper keeper){ 
        //if the goalkeeper moves to far away from the goal he should return to his
        //home region UNLESS he is the closest player to the ball, in which case,
        //he should keep trying to intercept it.
        if (keeper.isTooFarFromGoalMouth() && !keeper.isClosestPlayerOnPitchToBall()){
            keeper.getFSM().changeState(ReturnHome.Instance());

            return;
        }
 
        //if the ball becomes in range of the goalkeeper's hands he traps the 
        //ball and puts it back in play
        if (keeper.isBallWithinKeeperRange()){
            keeper.getBall().trap();
            keeper.getMatchGamePlay().setGoalKeeperHasBall(true);
            keeper.getFSM().changeState(PutBallBackInPlay.Instance());

            return;
        }
    }

    public void exit(GoalKeeper keeper){
        keeper.getSteering().pursuitOff();
    }

    public boolean onMessage(GoalKeeper keeper, Telegram telegram){
        return false;
    }
}

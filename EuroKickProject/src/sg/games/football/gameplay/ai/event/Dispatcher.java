package sg.games.football.gameplay.ai.event;

import java.util.PriorityQueue;
import sg.games.football.entities.BaseGameEntity;
import sg.games.football.entities.FootballEntityManager;
import static sg.games.football.geom.Utils.*;

public class Dispatcher {

    static Dispatcher instance;

    //a std::set is used as the container for the delayed messages
    //because of the benefit of automatic sorting and avoidance
    //of duplicates. Messages are sorted by their dispatch time.
    PriorityQueue<Telegram> PriorityQ;
    //to make code easier to read
    public static double SEND_MSG_IMMEDIATELY = 0.0;
    public static int NO_ADDITIONAL_INFO = 0;
    public static int SENDER_ID_IRRELEVANT = -1;

    public static Dispatcher Instance() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    //----------------------------- Dispatch ---------------------------------
    //this method is utilized by DispatchMsg or DispatchDelayedMessages.
    //This method calls the message handling member function of the receiving
    //entity, pReceiver, with the newly created telegram
    //------------------------------------------------------------------------                                  
    public void discharge(BaseGameEntity pReceiver, Telegram telegram) {
        if (!pReceiver.handleMessage(telegram)) {
            //telegram could not be handled
        }
    }

    //---------------------------- DispatchMsg ---------------------------
    //
    // given a message, a receiver, a sender and any time delay, this function
    // routes the message to the correct agent (if no delay) or stores
    // in the message queue to be dispatched at the correct time
    //------------------------------------------------------------------------
    public void dispatchMsg(double delay,
            int sender,
            int receiver,
            Telegram.MessageType msg,
            Object AdditionalInfo) {

        //get a pointer to the receiver
        BaseGameEntity pReceiver = FootballEntityManager.Instance().getEntityById(receiver);

        //make sure the receiver is valid
        if (pReceiver == null) {
            return;
        }

        //create the telegram
        Telegram telegram = new Telegram(0, sender, receiver, msg, AdditionalInfo);

        //if there is no delay, route telegram immediately 
        if (delay <= 0.0) {
            //send the telegram to the recipient
            discharge(pReceiver, telegram);
        } //else calculate the time when the telegram should be dispatched
        else {
            double CurrentTime = timeGetTime();

            telegram.DispatchTime = CurrentTime + delay;

            //and put it in the queue
            PriorityQ.add(telegram);

        }
    }

    //---------------------- DispatchDelayedMessages -------------------------
    //
    // This function dispatches any telegrams with a timestamp that has
    // expired. Any dispatched telegrams are removed from the queue
    //------------------------------------------------------------------------
    public void dispatchDelayedMessages() {
        //first get current time
        double CurrentTime = timeGetTime();

        //now peek at the queue to see if any telegrams need dispatching.
        //remove all telegrams from the front of the queue that have gone
        //past their sell by date
        while (!PriorityQ.isEmpty()
                && (PriorityQ.peek().DispatchTime < CurrentTime)
                && (PriorityQ.peek().DispatchTime > 0)) {
            //read the telegram from the front of the queue
            Telegram telegram = PriorityQ.peek();

            //find the recipient
            BaseGameEntity pReceiver = FootballEntityManager.Instance().getEntityById(telegram.Receiver);

            //send the telegram to the recipient
            discharge(pReceiver, telegram);

            //remove it from the queue
            PriorityQ.poll();
        }
    }
}

package sg.games.football.entities;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import sg.games.football.gameplay.SoccerPitch;
import sg.games.football.geom.Vector2D;
import java.util.ArrayList;
import java.util.Iterator;
import sg.atom.core.AtomMain;
import sg.atom.entity.Entity;
import sg.atom.entity.EntityManager;
import sg.games.football.FootballGame;

/**
 *
 * @author hungcuong
 */
public class FootballEntityManager extends EntityManager {

    /**
     * Singleton reference of Object.
     */
    private static FootballEntityManager selfRef;

    /**
     * Constructs singleton instance of Object.
     */
    private FootballEntityManager(AtomMain app) {
        super(app);
        selfRef = this;
    }

    /**
     * Provides reference to singleton object of Object.
     *
     * @return Singleton instance of Object.
     */
    public static final FootballEntityManager getInstance() {
        if (selfRef == null) {
            selfRef = new FootballEntityManager(FootballGame.getInstance());
        }
        return selfRef;
    }

    public static final FootballEntityManager Instance() {
        return getInstance();
    }

    public ArrayList<BaseGameEntity> getAllEntities() {
        return new ArrayList(entities.values());
    }

    public <T extends Entity> ArrayList<T> getAllEntitiesByClass(Class<T> clazz) {
        ArrayList<T> result = new ArrayList<T>();
        for (Iterator<Entity> it = entities.values().iterator(); it.hasNext();) {
            Entity e = it.next();

            if (clazz.isInstance(e)) {
                result.add((T) e);
            }
        }
        return result;
    }

    @Override
    public BaseGameEntity getEntityById(long id) {
        return (BaseGameEntity) super.getEntityById(id);
    }
    /*
     public FootballGameEntity findNear(FootballGameEntity target) {
     for (Entity e : entities.values()) {
     if (e instanceof FootballGameEntity) {
     FootballGameEntity ge = (FootballGameEntity) e;
     if (ge.distance(target) < FootballRuleManager.DISTANCE_NEAR) {
     return ge;
     }
     }
     }
     return null;
     }
    
    
     float distance(FootballGameEntity e1,FootballGameEntity e2) {
     return e1.postion.distance(e2.postion);
     }
     */
    /*
     *shortcut getter setter
     */

    public Transform getDefaultTransform(Vector2D pos2D, Vector2D heading) {
        Transform t = new Transform();
        SoccerPitch pitch = getApp().getGamePlayManager().getMatchGamePlay().getPitch();
        t.setTranslation(pitch.vec2DToVec3D(pos2D));
        return t;

    }

    public Vector3f getDefaultTranslation(Vector2D pos2D) {
        return getApp().getGamePlayManager().getMatchGamePlay().getPitch().vec2DToVec3D(pos2D);

    }

    @Override
    public FootballGame getApp() {
        return (FootballGame) super.getApp(); //To change body of generated methods, choose Tools | Templates.
    }
}

package agora.humano.seja.com.bis;

import android.hardware.SensorEvent;

import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

/**
 * Created by ricardoogliari on 6/8/17.
 */

import static agora.humano.seja.com.bis.DeviceSettings.screenHeight;
import static agora.humano.seja.com.bis.DeviceSettings.screenWidth;
import static agora.humano.seja.com.bis.DeviceSettings.screenResolution;

public class Player extends CCSprite {

    private ShootEngineDelegate delegate;

    float positionX = screenWidth() / 2;
    float positionY = 50;


    public void start() {
        this.schedule("update");
    }


    public Player() {
        super(Assets.NAVE);
        setPosition(positionX, positionY);

        start();
    }

    public void shoot() {
        delegate.createShoot(
                new Shoot(positionX, positionY));
    }

    public void setDelegate(ShootEngineDelegate delegate) {
        this.delegate = delegate;
    }

    public void moveLeft() {
        if (positionX > 30) {
            positionX -= 10;
        }
        setPosition(positionX, positionY);
    }

    public void moveRight() {
        if (positionX < screenWidth() - 30) {
            positionX += 10;
        }
        setPosition(positionX, positionY);
    }

    public void explode() {
        SoundEngine.sharedEngine().playEffect(
                CCDirector.sharedDirector().getActivity(), R.raw.over);
        // Para o agendamento
        this.unschedule("update");
        // Cria efeitos
        float dt = 0.2f;
        CCScaleBy a1 = CCScaleBy.action(dt, 2f);
        CCFadeOut a2 = CCFadeOut.action(dt);
        CCSpawn s1 = CCSpawn.actions(a1, a2);
        // Roda os efeitos
        this.runAction(CCSequence.actions(s1));
    }

    public void update(float dt) {
        // Configura posicao do aviao
        this.setPosition(CGPoint.ccp(this.positionX, this.positionY));
    }

}

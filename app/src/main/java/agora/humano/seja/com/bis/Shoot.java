package agora.humano.seja.com.bis;

import android.util.Log;

import org.cocos2d.actions.instant.CCCallFunc;
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

import static agora.humano.seja.com.bis.DeviceSettings.screenResolution;
import static agora.humano.seja.com.bis.DeviceSettings.screenHeight;

public class Shoot extends CCSprite {
    private ShootEngineDelegate delegate;
    float positionX, positionY;

    public Shoot(float positionX, float positionY) {
        super(Assets.SHOOT);
        this.positionX = positionX;
        this.positionY = positionY;
        setPosition(this.positionX, this.positionY);
        this.schedule("update");
        start();
    }

    public void update(float dt) {
        positionY += 2;

        this.setPosition(
                screenResolution(
                        CGPoint.ccp(positionX, positionY)
                )
        );

        if (positionY > screenHeight() + 10){
            this.delegate.removeShoot(this);
            removeMe();
        }

    }

    public void setDelegate(ShootEngineDelegate delegate) {
        this.delegate = delegate;
    }

    public void explode() {
        // Remove do array
        this.delegate.removeShoot(this);
        // Para o agendamento

        // Cria efeitos
        float dt = 0.2f;
        CCScaleBy a1 = CCScaleBy.action(dt, 2f);
        CCFadeOut a2 = CCFadeOut.action(dt);
        CCSpawn s1 = CCSpawn.actions(a1, a2);
        // Função a ser executada após efeito
        CCCallFunc c1 = CCCallFunc.action(this, "removeMe");
        // Roda efeito
        this.runAction(CCSequence.actions(s1, c1));
    }

    public void removeMe() {
        this.unschedule("update");
        this.removeFromParentAndCleanup(true);
    }

    public void start() {
        SoundEngine.sharedEngine().playEffect(
                CCDirector.sharedDirector().getActivity(), R.raw.shoot);
    }
}

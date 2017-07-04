package agora.humano.seja.com.bis;


import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static agora.humano.seja.com.bis.DeviceSettings.screenHeight;
import static agora.humano.seja.com.bis.DeviceSettings.screenResolution;
import static agora.humano.seja.com.bis.DeviceSettings.screenWidth;

public class GameScene extends CCLayer implements MeteorsEngineDelegate, ShootEngineDelegate {
    private ScreenBackground background;
    private MeteorsEngine meteorsEngine;
    private CCLayer meteorsLayer;
    private List meteorsArray;

    private CCLayer playerLayer;
    private Player player;

    private GameButtons gameButtonsLayer;

    private CCLayer shootsLayer;
    private ArrayList shootsArray;

    private List playersArray;

    private CCLayer scoreLayer;
    private Score score;

    private Pubnub pubnub;

    private GameScene() {
        this.background = new ScreenBackground(Assets.BACKGROUND);
        this.background.setPosition(
                screenResolution(
                        CGPoint.ccp(screenWidth() / 2.0f, screenHeight() / 2.0f)));
        this.addChild(this.background);

        this.meteorsLayer = CCLayer.node();
        this.addChild(this.meteorsLayer);

        this.playerLayer = CCLayer.node();
        this.addChild(this.playerLayer);

        gameButtonsLayer = new GameButtons();
        gameButtonsLayer.setDelegate(this);
        this.addChild(this.gameButtonsLayer);

        this.shootsLayer = CCLayer.node();
        this.addChild(this.shootsLayer);

        this.scoreLayer = CCLayer.node();
        this.addChild(this.scoreLayer);

        this.setIsTouchEnabled(true);

        this.addGameObjects();

        pubnub = new Pubnub("pub-c-128c8884-d4b7-4c72-bfd5-273a0a73c35c",
                "sub-c-c5dee452-6049-11e7-b272-02ee2ddab7fe");
    }

    @Override
    public void onEnter() {
        super.onEnter();
        this.schedule("checkHits");
        this.startEngines();
    }

    private void startEngines() {
        this.addChild(this.meteorsEngine);
        this.meteorsEngine.setDelegate(this);
    }

    public static CCScene createGame() {
        CCScene scene = CCScene.node();
        GameScene layer = new GameScene();
        scene.addChild(layer);
        return scene;
    }

    @Override
    public void createMeteor(Meteor meteor) {
        meteor.setDelegate(this);
        this.meteorsLayer.addChild(meteor);
        meteor.start();
        this.meteorsArray.add(meteor);
    }

    @Override
    public void removeMeteor(Meteor meteor) {
        this.meteorsArray.remove(meteor);
    }

    @Override
    public void removeShoot(Shoot shoot) {
        this.shootsArray.remove(shoot);
    }

    @Override
    public void createShoot(Shoot shoot) {
        this.shootsLayer.addChild(shoot);
        shoot.setDelegate(this);
        shoot.start();
        this.shootsArray.add(shoot);
    }

    private void addGameObjects() {
        this.meteorsArray = new ArrayList();
        this.meteorsEngine = new MeteorsEngine();

        this.player = new Player();
        this.playerLayer.addChild(this.player);

        this.shootsArray = new ArrayList();
        this.player.setDelegate(this);

        this.playersArray = new ArrayList();
        this.playersArray.add(this.player);

        // placar
        this.score = new Score();
        this.scoreLayer.addChild(this.score);
    }



    public boolean shoot() {
        player.shoot();
        return true;
    }

    public void moveLeft() {
        player.moveLeft();
    }

    public void moveRight() {
        player.moveRight();
    }

    public CGRect getBoarders(CCSprite object) {
        CGRect rect = object.getBoundingBox();
        return rect;
    }

    private boolean checkRadiusHitsOfArray(List<? extends CCSprite> array1,
                                           List<? extends CCSprite> array2,
                                           GameScene gameScene,
                                           String hit) {
        boolean result = false;
        for (int i = 0; i < array1.size(); i++) {
            // Pega objeto do primeiro array
            CGRect rect1 = getBoarders(array1.get(i));
            for (int j = 0; j < array2.size(); j++) {
                // Pega objeto do segundo array
                CGRect rect2 = getBoarders(array2.get(j));
                // Verifica colisão
                if (CGRect.intersects(rect1, rect2)) {
                    System.out.println("Colision Detected: " + hit);
                    result = true;

                    Method method;
                    try {
                        method = GameScene.class.getMethod(hit, CCSprite.class, CCSprite.class);
                        method.invoke(gameScene, array1.get(i), array2.get(j));
                    } catch (SecurityException e1) {
                        e1.printStackTrace();
                    } catch (NoSuchMethodException e1) {
                        e1.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    public void checkHits(float dt) {
        this.checkRadiusHitsOfArray(this.meteorsArray,
                this.shootsArray, this, "meteoroHit");
        this.checkRadiusHitsOfArray(this.meteorsArray,
                this.playersArray, this, "playerHit");
    }

    public void meteoroHit(CCSprite meteor, CCSprite shoot) {
        pubnub.publish("jogo_bis14", 1, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                Log.e("TAG", "successCallback: " + message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.e("TAG", "errorCallback: " + error.getErrorString());
            }
        });
        ((Meteor) meteor).shooted();
        ((Shoot) shoot).explode();
        this.score.increase();
    }

    public void playerHit(CCSprite meteor, CCSprite player) {
        pubnub.publish("jogo_bis14", 0, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                Log.e("TAG", "successCallback: " + message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                Log.e("TAG", "errorCallback: " + error.getErrorString());
            }
        });

        ((Meteor) meteor).shooted();
        //((Player) player).explode();
    }

}

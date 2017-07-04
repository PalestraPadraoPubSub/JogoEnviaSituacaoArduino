package agora.humano.seja.com.bis;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

/**
 * Created by ricardoogliari on 6/5/17.
 */


import static agora.humano.seja.com.bis.DeviceSettings.screenHeight;
import static agora.humano.seja.com.bis.DeviceSettings.screenResolution;
import static agora.humano.seja.com.bis.DeviceSettings.screenWidth;

public class TitleScreen extends CCLayer {


    private ScreenBackground background;


    public TitleScreen() {
        this.background = new ScreenBackground(Assets.BACKGROUND);
        this.background.setPosition(
                screenResolution(
                        CGPoint.ccp(
                                screenWidth() / 2.0f,
                                screenHeight() / 2.0f
                    ))
                );
        this.addChild(this.background);


        CCSprite title = CCSprite.sprite(Assets.LOGO);
        title.setPosition(
                screenResolution(
                    CGPoint.ccp(
                            screenWidth() /2,
                            screenHeight() - 130 )
                    )
                );
        this.addChild(title);

        MenuButtons menuLayer = new MenuButtons();
        this.addChild(menuLayer);
    }


    public CCScene scene() {
        CCScene scene = CCScene.node();
        scene.addChild(this);
        return scene;
    }

}


package agora.humano.seja.com.bis;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.opengl.CCBitmapFontAtlas;

/**
 * Created by ricardoogliari on 6/9/17.
 */

import static agora.humano.seja.com.bis.DeviceSettings.screenHeight;
import static agora.humano.seja.com.bis.DeviceSettings.screenWidth;
import static agora.humano.seja.com.bis.DeviceSettings.screenResolution;

public class Score extends CCLayer {
    private int score;
    private CCBitmapFontAtlas text;

    public Score() {
        this.score = 0;
        this.text = CCBitmapFontAtlas.bitmapFontAtlas(
                String.valueOf(this.score),
                "UniSansSemiBold_Numbers_240.fnt");
        this.text.setScale((float) 240 / 240);
        this.setPosition(screenWidth() - 50, screenHeight() - 50);
        this.addChild(this.text);
    }

    public void increase() {
        score++;
        this.text.setString(String.valueOf(this.score));
    }
}
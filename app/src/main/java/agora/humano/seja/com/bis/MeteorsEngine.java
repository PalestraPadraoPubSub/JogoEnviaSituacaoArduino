package agora.humano.seja.com.bis;

import org.cocos2d.layers.CCLayer;

import java.util.Random;

/**
 * Created by ricardoogliari on 6/5/17.
 */

public class MeteorsEngine extends CCLayer {
    private MeteorsEngineDelegate delegate;

    public MeteorsEngine() {
        this.schedule("meteorsEngine", 1.0f / 10f);
    }

    public void meteorsEngine(float dt) {
        // sorte: 1 em 30 gera um novo meteoro!
        if (new Random().nextInt(30) == 0) {
            this.getDelegate().createMeteor(new Meteor(Assets.METEOR));
        }
    }

    public void setDelegate(MeteorsEngineDelegate delegate) {
        this.delegate = delegate;
    }

    public MeteorsEngineDelegate getDelegate() {
        return delegate;
    }
}

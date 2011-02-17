package blah.blah.blah;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Bot implements IUpdateHandler{
	
	//constants
	
	Texture mBoxTexture;
	TextureRegion mBoxTextureRegion;
	FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0f, 0f);
	Body body;
	public Vector2 botPos = new Vector2(0, 0);
	
	public void onLoadResources(Context mContext, TextureManager mTextureManager){
		TextureRegionFactory.setAssetBasePath("gfx/");
	
		this.mBoxTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	this.mBoxTextureRegion = TextureRegionFactory.createFromAsset(this.mBoxTexture, mContext, "sphere.png", 0, 0);
    	
    	mTextureManager.loadTexture(this.mBoxTexture);
    	
	}
	
	public void addBot(Scene scene, PhysicsWorld physicsWorld, final float pX, final float pY){
			final Sprite bot;
			
			bot = new Sprite(pX, pY, this.mBoxTextureRegion);
			body = PhysicsFactory.createCircleBody(physicsWorld, bot, BodyType.DynamicBody, FIXTURE_DEF);
			botPos = new Vector2(pX,pY);
			
			bot.setUpdatePhysics(false);
			scene.getTopLayer().addEntity(bot);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(bot, body, true, true, false, false));
			
	}
	

	@Override
	public void onUpdate(float pSecondsElapsed) {
		this.botPos = body.getWorldCenter();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void setVelocity(float f, float g) {
		// TODO Auto-generated method stub
		
	}	
}
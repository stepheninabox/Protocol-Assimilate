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
	Texture mBotTexture;
	TextureRegion mBotTextureRegion;
	Sprite sprite;
	Body body;
	
	Vector2 botPos;
	
	public void onLoadResources(Context mContext, TextureManager mTextureManager){
		TextureRegionFactory.setAssetBasePath("gfx/");
	
		this.mBotTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	this.mBotTextureRegion = TextureRegionFactory.createFromAsset(this.mBotTexture, mContext, "sphere.png", 0, 0);
    	
    	mTextureManager.loadTexture(this.mBotTexture);
    	
	}
	
	int i;
	public void onLoadScene(Scene scene, PhysicsWorld physicsWorld){
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		fixtureDef.restitution = 0.7f;
			
			this.sprite = new Sprite(100, 100, this.mBotTextureRegion);
			body = PhysicsFactory.createCircleBody(physicsWorld, this.sprite, BodyType.DynamicBody, fixtureDef);
			
			this.sprite.setUpdatePhysics(false);
			scene.getTopLayer().addEntity(this.sprite);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.sprite, body, true, true, false, false));
			
			this.botPos = body.getWorldCenter();
			
	}
	

	@Override
	public void onUpdate(float dt) {
		this.botPos = this.body.getWorldCenter();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void setVelocity(float f, float g) {
		// TODO Auto-generated method stub
		
	}	
}
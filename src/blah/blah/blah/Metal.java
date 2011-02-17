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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Metal implements IUpdateHandler{
	
	Texture mMetalTexture;
	TextureRegion mMetalTextureRegion;
	FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
	Body body;
	float len, x, y;
	Vector2 botPos;
	Vector2 mPos;
	Vector2 genVec = new Vector2(0, 0);
	
	
	public Metal(Vector2 botPos){
		this.botPos = botPos;
	}

	public void onLoadResources(Context mContext, TextureManager mTextureManager){
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mMetalTexture = new Texture(16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	this.mMetalTextureRegion = TextureRegionFactory.createFromAsset(this.mMetalTexture, mContext, "rustMetal.png", 0, 0);
    	
    	mTextureManager.loadTexture(this.mMetalTexture);
	}
	
	public void addMetal(Scene scene, PhysicsWorld physicsWorld, final float pX, final float pY){
		final Sprite metal;
		
		metal = new Sprite(pX, pY, this.mMetalTextureRegion);
		body = PhysicsFactory.createCircleBody(physicsWorld, metal, BodyType.DynamicBody, FIXTURE_DEF);
		
		metal.setUpdatePhysics(false);
		scene.getTopLayer().addEntity(metal);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(metal, body, true, true, false, false));
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		mPos = body.getWorldCenter();
		x = this.botPos.x - this.mPos.x;
		y = this.botPos.y - this.mPos.y;
		len = (float)Math.sqrt(x*x + y*y);
		genVec.x = x/len*2;
		genVec.y = y/len*2;
		
		body.setLinearVelocity(genVec);
		
		
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	
		
}

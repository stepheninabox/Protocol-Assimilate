package blah.blah.blah;

import java.util.Random;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
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

public class Metal implements IUpdateHandler{
	Texture mMetalTexture;
	TextureRegion mMetalTextureRegion;
	Sprite sprite;
	Body body;
	
	Vector2 genVec = new Vector2(0, 0);
	Vector2 botPos;
	
	Body bodies[];
	Vector2 mPos[];
	Sprite sprites[];
	
	boolean gravMetal[];
	
	final int numMetal = 5;
	
	public Metal(Vector2 botPos){
		this.botPos = botPos;
		
		this.bodies = new Body[numMetal];
		this.mPos = new Vector2[numMetal];
		this.sprites = new Sprite[numMetal];
		this.gravMetal = new boolean[numMetal];
		
		
	}

	public void onLoadResources(Context mContext, TextureManager mTextureManager){
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mMetalTexture = new Texture(16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	this.mMetalTextureRegion = TextureRegionFactory.createFromAsset(this.mMetalTexture, mContext, "rustMetal.png", 0, 0);
    	
    	mTextureManager.loadTexture(this.mMetalTexture);
	}
	
	int i;
	public void onLoadScene(Scene scene, PhysicsWorld mPhysicsWorld){
		FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		fixtureDef.restitution = 0.7f;
		Random r = new Random();
		
		for (i=0; i<this.bodies.length; ++i) {
			this.sprites[i] = new Sprite(r.nextInt(700), r.nextInt(460), this.mMetalTextureRegion);
			
			this.bodies[i] = PhysicsFactory.createBoxBody(mPhysicsWorld, this.sprites[i], BodyType.DynamicBody, fixtureDef);
			
			this.sprites[i].setUpdatePhysics(false);
			scene.getTopLayer().addEntity(this.sprites[i]);
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this.sprites[i],this.bodies[i], true ,true ,false ,false));
			
			this.gravMetal[i] = false;
		}
	}

	float len, x, y;
	public void onUpdate(float dt) {
		for (i=0; i<bodies.length; ++i){
			mPos[i] = bodies[i].getWorldCenter();
			x = botPos.x - mPos[i].x;
			y = botPos.y - mPos[i].y;
			len = (float)Math.sqrt(x*x + y*y);
			genVec.x = x/len*10;
			genVec.y = y/len*10;
			
			if (this.len <= 4 | this.gravMetal[i] == true) {
				bodies[i].applyForce(genVec, mPos[i]);
				gravMetal[i] = true;
			}

		}
	}
	
	

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}
	
	//void removeMetal(Shape mMetal){
    	//final Scene scene = this.mEngine.getScene();
    	
    	//final PhysicsConnector mMetalPhysicsConnector = this.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(mMetal);
    	
    	//this.mPhysicsWorld.unregisterPhysicsConnector(mMetalPhysicsConnector);
		//this.mPhysicsWorld.destroyBody(mMetalPhysicsConnector.getBody());
		
		//scene.getTopLayer().removeEntity(mMetal);
    //}
		
}

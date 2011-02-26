package blah.blah.blah;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchException;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class MyMain extends BaseGameActivity implements IAccelerometerListener{
	
	//	===========================================================
	//	Constants
	//	===========================================================
	
	private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;
    
    //	===========================================================
    //	Fields
    //	===========================================================
    
    private Camera mCamera;
    
    Bot mBot = new Bot();
    Metal mMetal = new Metal(mBot.botPos);
   
    
    private Texture mOnScreenControlTexture;
    private TextureRegion mOnScreenControlBaseTextureRegion;
    private TextureRegion mOnScreenControlKnobTextureRegion;
    
    private Texture mLevelBackground;
    private TextureRegion mLevelBackgroundTextureRegion;
    
    private boolean mPlaceOnScreenControlsAtDifferentVerticalLocations = false;
    
    private PhysicsWorld mPhysicsWorld;
    
    private boolean mStopRot;
    
    // 	===========================================================
	// 	Constructors
	// 	===========================================================
    
	// 	===========================================================
	// 	Getter & Setter
	// 	===========================================================

	// 	===========================================================
	// 	Methods for/from SuperClass/Interfaces
	// 	===========================================================
    
    @Override
	public Engine onLoadEngine() {
    	this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    	Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,new
    			RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
    	
    	try {
			if(MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
				if(MultiTouch.isSupportedDistinct(this)) {
					Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_LONG).show();
				} else {
					this.mPlaceOnScreenControlsAtDifferentVerticalLocations = true;
					Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
		} catch (final MultiTouchException e) {
			Toast.makeText(this, "Sorry your Android Version does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
		}
		
		return engine;
    }
    
    
    @Override
	public void onLoadResources() {
    	
    	TextureRegionFactory.setAssetBasePath("gfx/");
    	
    	final TextureManager mTextureManager = this.mEngine.getTextureManager();
    	
    	// Texture and region
    	//this.mBoxTexture = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	//this.mBoxTextureRegion = TextureRegionFactory.createFromAsset(this.mBoxTexture, this, "box.png", 0, 0);
    	
    	// Control Textures
    	this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	this.mOnScreenControlBaseTextureRegion = 
    		TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
    	this.mOnScreenControlKnobTextureRegion = 
    		TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);
    	
    	this.mLevelBackground = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	this.mLevelBackgroundTextureRegion = 
    		TextureRegionFactory.createFromAsset(this.mLevelBackground , this, "lavaLevel.png",0,0);
    	
    	mTextureManager.loadTextures(this.mOnScreenControlTexture, this.mLevelBackground);
    	mBot.onLoadResources(this, mTextureManager);
    	mMetal.onLoadResources(this, mTextureManager);
    	//this.mEngine.getTextureManager().loadTextures(this.mBoxTexture);
    	
    	//this.enableAccelerometerSensor(this);
    }
    
    @Override
	public Scene onLoadScene() {
    	this.mEngine.registerUpdateHandler(new FPSLogger());
    	
    	Sprite lavaLevel = new Sprite(0, 0, 720, 480, mLevelBackgroundTextureRegion);

		final Scene scene = new Scene(2);
		scene.setBackground(new SpriteBackground(lavaLevel));
		
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
    	
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		
		scene.getBottomLayer().addEntity(ground);
		scene.getBottomLayer().addEntity(roof);
		scene.getBottomLayer().addEntity(left);
		scene.getBottomLayer().addEntity(right);
		
		mBot.onLoadScene(scene, mPhysicsWorld);
		mMetal.onLoadScene(scene, mPhysicsWorld);
		
		
		AnalogOnScreenControl analogOnScreenControlRight = new AnalogOnScreenControl(560, (this.mPlaceOnScreenControlsAtDifferentVerticalLocations) ? 0 : 
					CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion,
        		this.mOnScreenControlKnobTextureRegion, 0.1f, 200, new IAnalogOnScreenControlListener(){
        	@Override
        	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float
        			pValueX, final float pValueY){
        			if (pValueX != 0 && pValueY != 0){
        			float rotationInRad = (float)Math.atan2(-pValueX, pValueY);
        			mBot.body.setTransform(mBot.body.getWorldCenter(), rotationInRad);        		
            		}else{
            			mStopRot = false;
            			
        		}
        	}
        	
        
        	
        	@Override
        	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl){
        		//box.addShapeModifier(new SequenceShapeModifier(new ScaleModifier(0.25f, 1, 1.5f),
        				//new ScaleModifier(0.25f, 1.5f, 1)));
        	}
        
        });
		
		AnalogOnScreenControl analogOnScreenControlLeft = new AnalogOnScreenControl(0, CAMERA_HEIGHT -
        		this.mOnScreenControlBaseTextureRegion.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion,
        		this.mOnScreenControlKnobTextureRegion, 0.1f, 200, new IAnalogOnScreenControlListener(){
        	@Override
        	public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float
        			pValueX, final float pValueY){
        		mBot.body.setLinearVelocity(new Vector2(pValueX*5, pValueY*5));
        		
        		if (pValueX != 0 && pValueY != 0 && mStopRot == false){
        			float rotationInRad = (float)Math.atan2(-pValueX, pValueY);
        			mBot.body.setTransform(mBot.body.getWorldCenter(), rotationInRad);        		
            		}else{
            			mStopRot = true;
        		}
        	}
        	
        
        	
        	@Override
        	public void onControlClick(AnalogOnScreenControl pAnalogOnScreenControl){
        		//box.addShapeModifier(new SequenceShapeModifier(new ScaleModifier(0.25f, 1, 1.5f),
        				//new ScaleModifier(0.25f, 1.5f, 1)));
        	}
        
        });
		
		analogOnScreenControlRight.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA,
        		GL10.GL_ONE_MINUS_SRC_ALPHA);
        analogOnScreenControlRight.getControlBase().setAlpha(0.5f);
        analogOnScreenControlRight.getControlBase().setScaleCenter(0, 128);
        analogOnScreenControlRight.getControlBase().setScale(1.25f);
        analogOnScreenControlRight.getControlKnob().setScale(1.25f);
        analogOnScreenControlRight.refreshControlKnobPosition();
		
        analogOnScreenControlLeft.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA,
        		GL10.GL_ONE_MINUS_SRC_ALPHA);
        analogOnScreenControlLeft.getControlBase().setAlpha(0.5f);
        analogOnScreenControlLeft.getControlBase().setScaleCenter(0, 128);
        analogOnScreenControlLeft.getControlBase().setScale(1.25f);
        analogOnScreenControlLeft.getControlKnob().setScale(1.25f);
        analogOnScreenControlLeft.refreshControlKnobPosition();
        
        scene.setChildScene(analogOnScreenControlLeft);
        
        analogOnScreenControlLeft.setChildScene(analogOnScreenControlRight);
        
        this.mPhysicsWorld.setContactListener( new ContactListener(){
			@Override
			public void beginContact(final Contact pContact){

			}
			
			@Override
			public void endContact(final Contact pContact){
				
			}
				
		});
        
        
        
		scene.registerUpdateHandler(mBot);
		mMetal.botPos = mBot.botPos;
		scene.registerUpdateHandler(mMetal);
		scene.registerUpdateHandler(this.mPhysicsWorld);
		
		return scene;
    }
    
    public void onLoadComplete(){
    	
    }
    
    @Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		this.mPhysicsWorld.setGravity(new Vector2(pAccelerometerData.getY(), pAccelerometerData.getX()));
	}
    
    //new removal code for collision
    
    
    //	==============================================================
    //	Methods
    //	==============================================================
    
    // ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}


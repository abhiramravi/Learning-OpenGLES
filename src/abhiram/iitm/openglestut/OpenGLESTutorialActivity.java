package abhiram.iitm.openglestut;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OpenGLESTutorialActivity extends Activity 
{
	 private GLSurfaceView mGLView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        mGLView = new HelloWorldGLSurfaceView(this);
        
        setContentView(mGLView);
    }
}
class HelloWorldGLSurfaceView extends GLSurfaceView implements OnTouchListener
{
	HelloWorldRenderer mRenderer;
	/**
	 * @param context
	 */
	public HelloWorldGLSurfaceView(Context context)
	{
		super(context);
		
		/* Making sure the context is OpenGL 2.0 */
		setEGLContextClientVersion(2);
		
		/* Setting the renderer for the SurfaceView */
		mRenderer = new HelloWorldRenderer();
		setRenderer(mRenderer);
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		float startX = event.getX();
		float startY = event.getY();
		
		mRenderer.setFrustum((int) (startY - startX));
		return true;
	}
	
}
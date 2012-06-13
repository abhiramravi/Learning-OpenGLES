package abhiram.iitm.openglestut;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

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
class HelloWorldGLSurfaceView extends GLSurfaceView
{

	/**
	 * @param context
	 */
	public HelloWorldGLSurfaceView(Context context)
	{
		super(context);
		
		/* Making sure the context is OpenGL 2.0 */
		setEGLContextClientVersion(2);
		
		/* Setting the renderer for the SurfaceView */
		setRenderer(new HelloWorldRenderer());
	}
	
}
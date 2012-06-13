package abhiram.iitm.openglestut;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

public class HelloWorldRenderer implements GLSurfaceView.Renderer
{
	/*
	 * The float buffer that hold the information about all the triangle
	 * vertices
	 */
	private FloatBuffer triangleVB;

	/*
	 * The vertex shader controls how OpenGL positions and draws the vertices of
	 * shapes in space. The fragment shader controls what OpenGL draws between
	 * the vertices of shapes.
	 */
	private final String vertexShaderCode =
		// This matrix member variable provides a hook to manipulate
		// the coordinates of the objects that use this vertex shader
		"uniform mat4 uMVPMatrix;   \n" + 
		"attribute vec4 vPosition; \n" + 
		"void main(){              \n" +
		// the matrix must be included as a modifier of gl_Position
		" gl_Position = uMVPMatrix * vPosition; \n" +
		"}                         \n";

	private final String fragmentShaderCode = 
		"precision mediump float;  \n" + 
		"void main(){              \n" + 
		" gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" + 
		"}                         \n";

	private int mProgram;
	private int maPositionHandle;

	private int muMVPMatrixHandle;
	private float[] mMVPMatrix = new float[16];
	private float[] mMMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mProjMatrix = new float[16];	

	public void onDrawFrame(GL10 gl)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		GLES20.glUseProgram(mProgram);

		// Preparing the triangle data
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 12, triangleVB);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		
		// Create a rotation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mMMatrix, 0, angle, 0, 0, 1.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Apply a ModelView Projection transformation
		//Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		//GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{

		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;

		// this projection matrix is applied to object coodinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 7, 110);

		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -10, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{

		GLES20.glClearColor(0.3f, 0.4f, 0.2f, 1.0f);

		/* Calling the function to initialize the triangle */

		/*
		 * NOTE : Initialize all vertices and shapes only in onSurfaceCreated,
		 * if you try doing it in drawFrame, then the performance drastically
		 * reduces.
		 */
		initializeVertices();

		int vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
														// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
															// shader to program
		GLES20.glLinkProgram(mProgram); // creates OpenGL program executables

		// get handle to the vertex shader's vPosition member
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

	}

	public void initializeVertices()
	{
		/* Defining the triangle coordinates */
		float triangleCoords[] =
		{
				// X, Y, Z
				-0.5f, -0.25f, 0, 0.5f, -0.25f, 0, 0.0f, 0.559016994f, 0 };

		/*
		 * Creating a byte buffer that contains information as to how to
		 * allocate memory NOTE : This is done as an intermediate step between
		 * java and openGL processing because openGL does not know which
		 * ordering the vertices use in the JVM
		 */
		ByteBuffer vbb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());

		/*
		 * Now we tell our float buffer to use thie byte buffer as a reference
		 * to allocate memory and stuff
		 */
		triangleVB = vbb.asFloatBuffer();

		/*
		 * Now we put our triangle coordinate values in the floatbuffer and it
		 * is stored following the rules of vbb, out byte buffer
		 */
		triangleVB.put(triangleCoords);

		/*
		 * Initializing the float buffer to kind of point to the first element,
		 * or rather the first memory location in the float buffer. here it is
		 * &triangleCoord[0]
		 */
		triangleVB.position(0);
	}

	public int LoadShader(int type, String shaderCode)
	{
		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;

	}
}

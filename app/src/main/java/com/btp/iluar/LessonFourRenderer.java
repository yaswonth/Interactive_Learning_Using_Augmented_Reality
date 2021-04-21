package com.btp.iluar;

import android.content.Context;
import android.hardware.camera2.CameraDevice;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import org.opencv.core.Mat;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class LessonFourRenderer implements GLSurfaceView.Renderer
{
    /** Used for debug logs. */
    private static final String TAG = "LessonFourRenderer";
    ObjLoader loader,bloader;
    Mat texmat=null;
    float ratio;
    int i=0;
    int textureid;
    String objname;
    private String[] mlist = {"apple.obj", "ant.obj", "axe.obj", "book.obj", "bell.obj", "butter.obj", "ball.obj", "banana.obj", "cake.obj", "car.obj", "cat.obj", "chair.obj", "carro.obj", "dog.obj", "dolphin.obj", "duck.obj", "Donut2.obj", "ea.obj", "egg.obj", "elephant.obj", "ear.obj", "frouug.obj", "fan.obj", "fox.obj", "fish.obj", "gift.obj", "gold.obj", "hammer.obj", "house.obj", "hat.obj", "ice.obj", "iron.obj", "jeans.obj", "jug.obj", "key.obj", "knife.obj", "kite.obj", "Lock.obj", "lemon.obj", "ladder.obj", "mushroom.obj", "monkey.obj", "mobi.obj", "nuts.obj", "na.obj", "ora.obj", "ow.obj", "pine.obj", "pizza.obj", "pen.obj", "pig.obj", "pump.obj", "yellow.obj", "yellow.obj", "rose.obj", "rat.obj", "rocket.obj", "ring.obj", "santa.obj", "snow.obj", "snake.obj", "shoes.obj", "tur.obj", "train.obj", "teddy.obj", "uni.obj", "umb.obj", "violin.obj", "van.obj", "watch.obj", "wheel.obj", "yellow.obj", "yellow.obj", "yach.obj", "yellow.obj", "zodiac.obj", "zebra.obj"};
    private int[] tlist = {R.drawable.apple, R.drawable.ant, R.drawable.axe, R.drawable.book, R.drawable.bell, R.drawable.butter, R.drawable.ball, R.drawable.banana, R.drawable.cake, R.drawable.car, R.drawable.cat, R.drawable.chair, R.drawable.carrot, R.drawable.imo, R.drawable.dolphin, R.drawable.duck, R.drawable.donut, R.drawable.fogel, R.drawable.ball, R.drawable.ele, R.drawable.ear, R.drawable.frog, R.drawable.fan, R.drawable.ia, R.drawable.fish, R.drawable.gift, R.drawable.gold, R.drawable.ham, R.drawable.ho, R.drawable.hat, R.drawable.ice, R.drawable.iron, R.drawable.jeans, R.drawable.jug, R.drawable.key, R.drawable.knife, R.drawable.kite, R.drawable.lock, R.drawable.lemon, R.drawable.mus, R.drawable.monkey, R.drawable.im, R.drawable.nuts, R.drawable.nail, R.drawable.c, R.drawable.ow, R.drawable.pineapple, R.drawable.pizza, R.drawable.pen, R.drawable.pig, R.drawable.pump, R.drawable.quest, R.drawable.queen, R.drawable.rose, R.drawable.rat, R.drawable.rocket, R.drawable.ring, R.drawable.santa, R.drawable.snow, R.drawable.snake, R.drawable.shoes, R.drawable.turtle, R.drawable.train, R.drawable.texbear, R.drawable.uni, R.drawable.umb, R.drawable.violin, R.drawable.van, R.drawable.watch, R.drawable.wheel, R.drawable.xray, R.drawable.xyl, R.drawable.yacht, R.drawable.yellow, R.drawable.zodiac, R.drawable.zebra};


    private final Context mActivityContext;

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];

    private float[] mModelMatrix1 = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix1 = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix1 = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix1 = new float[16];
    private float[] mModelMatrix2 = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix2 = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix2 = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix2 = new float[16];

    /** Store our model data in a float buffer. */
    private final FloatBuffer mCubePositions;
//    private final FloatBuffer mCubeColors;
    private final FloatBuffer mCubeNormals;
    private final FloatBuffer mCubeTextureCoordinates;
    private final FloatBuffer mCubePositions1;
    //    private final FloatBuffer mCubeColors;
    private final FloatBuffer mCubeNormals1;
    private final FloatBuffer mCubeTextureCoordinates1;

    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;

    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;


    /** This will be used to pass in the texture. */
    private int mTextureUniformHandle;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mColorHandle;

    /** This will be used to pass in model normal information. */
    private int mNormalHandle;

    /** This will be used to pass in model texture coordinate information. */
    private int mTextureCoordinateHandle;

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    /** Size of the normal data in elements. */
    private final int mNormalDataSize = 3;

    /** Size of the texture coordinate data in elements. */
    private final int mTextureCoordinateDataSize = 2;

    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     *  we multiply this by our transformation matrices. */
//    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
//
//    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
//    private final float[] mLightPosInWorldSpace = new float[4];
//
//    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
//    private final float[] mLightPosInEyeSpace = new float[4];

    /** This is a handle to our cube shading program. */
    private int mProgramHandle;

    /** This is a handle to our light point program. */
//    private int mPointProgramHandle;

    /** This is a handle to our texture data. */
    private int mTextureDataHandle;
    private int mTextureDataHandle1;
    private float tx = 0.0f;
    private float ty = 0.0f;
    private float tz = 30.0f;

    /**
     * Initialize the model data.
     */
    public LessonFourRenderer(final Context activityContext,ObjLoader obj,ObjLoader objb, int v)
    {
        mActivityContext = activityContext;

        this.loader = obj;
        this.bloader=objb;
        this.objname=mlist[v];
        this.textureid = tlist[v];
        try {
            bloader.load("yellow.obj");
            loader.load(objname);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final float[] cubePositionData =bloader.posd;
        final float[] cubePositionData1 =loader.posd;
        final float[] cubeNormalData =bloader.nord;
        final float[] cubeTextureCoordinateData =bloader.texd;
        final float[] cubeNormalData1 =loader.nord;
        final float[] cubeTextureCoordinateData1 =loader.texd;

        // Initialize the buffers.
        mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions.put(cubePositionData).position(0);

//        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mCubeColors.put(cubeColorData).position(0);

        mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeNormals.put(cubeNormalData).position(0);

        mCubeTextureCoordinates = ByteBuffer.allocateDirect(cubeTextureCoordinateData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeTextureCoordinates.put(cubeTextureCoordinateData).position(0);


        mCubePositions1 = ByteBuffer.allocateDirect(cubePositionData1.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubePositions1.put(cubePositionData1).position(0);

//        mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mCubeColors.put(cubeColorData).position(0);

        mCubeNormals1 = ByteBuffer.allocateDirect(cubeNormalData1.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeNormals1.put(cubeNormalData1).position(0);

        mCubeTextureCoordinates1 = ByteBuffer.allocateDirect(cubeTextureCoordinateData1.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeTextureCoordinates1.put(cubeTextureCoordinateData1).position(0);
    }

    protected String getVertexShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
    }

    protected String getFragmentShader()
    {
        return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        // Set the background clear color to black.
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Use culling to remove back faces.
//        GLES20.glEnable(GLES20.GL_CULL_FACE);
//        // Enable depth testing
//        GLES30.glClearDepthf(1.0f);
//        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
//        GLES30.glDepthFunc(GLES30.GL_LEQUAL);
//        GLES30.glEnable(GLES30.GL_BLEND);
//        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();

        final int vertexShaderHandle = ShaderHelper.compileShader(GLES30.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = ShaderHelper.compileShader(GLES30.GL_FRAGMENT_SHADER, fragmentShader);

        mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position",  "a_Color", "a_Normal", "a_TexCoordinate"});
        mTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.yellow);
        mTextureDataHandle1 = TextureHelper.loadTexture(mActivityContext, textureid);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        // Set the OpenGL viewport to the same size as the surface.
        GLES30.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 0.1f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // Set our per-vertex lighting program.
        GLES30.glUseProgram(mProgramHandle);

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES30.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
        mTextureUniformHandle = GLES30.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES30.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle = GLES30.glGetAttribLocation(mProgramHandle, "a_Color");
        mNormalHandle = GLES30.glGetAttribLocation(mProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES30.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");

        // Set the active texture unit to texture unit 0.


        // Bind the texture to this unit.
        GLES30.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES30.glBindTexture(GLES20.GL_TEXTURE0, mTextureDataHandle1);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES30.glUniform1i(mTextureUniformHandle, 0);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, tx,ty,tz);
        System.out.println("vectors:");
        System.out.println(tx);
        System.out.println(ty);
        System.out.println(tz);
        Matrix.scaleM(mModelMatrix,0,1.0f,1.0f,1.0f);
//        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawCube();

//          Main background pic

//        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
//
//        GLES30.glUniform1i(mTextureUniformHandle, 0);
//        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureDataHandle);
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 0.0f, -0.0f, -39.9f);
////        Matrix.rotateM(mModelMatrix, 0, 180, 0.0f, 2.0f, 0.0f);
//        Matrix.rotateM(mModelMatrix, 0, -90, 0.0f, 0.0f, 2.0f);
//        Matrix.scaleM(mModelMatrix,0,200.0f/ratio,200.0f,1.0f);
//        drawBack();


    }

    /**
     * Draws a cube.
     */
    private void drawBack()
    {


        mModelMatrix1 = mModelMatrix;
        mViewMatrix1 = mViewMatrix;
        mProjectionMatrix1 = mProjectionMatrix;
        mMVPMatrix1 = mMVPMatrix;



        // Pass in the position information
        mCubePositions.position(0);
        GLES30.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES30.GL_FLOAT, false,
                0, mCubePositions);

        GLES30.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
//        mCubeColors.position(0);
//        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
//                0, mCubeColors);
//
//        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        mCubeNormals.position(0);
        GLES30.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES30.GL_FLOAT, false,
                0, mCubeNormals);

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Pass in the texture coordinate information
        mCubeTextureCoordinates.position(0);
        GLES30.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES30.GL_FLOAT, false,
                0, mCubeTextureCoordinates);

        GLES30.glEnableVertexAttribArray(mTextureCoordinateHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix1, 0, mViewMatrix1, 0, mModelMatrix1, 0);

        // Pass in the modelview matrix.
        GLES30.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix1, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix1, 0, mProjectionMatrix1, 0, mMVPMatrix1, 0);

        // Pass in the combined matrix.
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix1, 0);
//
//        // Pass in the light position in eye space.
//        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,bloader.posfin.size());
    }

    private void drawCube()
    {


        mModelMatrix2 = mModelMatrix;
        mViewMatrix2 = mViewMatrix;
        mProjectionMatrix2 = mProjectionMatrix;
        mMVPMatrix2 = mMVPMatrix;

        // Pass in the position information
        mCubePositions1.position(0);
        GLES30.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES30.GL_FLOAT, false,
                0, mCubePositions1);

        GLES30.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
//        mCubeColors.position(0);
//        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
//                0, mCubeColors);
//
//        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        mCubeNormals1.position(0);
        GLES30.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES30.GL_FLOAT, false,
                0, mCubeNormals1);

        GLES30.glEnableVertexAttribArray(mNormalHandle);

        // Pass in the texture coordinate information
        mCubeTextureCoordinates1.position(0);
        GLES30.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES30.GL_FLOAT, false,
                0, mCubeTextureCoordinates1);

        GLES30.glEnableVertexAttribArray(mTextureCoordinateHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix2, 0, mViewMatrix2, 0, mModelMatrix2, 0);

        // Pass in the modelview matrix.
        GLES30.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix2, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix2, 0, mProjectionMatrix2, 0, mMVPMatrix2, 0);

        // Pass in the combined matrix.
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix2, 0);
//
//        // Pass in the light position in eye space.
//        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,loader.posfin.size());
    }

    /**
     * Draws a point representing the position of the light.
     */
     public void setMat(Mat m){
         if (texmat!=null){
             texmat.release();
         }
         texmat = m;
     }

     public void settran(float x, float y,float z){
         tx = x;
         ty = y;
         tz = z-10.0f;
     }
}
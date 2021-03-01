package com.btp.iluar;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.Dimension;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] chibiposMatrix = new float[16];
    private float[] quadposMatrix = new float[16];
    private ObjLoader loader;
    private int prog;
    final int VAO[]=new int[2];
    final int VBO[]=new int[2];
    final int EBO[]=new int[1];
    int model_loc,proj_loc,view_loc,switcher_loc;
    int chibitexture,quadtexture;

    private final String vertexShaderCode=
            "layout(location = 0) in vec3 a_position;" +
                    "layout(location = 1) in vec2 a_texture;" +
                    "layout(location = 2) in vec3 a_normal;" +
                    "uniform mat4 model;" +
                    "uniform mat4 projection;" +
                    "uniform mat4 view;" +
                    "out vec2 v_texture;" +
                    "void main()" +
                    "{" +
                    "    gl_Position = projection * view * model * vec4(a_position, 1.0);" +
                    "    v_texture = a_texture;" +
                    "}";
    private final String fragmentShaderCode=
            "in vec2 v_texture;" +
                    "out vec4 out_color;" +
                    "uniform sampler2D s_texture;" +
                    "void main()" +
                    "{" +
                    "    out_color = texture(s_texture, v_texture);" +
                    "}";
    public MyGLRenderer(Context context,ObjLoader lo){
        this.context = context;
        this.loader=lo;
        try {
            loader.load("yellow.obj");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);





    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
      GLES30.glViewport(0,0,width,height);
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 0.1f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        Matrix.perspectiveM(projectionMatrix,0,45.0f,ratio,near,far);
    }


    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT);







    }
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }

}

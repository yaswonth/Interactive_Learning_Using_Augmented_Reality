package com.btp.iluar;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Quad {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices


    private final String vertexShaderCode=
            "attribute vec3 a_position;" +
                    "attribute vec2 a_texture;" +
                    "attribute vec3 a_normal;" +
                    "uniform mat4 model;" +
                    "uniform mat4 projection;" +
                    "uniform mat4 view;" +
                    "varying vec2 v_texture;" +
                    "void main()" +
                    "{" +
                    "    gl_Position = projection * view * model * vec4(a_position, 1.0);" +
                    "    v_texture = a_texture;" +
                    "}";
    private final String fragmentShaderCode=
            "varying vec2 v_texture;" +
                    "varying vec4 out_color;" +
                    "uniform sampler2D s_texture;" +
                    "void main()" +
                    "{" +
                    "    out_color = vec4 ( 1.0, 0.0, 0.0, 1.0 )" +
                    "}";
    private final int mprogram;
    public Quad(){
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mprogram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mprogram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mprogram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mprogram);

    }

    public void draw(){
        GLES20.glUseProgram(mprogram);

    }
}

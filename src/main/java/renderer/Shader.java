package renderer;

import jdk.internal.util.xml.impl.Input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Shader {

    private int shaderProgramId;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filePath) {
        this.filePath = filePath;
        try {
            String source = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream(filePath)))
                    .lines().collect(Collectors.joining("\n"));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (secondPattern.equals("fragment")){
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token: " + firstPattern);
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token: " + secondPattern);
            }
        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: couldn't open file for shader: '" + filePath + "'.";
        }
    }

    public void compile() {
        int vertexId, fragmentId;
        // Compile vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertexSource);
        glCompileShader(vertexId);

        // Check for errors.
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Vertex "+ filePath + " compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragmentSource);
        glCompileShader(fragmentId);

        // Check for errors.
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Fragment " + filePath + " compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }

        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Shader program " + filePath + " linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramId, len));
            assert false : "";
        }

    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
    }
}

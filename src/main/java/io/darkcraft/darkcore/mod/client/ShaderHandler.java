package io.darkcraft.darkcore.mod.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.Pair;

public class ShaderHandler
{
	private static String readStream(InputStream str)
	{
		StringBuilder b = new StringBuilder();
		try(BufferedReader r = new BufferedReader(new InputStreamReader(str)))
		{
			String l;
			while((l = r.readLine()) != null)
				b.append(l).append('\n');
		}
		catch(IOException e){ e.printStackTrace(); }
		return b.toString();
	}

	private static String getLogInfo(int shader)
	{
		int t = ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
        return ARBShaderObjects.glGetInfoLogARB(shader, t);
    }

	public static int getShader(InputStream str, int shaderType)
	{
		int s = 0;
		try
		{
			s = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			if(s == 0) return 0;

			ARBShaderObjects.glShaderSourceARB(s, readStream(str));
			ARBShaderObjects.glCompileShaderARB(s);
			if (ARBShaderObjects.glGetObjectParameteriARB(s, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException(getLogInfo(s));
		}
		catch(Exception e)
		{
			if(s != 0)
				ARBShaderObjects.glDeleteObjectARB(s);
			throw new RuntimeException(e);
		}
		return s;
	}

	public static int getShaderProgram(Pair<InputStream,Integer>... shaders)
	{
		int[] ids = new int[shaders.length];
		for(int i = 0; i < shaders.length; i++)
		{
			Pair<InputStream,Integer> shader = shaders[i];
			int shaderID = getShader(shader.a,shader.b);
			ids[i] = shaderID;
		}

		int program = ARBShaderObjects.glCreateProgramObjectARB();
		if(program == 0) throw new RuntimeException("Program id 0");
		for(int i : ids)
			ARBShaderObjects.glAttachObjectARB(program, i);
		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
			throw new RuntimeException(getLogInfo(program));

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
            throw new RuntimeException(getLogInfo(program));
        return program;
	}
}

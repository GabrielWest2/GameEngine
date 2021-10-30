package com.gabe.GEngine.objConverter;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class BlockbenchFileLoader {
	
	private static final String RES_LOC = "assets/models/";

	public static ModelData loadOBJ(String objFileName) throws IOException {
		// Read an OBJ file
		InputStream objInputStream = new FileInputStream(RES_LOC + objFileName + ".obj");
		Obj obj = ObjReader.read(objInputStream);

		// Prepare the Obj so that its structure is suitable for
		// rendering with OpenGL:
		// 1. Triangulate it
		// 2. Make sure that texture coordinates are not ambiguous
		// 3. Make sure that normals are not ambiguous
		// 4. Convert it to single-indexed data
		obj = ObjUtils.convertToRenderable(obj);

		// Obtain the data from the OBJ, as direct buffers:
		IntBuffer indices = ObjData.getFaceVertexIndices(obj, 3);
		FloatBuffer vertices = ObjData.getVertices(obj);
		FloatBuffer texCoords = ObjData.getTexCoords(obj, 2);
		FloatBuffer normals = ObjData.getNormals(obj);

		int[] indicesArray = new int[indices.remaining()];
		indices.get(indicesArray);

		float[] verticesArray = new float[vertices.remaining()];
		vertices.get(verticesArray);

		float[] texCoordsArray = new float[texCoords.remaining()];
		texCoords.get(texCoordsArray);

		float[] normalsArray = new float[normals.remaining()];
		normals.get(normalsArray);

		return new ModelData(verticesArray, texCoordsArray, normalsArray, indicesArray, verticesArray.length);
	}

	private static String createString(IntBuffer buffer, int stride)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<buffer.capacity(); i+=stride)
		{
			for (int j=0; j<stride; j++)
			{
				if (j > 0)
				{
					sb.append(", ");
				}
				sb.append(buffer.get(i+j));
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	private static String createString(FloatBuffer buffer, int stride)
	{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<buffer.capacity(); i+=stride)
		{
			for (int j=0; j<stride; j++)
			{
				if (j > 0)
				{
					sb.append(", ");
				}
				sb.append(buffer.get(i+j));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
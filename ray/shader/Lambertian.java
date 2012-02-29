package ray.shader;

import java.util.ArrayList;
import java.util.Iterator;

import ray.IntersectionRecord;
import ray.Scene;
import ray.light.Light;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A Lambertian material scatters light equally in all directions. BRDF value is
 * a constant
 *
 * @author ags, modified by DS 2/2012
 */
public class Lambertian extends Shader {
	
	/** The color of the surface. */
	protected final Color diffuseColor = new Color(1, 1, 1);
	public void setDiffuseColor(Color inDiffuseColor) { diffuseColor.set(inDiffuseColor); }
	
	public Lambertian() { }
	
	/**
	 * Calculate the BRDF value for this material at the intersection described in record.
	 * Returns the BRDF color in outColor.
	 * @param outColor Space for the output color
	 * @param scene The scene
	 * @param lights The lights
	 * @param toEye Vector pointing towards the eye
	 * @param record The intersection record, which hold the location, normal, etc.
	 */
	public void shade(Color outColor, Scene scene, ArrayList<Light> lights, Vector3 toEye, 
			IntersectionRecord record) {
		// TODO: fill in this function.
		// Hint: 
		//   1. Add contribution to the final pixel from each light source. 
		//   2. See how to use isShadowed().
		Color kd = new Color(diffuseColor.r,diffuseColor.g,diffuseColor.b);
		Vector3 n = new Vector3();
		n.set(record.normal);
		n.normalize();
		double r=0,g=0,b=0;


		
		// Can iterate over lights as like this:
		for (Iterator<Light> iter = lights.iterator(); iter.hasNext();) {
			Light l = iter.next();
			Vector3 lV = new Vector3();
			lV.sub(l.position,record.location);
			lV.normalize();
			//if(!isShadowed(scene, l, record)) {
				r += (kd.r * l.intensity.r * Math.max(0,lV.dot(n)));
				g += (kd.g * l.intensity.g * Math.max(0,lV.dot(n)));
				b += (kd.b * l.intensity.b * Math.max(0,lV.dot(n)));
			//}
		}
		outColor.set(r, g, b);
		//outColor.set(kd);
		//System.out.println(outColor);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "Lambertian: " + diffuseColor;
	}
	
}
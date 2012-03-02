package ray.shader;

import java.util.ArrayList;
import java.util.Iterator;

import ray.IntersectionRecord;
import ray.Scene;
import ray.light.Light;
import ray.math.Color;
import ray.math.Vector3;

/**
 * A Phong material. Uses the Modified Blinn-Phong model which is energy
 * preserving and reciprocal.
 *
 * @author ags, modified by DS 2/2012
 */
public class Phong extends Shader {
	
	/** The color of the diffuse reflection. */
	protected final Color diffuseColor = new Color(1, 1, 1);
	public void setDiffuseColor(Color diffuseColor) { this.diffuseColor.set(diffuseColor); }
	
	/** The color of the specular reflection. */
	protected final Color specularColor = new Color(1, 1, 1);
	public void setSpecularColor(Color specularColor) { this.specularColor.set(specularColor); }
	
	/** The exponent controlling the sharpness of the specular reflection. */
	protected double exponent = 1.0;
	public void setExponent(double exponent) { this.exponent = exponent; }
	
	public Phong() { }
	
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

		Vector3 h = new Vector3();

		Vector3 lV = new Vector3();
		Vector3 n = new Vector3(record.normal);
		
		double r=0,g=0,b=0;
		Color kd=new Color(diffuseColor.r,diffuseColor.g,diffuseColor.b);
		Color ks=new Color(specularColor.r,specularColor.g,specularColor.b);
		
		for (Iterator<Light> iter = lights.iterator(); iter.hasNext();) {
			Light l = iter.next();
			// Calculate l and h
			lV.sub(record.location,l.position);
			lV.normalize();
			h.add(toEye, lV);
			h.normalize();

			// Lambertian Component
			r += (kd.r * l.intensity.r * Math.max(0,lV.dot(n)));
			g += (kd.g * l.intensity.g * Math.max(0,lV.dot(n)));
			b += (kd.b * l.intensity.b * Math.max(0,lV.dot(n)));
			// Phong Component
			r += (ks.r * l.intensity.r * Math.pow(Math.max(0, n.dot(h)),exponent));
			g += (ks.g * l.intensity.g * Math.pow(Math.max(0, n.dot(h)),exponent));
			b += (ks.b * l.intensity.b * Math.pow(Math.max(0, n.dot(h)),exponent));
			

		}
		outColor.set(r, g, b);
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		
		return "phong " + diffuseColor + " " + specularColor + " " + exponent + " end";
	}
}
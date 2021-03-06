package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

/**
 * Represents a sphere as a center and a radius.
 *
 * @author ags
 */
public class Sphere extends Surface {
	
	/** The center of the sphere. */
	protected final Point3 center = new Point3();
	public void setCenter(Point3 center) { this.center.set(center); }
	
	/** The radius of the sphere. */
	protected double radius = 1.0;
	public void setRadius(double radius) { this.radius = radius; }
	
	public Sphere() { }
	
	/**
	 * Tests this surface for intersection with ray. If an intersection is found
	 * record is filled out with the information about the intersection and the
	 * method returns true. It returns false otherwise and the information in
	 * outRecord is not modified.
	 *
	 * @param outRecord the output IntersectionRecord
	 * @param ray the ray to intersect
	 * @return true if the surface intersects the ray
	 */
	public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {

		// Calculate the various components of the sphere intersection formula.
		// Comes from the Computer Graphics textbook.
		Point3 o = new Point3(rayIn.origin);
		Vector3 d = new Vector3(rayIn.direction);
		
		Point3 c = center;
		Vector3 oc = new Vector3();
		oc.sub(o,c);	
		double A = d.dot(d);
		double C = oc.dot(oc) - Math.pow(radius, 2);
		d.scale(2);
		double B = d.dot(oc);
		double discriminant = (Math.pow(B, 2)-(4*A*C));
		if(discriminant<0.0) {
			return false;
		} else {
			d = new Vector3(rayIn.direction);
			double t0 = ((-B) + Math.sqrt(discriminant))/(2*A);
			double t1 = ((-B) - Math.sqrt(discriminant))/(2*A);
			double t;
			if(t0<0) t=t1;
			else if(t1<0) t=t0;	
			else if(t1>=t0) t=t0;
			else if(t1<t0) t=t1;
			else return false;
			d.scale(t);

			// Calculate the outRecord
			rayIn.evaluate(outRecord.location, t);
			outRecord.surface = this;
			outRecord.normal.set(new Vector3());
			outRecord.normal.sub(center,outRecord.location);
			outRecord.normal.normalize();
			outRecord.t=t;

			rayIn.start=0;
			rayIn.end=t;

			return true;
		}
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "sphere " + center + " " + radius + " " + shader + " end";
	}

}
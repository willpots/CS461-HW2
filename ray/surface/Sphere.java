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
		// TODO: fill in this function.
		// compute ray-sphere intersection

		// 
		Point3 o = new Point3();
		o.set(rayIn.origin);
		Vector3 d = rayIn.direction;
		Point3 c = center;
		Vector3 oc = new Vector3();
		oc.sub(o, c);
		
		
		
		double A = d.dot(d);
		double C = oc.dot(oc) - Math.pow(radius, 2);
		oc.scale(2);
		double B = d.dot(oc);
		
		
		if(Math.pow(B, 2)-(4*A*C)<0) {
			return false;
		} else {
			double t;
			if(B<0) {
				t = ((-1*B) + Math.sqrt(Math.pow(B, 2)-(4*A*C)))/(2*A);
			} else {
				t = ((-1*B) - Math.sqrt(Math.pow(B, 2)-(4*A*C)))/(2*A);
			}
			d = rayIn.direction;
			d.scale(t);
			outRecord.location.set(d);
			outRecord.surface = this;

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
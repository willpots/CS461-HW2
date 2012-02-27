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


		Point3 o = new Point3(rayIn.origin);
		Vector3 d = new Vector3(rayIn.direction);
		
		//System.out.println(d);
		Point3 c = center;
		//System.out.println(c);
		Vector3 oc = new Vector3();
		oc.sub(o, c);	
		//System.out.println(radius);
		double A = d.dot(d);
		//System.out.println(A);
		double C = oc.dot(oc) - Math.pow(radius, 2);
		d.scale(2);
		double B = d.dot(oc);
		double discriminant = (Math.pow(B, 2)-(4*A*C));
		//System.out.print("D: "+d+" A: "+A+" B: "+B+" C: "+C+" ");
		//System.out.print(discriminant+"  ");
		//System.out.println(rayIn.toString());
		if(discriminant<0.0) {
			//System.out.println("Returning false!\n\n");
			return false;
		} else {
			d = new Vector3(rayIn.direction);
			double t0 = ((-1*B) + Math.sqrt(discriminant))/(2*A);
			double t1 = ((-1*B) - Math.sqrt(discriminant))/(2*A);
			double t;
			if(t0<0) t=t1;
			else if(t1<0) t=t0;	
			else if(t1>t0) t=t0;
			else if(t1<t0) t=t1;
			else return false;
			d.scale(t);

			outRecord.location.set(new Point3(d.x,d.y,d.z));
			outRecord.surface = this;
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
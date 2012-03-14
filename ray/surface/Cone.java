package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
//import ray.math.Vector3;
import ray.math.Vector3;

public class Cone extends Surface {
	
	/** The center of the the truncated cone. */
    protected final Point3 center = new Point3();
    public void setCenter(Point3 center) { this.center.set(center); }

    /** The z-location of the tip of the cone. */
    protected double tipz = 0.0;
    public void setTipz(double tipz) { this.tipz = tipz; }

    /** The radius of the cone in the plane z = center.z. */
    protected double radius = 1.0;
    public void setRadius(double radius) { this.radius = radius; }

    /** The height of the cone.
     *  Truncation of the cone occurs at center.z - height/2 and center.z + height/2
     */
    protected double height = 1.0;
    public void setHeight(double height) { this.height = height; }

	public Cone() { }

	/**
	 * Tests this surface for intersection witheightray. If an intersection is found
	 * record is filled out witheightthe information about the intersection and the
	 * method returns true. It returns false otherwise and the information in
	 * outRecord is not modified.
	 *
	 * @param outRecord the output IntersectionRecord
	 * @param ray the ray to intersect
	 * @return true if the surface intersects the ray
	 */
	public boolean intersect(IntersectionRecord outRecord, Ray rayIn) {
		
		Point3 e = new Point3(rayIn.origin);
		Vector3 d = new Vector3(rayIn.direction);
		Point3 p = new Point3(e.x-center.x, e.y-center.y, e.z-center.z);
		Point3 c = new Point3(center);
		double h = height;
		double r = radius;
		
		double s = Math.pow(radius, 2)/Math.pow(height, 2);
		
		double A = Math.pow(d.x, 2) + Math.pow(d.y, 2) - (s*Math.pow(d.z, 2));
		double B = 2 * (p.x*d.x + p.y*d.y - s*(p.z-h)*d.z);
		double C = Math.pow(p.x, 2) + Math.pow(p.y, 2) - (s*Math.pow(p.z-h,2));
		

		double discriminant = (Math.pow(B, 2) - (4 * A * C));
		if (discriminant < 0.0) {
			return false;
		} else {
			double t0 = ((-B) + Math.sqrt(discriminant)) / (2 * A);
			double t1 = ((-B) - Math.sqrt(discriminant)) / 	(2 * A);
			double t = 0.0;

			double tc0 = ((c.z+(h/2))-p.z)/d.z;
			double tc1 = ((c.z+(h/2))-p.z)/d.z;

			Point3 q0 = new Point3();
			Point3 q1 = new Point3();
			Point3 qc0 = new Point3();
			Point3 qc1 = new Point3();
			
			rayIn.evaluate(q0, t0);
			rayIn.evaluate(q1, t1);
			rayIn.evaluate(qc0, tc0);
			rayIn.evaluate(qc1, tc1);
			
			boolean cond0 = (q0.z >= c.z - h/2 && q0.z <= c.z + h/2);
			boolean cond1 = (q1.z >= c.z - h/2 && q1.z <= c.z + h/2);
			boolean a0 = Math.pow(qc0.x+c.x, 2)+Math.pow(qc0.y+c.y, 2)<=Math.pow(radius, 2);
			boolean a1 = Math.pow(qc1.x-c.x, 2)+Math.pow(qc1.y-c.y, 2)<=Math.pow(radius, 2);

			if (cond0 && cond1) {
				if (t0 < t1) {
					t = t0;
				} else {
					t = t1;
				}
			} else if (a0&&tc0<t0&&tc0<t1) {
				t=tc0;
			} else if (a1&&tc1<t0&&tc1<t1) {
				t=tc1;
			} else if (cond0 && !cond1) {
				t = t0;
			} else if (!cond0 && cond1) {
				t=t1;
			} else {
				return false;
			}
			Point3 q = new Point3();
			rayIn.evaluate(q, t);
			q.x -= c.x;
			q.y -= c.y;
			q.z = (r/h);
			outRecord.location.set(q);
			outRecord.surface = this;
			outRecord.t = t;
			if(a0) {
				outRecord.normal.set(new Vector3(0,0,-1));
			} else if(a1) {
				outRecord.normal.set(new Vector3(0,0,1));				
			} else {
				outRecord.normal.set(q);
				outRecord.normal.scale(1);
			}
			outRecord.normal.normalize();

			return true;
		}
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "Cone " + center + " "+ tipz + " "+ height + " "+ shader + " end";
	}
}
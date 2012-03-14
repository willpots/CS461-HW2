package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;

import ray.math.Vector3;
// TEST2/four-spheres.xml TEST2/green-cone-and-sphere.xml TEST2/one-blue-cylinder.xml TEST2/one-shiny-cylinder.xml TEST2/snow-man.xml TEST2/solids.xml TEST2/two-boxes.xml TEST2/wire-box-3.xml TEST2/pencil.xml
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
		
		Point3 e = new Point3(rayIn.origin);
		Vector3 d = new Vector3(rayIn.direction);
		// C = center;
		Point3 c = new Point3(center);
		// P = E-C
		Point3 p = new Point3(e.x-c.x, e.y-c.y, e.z-c.z);
		// Distance from point of cone to center point along z axis
		double h = tipz-c.z;
//		double h0 = tipz - (c.z+(height/2));
//		double h1 = tipz - (c.z-(height/2));
//		double r0 = radius * h0 / height;
//		double r1 = radius * h1 / height;
		
		// Calculate the components of the quadratic equation
		double s = Math.pow(radius, 2)/Math.pow(h, 2);
		
		double A = Math.pow(d.x, 2) + Math.pow(d.y, 2) - (s*Math.pow(d.z, 2));
		double B = 2 * (p.x*d.x + p.y*d.y - s*(p.z-h)*d.z);
		double C = Math.pow(p.x, 2) + Math.pow(p.y, 2) - (s*Math.pow(p.z-h,2));
		double discriminant = (Math.pow(B, 2) - (4 * A * C));
		
		
		if (discriminant < 0.0) {
			return false;
		} else {
			double t = 0.0;
			double tStart = rayIn.start;
			double tEnd = rayIn.end;

			// Calculate T Values
			double tw0 = ((-B)+Math.sqrt(discriminant))/(2*A);
			double tw1 = ((-B)-Math.sqrt(discriminant))/(2*A);
			double tc0 = ((c.z+(height/2))-e.z)/d.z;
			double tc1 = ((c.z-(height/2))-e.z)/d.z;

			// Calculate four intersection points.
			Point3 qw0 = new Point3();
			Point3 qw1 = new Point3();
			Point3 qc0 = new Point3();
			Point3 qc1 = new Point3();
			rayIn.evaluate(qw0, tw0);
			rayIn.evaluate(qw1, tw1);
			rayIn.evaluate(qc0, tc0);
			rayIn.evaluate(qc1, tc1);
			
			// Calculate booleans to see if t values are valid or not.
			boolean tw0Valid = (qw0.z >= c.z-(height/2) && qw0.z <= c.z+(height/2)) && (tw0<tEnd) && (tw0>tStart);
			boolean tw1Valid = (qw1.z >= c.z-(height/2) && qw1.z <= c.z+(height/2)) && (tw1<tEnd) && (tw1>tStart);
			boolean tc0Valid = Math.pow(qc0.x+c.x, 2)+Math.pow(qc0.y+c.y, 2)<=Math.pow(radius, 2) && (tc0<tEnd) && (tc0>tStart);
			boolean tc1Valid = Math.pow(qc1.x-c.x, 2)+Math.pow(qc1.y-c.y, 2)<=Math.pow(radius, 2) && (tc1<tEnd) && (tc1>tStart);
			
			// If any T value is deemed invalid, set to positive infinity.
			if(!tw0Valid) tw0 = Double.POSITIVE_INFINITY;
			if(!tw1Valid) tw1 = Double.POSITIVE_INFINITY;
			if(!tc0Valid) tc0 = Double.POSITIVE_INFINITY;
			if(!tc1Valid) tc1 = Double.POSITIVE_INFINITY;
			
			// Calculate the minimum T Value
			double tw = Math.min(tw0, tw1);
			double tc = Math.min(tc0, tc1);
			Point3 q = new Point3();
			boolean isCap = false;
			
			// Check to see which T value represents the first intersection.
			if(tw<tc&&tw<Double.POSITIVE_INFINITY) { // Ray hit Wall
				t = tw;
				isCap=false;
			} else if(tc<Double.POSITIVE_INFINITY) { // Ray hit Cap
				t = tc;
				isCap=true;
			} else {
				return false;
			}
			rayIn.evaluate(q,t);
			outRecord.surface = this;
			outRecord.t = t;
			outRecord.location.set(q);
			
			double rad = (-radius/h) * (q.z - h);

			q.x -= c.x;
			q.y -= c.y;
			q.z = (rad/h);

			if(isCap&&q.z>c.z) {
				outRecord.normal.set(new Vector3(0,0,-1));
			} else if(isCap) {
				outRecord.normal.set(new Vector3(0,0,1));				
			} else {
				outRecord.normal.set(q);
				outRecord.normal.scale(-1);
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
package ray.surface;

import ray.IntersectionRecord;
import ray.Ray;
import ray.math.Point3;
import ray.math.Vector3;

public class Box extends Surface {
	
	/* The corner of the box with the smallest x, y, and z components. */
	protected final Point3 minPt = new Point3();
	public void setMinPt(Point3 minPt) { this.minPt.set(minPt); }
	
	/* The corner of the box with the largest x, y, and z components. */
	protected final Point3 maxPt = new Point3();
	public void setMaxPt(Point3 maxPt) { this.maxPt.set(maxPt); }
	
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
		// We implemented the three-slab intersection test
		
		// Our notes on box intersection were thin compared to
		// those on sphere intersection, so we consulted this URL
		// to help: http://courses.csusm.edu/cs697exz/ray_box.htm
		
		double tNear = Double.NEGATIVE_INFINITY;
		double tFar = Double.POSITIVE_INFINITY;

		double x=0,y=0,z=0;
		// Calculate all of the T Values.
		double t1 = (minPt.x - rayIn.origin.x) / rayIn.direction.x;
		double t2 = (maxPt.x - rayIn.origin.x) / rayIn.direction.x;
		double t3 = (minPt.y - rayIn.origin.y) / rayIn.direction.y;
	    double t4 = (maxPt.y - rayIn.origin.y) / rayIn.direction.y;
	    double t5 = (minPt.z - rayIn.origin.z) / rayIn.direction.z;
	    double t6 = (maxPt.z - rayIn.origin.z) / rayIn.direction.z;
		//For the pair of X planes
		if (rayIn.direction.x == 0) {
			if (rayIn.origin.x < minPt.x || rayIn.origin.x > maxPt.x) {
				System.out.println("Y RAY MISSED");
				return false;
			}
		} else {	
			// X component of the normal vector is 1 by default.
			x=1;
		    if (t1 > t2) {
		    	// If it enters on the other side however, switch x to -1 and switch
		    	// t1 and t2
		    	double tmp1 = t1;
		    	t1 = t2;
		    	t2 = tmp1;
		    	x=-1;
		    }
		    if (t1 > tNear)
		    	tNear = t1;
		    if (t2 < tFar)
		    	tFar = t2;
		    if (tNear > tFar)
		    	return false;
		    if (tFar < 0)
		    	return false;
		}
		
		//For the pair of Y planes
		if (rayIn.direction.y == 0) {
			if (rayIn.origin.y < minPt.y || rayIn.origin.y > maxPt.y) {
				System.out.println("Y RAY MISSED");
				return false;
			}
		} else {
			// Y component of the normal vector is 1 by default.	
			y=1;
	    	// If it enters on the other side however, switch z to -1 and switch
	    	// t3 and t4
		    if (t3 > t4) {
		    	double tmp2 = t3;
		    	t3 = t4;
		    	t4 = tmp2;
		    	y=-1;
		    }
		    if (t3 > tNear)
		    	tNear = t3;
		    if (t4 < tFar)
		    	tFar = t4;
		    if (tNear > tFar)
		    	return false;
		    if (tFar < 0)
		    	return false;
		}    
		
		//For the pair of Z planes
		if (rayIn.direction.z == 0) {
			if (rayIn.origin.z < minPt.z || rayIn.origin.z > maxPt.z) {
				System.out.println("z RAY MISSED");
				return false;
			}
		} else {
			// Z component of the normal vector is 1 by default.	
			z=1;
	    	// If it enters on the other side however, switch z to -1 and switch
	    	// t5 and t6		    
			if (t5 > t6) {
		    	double tmp3 = t5;
		    	t5 = t6;
		    	t6 = tmp3;
		    	z=-1;
		    }
		    if (t5 > tNear)
		    	tNear = t5;
		    if (t6 < tFar)
		    	tFar = t6;
		    if (tNear > tFar)
		    	return false;
		    if (tFar < 0)
		    	return false;
		}
		// Set the location point.
		rayIn.evaluate(outRecord.location, tNear);
		rayIn.start=0;
		rayIn.end=tNear;
		outRecord.surface=this;
		outRecord.t=tNear;
		// Set the Normal Vector.
		if(tNear==t1) {
			outRecord.normal.set(new Vector3(x,0,0));
		} else if (tNear==t3) {
			outRecord.normal.set(new Vector3(0,y,0));
		} else if (tNear==t5) {
			outRecord.normal.set(new Vector3(0,0,z));
		}
		outRecord.normal.normalize();

		return true;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "Box " + minPt + " " + maxPt + " " + shader + " end";
	}
}


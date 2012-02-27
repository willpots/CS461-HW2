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
		// TODO: fill in this function.
		// You will need to implement the three-slab intersection test
		
		double tNear = Double.NEGATIVE_INFINITY;
		double tFar = Double.POSITIVE_INFINITY;
		
		//Ray: R0 = (x0, y0, z0) , Rd = (xd, yd, zd) ray equation is R0 + Rdt 
		
		//minPt = (xl, yl, zl)
		//maxPt = (xh, yh, zh)
		
		//For the pair of X planes
		if (rayIn.direction.x == 0)
			if (rayIn.origin.x < minPt.x || rayIn.origin.x > maxPt.x)
				return false;
		else {
		    double t1 = (minPt.x - rayIn.origin.x) / rayIn.direction.x;
		    double t2 = (maxPt.x - rayIn.origin.x) / rayIn.direction.x;
		    
		    if (t1 > t2) {
		    	double tmp = 0.0;
		    	tmp = t1;
		    	t1 = t2;
		    	t2 = tmp;
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
		if (rayIn.direction.y == 0)
			if (rayIn.origin.y < minPt.y || rayIn.origin.y > maxPt.y)
				return false;
		else {
		    double t1 = (minPt.y - rayIn.origin.y) / rayIn.direction.y;
		    double t2 = (maxPt.y - rayIn.origin.y) / rayIn.direction.y;
		    
		    if (t1 > t2) {
		    	double tmp = 0.0;
		    	tmp = t1;
		    	t1 = t2;
		    	t2 = tmp;
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
		
		//For the pair of Z planes
		if (rayIn.direction.z == 0)
			if (rayIn.origin.z < minPt.z || rayIn.origin.z > maxPt.z)
				return false;
		else {
		    double t1 = (minPt.z - rayIn.origin.z) / rayIn.direction.z;
		    double t2 = (maxPt.z - rayIn.origin.z) / rayIn.direction.z;
		    
		    if (t1 > t2) {
		    	double tmp = 0.0;
		    	tmp = t1;
		    	t1 = t2;
		    	t2 = tmp;
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
		        
		return true;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "Box " + minPt + " " + maxPt + " " + shader + " end";
	}
}


package tspi.model;
import rotation.Angle;
//import rotation.Principle;
import rotation.Vector3;

public class Target {
	long time;
	Ellipsoid local;
	T_EFG_NED wgs84;
	Vector3 geo;
	Solution solution;
	
	public Target(long time, double lat, double lon, double h) {
		this.time = time;
		this.wgs84 = new T_EFG_NED();
		this.local = new Ellipsoid(Angle.inDegrees(lat), Angle.inDegrees(lon), h);
		this.wgs84.set(local);
		
		this.geo = wgs84.getGeocentric();
		this.solution = null;
	}

	public T_EFG_NED getEllipsoidalCoordinates() { return this.wgs84; }
	public Vector3 getGeocentricCoordinates() { return this.geo; }
	public long getTime() { return this.time; } 
	public Angle getLatitude() { return local.getNorthLatitude(); }
	public Angle getLongitude() { return local.getEastLongitude().signedPrincipleAngle(); }
	public double getHeight() { return this.local.getEllipsoidHeight(); }
	public double getE() { return this.geo.getX(); }
	public double getF() { return this.geo.getY(); }
	public double getG() { return this.geo.getZ(); }
	
	public Double getError() {
		if(solution==null) return null;
		else return solution.error;
	}
	public Double getConditionNumber() {
		if(solution==null) return null;
		else return solution.condition;
	}

	public void setTime(long time) { this.time = time; }

	public void setLatitude(double lat) {
		if(wgs84.equals(null)){
			wgs84 = new T_EFG_NED();
		}
		this.local.setNorthLatitude(Angle.inDegrees(lat));
		wgs84.set(local);
		this.geo = wgs84.getGeocentric();
	}

	public void setLongitude(double lon) {
		if(wgs84.equals(null)){
			wgs84 = new T_EFG_NED();
		}
		this.local.setEastLongitude(Angle.inDegrees(lon).signedPrincipleAngle());
		this.wgs84.set(local);
		this.geo = wgs84.getGeocentric();
	}

	public void setHeight(double h) {
		if(wgs84.equals(null)){
			wgs84 = new T_EFG_NED();
		}
		this.local.setEllipsoidHeight(h);
		this.wgs84.set(local); 
		this.geo = wgs84.getGeocentric();
	}

	public void setE(double E) {
		this.geo.put(E, geo.getY(), geo.getZ());
		if(wgs84.equals(null)){
			wgs84 = new T_EFG_NED();
		}
		this.wgs84.set( geo );
	}

	public void setF(double F) {
		this.geo.put(geo.getX(), F, geo.getZ());
		if(wgs84.equals(null)){
			wgs84 = new T_EFG_NED();
		}
		this.wgs84.set( geo );
	}

	public void setG(double G) {
		this.geo.put(geo.getX(), geo.getY(), G);
		if(wgs84.equals(null)){
			wgs84 = new T_EFG_NED();
		}
		this.wgs84.set( geo );
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public String toString() { 
		return this.time 
				+ "(" + this.getLatitude().toDegrees(Angle.DIGITS) + ", " + this.getLongitude().signedPrincipleAngle().toDegrees(Angle.DIGITS)
				+ ", " + this.getHeight() + ")";
	}
}



package tspi.model;
import rotation.Angle;
//import rotation.Principle;
import rotation.Vector3;

public class Target {
	long time;
	GeodeticLocation wgs84;
	Vector3 geo;
	Solution solution;
	
	public Target(long time, double lat, double lon, double h) {
		this.time = time;
		this.wgs84 = new GeodeticLocation(Double.NaN);
		this.wgs84.set(Angle.inDegrees(lat),Angle.inDegrees(lon),h);
		this.geo = wgs84.geocentric();
		this.solution = null;
	}

	public GeodeticLocation getEllipsoidalCoordinates() { return this.wgs84; }
	public Vector3 getGeocentricCoordinates() { return this.geo; }

	public long getTime() { return this.time; } 
	public double getLatitude() { return wgs84.getNorthLatitude().getDegrees(); }
	public double getLongitude() { return wgs84.getEastLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84.getEllipsoidHeight(); }
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
			wgs84 = new GeodeticLocation(0);
		}
		this.wgs84.setNorthLatitude( Angle.inDegrees(lat) );
		this.geo = wgs84.geocentric();
	}

	public void setLongitude(double lon) {
		if(wgs84.equals(null)){
			wgs84 = new GeodeticLocation(0);
		}
		this.wgs84.setEastLongitude( Angle.inDegrees(lon));
		this.geo = wgs84.geocentric();
	}

	public void setHeight(double h) {
		if(wgs84.equals(null)){
			wgs84 = new GeodeticLocation(0);
		}
		this.wgs84.setEllipsoidHeight(h);
		this.geo = wgs84.geocentric();
	}

	public void setE(double E) {
		this.geo.put(E, geo.getY(), geo.getZ());
		if(wgs84.equals(null)){
			wgs84 = new GeodeticLocation(0);
		}
		this.wgs84.set( geo );
	}

	public void setF(double F) {
		this.geo.put(geo.getX(), F, geo.getZ());
		if(wgs84.equals(null)){
			wgs84 = new GeodeticLocation(0);
		}
		this.wgs84.set( geo );
	}

	public void setG(double G) {
		this.geo.put(geo.getX(), geo.getY(), G);
		if(wgs84.equals(null)){
			wgs84 = new GeodeticLocation(0);
		}
		this.wgs84.set( geo );
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public String toString() { 
		return this.time 
				+ "("+this.getLatitude()+", "+this.getLongitude()+", "+this.getHeight()+")";
	}
}



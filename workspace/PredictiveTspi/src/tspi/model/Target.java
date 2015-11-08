package tspi.model;
import rotation.Angle;
//import rotation.Principle;
import rotation.Vector3;

public class Target {
	long time;
	WGS84 wgs84;
	Vector3 geo;

	Double error; // magnitude of error when deriving the target from sensor 
	// TODO will eventually be multiple values, from multiple startegies, so this will need to be revisited.

	public Target(long time, double lat, double lon, double h) {
		this.time = time;
		this.wgs84 = new WGS84(Angle.inDegrees(lat),Angle.inDegrees(lon),h);
//		this.wgs84.setLatitude( new Principle( Angle.inDegrees(lat) ) );
//		this.wgs84.setLongitude( new Principle( Angle.inDegrees(lon) ) );
//		this.wgs84.setHeight( h );
		this.geo = wgs84.getXYZ();
		this.error = null;
	}

	public WGS84 getEllipsoidalCoordinates() { return this.wgs84; }
	public Vector3 getGeocentricCoordinates() { return this.geo; }

	public long getTime() { return this.time; } 
	public double getLatitude() { return wgs84.getAngleLatitude().getDegrees(); }
	public double getLongitude() { return wgs84.getAngleLongitude().getDegrees(); }
	public double getHeight() { return this.wgs84.getHeight(); }
	public double getE() { return this.geo.getX(); }
	public double getF() { return this.geo.getY(); }
	public double getG() { return this.geo.getZ(); }
	public Double getError() { return this.error; }

	public void setTime(long time) { this.time = time; }

	public void setLatitude(double lat) {
		if(wgs84.equals(null)){
			wgs84 = new WGS84(Angle.inDegrees(0),Angle.inDegrees(0),0);
		}
		this.wgs84.putLatitude( Angle.inDegrees(lat) );
		this.geo = wgs84.getXYZ();
	}

	public void setLongitude(double lon) {
		if(wgs84.equals(null)){
			wgs84 = new WGS84(Angle.inDegrees(0),Angle.inDegrees(0),0);
		}
		this.wgs84.putLongitude( Angle.inDegrees(lon));
		this.geo = wgs84.getXYZ();
	}

	public void setHeight(double h) {
		if(wgs84.equals(null)){
			wgs84 = new WGS84(Angle.inDegrees(0),Angle.inDegrees(0),0);
		}
		this.wgs84.putHeight(h);
		this.geo = wgs84.getXYZ();
	}

	public void setE(double E) {
		this.geo.put(E, geo.getY(), geo.getZ());
		if(wgs84.equals(null)){
			wgs84 = new WGS84(Angle.inDegrees(0),Angle.inDegrees(0),0);
		}
		this.wgs84.putXYZ( geo );
	}

	public void setF(double F) {
		this.geo.put(geo.getX(), F, geo.getZ());
		if(wgs84.equals(null)){
			wgs84 = new WGS84(Angle.inDegrees(0),Angle.inDegrees(0),0);
		}
		this.wgs84.putXYZ( geo );
	}

	public void setG(double G) {
		this.geo.put(geo.getX(), geo.getY(), G);
		if(wgs84.equals(null)){
			wgs84 = new WGS84(Angle.inDegrees(0),Angle.inDegrees(0),0);
		}
		this.wgs84.putXYZ( geo );
	}

	public void setError(double error) {
		this.error = new Double(error);
	}

	public String toString() { 
		return this.time 
				+ "("+this.getLatitude()+", "+this.getLongitude()+", "+this.getHeight()+")";
	}
}



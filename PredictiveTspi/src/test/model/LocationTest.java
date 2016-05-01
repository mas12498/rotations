/**
 * 
 */
package test.model;

import junit.framework.TestCase;
import rotation.Angle;
//import rotation.BasisUnit;
import rotation.Rotator;
import rotation.Vector3;
import tspi.model.Ellipsoid;
//import rotation.Principle;
import tspi.model.EFG_NED;

/**
 * @author mike
 *
 */
public class LocationTest extends TestCase {

	public final void testWGS84WGS84() {
		int cnt = 0;
		Rotator nav2geoOp = new Rotator(Rotator.EMPTY);
		Ellipsoid tmp2 = new Ellipsoid();
		Ellipsoid tmp = new Ellipsoid();

		Double qHeight = Double.NaN;
		Rotator qloc = new Rotator();
		Vector3 geoloc = new Vector3(Vector3.NAN);
		EFG_NED loc = new EFG_NED();
		EFG_NED loc2 = new EFG_NED();
		double qlat;
		double qlon;
		for (int i = -3; i <= 3; i++) {
            //int i = 1; {//lats
			double phi = i * 30.0d; // + 0.001d; //latitude pole to pole
			for (int j = 0; j <= 12; j++) {
				//int j=2; {//lon
				double lambda = j * 30.0d; // - 0.01d; //longitude 360
				//for (int h = -1; h <= 2; h++) {
					int h=2; {
					double hgt = h * 1000.0d; //height above and below ellipsoid
					
					tmp2.setNorthLatitude(Angle.inDegrees(phi));
					tmp2.setEastLongitude(Angle.inDegrees(lambda));
					tmp2.setEllipsoidHeight(hgt);
					loc2.set(tmp2);// = new Geodetic(tmp2);
					nav2geoOp.set(loc2.getLocalHorizontal());
					qloc = loc2.getLocalHorizontal();
                    qHeight = loc2.getLocalVertical();
					tmp = loc2.getEllipsoid();
					
					//System.out.print(String.format(" q = " + nav2geoOp.toString(15))); //tmp.axialOperator_NG().toString(15)));
					System.out.print(String.format("max = %5f; ",qloc.getNormInf()));
					System.out.print(String.format("abs = %5f; ",qloc.getAbs()));
					System.out.print(String.format(" q = " + qloc.toString(15))); //tmp.axialOperator_NG().toString(15)));
					//System.out.print(String.format(" phi = %8f" , phi)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
					//System.out.print(String.format(" lambda = %8f" , lambda)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
					System.out.print(String.format(" latitude = %8f" , tmp.getNorthLatitude().getDegrees()));
					System.out.print(String.format(" longitude = %.8f", tmp.getEastLongitude().getDegrees() ));
						qlat = tmp.getNorthLatitude().getDegrees();						
						qlon = tmp.getEastLongitude().getDegrees();												
					System.out.print(String.format(" Qlat = %.8f", qlat ));
					System.out.print(String.format(" Qlon = %.8f", qlon ));
					System.out.println();
					
					geoloc = loc.getGeocentric();
					//System.out.println("q = " + qloc.toString(15) + " height = " + qHeight);
						
					cnt = cnt+1;
					
				}
			}
		}
		System.out.println("Success Constructor LLh and getters. Count = "+cnt);
	}


	/**
	 * Test method for {@link tspi.model.EFG_NED#WGS84(rotation.Vector3)}.
	 */
	public final void testWGS84Vector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#WGS84(rotation.Rotator, double)}.
	 */
	public final void testWGS84OperatorDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#WGS84(rotation.Rotator, rotation.Vector3)}.
	 */
	public final void testWGS84OperatorVector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#getLocationXYZ()}.
	 */
	public final void testGetXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#getFromNEDtoEFG()}.
	 */
	public final void testGetFromNEDtoEFG() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#getNorthLatitude()}.
	 */
	public final void testGetAngleLatitude() {
		fail("Not yet implemented"); // TODO
	}


	/**
	 * Test method for {@link tspi.model.EFG_NED#putXYZ(rotation.Vector3)}.
	 */
	public final void testPutXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#set(rotation.Rotator, double)}.
	 */
	public final void testPut() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#putLatitude(rotation.Principle)}.
	 */
	public final void testPutLatitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#putLatitude(rotation.Angle)}.
	 */
	public final void testPutLatitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#putLongitude(rotation.Principle)}.
	 */
	public final void testPutLongitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#putLongitude(rotation.Angle)}.
	 */
	public final void testPutLongitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.EFG_NED#putHeight(double)}.
	 */
	public final void testPutHeight() {
		fail("Not yet implemented"); // TODO
	}

}

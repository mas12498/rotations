/**
 * 
 */
package test.model;

import junit.framework.TestCase;
import rotation.Angle;
//import rotation.BasisUnit;
import rotation.Operator;
//import rotation.Principle;
import tspi.model.Location;

/**
 * @author mike
 *
 */
public class LocationTest extends TestCase {

	public final void testWGS84WGS84() {
		int cnt = 0;
		Operator nav2geo = new Operator(Operator.NAN);
		Location tmp2 = new Location();
		Location tmp = new Location();
		tmp2.set(Angle.inDegrees(Double.NaN), Angle.inDegrees(Double.NaN),Double.NaN);
		tmp.set(Angle.inDegrees(Double.NaN), Angle.inDegrees(Double.NaN),0.0);
//		Principle plat = tmp2.getNorthLatitude().getPrinciple();
//		Principle plon =  tmp2.getEastLongitude().getPrinciple();
//		Principle ptheta = tmp2.getNorthLatitude().getPrinciple(); //Principle.arcTanHalfAngle(q_NG.getEuler_j_kji().cotHalf());
		double qlat;
		double qlon;
		for (int i = -3; i <= 3; i++) {
            //int i = 0; {//lats
			double phi = i * 30.0d; // + 0.001d; //latitude pole to pole
			for (int j = 0; j <= 12; j++) {
				//int j=6; {//lon
				double lambda = j * 30.0d; // - 0.01d; //longitude 360
				//for (int h = -1; h <= 2; h++) {
					int h=2; {
					double hgt = h * 1000.0d; //height above and below ellipsoid
					
					tmp2.setNorthLatitude(Angle.inDegrees(phi));
					tmp2.setEastLongitude(Angle.inDegrees(lambda));
					tmp2.setEllipsoidHeight(hgt);
					nav2geo.set(tmp2.axialOperator_NG());
//					Location tmp1 = new Location(tmp2.getNorthLatitude(), tmp2.getEastLongitude(), tmp2.getEllipsoidHeight());
//					Location tmp = new Location(tmp1);
					tmp.setEllipsoidHorizontal(nav2geo);
					System.out.print(String.format(" q = " + nav2geo.toString(15))); //tmp.axialOperator_NG().toString(15)));
					System.out.print(String.format(" phi = %8f" , phi)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
					System.out.print(String.format(" lambda = %8f" , lambda)); //tmp.getLatitude().addRight().negate().signedAngle().getDegrees()));
					System.out.print(String.format(" latitude = %8f" , tmp.getNorthLatitude().getDegrees()));
					System.out.print(String.format(" longitude = %.8f", tmp.getEastLongitude().getDegrees() ));
					
					//Operator q_NG = tmp.getOpNavToGeo();//load operator...
					//Operator tst = new Operator(q_NG);
					
//					alon = tst.getEuler_k_kji().signedAngle();					
//					Angle aatheta = tst.getEuler_j_kji().signedAngle().negate();
//						atheta.put(aatheta.add(Angle.QUARTER_REVOLUTION));
						qlat = tmp.getNorthLatitude().getDegrees();						
						qlon = tmp.getEastLongitude().getDegrees();												
//					System.out.print(tst.unit().toString(3));
					System.out.print(String.format(" Qlat = %.8f", qlat ));
					System.out.print(String.format(" Qlon = %.8f", qlon ));

					System.out.println();
///					assertEquals(tmp.getLatitude().signedAngle().getDegrees(), phi, 1e-14);
///					assertEquals(tmp.getEastLongitude().getDegrees(), lambda  % 360.0, 1e-13);
///					assertEquals(tmp.getNorthLatitude().getDegrees(), phi, 1e-14);
///					assertEquals(tmp.getEastLongitude().getDegrees(), lambda % 360.0, 1e-13);
// // // //					assertEquals(tmp.getTheta().signedAngle().getDegrees(),theta, 1e-13 );

//					assertTrue(q_NG.getEuler_k_kji().isEqualTo(tmp.getLongitude(),Principle.arcTanHalfAngle(1e-10)));
//					assertTrue(q_NG.getEuler_j_kji().isEqualTo(tmp.getTheta(),Principle.arcTanHalfAngle(1e-10)));
//					assertEquals(tmp.getEllipsoidHeight(), hgt, 1e-14);
						
					cnt = cnt+1;
					
				}
			}
		}
		System.out.println("Success Constructor LLh and getters. Count = "+cnt);
	}


	/**
	 * Test method for {@link tspi.model.Location#WGS84(rotation.Vector3)}.
	 */
	public final void testWGS84Vector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#WGS84(rotation.Operator, double)}.
	 */
	public final void testWGS84OperatorDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#WGS84(rotation.Operator, rotation.Vector3)}.
	 */
	public final void testWGS84OperatorVector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#getLocationXYZ()}.
	 */
	public final void testGetXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#getFromNEDtoEFG()}.
	 */
	public final void testGetFromNEDtoEFG() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#getNorthLatitude()}.
	 */
	public final void testGetAngleLatitude() {
		fail("Not yet implemented"); // TODO
	}


	/**
	 * Test method for {@link tspi.model.Location#putXYZ(rotation.Vector3)}.
	 */
	public final void testPutXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#set(rotation.Operator, double)}.
	 */
	public final void testPut() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLatitude(rotation.Principle)}.
	 */
	public final void testPutLatitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLatitude(rotation.Angle)}.
	 */
	public final void testPutLatitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLongitude(rotation.Principle)}.
	 */
	public final void testPutLongitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putLongitude(rotation.Angle)}.
	 */
	public final void testPutLongitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.Location#putHeight(double)}.
	 */
	public final void testPutHeight() {
		fail("Not yet implemented"); // TODO
	}

}

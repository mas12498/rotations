/**
 * 
 */
package tspi.model;

import junit.framework.TestCase;
import rotation.Angle;
import rotation.Operator;
import rotation.Principle;

/**
 * @author mike
 *
 */
public class TestWGS84 extends TestCase {

	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Principle, rotation.Principle, double)}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Angle, rotation.Angle, double)}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(tspi.model.WGS84)}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getLatitude()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getTheta()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getAngleLongitude()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getLongitude()}.
	 */
	/**
	 * Test method for {@link tspi.model.WGS84#getHeight()}.
	 */
	public final void testWGS84WGS84() {
		System.out.println("Begin test of Constructor LLh and gets:");
		System.out.println(" Test method for {@link tspi.model.WGS84#WGS84(rotation.Angle, rotation.Angle, double)}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#WGS84(rotation.Principle, rotation.Principle, double)}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#WGS84(tspi.model.WGS84)}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getLatitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getAngleLatitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getAngleLongitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getLongitude()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getTheta()}. ");
		System.out.println(" Test method for {@link tspi.model.WGS84#getHeight()}. ");
		int cnt = 0;
		for (int i = -2; i <= 2; i++) {
			double phi = i * 30.0d;
			double theta = -phi-90.0;
			for (int j = 0; j <= 12; j++) {
				double lambda = j * 30.0d;
				for (int h = -1; h <= 2; h++) {
					double hgt = h * 1000.0d;
					WGS84 tmp2 = new WGS84(Angle.inDegrees(phi), Angle.inDegrees(lambda), hgt);
					WGS84 tmp1 = new WGS84(tmp2.getLatitude(), tmp2.getLongitude(), tmp2.getHeight());
					WGS84 tmp = new WGS84(tmp1);
					Operator q_NG = tmp.getFromNEDtoEFG();
					System.out.print(String.format(" latitude = %8f" , tmp.getLatitude().signedAngle().getDegrees()));
					System.out.print(String.format(" theta = %8f" , tmp.getTheta().signedAngle().getDegrees()));
					System.out.print(String.format(" longitude = %.8f", tmp.getLongitude().unsignedAngle().getDegrees() ));
					double qlon = q_NG.getEuler_k_kji().unsignedAngle().getDegrees();
					if(theta < -90) {
						qlon+=180.0;
						qlon %= 360.0;
					}
					System.out.print(String.format(" Qlon = %.8f", qlon ));
					// System.out.print(" Theta = "+tmp.getTheta().signedAngle().getDegrees());
					// System.out.print("test Theta == "+theta);
					// System.out.print(" height = "+ tmp.getHeight());
					// System.out.print(" Q_NG = " + tmp.getFromNEDtoEFG().unit().toString(10));
					System.out.println();
					assertEquals(tmp.getLatitude().signedAngle().getDegrees(), phi, 1e-14);
					assertEquals(tmp.getLongitude().unsignedAngle().getDegrees(), lambda  % 360.0, 1e-13);
					assertEquals(tmp.getAngleLatitude().getDegrees(), phi, 1e-14);
					assertEquals(tmp.getAngleLongitude().getDegrees(), lambda % 360.0, 1e-13);
					assertEquals(tmp.getTheta().signedAngle().getDegrees(),theta, 1e-13 );

//					assertTrue(q_NG.getEuler_k_kji().isEqualTo(tmp.getLongitude(),Principle.arcTanHalfAngle(1e-10)));
//					assertTrue(q_NG.getEuler_j_kji().isEqualTo(tmp.getTheta(),Principle.arcTanHalfAngle(1e-10)));
						assertEquals(tmp.getHeight(), hgt, 1e-14);
					cnt = cnt+1;
					//System.out.println("Success Constructor LLh. Count = "+cnt);

				}
			}
		}
		System.out.println("Success Constructor LLh and getters. Count = "+cnt);
	}


	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Vector3)}.
	 */
	public final void testWGS84Vector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Operator, double)}.
	 */
	public final void testWGS84OperatorDouble() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#WGS84(rotation.Operator, rotation.Vector3)}.
	 */
	public final void testWGS84OperatorVector3() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getXYZ()}.
	 */
	public final void testGetXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getFromNEDtoEFG()}.
	 */
	public final void testGetFromNEDtoEFG() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getAngleLatitude()}.
	 */
	public final void testGetAngleLatitude() {
		fail("Not yet implemented"); // TODO
	}


	/**
	 * Test method for {@link tspi.model.WGS84#putXYZ(rotation.Vector3)}.
	 */
	public final void testPutXYZ() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#put(rotation.Operator, double)}.
	 */
	public final void testPut() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLatitude(rotation.Principle)}.
	 */
	public final void testPutLatitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLatitude(rotation.Angle)}.
	 */
	public final void testPutLatitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLongitude(rotation.Principle)}.
	 */
	public final void testPutLongitudePrinciple() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putLongitude(rotation.Angle)}.
	 */
	public final void testPutLongitudeAngle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#putHeight(double)}.
	 */
	public final void testPutHeight() {
		fail("Not yet implemented"); // TODO
	}

}

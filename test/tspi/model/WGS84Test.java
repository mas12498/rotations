package tspi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import junit.framework.TestCase;
import tspi.rotation.Angle;
import tspi.rotation.Vector3;




public class WGS84Test extends TestCase{

//	@Test
//	void test() {
////		fail("Not yet implemented");
	
	/**
	 * Test method for
	 * {@link tspi.model.WGS84#WGS84(rotation.Angle, rotation.Angle, double)}.
	 */
	@Test
	public final  void testWGS84AngleAngleDouble() {
		Angle lat = Angle.inDegrees(30);
		Angle lon = Angle.inDegrees(-120);
		double h = 2000;
		WGS84 p = new WGS84();
		p.set(lat, lon, h);
		WGS84 q = new WGS84();
		WGS84 t = new WGS84(p);
//		 System.out.println("Test lat: "+lat.toDegreesString(8));
//		 System.out.println("Test getNorthLatitude: "+p.getNorthLatitude().toDegreesString(8));
//		 System.out.println("Test: "+lat.toDegreesString(8));
		 
		 assertEquals(p.getNorthLatitude().getPiRadians(), lat.getPiRadians(), 1e-10);
		 assertEquals(p.getEastLongitude().signedPrinciple().getPiRadians(), lon.getPiRadians(), 1e-10);
		//assertTrue(p.getNorthLatitude().equals(lat));
		//assertTrue(p.getEastLongitude().equals(lon));
		
		assertEquals(p.getEllipsoidHeight(), 2000, 0);
		t.set(q);
		assertTrue(t.getNorthLatitude().equals(Angle.EMPTY));
		assertTrue(t.getEastLongitude().equals(Angle.EMPTY));
		assertTrue(Double.isNaN(t.getEllipsoidHeight()));
		t.set(p.getNorthLatitude(), p.getEastLongitude(), p.getEllipsoidHeight());
		
		 assertEquals(t.getNorthLatitude().getPiRadians(), lat.getPiRadians(), 1e-10);
		 assertEquals(t.getEastLongitude().unsignedPrinciple().getPiRadians(), lon.unsignedPrinciple().getPiRadians(), 1e-10);
//		assertTrue(t.getNorthLatitude().equals(lat));
//		assertTrue(t.getEastLongitude().equals(lon));
		
		assertEquals(t.getEllipsoidHeight(), 2000, 0);
		q.set(p);
		
		 assertEquals(q.getNorthLatitude().getPiRadians(), lat.getPiRadians(), 1e-10);
		 assertEquals(q.getEastLongitude().signedPrinciple().getPiRadians(), lon.getPiRadians(), 1e-10);
//		assertTrue(q.getNorthLatitude().equals(lat));
//		assertTrue(q.getEastLongitude().equals(lon));
		
		assertEquals(q.getEllipsoidHeight(), 2000, 0);
		q.setNorthLatitude(Angle.inDegrees(40));
		q.setEastLongitude(Angle.inDegrees(240));
		q.setEllipsoidHeight(1000);

		 assertEquals(q.getNorthLatitude().getPiRadians(), Angle.inDegrees(40).getPiRadians(), 1e-10);
		 assertEquals(q.getEastLongitude().unsignedPrinciple().getPiRadians(), Angle.inDegrees(240).getPiRadians(), 1e-10);
//		assertTrue(q.getNorthLatitude().equals(Angle.inDegrees(40)));
//		assertTrue(q.getEastLongitude().equals(Angle.inDegrees(240)));
		
		assertEquals(q.getEllipsoidHeight(), 1000, 0);
		Vector3 x = p.getGeocentric();
		
		q.setGeocentric(x);
		assertEquals(q.getNorthLatitude().signedPrinciple().getDegrees(), p.getNorthLatitude().signedPrinciple().getDegrees(), 1e-13);
		assertEquals(q.getEastLongitude().signedPrinciple().getDegrees(), p.getEastLongitude().signedPrinciple().getDegrees(), 1e-13);
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link tspi.model.WGS84#getGeocentric()}.
	 */
	@Test
	public final void testGeocentric() {

		int cnt = 0;
		WGS84 geodetic = new WGS84();
		WGS84 tgeodetic = new WGS84();

		Double qHeight = Double.NaN;
		
		//Note: efg cannot handle dumping: ambiguous Euler assignments!
		
		Vector3 efg = new Vector3(Vector3.EMPTY);
		Vector3 tefg = new Vector3(Vector3.EMPTY);
		double qlat;
		double qlon;
		double qhgt;
		for (int k = -1; k <= 1; k++) {
			double fudge = k * 0.00d;
			for (int i = -3; i <= 3; i++) { // latitudes [-90..90]
				double phi = i * 30.0d; // + 0.001d;
				for (int j = 0; j <= 12; j++) { // longitudes [-360..360]
					double lambda = j * 30.0d + fudge; // - 0.01d;
					for (int h = -1; h <= 2; h++) { // heights meters above, on, and below WGS84
						double hgt = h * 1000.0d + fudge; // - 0.1d;
//						System.out.println(phi+" "+lambda + " near rev = "
//						 + StrictMath.round(Angle.inDegrees(j*30).getRevolutions()));
						geodetic.set(Angle.inDegrees(phi), Angle.inDegrees(lambda), hgt);
						
						efg.set(geodetic.getGeocentric());
						tgeodetic.setGeocentric(efg);
						
						qlat = tgeodetic.getNorthLatitude().getDegrees();
						assertEquals(qlat, phi, 1e-13);
						qlon = tgeodetic.getEastLongitude().unsignedPrinciple().getDegrees();
						qhgt = tgeodetic.getEllipsoidHeight();
						System.out.print(String.format(" Qlat = %14.10f", qlat));
//						System.out.print(String.format(" phi = %14.10f", phi));
						System.out.print(String.format(" Qlon = %15.10f", qlon));
//						System.out.print(String.format(" lambda = %15.10f", lambda));
//						System.out.print(String.format(" height = %8.3f", hgt));
						System.out.print(String.format(" Qheight = %8.3f", qhgt));
						assertEquals(Angle.inDegrees(qlon).unsignedPrinciple().getDegrees(),
								Angle.inDegrees(lambda).unsignedPrinciple().getDegrees(), 1e-13);
						        assertEquals(qhgt, hgt, 1e-8);

						System.out.println();
						cnt = cnt + 1;

					}
				}
			}
		}
		
		System.out.println(
				"Test -0 < +0: " + (-0d < +0d) + "    Double.compare: -0 < +0: " + (Double.compare(-0d, +0d) < 0d));
		System.out.println("Test -0 == +0: " + (-0d == +0d));

		System.out.println("Success of geo-location structures and earth coordinate transformations. Count = " + cnt);

		// fail("Not yet implemented"); // TODO
	}
	
	}

//}

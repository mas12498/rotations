package rotationTest;

import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;

import org.junit.Test;

import rotation.Angle;
import rotation.Principle;

public class PrincipleTest {

/* Protected tested indirectly in public exercises... */
/*
//	@Test
//	public void testGetRadians() {
//	}
//
//	@Test
//	public void testGetUnsignedRadians() {
//	}
//
//	@Test
//	public void testPrincipleDouble() {
//	}
*/
	
	
	@Test
	public void testGetSetAnglePrinciple() {
// Constructors:
//		public void testPrincipleAngle() {
//		public void testPrinciplePrinciple() {
// Setters:
//		public void testSetAngle() {
//		public void testSetPrinciple() {
// Angle Factories:
//		public void testSignedAngle() {
//		public void testUnsignedAngle() {
		Angle a = Angle.inDegrees(30);
		Angle b = Angle.inDegrees(60);

		Principle p = new Principle(a);
		assertEquals(p.signedAngle().getDegrees(),30,1e-14);

		Principle t = new Principle(p);
		assertEquals(t.signedAngle().getDegrees(),30,1e-14);

		p.set(b); //angle
		assertEquals(p.signedAngle().getDegrees(),60,1e-14);

		p.set(t); //principle angle	
		assertEquals(p.signedAngle().getDegrees(),30,1e-14);

	  //check factory casts back to Angle class
		
		t.set(Angle.inDegrees(-30)); 
		assertEquals(t.signedAngle().getDegrees(),-30,1e-14);
		assertEquals(t.unsignedAngle().getDegrees(),330,1e-13);

		t.set(Angle.inDegrees(-150));
		assertEquals(t.signedAngle().getDegrees(),-150,1e-13);
		assertEquals(t.unsignedAngle().getDegrees(),210,1e-13);

		t.set(Angle.inDegrees(-180));
		assertEquals(t.signedAngle().getDegrees(),-180,1e-13);
		assertEquals(t.unsignedAngle().getDegrees(),180,1e-13);
	
	}


	@Test
	public void testTanHalf() {
		
		Angle a = Angle.inDegrees(90);
		Principle p = new Principle(a);
		//System.out.println(p.tanHalf());
		assertEquals(p.tanHalf(),1.0D,1e-15);
		
		a.setDegrees(180.0D);
		p.set(a);
		//System.out.println(p.tanHalf());
		assertEquals(1d/p.tanHalf(),0,1e-15);
		
		a.setDegrees(-90.0D);
		p.set(a);
		//System.out.println(p.tanHalf());
		assertEquals(p.tanHalf(),-1.0d,1e-15);
		
		a.setDegrees(-180.0D);
		p.set(a);
		//System.out.println(p.tanHalf());
		assertEquals(1d/p.tanHalf(),0,1e-15);
		
		a.setDegrees(0d);
		p.set(a);
		//System.out.println(p.tanHalf());
		assertEquals(p.tanHalf(),0,1e-15);
		
	}

//	@Test
//	public void testSinHalf() {
//		
//////This for Sin!
////		Angle a = Angle.inDegrees(0);
////		Principle p = new Principle(a);	
////		//System.out.println(p.tan());
////		assertEquals(p.sin(),0,1e-15);
////		
////		a.setDegrees(15d);
////		p.set(a);
////		//System.out.println(" Sin of 15 degrees: "+p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(15)),1e-15);
////		
////
////		a.setDegrees(30d);
////		p.set(a);
////		//System.out.println(p.signedAngle().getDegrees());
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),.5d,1e-15);
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(30)),1e-15);
////		
////		a.setDegrees(90d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),1d,1e-15);
////		
////		a.setDegrees(180d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),0d,1e-15);
////			
////		a.setDegrees(-30d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),-.5d,1e-15);
////		
////		a.setDegrees(-90d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),-1d,1e-15);
////		
////		a.setDegrees(-180d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),0d,1e-15);
////		
////		a.setDegrees(150d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),.5d,1e-15);
////		
////		a.setDegrees(210d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),-.5d,1e-15);
////		
////		a.setDegrees(0.1d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(.1)),1e-15);
////
////		a.setDegrees(0.0d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(0)),1e-15);
////		
////		a.setDegrees(-0.1d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(-.1)),1e-15);
////
////		a.setDegrees(90.0001d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(90.0001)),1e-15);
////
////		a.setDegrees(90.0d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(90)),1e-15);
////	
////		a.setDegrees(89.9999d);
////		p.set(a);
////		//System.out.println(p.sin());
////		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(89.9999d)),1e-15);
//
////		fail("Not yet implemented"); // TODO
//	}

//	@Test
//	public void testCosHalf() {
////		fail("Not yet implemented"); // TODO
//	}
//	@Test
//	public void testSecHalf() {
////		fail("Not yet implemented"); // TODO
//	}

	@Test
	public void testTan() {

		Angle a = Angle.inDegrees(0);
		Principle p = new Principle(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),0D,1e-15);
		
		a.setDegrees(45d);
		p.set(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),1,1e-15);
		
		a.setDegrees(90d);
		p.set(a);
		//System.out.println(p.tan());
		//System.out.println("ans: " + (p.tan()) +" ans+eps: " + ((p.tan())+ 0.4999999999999999d));
		assertEquals(1/p.tan(),0d,1e-15);
		
		a.setDegrees(180d);
		p.set(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),0d,1e-15);
			
		a.setDegrees(-45d);
		p.set(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),-1,1e-15);
		
		a.setDegrees(-90d);
		p.set(a);
		//System.out.println(p.tan());
		//System.out.println("ans: " + (p.tan()) +" ans+eps: " + ((p.tan())+ 0.4999999999999999d));
		assertEquals(1/p.tan(),0d,1e-15);
		
		a.setDegrees(-180d);
		p.set(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),0d,1e-15);
		
		a.setDegrees(270d);
		p.set(a);
		//System.out.println(p.tan());
		//System.out.println("ans: " + (p.tan()) +" ans+eps: " + ((p.tan())+ 0.4999999999999999d));
		assertEquals(1/p.tan(),0d,1e-15);
		
		a.setDegrees(-270d);
		p.set(a);
		//System.out.println(p.tan());
		//System.out.println("ans: " + (p.tan()) +" ans+eps: " + ((p.tan())+ 0.4999999999999999d));
		assertEquals(1/p.tan(),0d,1e-15);
		
		a.setDegrees(-179.99d);
		p.set(a);
		//System.out.println("tan(-179.99d) = "+ p.tan());
		//System.out.println("tan(-179.99d) = "+ StrictMath.tan(StrictMath.toRadians(-179.99d)));
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(-179.99d)),1e-15);
		
		a.setDegrees(-180.0d);
		p.set(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(-180.0d)),1e-15);
		
		a.setDegrees(179.99d);
		p.set(a);
		//System.out.println(p.tan());
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(179.99d)),1e-15);
		
	
		a.setDegrees(89.99d);
		p.set(a);
		//System.out.println("my tan(89.99d) = "+ p.tan());
		//System.out.println("tan(89.99d) = "+ StrictMath.tan(StrictMath.toRadians(89.99d)));
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(89.99d)),1e-7);

		a.setDegrees(90.0d);
		p.set(a);
		//System.out.println(p.tan());
		//System.out.println("tan(90.0d) = "+ p.tan());
		////System.out.println("tan(90.0d) = "+ StrictMath.tan(StrictMath.toRadians(90.0d)));
		//System.out.println("1/tan(90.0d) = "+ 1/StrictMath.tan(StrictMath.toRadians(90.0d)));
		assertEquals(1/p.tan(),1/StrictMath.tan(StrictMath.toRadians(90.0d)),1e-15);
		
		
		a.setDegrees(90.01d);
		p.set(a);
		//System.out.println("tan(90.01d) = "+ p.tan()); //mine more accurate!!
		//System.out.println("tan(90.01d) = "+ StrictMath.tan(StrictMath.toRadians(90.01d)));
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(90.01d)),1e-8);
		
		
		a.setDegrees(-90.01d);
		p.set(a);
		//System.out.println("tan(-90.01d) = "+ p.tan());
		//System.out.println("tan(-90.01d) = "+ StrictMath.tan(StrictMath.toRadians(90.0d)));
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(-90.01d)),1e-8);

		a.setDegrees(45.01d);
		p.set(a);
		//System.out.println("tan(45.01d) = "+ p.tan());
		//System.out.println("tan(45.01d) = "+ StrictMath.tan(StrictMath.toRadians(90.0d)));
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(45.01d)),1e-14);
		
		a.setDegrees(44.99d);
		p.set(a);
		//System.out.println(p.tan()+" is pos??");
		assertEquals(p.tan(),StrictMath.tan(StrictMath.toRadians(44.99d)),1e-14);

	}

	@Test
	public void testSin() {
		Angle a = Angle.inDegrees(0);
		Principle p = new Principle(a);
		
		//System.out.println(p.tan());
		assertEquals(p.sin(),0,1e-15);
		
		a.setDegrees(15d);
		p.set(a);
		//System.out.println(" Sin of 15 degrees: "+p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(15)),1e-15);
		

		a.setDegrees(30d);
		p.set(a);
		//System.out.println(p.signedAngle().getDegrees());
		//System.out.println(p.sin());
		assertEquals(p.sin(),.5d,1e-15);
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(30)),1e-15);
		
		a.setDegrees(90d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),1d,1e-15);
		
		a.setDegrees(180d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),0d,1e-15);
			
		a.setDegrees(-30d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),-.5d,1e-15);
		
		a.setDegrees(-90d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),-1d,1e-15);
		
		a.setDegrees(-180d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),0d,1e-15);
		
		a.setDegrees(150d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),.5d,1e-15);
		
		a.setDegrees(210d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),-.5d,1e-15);
		
		a.setDegrees(0.1d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(.1)),1e-15);

		a.setDegrees(0.0d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(0)),1e-15);
		
		a.setDegrees(-0.1d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(-.1)),1e-15);

		a.setDegrees(90.0001d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(90.0001)),1e-15);

		a.setDegrees(90.0d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(90)),1e-15);
	
		a.setDegrees(89.9999d);
		p.set(a);
		//System.out.println(p.sin());
		assertEquals(p.sin(),StrictMath.sin(StrictMath.toRadians(89.9999d)),1e-15);

	}

	@Test
	public void testCos() {
		Angle a = Angle.inDegrees(0);
		Principle p = new Principle(a);
		
		//System.out.println(p.tan());
		
		//System.out.println(p.cos());
		assertEquals(p.cos(),1,1e-15);
		
		a.setDegrees(60d);
		p.set(a);
		//System.out.println(p.cos()+ " Cosine of 60 degrees: "+ StrictMath.cos(StrictMath.toRadians(60)));
		assertEquals(p.cos(),.5d,1e-14);
		
		a.setDegrees(90d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),0d,1e-15);
		
		a.setDegrees(180d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),-1d,1e-15);
			
		a.setDegrees(-60d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),.5d,1e-15);
		
		a.setDegrees(-90d);
		p.set(a);
		//System.out.println(p.cos()+" Cosine -90 degrees: " + StrictMath.cos(StrictMath.toRadians(-90)));
		assertEquals(p.cos(),0d,1e-15);
		
		a.setDegrees(-180d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),-1d,1e-15);
		
		//a.setDegrees(120d);
		a.setRadians(StrictMath.PI*(2d/3));
		p.set(a);
		//System.out.println(a.getDegrees() + " Degrees. "+ p.signedAngle().getDegrees());
		//System.out.println(p.cos());
		assertEquals(p.cos(),-.5d,1e-15);
		
		a.setDegrees(-120d);
		p.set(a);
		//System.out.println(p.cos()+ " of Degrees. "+ p.unsignedAngle().getDegrees());
		assertEquals(p.cos(),-.5d,1e-15);			
		
		a.setDegrees(240d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),-.5d,1e-15);

		a.setDegrees(60d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),.5d,1e-15);
		
		a.setDegrees(-60d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),.5d,1e-15);
		
		a.setDegrees(300d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),.5d,1e-15);
		
		a.setDegrees(300d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),.5d,1e-15);
		
		a.setDegrees(89.99999999999999d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),StrictMath.cos(StrictMath.toRadians(89.99999999999999d)),1e-15);

		a.setDegrees(90d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),StrictMath.cos(StrictMath.toRadians(90.0d)),1e-15);
		
		a.setDegrees(90.00000000000001d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),StrictMath.cos(StrictMath.toRadians(90.00000000000001d)),1e-15);
		
		a.setDegrees( 0.000001d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),StrictMath.cos(StrictMath.toRadians(0.000001d)),1e-15);

		a.setDegrees(-0.000001d);
		p.set(a);
		//System.out.println(p.cos());
		assertEquals(p.cos(),StrictMath.cos(StrictMath.toRadians(-0.000001d)),1e-15);
		
	}

	@Test
	public void testNegate() {
		Angle a = Angle.inDegrees(30);
		Principle p = new Principle(a);
        p.negate();
        //System.out.println(p.toSignedAngle().getDegrees());
		assertEquals(p.signedAngle().getDegrees(),-30d,1e-13);
		assertEquals(p.negate().signedAngle().getDegrees(),30d,1e-13);

	}

	@Test
	public void testAdd() {
		Angle a = Angle.inDegrees(0);
		Angle b = Angle.inDegrees(30);
		Principle p1 = new Principle(a);
		Principle p2 = new Principle(b);
		
        //System.out.println(p1.signedAngle().getDegrees()-0d);
		assertEquals(p2.signedAngle().getDegrees(),30d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-30d);
		assertEquals(p1.signedAngle().getDegrees(),30d,1e-13);
       
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-60d);
		assertEquals(p1.signedAngle().getDegrees(),60d,1e-13);
       
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-90d);
		assertEquals(p1.signedAngle().getDegrees(),90d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-120d);
		assertEquals(p1.signedAngle().getDegrees(),120d,1e-13);
        
        p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-150d);
		assertEquals(p1.signedAngle().getDegrees(),150d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-180d);
		assertEquals(p1.signedAngle().getDegrees(),180d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+150d);
		assertEquals(p1.signedAngle().getDegrees(),-150d,1e-13);
       
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+120d);
		assertEquals(p1.signedAngle().getDegrees(),-120d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+90d);
		assertEquals(p1.signedAngle().getDegrees(),-90d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+60d);
		assertEquals(p1.signedAngle().getDegrees(),-60d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+30d);
		assertEquals(p1.signedAngle().getDegrees(),-30d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+0d);
		assertEquals(p1.signedAngle().getDegrees(),0d,1e-13);
        
		p1.add(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-30d);
		assertEquals(p1.signedAngle().getDegrees(),30d,1e-13);

		double n=0d;
		a = Angle.inRadians(n);
		p1 = new Principle(a);
		b = Angle.inRadians(StrictMath.atan(1)*(30d/45d));
		p2 = new Principle(b);
		for (int i = 0; i < 13; i++) {
			a = Angle.inRadians(n);
		    //System.out.println("Summed Principle Angle: " + a.getRadians());
		    assertEquals(p1.unsignedAngle().getRadians(), a.getRadians() ,1e-13);
			n += b.getRadians();
			p1.add(p2);
		}
	
	}

	@Test
	public void testSubtract() {
		Angle a = Angle.inDegrees(0);
		Angle b = Angle.inDegrees(-30);
		Principle p1 = new Principle(a);
		Principle p2 = new Principle(b);
		
        //System.out.println(p1.signedAngle().getDegrees()-0d);
		assertEquals(p1.signedAngle().getDegrees(),0d,1e-13);
        
		p1.subtract(p2).signedAngle();		
        //System.out.println(p1.signedAngle().getDegrees()-30d);
		assertEquals(p1.signedAngle().getDegrees(),30d,1e-13);
        
		p1.subtract(p2).signedAngle();		
        //System.out.println(p1.signedAngle().getDegrees()-60d);
		assertEquals(p1.signedAngle().getDegrees(),60d,1e-13);
       
		p1.subtract(p2).signedAngle();		
        //System.out.println(p1.signedAngle().getDegrees()-90d);
		assertEquals(p1.signedAngle().getDegrees(),90d,1e-13);
      
		p1.subtract(p2).signedAngle();		
        //System.out.println(p1.signedAngle().getDegrees()-120d);	
		assertEquals(p1.signedAngle().getDegrees(),120d,1e-13);

        
        p1.subtract(p2).signedAngle();        
        //System.out.println(p1.signedAngle().getDegrees()-150d);
		assertEquals(p1.signedAngle().getDegrees(),150d,1e-13);

        
		p1.subtract(p2).signedAngle();	
        //System.out.println(p1.signedAngle().getDegrees()-180d);
		assertEquals(p1.signedAngle().getDegrees(),180d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+150d);
		assertEquals(p1.signedAngle().getDegrees(),-150d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+120d);
		assertEquals(p1.signedAngle().getDegrees(),-120d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+90d);
		assertEquals(p1.signedAngle().getDegrees(),-90d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+60d);
		assertEquals(p1.signedAngle().getDegrees(),-60d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+30d);
		assertEquals(p1.signedAngle().getDegrees(),-30d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()+0d);
		assertEquals(p1.signedAngle().getDegrees(),0d,1e-13);

        
		p1.subtract(p2).signedAngle();
        //System.out.println(p1.signedAngle().getDegrees()-30d);
		assertEquals(p1.signedAngle().getDegrees(),30d,1e-13);

		double n=StrictMath.PI*2d;
		a = Angle.inRadians(n);
		p1 = new Principle(a);
		b = Angle.inRadians(StrictMath.atan(1)*(30d/45d));
		p2 = new Principle(b);
		for (int i = 0; i < 12; i++) {
			n -= b.getRadians();
			p1.subtract(p2);
			a = Angle.inRadians(n);
		    //System.out.println("Principle Angle: " + a.getRadians());
		    assertEquals(p1.unsignedAngle().getRadians(), a.getRadians() ,1e-14);
		}
		
		p1.set(Angle.inDegrees(359.9));
		p2.set(Angle.inDegrees(0.1));
		//System.out.println("Principle Angle Diff: " + (new Principle(p1).subtract(p2).abs()).signedAngle().getDegrees());
		//System.out.println("Principle Angle Diff: " + (new Principle(p2).subtract(p1).abs()).signedAngle().getDegrees());
	    assertEquals(0.2d, (new Principle(p1).subtract(p2).abs()).signedAngle().getDegrees() ,1e-13);
		p1.set(Angle.inDegrees(179.85));
		p2.set(Angle.inDegrees(180.15));
		//System.out.println("Principle Angle Diff: " + (new Principle(p1).subtract(p2).abs()).signedAngle().getDegrees());
		//System.out.println("Principle Angle Diff: " + (new Principle(p2).subtract(p1).abs()).signedAngle().getDegrees());
	    assertEquals(0.3d, (new Principle(p1).subtract(p2).abs()).signedAngle().getDegrees() ,1e-13);
		p1.set(Angle.inDegrees(-0.1));
		p2.set(Angle.inDegrees(0.1));
		//System.out.println("Principle Angle Diff: " + (new Principle(p1).subtract(p2).abs()).signedAngle().getDegrees());
		//System.out.println("Principle Angle Diff: " + (new Principle(p2).subtract(p1).abs()).signedAngle().getDegrees());
	    assertEquals(0.2d, (new Principle(p1).subtract(p2).abs()).signedAngle().getDegrees() ,1e-13);
		
	}

	@Test
	public void testMultiply() {
//		fail("Not yet implemented"); // TODO
		//a*Theta -->
		double n;
		Angle a = Angle.inDegrees(0);
		Angle b = Angle.inRadians(StrictMath.atan(1)*(30d/45d));
		Principle p1 = new Principle(a);
		Principle p2 = new Principle(b);
		for (int i = 0; i < 13; i++) {
			n=i;
			p1 = new Principle(p2);
			p1.multiply(n);
		    //System.out.println("Principle Angle: " + p1.signedAngle().getDegrees());
		    assertEquals(p1.tanHalf(),StrictMath.tan( (n/2d)*b.getRadians() ),0d);//1e-18);
		}
		for (int i = 0; i < 13; i++) {
			n= -i;
			p1 = new Principle(p2);
			p1.multiply(n);
		    //System.out.println("Principle Angle: " + p1.signedAngle().getDegrees());
		    assertEquals(p1.tanHalf(),StrictMath.tan( (n/2d)*b.getRadians() ),0d);//1e-18);
		}
		
	}

	@Test
	public void testIsZero() {
		Angle angle = Angle.inDegrees(0.0);		
		Principle test=new Principle(angle);
		assertEquals(test.isZero(),Boolean.TRUE);

		
		//show min possible representation lost a bit...
	
		angle.setPiRadians(-2*Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.FALSE);
		
		angle.setRadians(-Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.TRUE); //lost last sig bit of Radians
	
		angle.setRadians(Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.TRUE); //lost last sig bit of Radians
	
		angle.setPiRadians(2*Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.FALSE);
		
		
		angle.setPiRadians(Double.MIN_NORMAL);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.FALSE);
		
		angle.setRadians(Double.MIN_NORMAL/256);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.FALSE);
		
		angle.setPiRadians(2*Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isZero(),Boolean.FALSE);
		
	}

	@Test
	public void testIsPositive() {
		Angle angle = Angle.inRadians(Double.MIN_VALUE);
		Principle test=new Principle(angle);
		assertEquals(test.isPositive(),Boolean.TRUE); //lost last sig bit of Radians
	
		//show zero case
		angle.set(Angle.inRadians(0.0));		
		test.set(angle);
		assertEquals(test.isPositive(),Boolean.TRUE);

		//show negative zero becomes zero case
		angle.set(Angle.inRadians(-0.0));		
		test.set(angle);
		assertEquals(test.isPositive(),Boolean.TRUE);

		
		//show greatest value less than negative-zero computes as zero by loss of bit representation...	
		angle.setRadians(-Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isPositive(),Boolean.TRUE); //lost last sig bit of Radians
	
		//show value largest value not to test positive...	
		angle.setPiRadians(-2*Double.MIN_VALUE);
		test.set(angle);
		assertEquals(test.isPositive(),Boolean.FALSE); //neg last sig bit of Radians now visible.
	
		
	}

	@Test
	public void testIsAcute() {
		Angle angle = Angle.inRadians(Double.MIN_VALUE);
		Principle test=new Principle(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); //lost last sig bit of Radians

		angle.set(Angle.inRadians(StrictMath.scalb(StrictMath.PI,-1)));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(StrictMath.nextUp(StrictMath.scalb(StrictMath.PI,-1))));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 

		angle.set(Angle.inRadians(-StrictMath.scalb(StrictMath.PI,-1)));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(-StrictMath.nextUp(StrictMath.scalb(StrictMath.PI,-1))));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 


		
		angle.set(Angle.inRadians(7));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(6));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(5));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(4));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 

		angle.set(Angle.inRadians(3));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 

		angle.set(Angle.inRadians(2));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 

		angle.set(Angle.inRadians(1));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(-1));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(-2));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 

		
		angle.set(Angle.inRadians(-3));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 

		angle.set(Angle.inRadians(-4));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.FALSE); 
		
		angle.set(Angle.inRadians(-5));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 
		
		angle.set(Angle.inRadians(-6));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

		angle.set(Angle.inRadians(-7));
		test.set(angle);
		assertEquals(test.isAcute(),Boolean.TRUE); 

	}

	@Test
	public void testIsRight() {
//		Angle angle = Angle.inRadians(0);
		Principle test=Principle.RIGHT; //new Principle(angle).setRight();
		assertEquals(test.isAcute(),Boolean.FALSE); 
		assertEquals(test.isRight(),Boolean.TRUE); 
		assertEquals(test.isObtuse(),Boolean.FALSE); 
		test.negate();
		assertEquals(test.isAcute(),Boolean.FALSE); 
		assertEquals(test.isRight(),Boolean.TRUE); 
		assertEquals(test.isObtuse(),Boolean.FALSE); 
		
	}

	@Test
	public void testIsEqualTo() {
		Angle angle = Angle.inRadians(3);
		Principle test=new Principle(angle);
		Principle tryit = new Principle(test);
		Principle tol = new Principle(Principle.ZERO);
	    	
		assertEquals(test.isEqualTo(tryit, tol),Boolean.TRUE);
		test.set(Angle.inRadians(StrictMath.PI));
		assertEquals(test.isEqualTo(tryit, tol),Boolean.FALSE);
		tol.set(Angle.inRadians(0.2));
		assertEquals(test.isEqualTo(tryit, tol),Boolean.TRUE);
		test.add(tol);
		assertEquals(test.isEqualTo(tryit, tol),Boolean.FALSE);
		test.subtract(new Principle(tol).multiply(3));
		assertEquals(test.isEqualTo(tryit, tol),Boolean.FALSE);
		
	}
	@Test
	public void testIsStraight() {
//		Angle angle = Angle.inRadians(0);
		Principle test=Principle.STRAIGHT; //new Principle(angle).setStraight();
		assertEquals(test.isAcute(),Boolean.FALSE); 
		assertEquals(test.isStraight(),Boolean.TRUE); 
		assertEquals(test.isObtuse(),Boolean.TRUE); 
		test.negate();
		assertEquals(test.isAcute(),Boolean.FALSE); 
		assertEquals(test.isStraight(),Boolean.TRUE); 
		assertEquals(test.isObtuse(),Boolean.TRUE); 
	}

}

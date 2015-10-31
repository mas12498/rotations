/**
 * 
 */
package rotationTest;

import static org.junit.Assert.*;

import org.junit.Test;

import rotation.Angle;
import rotation.Principle;

/**
 * Test rotation.Angle
 * Adapter class for angle measure:
 * 	<p>-- positive and negative units.
 *  <p>-- signed measures.
 *  <p>-- stores more than span of revolution.
 *  
 * @author mike
 *
 */
public class AngleTest {

//	/**
//	 * Protected Constructor:
//	 * Test method for {@link rotation.Angle#Angle(double)}.
//	 */
//	@Test
//	public void testAngleDouble() {
//		double a = 30;
//		Angle b = Angle.inDegrees(a);
////		System.out.println( a.getDegrees() + " degrees" );
////		System.out.println(b.getDegrees() + " degrees" );
//		assertEquals("Compare double constructor: ",
//				b.getDegrees(),30.0,1e-14);
////		fail("Not yet implemented"); // TODO
//	}

	
	/**
	 * Test method for {@link rotation.Angle#Angle(rotation.Angle)}.
	 * Test method for {@link rotation.Angle#put(rotation.Angle)}.
	 * Test method for {@link rotation.Angle#half()}.
	 * 
	 */
	@Test
//	public void testAngleAngle() {
//	public void testSet() {
//	public void testHalf() {
	public void testAngleSetHalfAngle() {
		Angle a = Angle.inDegrees(30);
		Angle b = Angle.inDegrees(60);
		b = new Angle(a);
		assertEquals(b.getDegrees(),30,1e-14);
		a.put(b);
		assertEquals(a.getDegrees(),30,1e-14);
		a.put(new Angle(b).half());
		assertEquals(a.getDegrees(),15,1e-14);
		assertEquals(b.getDegrees(),30,1e-14);
		
		a = Angle.inDegrees(120);
		b.put(new Angle(a).half());
		assertEquals(b.getDegrees(),60,1e-14);

		a = Angle.inDegrees(240);
		b.put(new Angle(a).half());
		//System.out.println(b.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(b.getDegrees(),120,1e-13);
		
		a = Angle.inDegrees(380);
		assertEquals(a.getDegrees(),380,1e-14);
		b.put(new Angle(a).half());
		assertEquals(b.getDegrees(),190,1e-14);
		
		a = Angle.inDegrees(-120);
		b.put(new Angle(a).half());
		assertEquals(b.getDegrees(),-60,1e-14);

		
	}

	/**
	 * Test method for {@link rotation.Angle#inRadians(double)}.
	 * Test method for {@link rotation.Angle#putRadians(double)}.
	 * Test method for {@link rotation.Angle#getRadians()}.
	 */
//  public void testGetRadians() {
//	public void testSetRadians() {
//	public void testInRadians() {
	@Test
	public void testGetSetInRadians() {
		Angle a = Angle.inRadians(1.0);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),1.0,0);
		
		a = a.putRadians(.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),.2,1e-14);
		
		a = a.putRadians(-.2);
//		System.out.println(a.getRadians()+" degrees"+(-StrictMath.nextUp(StrictMath.abs(a.getRadians()))));
		assertEquals(a.getRadians(),-.2,1e-14);
		
		a = a.putRadians(2.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextAfter(a.getRadians(),1));
		assertEquals(a.getRadians(),2.2,1e-14);
		
		a = a.putRadians(-2.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians())); 
		assertEquals(a.getRadians(),-2.2,1e-14);

		a = a.putRadians(5.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),5.2,1e-14);
		
		a = a.putRadians(-5.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),-5.2,1e-14);
		
		a = a.putRadians(50.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),50.2,1e-14);	

		a = a.putRadians(-50.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),-50.2,1e-14);	

		a = Angle.inRadians(1.0);
//		System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getRadians(),1.0,0);
		
		a = Angle.inRadians(.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),.2,1e-14);
		
		a = Angle.inRadians(-.2);
//		System.out.println(a.getRadians()+" degrees"+(-StrictMath.nextUp(StrictMath.abs(a.getRadians()))));
		assertEquals(a.getRadians(),-.2,1e-14);
		
		a = Angle.inRadians(2.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextAfter(a.getRadians(),1));
		assertEquals(a.getRadians(),2.2,1e-14);
		
		a = Angle.inRadians(-2.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians())); 
		assertEquals(a.getRadians(),-2.2,1e-14);

		a = Angle.inRadians(5.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),5.2,1e-14);
		
		a = Angle.inRadians(-5.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),-5.2,1e-14);	
		
		a = Angle.inRadians(50.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),50.2,1e-14);	

		a = Angle.inRadians(-50.2);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getRadians(),-50.2,1e-14);	

		a = Angle.inRadians(StrictMath.PI/6);
//		System.out.println(a.getRadians()+" degrees"+StrictMath.nextUp(a.getRadians()));
		assertEquals(a.getDegrees(),30,1e-14);	
		
	//	fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link rotation.Angle#inDegrees(double)}.
	 * Test method for {@link rotation.Angle#getDegrees()}.
	 * Test method for {@link rotation.Angle#putDegrees(double)}.
	 */
//	public void testSetDegrees() {
//	public void testGetDegrees() {
//	public void testInDegrees() {
	@Test
	public void testGetSetInDegrees() {
		
		Angle a = Angle.inDegrees(45);
//		System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),45.0,0);
		
		a.putDegrees(30);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),30.0,1e-14);
		
		a.putDegrees(-30);
//		System.out.println(a.getDegrees()+" degrees"+(-StrictMath.nextUp(StrictMath.abs(a.getDegrees()))));
		assertEquals(a.getDegrees(),-30.0,1e-14);
		
		a.putDegrees(150);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextAfter(a.getDegrees(),1));
		assertEquals(a.getDegrees(),150.0,1e-13);
		
		a.putDegrees(-150);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees())); 
		assertEquals(a.getDegrees(),-150.0,1e-13);

		a.putDegrees(330);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),330.0,1e-13);
		
		a.putDegrees(-330);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),-330.0,1e-13);

		a.putDegrees(530);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),530.0,1e-13);
		
		a.putDegrees(-530);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),-530.0,1e-13);

		a = Angle.inDegrees(45);
//		System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),45.0,0);
		
		a = Angle.inDegrees(30);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),30.0,1e-14);
		
		a = Angle.inDegrees(-30);
//		System.out.println(a.getDegrees()+" degrees"+(-StrictMath.nextUp(StrictMath.abs(a.getDegrees()))));
		assertEquals(a.getDegrees(),-30.0,1e-14);
		
		a = Angle.inDegrees(150);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextAfter(a.getDegrees(),1));
		assertEquals(a.getDegrees(),150.0,1e-13);
		
		a = Angle.inDegrees(-150);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees())); 
		assertEquals(a.getDegrees(),-150.0,1e-13);

		a = Angle.inDegrees(330);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),330.0,1e-13);
		
		a = Angle.inDegrees(-330);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),-330.0,1e-13);
		

		a = Angle.inDegrees(530);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),530.0,1e-13);
		
		a = Angle.inDegrees(-530);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),-530.0,1e-13);
		
		a = Angle.inDegrees(-530.3);
//		System.out.println(a.getDegrees()+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getDegrees(),-530.3,1e-13);
		
	}

	/**
	 * Test method for {@link rotation.Angle#inMeasure(double, double)}.
	 * Test method for {@link rotation.Angle#put(double, double)}.
	 * Test method for {@link rotation.Angle#getMeasure(double)}.
	 */
//	public void testGetMeasure() {
//	public void testSetMeasure() {
//	public void testInMeasure() {
	@Test
	public void testGetSetInMeasure() {
		
		Angle a = Angle.inMeasure(45,Angle.REVOLUTION_DEGREES);
		
		//System.out.println(a.getMeasure(360) + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),45.0,0);
		
		a.put(30,Angle.REVOLUTION_DEGREES);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getMeasure(Angle.REVOLUTION_DEGREES),30.0,1e-14);
		
		a.put(-30*60, Angle.REVOLUTION_ARCMINUTES);
		//System.out.println(a.getMeasure(360)+" degrees"+(-StrictMath.nextUp(StrictMath.abs(a.getDegrees()))));
		assertEquals(a.getMeasure(Angle.REVOLUTION_DEGREES),-30.0,1e-14);
		
		a.put(150,360);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextAfter(a.getDegrees(),1));
		assertEquals(a.getMeasure(360),150.0,1e-13);
		
		a.put(-150,360);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees())); 
		assertEquals(a.getMeasure(360),-150.0,1e-13);

		a.put(330,360);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getMeasure(360),330.0,1e-13);
		
		a.put(-330,360);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getMeasure(360),-330.0,1e-13);

		a.put(530,360);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getMeasure(360),530.0,1e-13);
		
		a.put(-530,360);
		//System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(a.getMeasure(360),-530.0,1e-13);

		a = Angle.inMeasure(60,360);
		//System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),60,1e-14);
		
		a = Angle.inMeasure(60,-360);
		//System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),-60,1e-14);
		
		a = Angle.inMeasure(-60,-360);
		//System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),60,1e-14);
		
		a = Angle.inMeasure(-60,360);
		//System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),-60,1e-14);
		
		a = Angle.inMeasure(-560,-360);
		//System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),560,1e-14);
		
		a = Angle.inMeasure(-560,360);
		//System.out.println(a.getDegrees() + " degrees" + a.getDegrees() );
		assertEquals(a.getDegrees(),-560,1e-14);
		
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link rotation.Angle#inBinary(int, byte)}.
	 * Test method for {@link rotation.Angle#putModuloBinary(int, byte)}.
	 * Test method for {@link rotation.Angle#getBinary(byte)}.
	 */
	@Test
//	public void testGetBinary() {
//	public void testSetBinary() {
//	public void testInBinary() {
	public void testGetSetInBinary() {
		
		Angle a = Angle.inMeasure(45,Angle.REVOLUTION_DEGREES);		

		byte w=16;
		int m = 8192; //=2^
		long n = Angle.inBinary(m,w).getBinary(w);
		//System.out.println( n + " parts of revolution.");
		assertEquals(n,8192);		
		
		a.putModuloBinary(m, w);
		//System.out.println(a.getMeasure(360)+" degrees");
		assertEquals(a.getMeasure(360),45,1e-14);
		a.putModuloBinary(-m, w);
		//System.out.println(a.getMeasure(360)+" degrees");
		assertEquals(a.getMeasure(360),-45,1e-14);
		
		a.putModuloBinary(-9*m, w);
	    //System.out.println(a.getDegrees()+" degrees");
		assertEquals(a.getMeasure(360),-45,1e-12);
			
		a.putModuloBinary(3*m, w);
		//System.out.println(a.getMeasure(360)+" degrees");
		assertEquals(a.getMeasure(360),135,1e-14);
		a.putModuloBinary(-3*m, w);
		//System.out.println(a.getMeasure(360)+" degrees");
		assertEquals(a.getMeasure(360),-135,1e-14);
		
        w=8; //bits in revolution representation
        m=32;
		a.putModuloBinary(m, w);
		//System.out.println(a.getMeasure(360)+" degrees");
		assertEquals(a.getMeasure(360),45,1e-14);

		w=3;
        m=1;
		a.putModuloBinary(m, w);// 1/8
		long k=a.getBinary(w);
		////System.out.println(a.getMeasure(360)+" degrees"+StrictMath.nextUp(a.getDegrees()));
		//System.out.println(a.getMeasure(360)+" degrees");
		assertEquals(a.getMeasure(360),45,1e-14);
	    //System.out.println("for 3 bit machine...= "+k);
		assertEquals(k,1);

		
		w=8;
		k=a.getBinary(w);
		//System.out.println("for 8 bit machine...= "+k);
		assertEquals(k,32);
		
		a = Angle.inBinary(8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),45,1e-14);
		a = Angle.inBinary(2*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),90,1e-14);
		a = Angle.inBinary(3*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),135,1e-14);
		a = Angle.inBinary(4*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),180,1e-14);
		a = Angle.inBinary(5*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),225,1e-14);
		a = Angle.inBinary(6*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),270,1e-14);
		a = Angle.inBinary(7*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),315,1e-14);
		a = Angle.inBinary(8*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),360,1e-14);
		a = Angle.inBinary(9*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),405,1e-13);
		
		a = Angle.inBinary(-8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-45,1e-14);
		a = Angle.inBinary(-2*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-90,1e-14);
		a = Angle.inBinary(-3*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-135,1e-14);
		a = Angle.inBinary(-4*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-180,1e-14);
		a = Angle.inBinary(-5*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-225,1e-14);
		a = Angle.inBinary(-6*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-270,1e-14);
		a = Angle.inBinary(-7*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-315,1e-14);
		a = Angle.inBinary(-8*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-360,1e-14);
		a = Angle.inBinary(-9*8192,(byte) 16);
		//System.out.println(a.getDegrees() + " degrees");
		assertEquals(a.getDegrees(),-405,1e-13);
		
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link rotation.Angle#getCodedPrinciple()}.
	 */
	@Test
	public void testGetCodedPrinciple() {
		
		Angle a = Angle.inDegrees(390);
		Angle b = new Angle(a);
		
		Principle t = new Principle( Angle.inDegrees(0) ); //principle angle by constructor.
		//System.out.println("Principle: "+t.signedAngle().getDegrees()+"  degrees Angle:"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(t.signedAngle().getDegrees(),0,1e-13);

		t = new Principle( a ); //principle angle by constructor.
		//System.out.println("Principle: "+t.signedAngle().getDegrees()+"  degrees Angle:"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(t.signedAngle().getDegrees(),30,1e-13);
		
		b.put(t.signedAngle()); //convert principle angle to angle...signed and unsigned.
		
		Angle c = Angle.inRadians( t.signedAngle().getRadians() );

		//System.out.println("Principle: "+t.getDegrees()+"  degrees Angle:"+StrictMath.nextUp(a.getDegrees()));
		//System.out.println("Principle: "+b.getDegrees()+"  degrees Angle:"+StrictMath.nextUp(a.getDegrees()));
		assertEquals(b.getDegrees(),30,1e-13);
		assertEquals(c.getDegrees(),30,1e-13);
		assertEquals(a.getDegrees(),390,1e-13);
		
		c.put(t.unsignedAngle());
		assertEquals(c.getDegrees(),30,1e-13);
		a.putDegrees(-721);
		t.put(a);
		//System.out.println( "Principle: " + t.signedAngle().getDegrees() + "  degrees. " );
		assertEquals("Ugh: ",t.signedAngle().getDegrees(),-1.0d,1e-13);

		//		fail("Not yet implemented"); // TODO
	}

}

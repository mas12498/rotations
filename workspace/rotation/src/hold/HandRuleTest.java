package hold;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rotation.Angle;

public class HandRuleTest {

	@Test
	public void testHandRule() {
		HandRule definedRight = HandRule.RIGHT;
		HandRule definedLeft = HandRule.LEFT;
		assertEquals((definedRight.isRight()),true);
		assertEquals((definedLeft.isRight()),false);

		double a = 3;
		assertEquals(HandRule.reverse(a),-3,1e-14);
		assertEquals(definedRight.right(a),3,1e-14);
		assertEquals(definedLeft.right(a),-3,1e-14);
		
		a = -3;
		assertEquals(HandRule.reverse(a),3,1e-14);
		assertEquals(definedRight.right(a),-3,1e-14);
		assertEquals(definedLeft.right(a),3,1e-14);
 
//		HandRule aDefinedRightHanded = HandRule.RIGHT;
//		HandRule bDefinedLeftHanded = HandRule.LEFT;
		Angle a1 = Angle.inDegrees(30);
		Angle b1 = Angle.inDegrees(-30);
		
		assertEquals(a1.getDegrees(), -b1.getDegrees(), 1e-14);
//		assertEquals(a1.getDegrees(), HandRule.reverse(b1).getDegrees(), 1e-14);
//		assertEquals(aDefinedRightHanded.right(a1).getDegrees(),
//				bDefinedLeftHanded.right(b1).getDegrees(), 1e-14);
//		assertEquals(aDefinedRightHanded.left(a1).getDegrees(),
//				bDefinedLeftHanded.left(b1).getDegrees(), 1e-14);
//
		//		fail("Not yet implemented"); // TODO
	}

}

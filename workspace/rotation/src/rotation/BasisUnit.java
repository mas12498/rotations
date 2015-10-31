/**
 * 
 */
package rotation;

/** Elementary unit (Quaternion basis elements)
 * @author mike */
public enum BasisUnit {	
	
    S(0), I(1), J(2), K(3); 
  
  	private int unit;
  	
	private BasisUnit(int ptrIndex) { unit = ptrIndex; }
	
	public int index() { return unit; }	  
    	
}
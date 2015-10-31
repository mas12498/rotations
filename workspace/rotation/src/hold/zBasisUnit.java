/**
 * 
 */
package hold;

/** Elementary unit (Quaternion basis elements)
 * @author mike */
public enum zBasisUnit {	
	
    S(0), I(1), J(2), K(3); 
  
  	private int unit;
  	
	private zBasisUnit(int ptrIndex) { unit = ptrIndex; }
	
	public int index() { return unit; }	  
    	
}
package metapenta.processing.petrinet;

public class Constants {
	/**
	 * Colors	
	 */
	public final static RGBTuple BLUE = new RGBTuple(3, 152, 158); 
	public static final RGBTuple BLUE_KING = new RGBTuple(0, 74, 173);
	public static final RGBTuple ORANGE = new RGBTuple(252, 137, 0);
	public static final RGBTuple WHITE = new RGBTuple(255, 255, 255);		
	public static final RGBTuple BLACK = new RGBTuple(0, 0, 0);		
	public static final RGBTuple PURPLE = new RGBTuple(184,106,217);
	public static final RGBTuple GREEN = new RGBTuple(129, 217, 89);
	/**
	 * Size
	 */
	public static final int RADIUS=70;
	public final static int BS = 65;
	
	/**
	 * Offsets
	 */
	public static final int Y_INIT = 50;
	public static final int X_OFFSET = 400;
	public static final int Y_OFFSET = 100;
	
	
	public final static String METABOLITE = "Metabolite";
	public final static String REACTION = "Reaction";
	public final static String REVERSIBLE = "Reversible: ";
	
	/**
	 * Reaction
	 */	
	public final static String ID = "id: ";
	public final static String COMPARTMENT = "Compartment: ";
	public final static String CHEMICAL_FORMULA = "Chemical formula: ";		
	public final static String SUBSTRATES="Substrates";
	public final static String PRODUCTS="Products";
	
	/**
	 * Metabolite
	 */
	
	public final static String IS_SUBSTRATE="Reactions metbolite is substrate";
	public final static String IS_PRODUCT="Reactions metbolite is product";
	
	
}

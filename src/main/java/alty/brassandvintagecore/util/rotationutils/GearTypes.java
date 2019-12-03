package alty.brassandvintagecore.util.rotationutils;

import java.awt.Color;

public enum GearTypes {
		// The densities for the materials used here are kg/cubic meter of
		// the substance, for gears multiply by the number of cubic meters
		// it occupies.

		BRASS(8000D, Color.YELLOW);

		private final double density;
		private final Color color;

		GearTypes(double matDensity, Color matColor){
			density = matDensity;
			color = matColor;
		}

		public double getDensity(){
			return density;
		}

		public Color getColor(){
			return color;
		}

		/**This will return the name with all but the first char being lowercase,
		 * so COPPER becomes Copper, which is good for oreDict and registry
		 */
		@Override
		public String toString(){
			String name = name();
			char char1 = name.charAt(0);
			name = name.substring(1);
			name = name.toLowerCase();
			name = char1 + name;
			return name;
	}
}

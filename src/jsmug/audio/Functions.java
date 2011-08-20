package jsmug.audio;

public class Functions {
	private Functions(){}
	
	public static class Sin implements Function<Double,Double> {
		private double amplitude;
		private double frequency;
		
		public Sin(double amplitude, double frequency) {
			this.amplitude = amplitude;
			this.frequency = frequency;
		}

		@Override
		public Double eval(Double x) {
			return this.amplitude * Math.sin(frequency*2.0*Math.PI*x);
		}
	}

	public static class SinSquare implements Function<Double,Double> {
		private double amplitude;
		private double frequency;
		
		public SinSquare(double amplitude, double frequency) {
			this.amplitude = amplitude;
			this.frequency = frequency;
		}

		@Override
		public Double eval(Double x) {
			return this.amplitude * Math.sin(frequency*2.0*Math.PI*x) * Math.sin(frequency*2.0*Math.PI*x);
		}
	}
	
	public static class InterpolateDouble implements Interpolator {
		public Object eval(Object from, Object to, double percentage) {
			return (Double)(percentage*((Double)to - (Double)from) + (Double)from);
		}
	}
}

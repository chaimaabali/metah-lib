package br.unifor.metahlib.sa;

import br.unifor.metahlib.base.Heuristic;
import br.unifor.metahlib.base.Problem;
import br.unifor.metahlib.base.Solution;


/**
 * The simulated annealing optimization method
 * 
 * @author marcelo lotif
 *
 */
public class SimulatedAnnealing extends Heuristic {
	
	/**
	 * Maximum number of iterations for each temperature reached
	 */
	private int maxIterations;
	/**
	 * The maximum temperature of the system
	 */
	private double maxTemperature;
	/**
	 * The minimum temperature of the system
	 */
	private double minTemperature;
	/**
	 * The temperature decreasing step
	 */
	private double decreaseStep;

	/**
	 * Constructor of the class
	 * 
	 * @param function the function to be optimized
	 * @param tmax the maximum temperature of the system
	 * @param tmin the minimum temperature of the system
	 * @param b the decreasing step
	 * @param k the maximum number of iterations for each temperature reached
	 */
	public SimulatedAnnealing(Problem problem, double tmax, double tmin, double b, int k){
		super(problem);
		this.maxIterations = k;
		this.maxTemperature = tmax;
		this.minTemperature = tmin;
		this.decreaseStep = b;
	}
	
	private Solution newPertubedSolution(Solution s){
		// TODO: avaliar comportamento do pertub, pois podem ser geradas v�rias solu��es
	    Solution[] neighbors = problem.getNeighborhoodStructure().getNeighbors(s);
	    assert(neighbors.length >= 0);
	    return neighbors[0];
	}

	/**
	 * Executes the simulated annealing optimization
	 * 
	 * @return the best solution found
	 */
	@Override
	public Solution execute() {
		Solution x = problem.getInitialSolution();
		
		double temperature = maxTemperature;
		
		int currentIteration = 0;
		double eval = x.getCost();
		
		int totalIt = 1;
		while(temperature > minTemperature){
			for(int i = 0; i < maxIterations; i++){
				currentIteration++;
				
				Solution _x = newPertubedSolution(x);
				double _eval = _x.getCost();
				
				if(_eval < eval){
					x = _x;
					eval = _eval;
					lastBestFoundOn = totalIt;
					System.out.println("improved to: " + x);
					
				} else {
					double rand = problem.getRandom().nextDouble();
					double exp = Math.exp((eval - _eval)/temperature);
					if(rand < exp){
						x = _x;
						eval = _eval;
						System.out.println("worsened to: " + x);
					}
				}
				
				totalIt++;
			}
			temperature *= decreaseStep;
		}
		
		return x;
	}
	
	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}
	
	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public double getDecreaseStep() {
		return decreaseStep;
	}

	public void setDecreaseStep(double decreaseStep) {
		this.decreaseStep = decreaseStep;
	}
	
}

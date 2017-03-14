package test;

public class FlowError {
	public String name;
	
	public FlowError(String name){
		this.name = name;
	}
	
	public static FlowError createFlowConstraintError(){
		return new FlowError("Flusserhaltungsfehler");
	}
	
	public static FlowError createCapacityError(){
		return new FlowError("Kapazitätsfehler");
	}

	public static FlowError createFlowConstraintStartError() {
		return new FlowError("Flusserhaltung in Start/Ziel Fehler");
	}

	public static FlowError createSaturatedCutError() {
		return new FlowError("Kein Saturierter Schnitt");
	}

	public static FlowError createDifferentResultError() {
		return new FlowError("Unterschiedliche Ergebnisse Fehler");
	}
}

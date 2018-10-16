package com.classnotfound.roadtolasvegas.dynamodb.model;

public enum Obligation {
	AFTER_LN ("Non si può lavorare dopo il turno notturno"),
	REST_11H ("Devono esserci 11h di riposo tra i turni"),
	REST_5_DAYS ("Possono essere lavorati al massimo 5 giorni consecutivi"),
	WORKED_DAYS ("Devono essere lavorati 18 giorni su 28");

	private final String text;

	Obligation(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}


}

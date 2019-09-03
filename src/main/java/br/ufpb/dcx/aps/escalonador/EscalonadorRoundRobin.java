package br.ufpb.dcx.aps.escalonador;

public class EscalonadorRoundRobin extends Escalonador{

	public EscalonadorRoundRobin() {
	}
	
	public EscalonadorRoundRobin(TipoEscalonador tipo) {
		super(TipoEscalonador.RoundRobin);
	}
	
	public EscalonadorRoundRobin(int quantum) {
		super(TipoEscalonador.RoundRobin, quantum);
	}
	
	
}

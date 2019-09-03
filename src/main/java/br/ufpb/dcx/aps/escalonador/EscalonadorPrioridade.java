package br.ufpb.dcx.aps.escalonador;

public class EscalonadorPrioridade extends Escalonador {

	public EscalonadorPrioridade() {
	}

	public EscalonadorPrioridade(TipoEscalonador tipo) {
		super(TipoEscalonador.Prioridade);
	}

	public EscalonadorPrioridade(int quantum) {
		super(TipoEscalonador.Prioridade, quantum);
	}

	public void adicionarProcesso(String nomeProcesso) {
		throw new EscalonadorException();
	}
}

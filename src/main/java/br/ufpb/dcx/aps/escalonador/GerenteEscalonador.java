package br.ufpb.dcx.aps.escalonador;

public class GerenteEscalonador {

	private EscalonadorRoundRobin roundRobin = new EscalonadorRoundRobin();
	private EscalonadorPrioridade escalonadorPrioridade = new EscalonadorPrioridade();
	private TipoEscalonador escalonadorProcessos;
	
	public GerenteEscalonador() {}
	
	public GerenteEscalonador(TipoEscalonador tipoEscalonador) {
		if (tipoEscalonador == null) {
			throw new EscalonadorException();
		}
		if(tipoEscalonador.equals(roundRobin.getEscalonador())) {
			roundRobin = new EscalonadorRoundRobin(tipoEscalonador);
			this.escalonadorProcessos = tipoEscalonador;
		}
		else {
			escalonadorPrioridade = new EscalonadorPrioridade(tipoEscalonador);
			this.escalonadorProcessos = tipoEscalonador;
		}
		
	}

	public GerenteEscalonador(TipoEscalonador escalonador, int quantum) {
		if (escalonador.equals(roundRobin.getEscalonador())) {
			roundRobin = new EscalonadorRoundRobin(quantum);
			this.escalonadorProcessos = escalonador;
		} else {
			escalonadorPrioridade = new EscalonadorPrioridade(quantum);
			this.escalonadorProcessos = escalonador;
		}
	}

	public String getStatus() {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			return roundRobin.getStatus();
		}
		return escalonadorPrioridade.getStatus();
	}

	public void tick() {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			roundRobin.tick();
		} else {
			escalonadorPrioridade.tick();
		}
	}

	public void adicionarProcesso(String nomeProcesso) {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			roundRobin.adicionarProcesso(nomeProcesso);
		} else {
			escalonadorPrioridade.adicionarProcesso(nomeProcesso);
		}
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			roundRobin.adicionarProcesso(nomeProcesso, prioridade);
		} else {
			escalonadorPrioridade.adicionarProcesso(nomeProcesso, prioridade);
		}
	}

	public void finalizarProcesso(String nomeProcesso) {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			roundRobin.finalizarProcesso(nomeProcesso);
		} else {
			escalonadorPrioridade.finalizarProcesso(nomeProcesso);
		}
	}

	public void bloquearProcesso(String nomeProcesso) {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			roundRobin.bloquearProcesso(nomeProcesso);
		} else {
			escalonadorPrioridade.bloquearProcesso(nomeProcesso);
		}
	}

	public void retomarProcesso(String nomeProcesso) {
		if (escalonadorProcessos.equals(roundRobin.getEscalonador())) {
			roundRobin.retomarProcesso(nomeProcesso);
		} else {
			escalonadorPrioridade.retomarProcesso(nomeProcesso);
		}
	}
}

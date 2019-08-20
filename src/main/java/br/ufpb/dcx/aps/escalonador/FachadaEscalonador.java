package br.ufpb.dcx.aps.escalonador;

public class FachadaEscalonador {

	private GerenteEscalonador gerenteEscalonador = new GerenteEscalonador();

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		gerenteEscalonador = new GerenteEscalonador(tipoEscalonador);
	}

	public FachadaEscalonador(TipoEscalonador escalonador, int quantum) {
		gerenteEscalonador = new GerenteEscalonador(escalonador, quantum);
	}

	public String getStatus() {
		return gerenteEscalonador.getStatus();
	}

	public void tick() {
		gerenteEscalonador.tick();
	}

	public void adicionarProcesso(String nomeProcesso) {
		gerenteEscalonador.adicionarProcesso(nomeProcesso);
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		gerenteEscalonador.adicionarProcesso(nomeProcesso, prioridade);
	}

	public void finalizarProcesso(String nomeProcesso) {
		gerenteEscalonador.finalizarProcesso(nomeProcesso);
	}

	public void bloquearProcesso(String nomeProcesso) {
		gerenteEscalonador.bloquearProcesso(nomeProcesso);
	}

	public void retomarProcesso(String nomeProcesso) {
		gerenteEscalonador.retomarProcesso(nomeProcesso);
	}
}

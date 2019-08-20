package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;

public class Escalonador {

	private StatusEscalonador st = new StatusEscalonador();
	private TipoEscalonador tipoEscalonador;
	private int quantum = 3;
	private int tick = 0;
	private int valorAternar = 1;
	private String rodando, finalizado;
	private boolean alternar, ocupandoCPU, acaoBloqueio, processoBloqueado = false;
	private List<String> processosAlternando = new ArrayList<String>();
	private List<String> fila = new ArrayList<String>();
	private List<String> bloqueados = new ArrayList<String>();

	public Escalonador() {}
	
	public Escalonador(TipoEscalonador tipo) {
		this.tipoEscalonador = tipo;
	}
	
	public Escalonador(TipoEscalonador escalonador, int quantum) {
		this.tipoEscalonador = escalonador;
		this.quantum = quantum;
		if (quantum <= 0) {
			throw new EscalonadorException();
		}
	}

	public String getStatus() {
		if (alternar) {
			return statusComConcorrencia();
		}
		if (ocupandoCPU && fila.size() > 1) {
			return st.statusProcessoRodandoFila(tipoEscalonador, rodando, processosAlternando, quantum,
					tick);
		}
		return statusSemConcorrencia();
	}

	protected String statusComConcorrencia() {
		processosAlternando.add(rodando);
		rodando = processosAlternando.remove(0);
		ocupandoCPU = true;
		return st.statusProcessoRodandoFila(tipoEscalonador, rodando, processosAlternando, quantum, tick);
	}

	protected String statusSemConcorrencia() {
		if (fila.size() == 0 && rodando == null && finalizado == null) {
			return st.statusInicial(tipoEscalonador, quantum, tick);
		}
		if (tick == 0 && fila.size() > 0) {
			return st.statusFila(tipoEscalonador, fila, quantum, tick);
		}
		if (rodando != null && fila.size() == 1 && finalizado == null) {
			return st.statusRodando(tipoEscalonador, rodando, quantum, tick);
		}
		if (finalizado != null) {
			return finalizandoProcesso();
		}
		rodarProcessoFila();
		realizarTrocaProcessos();

		if (processosAlternando.size() == 0 && fila.size() == 1 && rodando != null) {
			return st.statusFila(tipoEscalonador, fila, quantum, tick);
		}
		if (bloqueados.size() > 0 && rodando != bloqueados.get(0)) {
			for (int i = 0; i < processosAlternando.size(); i++) {
				if (processosAlternando.get(i).equals(rodando)) {
					processosAlternando.remove(i);
				}
			}
			return st.statusRodandoFilaBloqueados(tipoEscalonador, rodando, processosAlternando, bloqueados,
					quantum, tick);
		}

		return st.statusProcessoRodandoFila(tipoEscalonador, rodando, processosAlternando, quantum, tick);
	}

	protected String finalizandoProcesso() {
		String status = "";
		if (rodando != null && processosAlternando.size() == 0) {
			processosAlternando.add(finalizado);
			return statusProcessoFinalizadoCPU();
		}
		if (processosAlternando.size() > 0) {
			if (rodando != null) {
				processosAlternando.add(0, finalizado);
				return statusProcessoFinalizadoCPU();
			}
			status = st.statusProcessoRodandoFila(tipoEscalonador, finalizado, processosAlternando, quantum,
					tick);
			finalizado = null;
			return status;
		}
		status = st.statusProcessoFinalizado(tipoEscalonador, finalizado, quantum, tick);
		finalizado = null;
		return status;
	}

	protected String statusProcessoFinalizadoCPU() {
		String status;
		status = st.statusProcessoRodandoFila(tipoEscalonador, rodando, processosAlternando, quantum, tick);
		processosAlternando.remove(0);
		finalizado = null;
		return status;
	}

	protected void realizarTrocaProcessos() {
		if (acaoBloqueio) {
			return;
		}
		for (int i = 1; i < fila.size(); i++) {
			if (processosAlternando.contains(fila.get(i))) {

			} else {
				processosAlternando.add(fila.get(i));
			}
		}
		if (processosAlternando.contains(rodando)) {
			processosAlternando.remove(0);
		}
	}

	protected void rodarProcessoFila() {
		if (rodando == null) {
			rodando = fila.get(0);
		}
		if (processosAlternando.size() > 0 && ocupandoCPU) {
			processosAlternando.remove(0);
		}
	}

	public void tick() {
		tick++;
		if (acaoBloqueio) {
			valorAternar = tick;
			acaoBloqueio = false;

		}
		if (processosAlternando.size() == fila.size()) {
			valorAternar = tick;
		}
		if (fila.size() == 1) {
			rodarProcessoFila();
		}
		if (fila.size() > 1) {
			verificarAlternancia();
			if (rodando == null) {
				rodarProcessoFila();
			}
		}
		if (bloqueados.size() > 0) {
			verificarAlternancia();
			if (processosAlternando.size() > 0 && processoBloqueado) {
				if (!ocupandoCPU) {
					rodando = processosAlternando.get(0);
				}

			}
		}
	}

	protected void verificarAlternancia() {
		if ((valorAternar + quantum) == tick) {
			valorAternar = tick;
			alternar = true;
			acaoBloqueio = false;
		} else {
			alternar = false;
		}
	}

	public void adicionarProcesso(String nomeProcesso) {

		if (fila.contains(nomeProcesso) || nomeProcesso == null) {
			throw new EscalonadorException();
		}
		if(tipoEscalonador.equals(getEscalonador())) {
			fila.add(nomeProcesso);
			if (tick > 0) {
				valorAternar = tick + 1;
			}
		}else {
			throw new EscalonadorException();
		}
		
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		if (fila.contains(nomeProcesso) || nomeProcesso == null || prioridade > 4) {
			throw new EscalonadorException();
		}
		if(tipoEscalonador.equals(getEscalonador())) {
			throw new EscalonadorException();
		}
		else {
			fila.add(nomeProcesso);
			if (tick > 0) {
				valorAternar = tick + 1;
			}
		}
		
	}

	public void finalizarProcesso(String nomeProcesso) {

		if (fila.contains(nomeProcesso) || processosAlternando.contains(nomeProcesso)
				|| bloqueados.contains(nomeProcesso)) {
			finalizado = nomeProcesso;
			for (int i = 0; i < processosAlternando.size(); i++) {
				if (processosAlternando.get(i).equals(nomeProcesso)) {
					processosAlternando.remove(i);
				}
			}
			fila.remove(nomeProcesso);
			if (rodando.equals(nomeProcesso)) {
				rodando = null;
			}
		} else {
			throw new EscalonadorException();
		}
	}

	public void bloquearProcesso(String nomeProcesso) {
		if (nomeProcesso != rodando) {
			throw new EscalonadorException();
		}
		if (fila.contains(nomeProcesso) || processosAlternando.contains(nomeProcesso)
				|| bloqueados.contains(nomeProcesso)) {
			bloqueados.add(nomeProcesso);
			acaoBloqueio = true;
			processoBloqueado = true;
		} else {
			throw new EscalonadorException();
		}
	}

	public void retomarProcesso(String nomeProcesso) {
		if (bloqueados.contains(nomeProcesso)) {
			if (bloqueados.size() > 0) {
				for (int i = 0; i < bloqueados.size(); i++) {
					if (bloqueados.get(i).equals(nomeProcesso)) {
						processosAlternando.add(bloqueados.remove(i));
					}
				}
			}
		} else {
			throw new EscalonadorException();
		}
	}

	public TipoEscalonador getEscalonador() {
		return TipoEscalonador.RoundRobin;
	}

}

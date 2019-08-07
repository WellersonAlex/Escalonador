package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;

public class FachadaEscalonador {
	
	private StatusEscalonador st = new StatusEscalonador();
	private int quantum = 3;
	private int tick = 0;
	private int valorAternar = 1;
	private String rodando, finalizado;
	private boolean alternar, ocupandoCPU, acaoBloqueio, processoBloqueado = false;
	private TipoEscalonador tipo;
	private List<String> processosAlternando = new ArrayList<String>();
	private List<String> fila = new ArrayList<String>();
	private List<String> bloqueados = new ArrayList<String>();

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {
		this.tipo = tipoEscalonador;
		if (tipoEscalonador == null) {
			throw new EscalonadorException();
		}

	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantusm) {
		this.tipo = roundrobin;
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
			return st.statusProcessoRodandoFila(tipo, rodando, processosAlternando, quantum, tick);
		}
		return statusSemConcorrencia();
	}

	protected String statusComConcorrencia() {
		processosAlternando.add(rodando);
		rodando = processosAlternando.remove(0);
		ocupandoCPU = true;
		return st.statusProcessoRodandoFila(tipo, rodando, processosAlternando, quantum, tick);
	}

	protected String statusSemConcorrencia() {
		if (fila.size() == 0 && rodando == null && finalizado == null) {
			return st.statusInicial(tipo, quantum, tick);
		}
		if (tick == 0 && fila.size() > 0) {
			return st.statusFila(tipo, fila, quantum, tick);
		}
		if (rodando != null && fila.size() == 1 && finalizado == null) {
			return st.statusRodando(tipo, rodando, quantum, tick);
		}
		if (finalizado != null) {
			return finalizandoProcesso();
		}
		rodarProcessoFila();
		realizarTrocaProcessos();

		if (processosAlternando.size() == 0 && fila.size() == 1 && rodando != null) {
			return st.statusFila(tipo, fila, quantum, tick);
		}
		if (bloqueados.size() > 0 && rodando != bloqueados.get(0)) {
			for (int i = 0; i < processosAlternando.size(); i++) {
				if (processosAlternando.get(i).equals(rodando)) {
					processosAlternando.remove(i);
				}
			}
			return st.statusRodandoFilaBloqueados(tipo, rodando, processosAlternando, bloqueados, quantum, tick);
		}

		return st.statusProcessoRodandoFila(tipo, rodando, processosAlternando, quantum, tick);
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
			status = st.statusProcessoRodandoFila(tipo, finalizado, processosAlternando, quantum, tick);
			finalizado = null;
			return status;
		}
		status = st.statusProcessoFinalizado(tipo, finalizado, quantum, tick);
		finalizado = null;
		return status;
	}

	protected String statusProcessoFinalizadoCPU() {
		String status;
		status = st.statusProcessoRodandoFila(tipo, rodando, processosAlternando, quantum, tick);
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

	protected void rodarProcessoFila() {
		if (rodando == null) {
			rodando = fila.get(0);
		}
		if (processosAlternando.size() > 0 && ocupandoCPU) {
			processosAlternando.remove(0);
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
		fila.add(nomeProcesso);
		if (tick > 0) {
			valorAternar = tick + 1;
		}
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {
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
		if(nomeProcesso != rodando) {
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
		}
		else {
			throw new EscalonadorException();
		}
	}
}

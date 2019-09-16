package br.ufpb.dcx.aps.escalonador;

import java.util.*;

public class EscalonadorMaisCurtoPrimeiro extends Escalonador {

	StatusEscalonadorMCP status = new StatusEscalonadorMCP();

	private int tick;
	private List<String> fila = new ArrayList<>();
	private List<Integer> duracoes = new ArrayList<>();

	private String processoRodando;

	private int duracaoFixa = 0;
	private int duracaoRodando = 0;

	public EscalonadorMaisCurtoPrimeiro() {
	}

	public EscalonadorMaisCurtoPrimeiro(TipoEscalonador tipoEscalonador) {
		super(TipoEscalonador.MaisCurtoPrimeiro);
	}

	public EscalonadorMaisCurtoPrimeiro(int quantum) {
		super(TipoEscalonador.MaisCurtoPrimeiro, quantum);
	}

	public String getStatus() {
		if (processoRodando == null && fila.size() == 0) {
			return status.statusInicialMCP(TipoEscalonador.MaisCurtoPrimeiro, 0, tick);
		}
		if (processoRodando == null && fila.size() > 0) {
			return status.statusFilaMCP(TipoEscalonador.MaisCurtoPrimeiro, fila, 0, tick);
		}
		if (tick > 0 && fila.size() == 0) {
			return status.statusRodandoMCP(TipoEscalonador.MaisCurtoPrimeiro, processoRodando, 0, tick);
		}
		return status.statusProcessoRodandoFilaMCP(TipoEscalonador.MaisCurtoPrimeiro, processoRodando, fila, 0, tick);
	}

}

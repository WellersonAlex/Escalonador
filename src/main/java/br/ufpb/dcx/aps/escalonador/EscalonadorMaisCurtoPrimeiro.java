package br.ufpb.dcx.aps.escalonador;

import java.awt.List;
import java.util.ArrayList;

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

}

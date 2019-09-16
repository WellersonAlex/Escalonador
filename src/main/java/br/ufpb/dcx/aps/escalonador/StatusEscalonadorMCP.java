package br.ufpb.dcx.aps.escalonador;

import java.util.List;

public class StatusEscalonadorMCP {

	public String statusInicialMCP(TipoEscalonador tipo, int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {};Quantum: " + quantum + ";Tick: " + tick;
	}

	public String statusFilaMCP(TipoEscalonador tipo, List<String> fila, int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {Fila: " + fila + "};Quantum: " + quantum + ";Tick: " + tick;
	}

	public String statusRodandoMCP(TipoEscalonador tipo, String rodando, int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + "};Quantum: " + quantum + ";Tick: " + tick;
	}

	public String statusProcessoRodandoFilaMCP(TipoEscalonador tipo, String rodando, List<String> processos,
			int quantum, int tick) {
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + ", Fila: " + processos + "};Quantum: "
				+ quantum + ";Tick: " + tick;
	}
}

package br.ufpb.dcx.aps.escalonador;

import java.util.List;

public class StatusEscalonador {
	
	public String statusInicial(TipoEscalonador tipo, int quantum, int tick) {		
		return "Escalonador " + tipo + ";Processos: {};Quantum: " + quantum + ";Tick: " + tick;
	}
	
	public String statusFila(TipoEscalonador tipo, List<String> fila, int quantum, int tick) {		
		return "Escalonador " + tipo + ";Processos: {Fila: " + fila + "};Quantum: " + quantum + ";Tick: " + tick;
	}
	
	public String statusRodando(TipoEscalonador tipo, String rodando, int quantum, int tick) {		
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + "};Quantum: " + quantum + ";Tick: "+ tick;
	}
	
	public String statusProcessoFinalizado(TipoEscalonador tipo, String finalizado, int quantum, int tick) {		
		return "Escalonador " + tipo + ";Processos: {Rodando: " + finalizado + "};Quantum: " + quantum + ";Tick: "+ tick;
	}
	
	public String statusProcessoRodandoFila(TipoEscalonador tipo, String rodando, List<String> processos, int quantum, int tick) {		
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + ", Fila: " + processos + "};Quantum: "+ quantum + ";Tick: " + tick;
	}
	
	public String statusRodandoFilaBloqueados(TipoEscalonador tipo, String rodando, List<String> processos, List<String> bloqueados, int quantum, int tick) {		
		return "Escalonador " + tipo + ";Processos: {Rodando: " + rodando + ", Fila: " + processos + ", Bloqueados: "+bloqueados+"};Quantum: "+ quantum + ";Tick: " + tick;
	}
}

package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.cod3r.cm.excecao.ExplosaoException;

public class Campo {

	private final int linha;
	private final int coluna;
	private boolean campoAberto = false;
	private boolean campoMinado = false;
	private boolean campoMarcado = false;
	
	private List<Campo> vizinhos = new ArrayList<Campo>();
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if (deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}

	}
	
	void alternarMarcacao() {
		if (!campoAberto) {
			campoMarcado = !campoMarcado;
		}
	}
	
	boolean abrir() {
		if (!campoAberto && !campoMarcado) {
			campoAberto = true;
			
			if (campoMinado) {
				throw new ExplosaoException();
			}
			
			if (VizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	boolean VizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.campoMinado);
	}
	
	void minar() {
		campoMinado = true;
	}
	
	public boolean isMinado() {
		return campoMinado;
	}
	
	public boolean isMarcado() {
		return campoMarcado;
	}
	
	public boolean isAberto() {
		return campoAberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean campoDesvendado = !campoMinado && campoAberto;
		boolean campoProtegido = campoMinado && campoMarcado;
		return campoDesvendado || campoProtegido;
	}
	
	long minasNaVizinhanca() {
		return vizinhos.stream().filter(v -> v.campoMinado).count();
	}
	
	void reiniciarJogo() {
		campoAberto = false;
		campoMinado = false;
		campoMarcado = false;
	}
	
	public String toString() {
		if (campoMarcado) {
			return "x"; // representa que o campo está marcado
		} else if (campoAberto && campoMinado) {
			return "*"; // representa que o campo é uma bomba
		} else if (campoAberto && minasNaVizinhanca() > 0) {
			return Long.toString(minasNaVizinhanca()); // mostra a quantidade de minas que existe perto da vizinhança
		} else if (campoAberto) {
			return " ";
		} else {
			return "?"; // representa que o campo está fechado
		}
	}
}

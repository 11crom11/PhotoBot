package photoBot.Drools.Reglas;

import java.util.ArrayList;
import java.util.List;

import photoBot.Gate.Etiqueta;

public class TipoResultadoReglas {
	
	private String tipo;
	private List<String> lPalabras;

	
	public TipoResultadoReglas(String t, Etiqueta e){
		this.tipo = t;
		this.lPalabras = new ArrayList<String>();
		this.lPalabras.add(e.getNombre());
	}
	
	public String getTipo(){
		return this.tipo;
	}
	
	public void insertarEtiqueta(Etiqueta e){
		this.lPalabras.add(e.getNombre());
	}
	
	public List<String> getListaPalabras(){
		return this.lPalabras;
	}
}

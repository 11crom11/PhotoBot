package photoBot.Drools.Reglas;

import java.util.HashMap;
import java.util.List;

import photoBot.Gate.Etiqueta;

public class ConclusionReglas {
		
	private HashMap<String, TipoResultadoReglas> resultadosReglas;
	
	public ConclusionReglas(){
		this.resultadosReglas = new HashMap<String, TipoResultadoReglas>();
	}
	
	public void addNuevaConclusion(String tipo, Etiqueta e){
		if(this.resultadosReglas.containsKey(tipo)){
			this.resultadosReglas.get(tipo).insertarEtiqueta(e);
		}
		else{
			this.resultadosReglas.put(tipo, new TipoResultadoReglas(tipo, e));
		}
	}
	
	public boolean existeTipo(String t){
		return (this.resultadosReglas.containsKey(t));
	}
	
	public List<String> getPalabrasTipo(String t){
		return this.resultadosReglas.get(t).getListaPalabras();
	}
}

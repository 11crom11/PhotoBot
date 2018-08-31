package photoBot.Gate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ProcesadorFechas {
	private static List<String> meses;

	static {
		meses = new ArrayList<String>();
		
		meses.add("enero");
		meses.add("febrero");
		meses.add("marzo");
		meses.add("abril");
		meses.add("mayo");
		meses.add("junio");
		meses.add("julio");
		meses.add("agosto");
		meses.add("septiembre");
		meses.add("octubre");
		meses.add("noviembre");
		meses.add("dciembre");
	}
	
	public ProcesadorFechas() {
		
	}
	
	public static Pair<Date, Date> obtenerFormatoDate(Etiqueta e) {
		Pair<Date, Date> ret = null;
		
		String fecha = e.getNombre();
		
		if(e.getTipo() == "FechaTipo1") {
			ret = formatoTipo1(fecha);
		}
		else if(e.getTipo() == "FechaTipo2") {
			ret = formatoTipo2(fecha);
		}
		else if(e.getTipo() == "FechaTipo3") {
			ret = formatoTipo3(fecha);
		}
		else if(e.getTipo() == "FechaTipo4") {
			ret = formatoTipo4(fecha);
		}
		else if(e.getTipo() == "FechaTipo5") {
			ret = formatoTipo5(fecha);
		}
		else if(e.getTipo() == "FechaTipo6") {
			ret = formatoTipo6(fecha);
		}
		else if(e.getTipo() == "FechaTipo7") {
			ret = formatoTipo7(fecha);
		}
		else if(e.getTipo() == "FechaTipo8") {
			ret = formatoTipo8(fecha);
		}
		else if(e.getTipo() == "FechaCompuestaTipo1") {
			ret = formatoTipoCompuesto1(fecha);
		}
		else if(e.getTipo() == "FechaCompuestaTipo2") {
			ret = formatoTipoCompuesto2(fecha);
		}
		else if(e.getTipo() == "FechaCompuestaTipo3") {
			ret = formatoTipoCompuesto3(fecha);
		}
		else if(e.getTipo() == "FechaCompuestaTipo4") {
			ret = formatoTipoCompuesto4(fecha);
		}
		else if(e.getTipo() == "FechaCompuestaTipo7") {
			ret = formatoTipoCompuesto7(fecha);
		}
		
		return ret;
	}
	
	private static Pair<Date, Date> formatoTipo1(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month = 0, day = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				month = Integer.valueOf(fecha[i]) - 1;
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		cAux.set(year, month, day, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month, day, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	
	private static Pair<Date, Date> formatoTipo2(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month = 0, day = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				month = Integer.valueOf(fecha[i]) - 1;
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year = Calendar.getInstance().get(Calendar.YEAR);
		
		cAux.set(year, month, day, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month, day, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipo3(String f) {
	
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month = 0, day = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		cAux.set(year, month, day, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month, day, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipo4(String f) {
	
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month = 0, day = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year = Calendar.getInstance().get(Calendar.YEAR);
		
		cAux.set(year, month, day, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month, day, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipo5(String f) {
	
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month1 = 0, month2 = 0, day1 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month1 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month2 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//DIA
		day1 = 1;
		Calendar aux = Calendar.getInstance();
		aux.set(2018, month2, 1);
		day2 = aux.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		cAux.set(year, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipo6(String f) {
	
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month1 = 0, month2 = 0, day1 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month1 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month2 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year = Calendar.getInstance().get(Calendar.YEAR);
		
		//DIA
		day1 = 1;
		Calendar aux = Calendar.getInstance();
		aux.set(2018, month2, 1);
		day2 = aux.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		cAux.set(year, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipo7(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month = 0, day1 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//DIA
		day1 = 1;
		Calendar aux = Calendar.getInstance();
		aux.set(2018, month, 1);
		day2 = aux.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		cAux.set(year, month, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipo8(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year = 0, month = 0, day1 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year = Calendar.getInstance().get(Calendar.YEAR);
		
		//DIA
		day1 = 1;
		Calendar aux = Calendar.getInstance();
		aux.set(2018, month, 1);
		day2 = aux.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		cAux.set(year, month, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year, month, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipoCompuesto1(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year1 = 0, month1 = 0, day1 = 0, year2 = 0, month2 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day1 = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				month1 = Integer.valueOf(fecha[i]) - 1;
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year1 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//DIA
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				day2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//MES
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				month2 = Integer.valueOf(fecha[i]) - 1;
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		cAux.set(year1, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year2, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipoCompuesto2(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year1 = 0, month1 = 0, day1 = 0, year2 = 0, month2 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day1 = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				month1 = Integer.valueOf(fecha[i]) - 1;
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year1 = Calendar.getInstance().get(Calendar.YEAR);
		
		//DIA
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				day2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//MES
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				month2 = Integer.valueOf(fecha[i]) - 1;
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year2 = Calendar.getInstance().get(Calendar.YEAR);
		
		cAux.set(year1, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year2, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipoCompuesto3(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year1 = 0, month1 = 0, day1 = 0, year2 = 0, month2 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day1 = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month1 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year1 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//DIA
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				day2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month2 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		cAux.set(year1, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year2, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////

	private static Pair<Date, Date> formatoTipoCompuesto4(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year1 = 0, month1 = 0, day1 = 0, year2 = 0, month2 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//DIA
		while(i < fecha.length) {
			day1 = Integer.valueOf(fecha[i]);
			
			i++;
			break;
		}
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month1 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year1 = Calendar.getInstance().get(Calendar.YEAR);
		
		//DIA
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				day2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month2 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		year2 = Calendar.getInstance().get(Calendar.YEAR);
		
		cAux.set(year1, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year2, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}

//////////////////////////////////////////////////////////////////////////////////////
	
	private static Pair<Date, Date> formatoTipoCompuesto7(String f) {
		
		Calendar cAux = Calendar.getInstance();
		
		int year1 = 0, month1 = 0, day1 = 0, year2 = 0, month2 = 0, day2 = 0;
		
		f = f.toLowerCase();
		
		String[] fecha = f.split(" ");
		
		int i = 0;
				
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month1 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year1 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//DIA
		day1 = 1;
		
		//MES
		while(i < fecha.length) {
			if(meses.contains(fecha[i])) {
				month2 = meses.indexOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		//ANYO
		while(i < fecha.length) {
			if(StringUtils.isNumeric(fecha[i])) {
				year2 = Integer.valueOf(fecha[i]);
				i++;
				break;
			}
			else {
				i++;
			}
		}
		
		Calendar aux = Calendar.getInstance();
		aux.set(2018, month2, 1);
		day2 = aux.getActualMaximum(Calendar.DAY_OF_MONTH);		
		
		cAux.set(year1, month1, day1, 0, 0, 0);
		Date d1 = cAux.getTime();
		
		cAux = Calendar.getInstance();
		cAux.set(year2, month2, day2, 23, 59, 59);
		Date d2 = cAux.getTime();
		
		return Pair.of(d1, d2);
	}	
}

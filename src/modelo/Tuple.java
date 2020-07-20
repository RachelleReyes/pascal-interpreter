package modelo;

import java.util.ArrayList;

import modelo.PascalLexer.Token;

public class Tuple {
	private int lineCode;
	private String type;
	private ArrayList <Token> list;
	private int lineTrue;
	private int lineFalse;
	
	
	public Tuple(int lineCode, String type, ArrayList<Token> list, int lineTrue, int lineFalse) {
		super();
		this.lineCode = lineCode;
		this.type = type;
		this.list = list;
		this.lineTrue = lineTrue;
		this.lineFalse = lineFalse;
	}


	public int getLineCode() {
		return lineCode;
	}


	public void setLineCode(int lineCode) {
		this.lineCode = lineCode;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public ArrayList<Token> getList() {
		return list;
	}


	public void setList(ArrayList<Token> list) {
		this.list = list;
	}


	public int getLineTrue() {
		return lineTrue;
	}


	public void setLineTrue(int lineTrue) {
		this.lineTrue = lineTrue;
	}


	public int getLineFalse() {
		return lineFalse;
	}


	public void setLineFalse(int lineFalse) {
		this.lineFalse = lineFalse;
	}

	/*
	@Override
	public String toString() {
		return "Tuple [lineCode=" + lineCode + ", type=" + type + ", list=" + list + ", lineTrue=" + lineTrue
				+ ", lineFalse=" + lineFalse + "]";
	}*/
	public String toString() {
		return "(" + lineCode + ","+ type + "," + printTokenValue(list) + ","+ lineTrue +"," + lineFalse + ")";
	}

	public ArrayList <String>  printTokenValue(ArrayList <Token> list) {
		ArrayList <String> cadena = new ArrayList<String>();
		int i = 0;
		for(i = 0; i<list.size(); i++) {
			cadena.add(list.get(i).data);
		}
		return cadena;
	}
	
}

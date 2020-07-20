package modelo;

/*Patrones - Tabla de simbolos para alcance monolitico*/
//Definicion de simbolos

public class Symbol { //A generic programming language symbol
	
	String name;	  //All symbols at least have a name
	Type type;
	String value;
	
	
	public Symbol(String name) {this.name = name;}
	public Symbol(String name,Type type) {this(name); this.type=type;}
	public Symbol(String name,Type type, String value) {this(name); this.type=type; this.value = value;}
	public String getName() {return name;}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public Type getType() {
		return type;
	}
	
	public String toString() {
		if(type!=null) return '<'+getName()+":"+type+ ':'+value+ '>';
		return getName();
	}
}

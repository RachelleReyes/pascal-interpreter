package modelo;

public interface Scope {
	public String getScopeName();		//Do I have a name
	public Scope getEnclosingScope();	//Am i nested in another
	public void define(Symbol sym); 	//Define sym in this scope
	public Symbol resolve(String name);	//Look up name in scope
}

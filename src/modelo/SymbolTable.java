package modelo;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements Scope{ //single-scope symtab
	Map<String,Symbol> symbols = new HashMap<String,Symbol>();
	StringBuilder errorSb = new StringBuilder();
	
	public SymbolTable() {initTypeSystem();}
	
	protected void initTypeSystem() {
		define(new BuiltInTypeSymbol("integer"));
		define(new BuiltInTypeSymbol("real"));
		define(new BuiltInTypeSymbol("char"));
		define(new BuiltInTypeSymbol("boolean"));
		//define(new BuiltInTypeSymbol("to"));
		//define(new BuiltInTypeSymbol("downto"));
	}
	//Satisfy Scope interface
	public String getScopeName() {return "global";};	
	public Scope getEnclosingScope() {return null;}	
	public void define(Symbol sym) {symbols.put(sym.name,sym);} 	
	//public Symbol resolve(String name) {return symbols.get(name);}
	///public void setValue(String value) {}
	
	
	public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
		if(s!=null) return s;
		else 
		throw new Error("\n Error: '" + name+ "' undeclared");
	}
	
	public String getErrors() {
		return errorSb.toString();
	}

	public String toString() {return getScopeName()+ ":" + symbols;}
	
	public String formatTable() {
		StringBuilder stringB = new StringBuilder();
		String name;
		stringB.append("NOMBRE");
		stringB.append(" [");
		stringB.append("TIPO");
		stringB.append("] ");
		stringB.append(" = ");
		stringB.append("VALOR");
		stringB.append(System.lineSeparator());
		
		for (Map.Entry<String,Symbol>  entry : symbols.entrySet()) {
			name = entry.getValue().name;
			if(name!= "boolean" && name!= "char" && name!= "integer" && name!= "real") {
				stringB.append(entry.getValue().name);
				stringB.append(" [");
				stringB.append(entry.getValue().type);
				stringB.append("] ");
				stringB.append(" = ");
				stringB.append(entry.getValue().value);
				stringB.append(System.lineSeparator());
			}
		}
		return stringB.toString();
	}
}

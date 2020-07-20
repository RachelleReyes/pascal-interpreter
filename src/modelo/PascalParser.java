package modelo;

import java.util.ArrayList;

import modelo.PascalLexer.Token;

/*Parser recursivo descendiente de tipo LL1*/
public class PascalParser {
	ArrayList <Token> tokens,tokenList,tokenListAux;
	Token tokenAux;
	int index = 0, tab = 0, lineaCodigo = 0,variables=0,i=0,j=0,line=0;
	String name,type,output,value,auxVar;
	SymbolTable table = new SymbolTable();
	StringBuilder sb = new StringBuilder();
	StringBuilder errorSb = new StringBuilder();
	ArrayList <String> varNames = new ArrayList <String>();
	ArrayList <Tuple> listTuples = new ArrayList<Tuple>(); 
	ArrayList <Integer> ifComparation,switchComparation;
	Tuple tuple;
	
	public PascalParser(String input, ArrayList <Tuple> listTuples, SymbolTable table,StringBuilder errorSb) {
		this.listTuples = listTuples;
		this.table = table;
		this.errorSb = errorSb;
		PascalLexer lexer = new PascalLexer();
		tokens = lexer.lex(input);
	}
	
	public String parse() {
		sb = new StringBuilder();
		table = new SymbolTable();
		errorSb = new StringBuilder();
		programa();
		output = sb.toString();
		return output;
	}
	public SymbolTable getSymbolTable() {
		return table;
	}
	
	public String getErrors() {
		return errorSb.toString();
	}
	
	public ArrayList <Tuple> getListTuples() {
		return listTuples;
	}
	
	private void programa() {
		tokenList = new ArrayList<Token>();
		match("INICIOPROGRAMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("VARIABLE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		tokenList.add(tokens.get(index-1));
		name = tokens.get(index-1).data;
		type = "funcion";
		
		match("PUNTOYCOMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		sb.append(System.lineSeparator());
		
		table.define(new VariableSymbol(name,new BuiltInTypeSymbol(type),null));
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"declaracion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
		declaraciones();
		match("INICIO");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		sb.append(System.lineSeparator());
		tokenList = new ArrayList<Token>();
		tokenList.add(tokens.get(index-1));
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"begin",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
		enunciados();
		match("FINPROGRAMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		sb.append(System.lineSeparator());
		
		tokenList = new ArrayList<Token>();
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"finPrograma",tokenList,0,0);
		listTuples.add(tuple);
	}
	
	
	private void clases() {
		tokenList = new ArrayList<Token>();
		
		match("CLASE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("VARIABLE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		tokenList.add(tokens.get(index-1));
		name = tokens.get(index-1).data;
		
		match("BRACKETSDER");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		enunciados();
		
		match("BRACKETSIZQ");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"declaracion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		sb.append(System.lineSeparator());
	}
	
	private void metodos() {
		
		tokenList = new ArrayList<Token>();
		
		match("CLASE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("VARIABLE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		tokenList.add(tokens.get(index-1));
		name = tokens.get(index-1).data;
		
		match("BRACKETSDER");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		enunciados();
		
		match("BRACKETSIZQ");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"declaracion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		sb.append(System.lineSeparator());
	}
	
	/*Funcion de declaraciones de constantes y variables*/
	private void declaraciones() {
		
		if(tokens.get(index).type.name().equals("DECLARACONST")) {
			match("DECLARACONST");
			tokenList = new ArrayList<Token>();
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			sb.append(System.lineSeparator());
			
			lineaCodigo++;
			tuple = new Tuple(lineaCodigo,"const",tokenList,lineaCodigo+1,lineaCodigo+1);
			listTuples.add(tuple);
			
			while(tokens.get(index).type.name().equals("VARIABLE") ) {
				declaracionConst();
			}
		}
		
		if(tokens.get(index).type.name().equals("DECLARAVARIABLE")) {
			match("DECLARAVARIABLE");
			//lineaCodigo++;
			tokenList = new ArrayList<Token>();
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			sb.append(System.lineSeparator());
			
			lineaCodigo++;
			tuple = new Tuple(lineaCodigo,"var",tokenList,lineaCodigo+1,lineaCodigo+1);
			listTuples.add(tuple);
			
			while(tokens.get(index).type.name().equals("VARIABLE") ) {
				declaracion();
			}
		}
	}
	
	/*Funcion de declaraciones de variables*/
	private void declaracion() {
		tokenList = new ArrayList<Token>();
		varNames = new ArrayList <String>();
		
		variables=0;
		while(tokens.get(index).type.name().equals("VARIABLE")) {
			match("VARIABLE");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			tokenList.add(tokens.get(index-1));
			varNames.add(tokens.get(index-1).data);
			variables++;
			
			if(tokens.get(index).type.name().equals("COMA")) {
				match("COMA");
				sb.append(tokens.get(index-1).type);
				sb.append(" ");
			}
			
		}
		
		match("DOSPUNTOS");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		if(tokens.get(index).type.name().equals("ENTERO")) {
			match("ENTERO");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			type = "integer";
		}
		else if(tokens.get(index).type.name().equals("REAL")) {
			match("REAL");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			type = "real";
		}else if(tokens.get(index).type.name().equals("CARACTER")) {
			match("CARACTER");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			type = "char";
		}else if(tokens.get(index).type.name().equals("BOOLEANO")) {
			match("BOOLEANO");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			type = "booleano";
		}
		
		match("PUNTOYCOMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		sb.append(System.lineSeparator());
		i=0;
		
		// TO GIVE ME ERROR
		while(i<variables) {
			table.define(new VariableSymbol(varNames.get(i).toString(),new BuiltInTypeSymbol(type),null));
			auxVar = varNames.get(i).toString();
			j = i;
			while(j<variables-1) {
				if(auxVar==varNames.get(j+1).toString()) {
					errorSb.append("Multiple declarations of variable ");
					errorSb.append(varNames.get(variables-1).toString());
					errorSb.append(System.lineSeparator());
				}	
				j++;	
			}
			i++;
		}
		
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"declaracion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
	}
	
	/*Funcion de declaraciones de constantes*/
	private void declaracionConst() {
		tokenList = new ArrayList<Token>();
		
		match("VARIABLE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		tokenList.add(tokens.get(index-1));
		name = tokens.get(index-1).data;
		match("IGUAL");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		if(tokens.get(index).type.name().equals("NUMERO")) {
			match("NUMERO");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			value = tokens.get(index-1).data;
		}else if(tokens.get(index).type.name().equals("CADENA")){
			match("CADENA");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			value = tokens.get(index-1).data;
		}
		
		match("PUNTOYCOMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		sb.append(System.lineSeparator());
		
		type = "constante";
		
		table.define(new VariableSymbol(name,new BuiltInTypeSymbol(type),value));
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"declaracion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
	}
	
	
	private void enunciados() {
		enunciado();
		if (tokens.get(index).type.name().equals("FIN") || tokens.get(index).type.name().equals("FINPROGRAMA")) 
			return; 
		enunciados();
	}
	
	private void enunciado() {
		if (tokens.get(index).type.name().equals("VARIABLE")) { asignacion(); return; }
		if (tokens.get(index).type.name().equals("LEER") || tokens.get(index).type.name().equals("LEERLN")) { leer(); return; }
		if (tokens.get(index).type.name().equals("ESCRIBIR") || tokens.get(index).type.name().equals("ESCRIBIRLN")) { escribir(); return; }
		if (tokens.get(index).type.name().equals("SI")) { si(); return; }
		if (tokens.get(index).type.name().equals("MIENTRAS")) { mientras(); return; }
		if (tokens.get(index).type.name().equals("CASE")) { switchCase(); return; }
		if (tokens.get(index).type.name().equals("FOR")) { forCiclo(); return; }
		
	}
	
	private void asignacion() {
		
		tokenList = new ArrayList<Token>();
		table.resolve(tokens.get(index).data); 
		match("VARIABLE");
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("DOSPUNTOS");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("IGUAL");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		expresion();
		match("PUNTOYCOMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"asignacion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		sb.append(System.lineSeparator());
	}
	
	private void match(String name) {
		if(tokens.get(index).type.name().equals(name)) {
				index++;
			return;
		}else throw new Error("Expecting " +  name + " found " + tokens.get(index).type.name());
	}
	
	
	/*My functions*/
	private void expresion() {
		/*<Expresion> -> <Valor> operador-aritmetico <Valor> | <Valor>*/
		valor();
		if(tokens.get(index).type.name().equals("OPARITMETICO")) {
			match("OPARITMETICO");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			valor();
		}
	}
	
	private void leer() {

		tokenList = new ArrayList<Token>();
		if(tokens.get(index).type.name().equals("LEER"))
			match("LEER");
		else
			match("LEERLN");
		
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("PARENTESISIZQ");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("VARIABLE");
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("PARENTESISDER");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("PUNTOYCOMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"leer",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		sb.append(System.lineSeparator());
	}
	
	private void escribir() {

		/*<Escribir> -> escribir cadena | escribir cadena, variable*/
		tokenList = new ArrayList<Token>();
		
		if(tokens.get(index).type.name().equals("ESCRIBIR"))
			match("ESCRIBIR");
		else
			match("ESCRIBIRLN");
		
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("PARENTESISIZQ");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("CADENA");
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		if(tokens.get(index).type.name().equals("COMA")) {
			match("COMA");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			table.resolve(tokens.get(index).data); 
			match("VARIABLE");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
		}
		match("PARENTESISDER");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		match("PUNTOYCOMA");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"escribir",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		sb.append(System.lineSeparator());
	}
	
	private void switchCase() {
		
		tokenListAux = new ArrayList<Token>();
		switchComparation = new ArrayList<Integer>(); 
		match("CASE");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("VARIABLE");
		tokenListAux.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("OF");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		lineaCodigo++;
		line = lineaCodigo;
		tuple = new Tuple(lineaCodigo,"cicloSwitch",tokenListAux,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
		while(tokens.get(index).type.name().equals("NUMERO")||tokens.get(index).type.name().equals("CADENA")) {
			tokenListAux = new ArrayList<Token>();
			if(tokens.get(index).type.name().equals("NUMERO"))
				match("NUMERO");
			else 
				match("CADENA");
			
			tokenAux = tokens.get(index-1);
			tokenListAux.add(tokenAux);
			
			lineaCodigo++;
			tuple = new Tuple(lineaCodigo,"comparar",tokenListAux,lineaCodigo+1,lineaCodigo+1);
			listTuples.add(tuple);
			
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			switchComparation.add(lineaCodigo);
			
			match("DOSPUNTOS");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			enunciado();
		}
		
		if(tokens.get(index).type.name().equals("ELSE")) {
			match("ELSE");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			
			tokenAux = tokens.get(index-1);
			tokenListAux.add(tokenAux);
			
			lineaCodigo++;
			tuple = new Tuple(lineaCodigo,"compararElse",tokenListAux,lineaCodigo+1,lineaCodigo+1);
			listTuples.add(tuple);
			
			enunciado();
		}
		
		tokenList = new ArrayList<Token>();
		match("FIN");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		tokenList.add(tokens.get(index-1));
		lineaCodigo++;
		i= 0;
		while(i<switchComparation.size()) {
			listTuples.get(switchComparation.get(i)).setLineFalse(lineaCodigo);
			listTuples.get(switchComparation.get(i)).setLineTrue(lineaCodigo);
			i++;
		}
		
		i= 0;
		while(i<switchComparation.size()) {
			listTuples.get(switchComparation.get(i)-1).setLineFalse(switchComparation.get(i)+2);
			i++;
		}
		
		tuple = new Tuple(lineaCodigo,"FinCase",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
	}
	
	
	private void si() {
		/*si <Comparacion> entonces <Enunciados> fin-si*/
		ifComparation = new ArrayList<Integer>(); 
		
		match("SI");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		comparacion();
		setLineFalseIf(lineaCodigo);
		
		match("THEN");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		if (tokens.get(index).type.name().equals("INICIO")) {
			match("INICIO");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			
			enunciados();
			ifComparation.add(lineaCodigo-1);
			
			match("FIN");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			
		}else {
			enunciado();
			ifComparation.add(lineaCodigo-1);
		}
		
		while(tokens.get(index).type.name().equals("ELSE")) {
			match("ELSE");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");

			if(tokens.get(index).type.name().equals("SI")) {
				match("SI");
				sb.append(tokens.get(index-1).type);
				sb.append(" ");
				comparacion();
				
				match("THEN");
				sb.append(tokens.get(index-1).type);
				sb.append(" ");
			}
			setLineFalseIf(lineaCodigo);
			//setLineFalse(lineaCodigo);
			if (tokens.get(index).type.name().equals("INICIO")) {
				match("INICIO");
				sb.append(tokens.get(index-1).type);
				sb.append(" ");
				
				enunciados();
				ifComparation.add(lineaCodigo-1);
				
				match("FIN");
				sb.append(tokens.get(index-1).type);
				sb.append(" ");
				
			}else {
				enunciado();
				ifComparation.add(lineaCodigo-1);
			}
		}
		
		
		//sb.append(System.lineSeparator());
		tokenList = new ArrayList<Token>();
		tokenList.add(tokens.get(index-1));
		lineaCodigo++;
		
		i= 0;
		while(i<ifComparation.size()) {
			listTuples.get(ifComparation.get(i)).setLineFalse(lineaCodigo);
			listTuples.get(ifComparation.get(i)).setLineTrue(lineaCodigo);
			i++;
		}
		
		tuple = new Tuple(lineaCodigo,"finIfCiclo",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
	}
	
	private void forCiclo() {
		tokenList = new ArrayList<Token>();
		
		match("FOR");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("VARIABLE");
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("DOSPUNTOS");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("IGUAL");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");

		match("NUMERO");
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		if (tokens.get(index).type.name().equals("TO")) {
			match("TO");
		}else {
			match("DOWNTO");
		}
	
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("NUMERO");
		tokenList.add(tokens.get(index-1));
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		lineaCodigo++;
		line = lineaCodigo;
		tuple = new Tuple(lineaCodigo,"cicloFor",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		
		match("DO");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		sb.append(System.lineSeparator());
		
		match("INICIO");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		enunciados();
		
		match("FIN");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		sb.append(System.lineSeparator());
		
		listTuples.get(lineaCodigo-1).setLineFalse(line);
		listTuples.get(lineaCodigo-1).setLineTrue(line);
		
		tokenList = new ArrayList<Token>();
		tokenList.add(tokens.get(index-1));
		lineaCodigo++;
		listTuples.get(line-1).setLineFalse(lineaCodigo);
		tuple = new Tuple(lineaCodigo,"cicloForEnd",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		//setLineFalseFor(lineaCodigo);//ERASE HERE
	}
	

	
	private void mientras() {

		/*<Mientras> -> mientras <Comparacion> <Enunciados> fin-mientras*/
		match("MIENTRAS");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		comparacion();
		
		match("DO");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		match("INICIO");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		
		enunciados();
		match("FIN");
		sb.append(tokens.get(index-1).type);
		
		setLineFalse(lineaCodigo);
		sb.append(System.lineSeparator());

		tokenList = new ArrayList<Token>();
		tokenList.add(tokens.get(index-1));
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"finComparacion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
	}
	
	private void comparacion() {

		/*<Comparacion> -> (<valor> operador-relacional <valor>)*/
		tokenList = new ArrayList<Token>();
		valor();
		match("OPRELACIONAL");
		sb.append(tokens.get(index-1).type);
		sb.append(" ");
		tokenList.add(tokens.get(index-1));
		valor();
		lineaCodigo++;
		tuple = new Tuple(lineaCodigo,"comparacion",tokenList,lineaCodigo+1,lineaCodigo+1);
		listTuples.add(tuple);
		sb.append(System.lineSeparator());
	}
	
	private void valor() {
		/*<Valor> -> variable | numero*/
		if (tokens.get(index).type.name().equals("VERDADERO")) {
			match("VERDADERO");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
		}else if (tokens.get(index).type.name().equals("FALSO")) {
			match("FALSO");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
		}else if (tokens.get(index).type.name().equals("VARIABLE")) {
			table.resolve(tokens.get(index).data); //Check variable
			match("VARIABLE");
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
			tokenList.add(tokens.get(index-1));
		}else if (tokens.get(index).type.name().equals("NUMERO")) {
			match("NUMERO");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
		}else if (tokens.get(index).type.name().equals("CADENA")) {
			match("CADENA");
			tokenList.add(tokens.get(index-1));
			sb.append(tokens.get(index-1).type);
			sb.append(" ");
		}
			
	}	
	
	// Function that sets all the False lines inside the comparing blocks
		private void setLineFalseFor(int line) {
			int numTuple = lineaCodigo-1;
			while(numTuple>0) {
				if(listTuples.get(numTuple).getType().equals("cicloFor") && listTuples.get(numTuple).getLineFalse()==listTuples.get(numTuple).getLineTrue()) {
					listTuples.get(numTuple).setLineFalse(line+1);
					
					if(!listTuples.get(lineaCodigo-2).getType().equals("cicloFor")) {
						listTuples.get(lineaCodigo-1).setLineFalse(numTuple+1);
						listTuples.get(lineaCodigo-1).setLineTrue(numTuple+1);
					}
					break;
				}
				numTuple--;
			}
		}
	
	
	// Function that sets all the False lines inside the comparing blocks
	private void setLineFalse(int line) {
		int numTuple = lineaCodigo-1;
		while(numTuple>0) {
			if(listTuples.get(numTuple).getType().equals("comparacion") && listTuples.get(numTuple).getLineFalse()==listTuples.get(numTuple).getLineTrue()) {
				listTuples.get(numTuple).setLineFalse(line+1);
				
				if(!listTuples.get(lineaCodigo-2).getType().equals("comparacion")) {
					listTuples.get(lineaCodigo-1).setLineFalse(numTuple+1);
					listTuples.get(lineaCodigo-1).setLineTrue(numTuple+1);
				}
				break;
			}
			numTuple--;
		}
	}
	
	// Function that sets all the False lines inside the comparing blocks
		private void setLineFalseIf(int line) {
			int numTuple = lineaCodigo-1;
			while(numTuple>0) {
				if(listTuples.get(numTuple).getType().equals("comparacion") && listTuples.get(numTuple).getLineFalse()==listTuples.get(numTuple).getLineTrue()) {
					listTuples.get(numTuple).setLineFalse(line+2);
					
					break;
				}
				numTuple--;
			}
		}
	
	
	// Function that prints Tuple
	public void printTuple() {
		int i = 0;
		for(i = 0; i<listTuples.size(); i++) {
			System.out.println(listTuples.get(i).toString());
		}
	}
	
	public String getTuple() {
		String tuple;
		StringBuilder stringB = new StringBuilder();
		programa();
		int i = 0;
		for(i = 0; i<listTuples.size(); i++) {
			stringB.append(listTuples.get(i).toString());
			stringB.append(System.lineSeparator());
		}
		tuple = stringB.toString();
		return tuple;
	}
	
}

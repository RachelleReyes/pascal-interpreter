package modelo;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Interpreter {
	ArrayList <Tuple> listTuples = new ArrayList<Tuple>();
	SymbolTable table = new SymbolTable();
	StringBuilder mensajeSb = new StringBuilder();
	StringBuilder errorSb = new StringBuilder();
	StringBuilder charInput = new StringBuilder();
	String value,value2,type1,type2,operacion, result; 
	Symbol symbol,symbol2;
	int numErrores=0, forDefined=0;
	boolean comparation;
	
	public Interpreter(ArrayList<Tuple> listTuples, SymbolTable table) {
		this.listTuples = listTuples;
		this.table = table;
		interpret();
	}
	
	
	public StringBuilder getErrors() {
		return errorSb;
	}
	
	private void interpret(){
		int i = 0,valI1 = 0, valI2 = 0;
		float valF1 = 0, valF2 = 0;
		numErrores=0;
		//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		errorSb = new StringBuilder();
		
		while(listTuples.get(i).getType() != "finPrograma") {
		     switch(listTuples.get(i).getType()) {
	            case "leer":
	            	
	                 //Leer del teclado un valor
	            	if(numErrores==0)
	            		value = JOptionPane.showInputDialog("");
	            	
	                 //Guardar el valor en la variable
	            	 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
	            	 if(symbol.type.toString() == "char") {
	            		 charInput = new StringBuilder();
	            		 charInput.append("\'");
	            		 charInput.append(value);
	            		 charInput.append("\'");
	            		 value = charInput.toString();
	            	 }
	            	symbol.setValue(value);
	                 i = listTuples.get(i).getLineTrue()-1;
	                 break;
	               
	             case "escribir": 
	                 //Escribir el mensaje
	            	 mensajeSb = new StringBuilder();
	            	 mensajeSb.append(listTuples.get(i).getList().get(0).data);
	            	 
	                 //Escribir el valor en la variable
	            	 if(listTuples.get(i).getList().size()>1) {
	            		 symbol = table.resolve(listTuples.get(i).getList().get(1).data); 
	            		 mensajeSb.append(" ");
	            		 mensajeSb.append(symbol.value.toString());

	            	 }
	            	 if(numErrores==0)
	            		 JOptionPane.showMessageDialog(null, mensajeSb.toString());
	            	 	
	            	 i = listTuples.get(i).getLineTrue()-1;
	                 break;
	                 
	            // asginacion, revisar cuantos numeros. revisar si son variables o numeros literales
	             case "asignacion": 
	       
	                 //Obtener los valores de la tabla de simbolos
	            	 if(listTuples.get(i).getList().size()>2) {
	            		// Si es el primer elemento es una variable
	            		 if(listTuples.get(i).getList().get(1).type.toString()!="NUMERO") {
	            			 // Si es una variable
	            			 symbol = table.resolve(listTuples.get(i).getList().get(1).data); 
	            			 type1 = symbol.type.toString();
	            			 value = symbol.value;
	            		 }else { // Si es un numero
	            			 value = listTuples.get(i).getList().get(1).data;
	            			 type1 = returnType(value);
	            		 }
	            		 
	            		 switch(type1) {
		            		 case "integer":  valI1 = Integer.parseInt(value); 
		            		 				  valF1 = Float.parseFloat(value); break;
		            		 case "real":  valF1 = Float.parseFloat(value); break;
	        			 }
	            
	            		 // Si es el segundo elemento es una variable
	            		 if(listTuples.get(i).getList().get(3).type.toString()!="NUMERO") {
	            			 // Si es una variable
	            			 symbol = table.resolve(listTuples.get(i).getList().get(3).data); 
	            			 type2 = symbol.type.toString();
	            			 value = symbol.value;
	            		 }else { // Si es un numero
	            			 value = listTuples.get(i).getList().get(3).data;
	            			 type2 = returnType(value);
	            		 }
	            		 
	            		 switch(type2) {
		            		 case "integer": valI2 = Integer.parseInt(value); 
						            		 valF2 = Float.parseFloat(value);  break;
		            		 case "real":  valF2 = Float.parseFloat(value); break;
	        			 }
	            		 
		            	 operacion = listTuples.get(i).getList().get(2).data;
	            		//Realizar la operacion 
		            	 if(type1 == type2 && type1=="integer") {
		            		 result = operacionInt(valI1,operacion,valI2);
		            	 }else if(type1 == type2 && type1=="real") {
		            		 result = operacionFloat(valF1,operacion,valF2);
		            	 }else { 
	        	        	errorSb.append("(");
	           	        	errorSb.append(i+1);
	           	        	errorSb.append(") ");
	           	        	errorSb.append("Warning: Operation between \"");
							errorSb.append(type1);
							errorSb.append("\" and \"");
							errorSb.append(type2);
							errorSb.append("\"");
							errorSb.append(System.lineSeparator());
							result = operacionFloat(valF1,operacion,valF2);
		            	 }
	            		 
			            //Guardar el resultado en la variable
	            		 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
	            		 value = result;
	            		 checkType(i);
	            		 symbol.setValue(result);
	            	 }else {
	            		 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
	            		 value = listTuples.get(i).getList().get(1).data;
	            		 checkType(i);
	            		 symbol.setValue(value);
	            	 }
	             
	            	 i = listTuples.get(i).getLineTrue()-1;
	                 break;
				
	            case "comparacion": 
	                
	            	 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
	            	 value = symbol.value;
	            	 type1 = symbol.type.toString();
	            	 
	            	 switch(type1) {
	            		 case "integer":  valI1 = Integer.parseInt(value); 
	            		 				  valF1 = Float.parseFloat(value); break;
	            		 case "real":  valF1 = Float.parseFloat(value); break;
	       			 }
	            	 
	            	 if(listTuples.get(i).getList().get(2).type.toString()!="NUMERO") {
	            		 symbol = table.resolve(listTuples.get(i).getList().get(2).data); 
		           		 type2 = symbol.type.toString();
		           		 value2 = symbol.value;
	           		 }else {
	           			value2 = listTuples.get(i).getList().get(2).data;
	           			type2 = returnType(value2);
	           		 }
	            	 
	            	 
	            	 switch(type2) {
	            		 case "integer": valI2 = Integer.parseInt(value2); 
					            		 valF2 = Float.parseFloat(value2);  break;
	            		 case "real":  valF2 = Float.parseFloat(value2); break;
	            	 }
	            	 
	            	 
	            	 operacion = listTuples.get(i).getList().get(1).data;
	            	 
	            	 if(type1 == type2 && type1=="integer") {
	            		 comparation = compareInt(valI1,operacion,valI2);
	            	 }else if(type1 == type2 && type1=="real") {
	            		 comparation = compareFloat(valF1,operacion,valF2);
	            	 }else if(type1 == "char" || type1 == "boolean") {
	            		 comparation = compareString(value,operacion,value2);
	            	 }else { 
        	        	errorSb.append("(");
           	        	errorSb.append(i+1);
           	        	errorSb.append(") ");
           	        	errorSb.append("Warning: Comparing between \"");
						errorSb.append(type1);
						errorSb.append("\" and \"");
						errorSb.append(type2);
						errorSb.append("\"");
						errorSb.append(System.lineSeparator());
	            		 comparation = compareFloat(valF1,operacion,valF2);
	            	 }
	            	 
	            	 if(comparation== true)
	            		 i = listTuples.get(i).getLineTrue()-1;
	            	 else
	            		 i = listTuples.get(i).getLineFalse()-1;
	        
                     break;
	            case "cicloFor":
	            	if(forDefined!= i) {
	            		forDefined = i;
	            		symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
		            	symbol.setValue(String.valueOf(listTuples.get(i).getList().get(1).data));
	            		
	            	}
	            	
	            	 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
	            	 value = symbol.value;
	            	 type1 = symbol.type.toString();
	            	 
	            	 switch(type1) {
	            		 case "integer":  valI1 = Integer.parseInt(value); 
	            		 				  valF1 = Float.parseFloat(value); break;
	            		 case "real":  valF1 = Float.parseFloat(value); break;
	       			 }
	            	
	            	 
	            	 if(listTuples.get(i).getList().get(3).type.toString()!="NUMERO") {
	            		 symbol = table.resolve(listTuples.get(i).getList().get(3).data); 
		           		 type2 = symbol.type.toString();
		           		 value = symbol.value;
	           		 }else {
	           			value = listTuples.get(i).getList().get(3).data;
	           			type2 = returnType(value);
	           		 }
	            	 
	            	 switch(type2) {
	            		 case "integer": valI2 = Integer.parseInt(value); 
					            		 valF2 = Float.parseFloat(value);  break;
	            		 case "real":  valF2 = Float.parseFloat(value); break;
	            	 }
	            	 
	            	 switch(listTuples.get(i).getList().get(2).data) {
	            		 case "to": operacion = "<="; 
		            		 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
			            	 symbol.setValue(String.valueOf(valI1+1));
	            		 break;
	            		 case "downto": operacion = ">="; 
		            		 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
			            	 symbol.setValue(String.valueOf(valI1-1));
	            		 break;
	            	 }
	            	 
	            	 comparation = compareInt(valI1,operacion,valI2);
	            	 
	            	 if(comparation== true) {
	            		 i = listTuples.get(i).getLineTrue()-1;
	            	 }else {
	            		 i = listTuples.get(i).getLineFalse()-1;
	            		 forDefined = 0;
		            	 i++;
	            	 }
	            	 break;
	            case "cicloSwitch":
	            	 symbol = table.resolve(listTuples.get(i).getList().get(0).data); 
	            	 value = symbol.value;
	            	 type1 = symbol.type.toString();
	            	 
	            	 switch(type1) {
	            		 case "integer":  valI1 = Integer.parseInt(value); 
	            		 				  valF1 = Float.parseFloat(value); break;
	            		 case "real":  valF1 = Float.parseFloat(value); break;
	            	 }
	            	 i++;
	            	 
	            	 while(listTuples.get(i).getType()=="comparar") {
	            		 value2 = listTuples.get(i).getList().get(0).data;
	            		 if(value.equals(value2)) {
	            			 i = listTuples.get(i).getLineTrue()-1;
	            		 }else {
	            			 i = listTuples.get(i).getLineFalse()-1;
	            		 }
	            	}
	            	 
	            	 if(listTuples.get(i).getType()=="compararElse")
	            		 i = listTuples.get(i).getLineTrue()-1;
	            	 
	            	break;
	            case "declaracion": 
	            case "const":
	            case "var":
	            case "begin":
	            case "finComparacion":
	            case "finIfCiclo":
	            case "cicloForEnd":
	            case "FinCase":
	            	i++;
                break;
               
		     }
		}
	}
	
	private String returnType(String value) {
		 String type;
			 try { 
   	            Integer.parseInt(value); 
   	            type = "integer";
   	        } catch (NumberFormatException e){ 
   	        	try { 
   	   	            Float.parseFloat(value); 
   	   	            type = "real";
   	   	        } catch (NumberFormatException e1){ 
   	   	        	if(value== "true" || value == "false")
   	   	        		type = "boolean";
   	   	        	else
   	   	        		type = "char";
   	   	        }
   	        }
		
		return type;
	}
	
	private boolean checkType(int i) {
		boolean success = true;
		switch(symbol.type.toString()) {
		 case "integer":  
			 try { 
   	            Integer.parseInt(value); 
   	            symbol.setValue(value);	
   	        } catch (NumberFormatException e){ 
   	        	numErrores++;
   	        	errorSb.append("(");
   	        	errorSb.append(i+1);
   	        	errorSb.append(") ");
   	        	errorSb.append("Error: Incompatible types: expected \"integer\" but got ");
   	        	errorSb.append(value);
   	        	errorSb.append(System.lineSeparator());
   	        	success = false;
   	        }
			 break;
		 case "real":
			 try { 
   	            Float.parseFloat(value); 
   	            symbol.setValue(value);	
   	        } catch (NumberFormatException e){ 
   	        	numErrores++;
   	        	errorSb.append("(");
   	        	errorSb.append(i+1);
   	        	errorSb.append(") ");
   	        	errorSb.append("Error: Incompatible types: expected \"real\" but got ");
   	        	errorSb.append(value);
   	        	errorSb.append(System.lineSeparator());
   	        	success = false;
   	        }
			 break;
		 case "char":
			 symbol.setValue(value);	
			 break;
			 
		 case "boolean":
			 symbol.setValue(value);	
			 break;
	 }
		return success;
	}
	
	private String operacionFloat(float val1,String symbol,float val2) {
		float resultado = 0;
		//OPARITMETICO("[*|/|+|-]"),
		switch(symbol) {
		case "+":
			resultado = val1 + val2;
		break;
		case "-":
			resultado = val1 - val2;
		break;
		case "*":
			resultado = val1 * val2;
		break;
		case "/":
			resultado = val1 / val2;
		break;	
	}
		
		return String.valueOf(resultado);
	}
	
	private String operacionInt(int val1,String symbol,int val2) {
		int resultado = 0;
		//OPARITMETICO("[*|/|+|-]"),
		switch(symbol) {
		case "+":
			resultado = val1 + val2;
		break;
		case "-":
			resultado = val1 - val2;
		break;
		case "*":
			resultado = val1 * val2;
		break;
		case "/":
			resultado = val1 / val2;
		break;	
	}
		
		return String.valueOf(resultado);
	}
	
	private boolean compareString(String val1,String symbol,String val2) {
		boolean result = false;
		switch(symbol) {
    		
    		case "==":
    			if(val1.equals(val2))
    				result = true;
    		break;
    		case "!=":
    			if(!val1.equals(val2))
    				result = true;
    		break;
		}
		return result;
	}
	
	
	private boolean compareInt(int val1,String symbol,int val2) {
		boolean result = false;
		//OPRELACIONAL("<|>|==|<=|>=|!="),
		switch(symbol) {
    		case "<":
    			if(val1<val2)
    				result = true;
    		break;
    		case ">":
    			if(val1>val2)
    				result = true;
    		break;
    		case "==":
    			if(val1==val2)
    				result = true;
    		break;
    		case "<=":
    			if(val1<=val2)
    				result = true;
    		break;
    		case ">=":
    			if(val1>=val2)
    				result = true;
    		break;
    		case "!=":
    			if(val1!=val2)
    				result = true;
    		break;
		}
		return result;
	}
	
	private boolean compareFloat(float val1,String symbol,float val2) {
		boolean result = false;
		//OPRELACIONAL("<|>|==|<=|>=|!="),
		switch(symbol) {
    		case "<":
    			if(val1<val2)
    				result = true;
    		break;
    		case ">":
    			if(val1>val2)
    				result = true;
    		break;
    		case "==":
    			if(val1==val2)
    				result = true;
    		break;
    		case "<=":
    			if(val1<=val2)
    				result = true;
    		break;
    		case ">=":
    			if(val1>=val2)
    				result = true;
    		break;
    		case "!=":
    			if(val1!=val2)
    				result = true;
    		break;
		}
		return result;
	}
	
}

package controlador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.Interpreter;
import modelo.PascalLexer;
import modelo.PascalLexer.Token;
import modelo.PascalParser;
import modelo.SymbolTable;
import modelo.Tuple;
import vista.Ventana;

public class ControladorBoton implements ActionListener{
	
	private Ventana ventana;
	private String archivo,cadena,etapa,resultado,errores;
	PascalParser parser;
	Interpreter interpreter;
	StringBuilder errorSb;
	ArrayList <Tuple> listTuples;
	SymbolTable table;
	JFileChooser j;
	int r;
	
	public ControladorBoton(Ventana ventana) {
		this.ventana = ventana;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comando=e.getActionCommand();
		
		try {
		switch(comando){
		case "CARGAR_FUENTE":
			/*
			j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
			  
            // invoca la funcion de showsOpenDialog function para mostrar el dialogo de abrir
            r = j.showOpenDialog(null); 
  
            // Si el usuario selecciona un archivo
            if (r == JFileChooser.APPROVE_OPTION) { 
            	archivo = j.getSelectedFile().getAbsolutePath();
            	cadena = leerArchivo(archivo);
            	ventana.setTextAreaSource(cadena);
            } 
            // Si el usuario cancelo la operacion
            else
            	JOptionPane.showMessageDialog((Component)ventana, "Elige otro archivo","ERROR",JOptionPane.ERROR_MESSAGE);
           */
           
			
			archivo = "src/pascalCodigo";
			cadena = leerArchivo(archivo);
			ventana.setTextAreaSource(cadena);
           
			break;
			
		case "BORRAR_FUENTE":
				cadena = "";
				ventana.setTextAreaSource(cadena);
			break;
			
		case "VER_RESULTADO":
			etapa = ventana.getEtapa();
			//ventana.setTextAreaResult(etapa);
			
			/*Analizador Lexico, Analizador Sintactico, Analizador de Alcances, Codigo Intermedio*/
			cadena =  ventana.getTextAreaSource();
			
			switch(etapa) {
				case "Analizador Lexico":
					PascalLexer lexer = new PascalLexer();
					ArrayList<Token> tokens = lexer.lex(cadena);
					StringBuilder sb = new StringBuilder();
					for(Token token:tokens) {
						sb.append(token.toString());
						sb.append(System.lineSeparator());
					}
					resultado = sb.toString();
					ventana.setTextAreaResult(resultado);
					break;
				case "Analizador Sintactico":
					listTuples = new ArrayList<Tuple>();
					table = new SymbolTable();
					errorSb = new StringBuilder();
					
					parser = new PascalParser(cadena,listTuples,table,errorSb);
					resultado = parser.parse();
					ventana.setTextAreaResult(resultado);
					break;
				case "Codigo Intermedio":
					listTuples = new ArrayList<Tuple>();
					table = new SymbolTable();
					errorSb = new StringBuilder();
					
					parser = new PascalParser(cadena,listTuples,table,errorSb);
					resultado = parser.getTuple();
					ventana.setTextAreaResult(resultado);
					
					break;
				case "Tabla de Simbolos":
					listTuples = new ArrayList<Tuple>();
					table = new SymbolTable();
					errorSb = new StringBuilder();
					
					parser = new PascalParser(cadena,listTuples,table,errorSb);
					parser.parse();
					resultado = parser.getSymbolTable().formatTable();
					errores =  parser.getErrors();
					ventana.setTextAreaResult(resultado);
					ventana.setTextAreaError(errores);
					break;
				}
			
			break;
			case "GENERAR_ARCHIVO":
				
				j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
	            j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
	            r = j.showSaveDialog(null); 
	            if (r == JFileChooser.APPROVE_OPTION) { 
	                archivo = j.getSelectedFile().getAbsolutePath(); 
	            } 
				
	            File file =new File(archivo.toString());
	            FileWriter escribir=new FileWriter(file,true);
	            escribir.write(resultado);
	            escribir.close();
				
				break;
				
				//Interpreter interpreter = new Interpreter(listTuples,table);
			case "INTERPRETAR":
				cadena =  ventana.getTextAreaSource();
				listTuples = new ArrayList<Tuple>();
				table = new SymbolTable();
				errorSb = new StringBuilder();
				
				parser = new PascalParser(cadena,listTuples,table,errorSb);
				parser.parse();
				listTuples = parser.getListTuples();
				table = parser.getSymbolTable();
				interpreter = new Interpreter(listTuples, table);
				errores = interpreter.getErrors().toString();
				ventana.setTextAreaError(errores);
				break;
		}
		}
		catch(Exception error) {
			JOptionPane.showMessageDialog((Component)ventana,error.getMessage(), "ERROR",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private static String leerArchivo(String archivo) throws FileNotFoundException {
		String cadena = null;
		//PseudoLexer lexer = new PseudoLexer();
		BufferedReader br = new BufferedReader(new FileReader(archivo));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = null;
			
				line = br.readLine();
				
				while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    cadena = sb.toString();
			    br.close();
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return cadena;
		
	}

}
